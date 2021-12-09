package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import lt.vu.mif.it.paskui.village.npc.entities.NPCEntity;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Class for storing npc and it's data
 */
public class NPC {

    private String name;
    private Location loc;
    private final Role role;
    private final Personality personality;
    private final int id;
    private SelectionScreen services;
    private NPCEntity npcEntity;

    public NPC(int id, String name, Location loc, Role role, Personality personality) {
        this.name = name;
        this.loc = loc;
        this.role = role;
        this.personality = personality;
        this.services = null;
        this.id = id;
        npcEntity = new CustomVillager(this, loc);
        npcEntity.setEntityPos(loc);
        npcEntity.setEntityName(name);
    }

    public NPC(int id, String name, Location loc, UUID uuid, Role role, Personality personality) {
        this(id, name, loc, role, personality);
        npcEntity.setEntityUUID(uuid);
    }

    // Getters
    public String getName() {
        return name;
    }

    public Location getLoc() {
        this.updateLocation();
        return loc;
    }

    public Location getLocation() { return loc; }

    public org.bukkit.entity.Entity getEntity() {
        return npcEntity.getBukkitEntity();
    }

    public UUID getUUID() {
        return npcEntity.getEntityUUID();
    }

    public Role getRole() {
        return role;
    }

    public Personality getPersonality() {
        return personality;
    }

    public SelectionScreen getServices() {
        return services;
    }

    public int getId() {return id;}

    public void setServices(SelectionScreen services) {
        this.services = services;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
        this.npcEntity.setEntityName(name);
    }

    public void setLoc(Location loc) {
        this.loc = loc;
        this.npcEntity.setEntityPos(loc);
    }

    public void setEntity(CustomVillager villager) {
        this.npcEntity = villager;
    }

    // Other
    /** Creates npc inside minecraft world.
     * @return status whether npc is spawned
     */
    public boolean spawn() {
        ServerLevel nmsWorld = ((CraftWorld) loc.getWorld()).getHandle();
        return nmsWorld.addEntity(npcEntity.getNMSEntity(), CreatureSpawnEvent.SpawnReason.COMMAND);
    }

    /**
     * Removes the trading player from the NPCs brain.
     */
    public void stopTrade() {npcEntity.stopEntityTrading();}

    /**
     * Removes npc entity from minecraft world.
     */
    public void remove() {
        npcEntity.removeEntity();
    }

    /**
     * Moves the NPC into the desired location.
     */
    public void moveTo(int time, Material material) {
        npcEntity.moveTo(time, material);
    }

    /**
     * Makes the NPC move back to the starting location.
     */
    public void moveBack(final Location loc) {npcEntity.moveBack(loc);}

    /**
     * Calculates the distance between two points.
     */
    public Double distanceTo(Material material) {return npcEntity.distanceTo(material);}

    /**
     * Deletes all the behaviors.
     */
    public void removeBrain() {npcEntity.removeBrain();}

    /**
     * Puts back the behaviors in place.
     */
    public void refreshBrains(final ServerLevel world) {npcEntity.refreshBrains(world);}

    /**
     * Move further if a block is not found.
     * @param location
     */
    public void moveFurther(Location location) {npcEntity.moveFurther(location);}

    /**
     * Update location
     */
    public void updateLocation() {
        Vec3 pos = npcEntity.getNPCPos();
        this.loc = new Location(loc.getWorld(), pos.x, pos.y, pos.z);
    }

    /**
     * Sets the NPC invisible.
     */
    public void setInvisible() { npcEntity.setInvisible(); }

    /**
     * Sets the NPC visible.
     */
    public void setVisible() { npcEntity.setVisible(); }

    public void setKillable() { npcEntity.setKillable(); }

    /**
     * Checks if there is a specific block in a radius
     * @return returns the vector which the NPC will use for walking to the log.
     */
    public Block getCuboid(Material material) {
        return this.searchMaterials(material);
    }

    public Block searchMaterials(final @NotNull Material material) {
        this.updateLocation();

        final double RADIUS = 8;
        final World WORLD = this.loc.getWorld();
        final Vec3 START = new Vec3(loc.getX(), loc.getY(), loc.getZ());
        final Vec3 MIN = START.subtract(RADIUS, RADIUS, RADIUS);
        final Vec3 MAX = START.add(RADIUS, RADIUS, RADIUS);

        LinkedList<Block> opened = new LinkedList<>(List.of(
                this.loc.getBlock(),
                this.loc.toBlockLocation().add(0, 1, 0).getBlock()
        ));
        LinkedList<Block> closed = new LinkedList<>();

        while (!opened.isEmpty()) {
            Block block = opened.pop();

            for (int y = block.getY() - 1; y <= block.getY() + 1; ++y) {
                for (int x = block.getX() - 1; x <= block.getX() + 1; ++x) {
                    for (int z = block.getZ() - 1; z <= block.getZ() + 1; ++z) {
                        if ((MIN.y > y || y > MAX.y)
                                || (MIN.x > x || x > MAX.x)
                                || (MIN.z > z || z > MAX.z)) {
                            continue;
                        }

                        Block newBlock = WORLD.getBlockAt(x, y, z);

                        if (newBlock.equals(block)
                            || opened.contains(newBlock)
                            || closed.contains(newBlock)) {
                            continue;
                        }

                        if (newBlock.getType() == Material.AIR
                                || newBlock.getType() == Material.CAVE_AIR) {
                            opened.add(newBlock);
                        } else if (newBlock.getType() == material) {
                            return newBlock;
                        } else {
                            closed.push(newBlock);
                        }
                    }
                }
            }

            closed.add(block);
        }

        return null;
    }
}
