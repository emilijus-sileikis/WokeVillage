package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import lt.vu.mif.it.paskui.village.npc.entities.NPCEntity;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import lt.vu.mif.it.paskui.village.util.Vector3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Class for storing npc and it's data
 */
public class NPC {

    private final int id;
    private final String name;
    private final Role role;
    private final Personality personality;
    private Location loc;
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
     * Puts back the behaviors in place.
     */
    public void refreshBrains(final ServerLevel world) {npcEntity.refreshBrains(world);}

    /**
     * Move further if a block is not found.
     * @param location loc to move to.
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

    /**
     * Sets the NPC killable.
     */
    public void setKillable() { npcEntity.setKillable(); }

    /**
     * Sets the NPC non collidable.
     */
    public void setNonCollidable() { npcEntity.setNonCollidable(); }

    /**
     * Sets the NPC Collidable.
     */
    public void setCollidable() { npcEntity.setCollidable(); }

    /**
     * Resets the Cosmetics.
     */
    public void itemReset() {
        npcEntity.setHandItem(new ItemStack(Material.AIR));
    }

    /**
     * Sets the Cosmetics for the NPC.
     */
    public void setWorkHand() {
        npcEntity.setHandItem(new ItemStack(role.workCosmetic));
    }

    /**
     * Sets {@link NPCEntity} hand based on its role.
     */
    public void setGoodsHand() {
        npcEntity.setHandItem(new ItemStack(role.goodsCosmetic));
    }

    /**
     * Checks if there is a specific block in a radius
     * @return returns the vector which the NPC will use for walking to the log.
     */
    public Block searchMaterials(final @NotNull Material material) {
        this.updateLocation();

        final int RADIUS = 8;
        final World WORLD = this.loc.getWorld();
        final Vector3 START = new Vector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        final Vector3 MIN = START.subtract(RADIUS);
        final Vector3 MAX = START.add(RADIUS);

        LinkedList<Block> opened = new LinkedList<>(List.of(
                this.loc.getBlock(),
                this.loc.toBlockLocation().add(0, 1, 0).getBlock()
        ));
        LinkedList<Block> closed = new LinkedList<>();
        int y, z, x;
        Vector3 fn, ln; // firstNeighbour, lastNeighbour

        while (!opened.isEmpty()) {
            Block block = opened.pop();

            // firstNeighbour
            fn = new Vector3(
                    block.getX() - 1,
                    block.getY() - 1,
                    block.getZ() - 1
            );
            fn = fn.add(
                    Math.max(0, MIN.x() - fn.x()),
                    Math.max(0, MIN.y() - fn.y()),
                    Math.max(0, MIN.z() - fn.z())
            );

            // lastNeighbour
            ln = new Vector3(
                    block.getX() + 1,
                    block.getY() + 1,
                    block.getZ() + 1
            );
            ln = ln.subtract(
                    Math.max(0, ln.x() - MAX.x()),
                    Math.max(0, ln.y() - MAX.y()),
                    Math.max(0, ln.z() - MAX.z())
            );

            for (y = fn.y(); y <= ln.y(); ++y) {
                for (z = fn.z(); z <= ln.z(); ++z) {
                    for (x = fn.x(); x <= ln.x(); ++x) {
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
