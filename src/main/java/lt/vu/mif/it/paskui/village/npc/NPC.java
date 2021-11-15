package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import lt.vu.mif.it.paskui.village.npc.entities.NPCEntity;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
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
    private Role role;
    private Personality personality;
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
     * Removes npc entity from minecraft world.
     */
    public void remove() {
        npcEntity.removeEntity();
    }
}
