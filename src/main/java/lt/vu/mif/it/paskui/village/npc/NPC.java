package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import lt.vu.mif.it.paskui.village.npc.entities.NPCEntity;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.UUID;

/**
 * Class for storing npc and it's data
 * TODO: make this primary class for npc
 */
public class NPC {

    private String name;
    private Location loc;
    private NPCEntity npcEntity;

    public NPC(String name, Location loc) {
        this.name = name;
        this.loc = loc;
        npcEntity = new CustomVillager(this, loc);
        npcEntity.setEntityPos(loc);
        npcEntity.setEntityName(name);
    }

    public NPC(String name, Location loc, UUID uuid) {
        this(name, loc);
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
     * Removes npc entity from minecraft world.
     */
    public void remove() {
        npcEntity.removeEntity();
    }
}
