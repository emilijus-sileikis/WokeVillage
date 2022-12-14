package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import lt.vu.mif.it.paskui.village.npc.entities.NPCEntity;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * Class for storing npc and it's data
 */
public class NPC {

    public final int id;
    public final String name;
    public final Role role;
    public final Personality personality;
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

    public SelectionScreen getServices() {
        return services;
    }

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
    public void stopTrade() {
        npcEntity.stopEntityTrading();
    }

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
    public void moveBack(final Location loc) {
        npcEntity.moveBack(loc);
    }

    /**
     * Puts back the behaviors in place.
     */
    public void refreshBrains(final ServerLevel world) {
        npcEntity.refreshBrains(world);
    }

    /**
     * Move further if a block is not found.
     * @param location loc to move to.
     */
    public void moveFurther(Location location) {
        npcEntity.moveFurther(location);
    }

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
    public void setInvisible() {
        npcEntity.setInvisible();
    }

    /**
     * Sets the NPC visible.
     */
    public void setVisible() {
        npcEntity.setVisible();
    }

    /**
     * Sets the NPC killable.
     */
    public void setKillable() {
        npcEntity.setKillable();
    }

    /**
     * Collision setting abstraction for NPC.
     * @param state state to set Collision at.
     */
    public void setCollidable(boolean state) {
        npcEntity.setCollidable(state);
    }

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

    public void resetSpeed() { npcEntity.resetSpeed(); }
}
