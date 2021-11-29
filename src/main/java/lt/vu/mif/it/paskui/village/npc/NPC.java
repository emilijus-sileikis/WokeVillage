package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import lt.vu.mif.it.paskui.village.npc.entities.NPCEntity;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import net.kyori.adventure.text.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

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
    public void moveBack(Location loc) {npcEntity.moveBack(loc);}

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
    public void refreshBrain() {npcEntity.refreshBrain();}

    /**
     * Move further if a block is not found.
     * @param location
     */
    public void moveFurther(Location location) {npcEntity.moveFurther(location);}

    /**
     * Checks if there is a specific block in a radius
     * @return returns the vector which the NPC will use for walking to the log.
     */
    public Vec3 getCuboid(Material material) {
        switch (role) {
            case LUMBERJACK -> {
                material = Material.SPRUCE_LOG;
            }
            case MINER -> {
                material = Material.STONE;
            }
            case FISHER -> {
                material = Material.WATER;
            }
        }

        Location center = this.getLoc();
        float radius = 16 / 2;
        Location minimum = new Location(center.getWorld(), center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        Location maximum = new Location(center.getWorld(), center.getX() + radius, center.getY() + radius, center.getZ() + radius);
        Block b;
        Vec3 v;

        for(int x = minimum.getBlockX(); x <= maximum.getBlockX(); x++) {
            for(int y = minimum.getBlockY(); y <= maximum.getBlockY(); y++) {
                for(int z = minimum.getBlockZ(); z <= maximum.getBlockZ(); z++) {
                    b = new Location(center.getWorld(), x, y, z).getBlock();
                    if (b.getType() == material) {
                        Bukkit.broadcast(Component.text(material.toString() + " Found at: X=" + x + " " + "Y=" + y + " " + "Z=" + z));
                        v = new Vec3((x + 1.3), y, z);
                        Bukkit.broadcast(Component.text("Move to: " + v));
                        return v;
                    }
                }
            }
        }
        return null;
    }
}
