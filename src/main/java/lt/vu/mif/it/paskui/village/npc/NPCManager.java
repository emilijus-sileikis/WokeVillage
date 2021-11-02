package lt.vu.mif.it.paskui.village.npc;

import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class NPCManager {

    private final HashMap<Integer, NPC> npcs;
    private final LinkedList<Integer> npcIds;

    public NPCManager() {
        npcs = new HashMap<>();
        npcIds = new LinkedList<>();
    }

    // getters
    public boolean npcsExist() {
        return !(npcs.isEmpty() && npcIds.isEmpty());
    }

    public Map<Integer, NPC> getNPCs() {
        return npcs;
    }

    public int getLastId() {
        return npcIds.getLast();
    }

    // other
    /** creates NPC and attempts to spawn it.
     * @param player Player that spawns the NPC
     * @param loc initial location of NPC and in which world.
     * @param type NPC entity type
     * @return Spawned NPC instance on success, null on fail.
     */
    public NPCTuple createNPC (Player player, Location loc, EntityType type) { //String skin
        NPC npc = new NPC("", loc);
        int id = npcIds.isEmpty() ? 0 : npcIds.getLast() + 1;

        return spawnNPC(id, npc);
    }


    /** Loads npc into the world
     * @param location data where npc is located
     * @param role
     * @param personality
     */
    public void loadNPC(int id, String name, Location location, UUID uuid, String role, String personality) {
        NPC npc = new NPC(name, location, uuid, role, personality);

        spawnNPC(id, npc);
    }

    /** removes NPC with given id.
     * @param id valid id of npc.
     */
    public void removeNPC(int id) {
        npcs.get(id).remove();

        npcs.remove(id);
        npcIds.remove(id);

        Bukkit.broadcast(Component.text("NPC was removed!"));
    }

    /**
     * Removes all NPC entities.
     */
    public void removeAllNPC() {
        if (npcIds.isEmpty() || this.npcs.isEmpty()) {
            Bukkit.broadcast(Component.text("There are no NPCs to remove!"));
            return;
        }

        for (NPC npc : npcs.values()) {
            npc.remove();
        }
        Bukkit.broadcast(Component.text("Total of " + npcs.size() + " NPCs were removed!"));

        npcs.clear();
        npcIds.clear();
    }

    // private
    /** Creates NPCEntity into world.
     * @param id int type identifier on NPC.
     * @param npc NPC to spawn.
     * @return Instance of NPC on success, null on failure.
     */
    private NPCTuple spawnNPC(int id, NPC npc) {
        boolean spawned = npc.spawn();

        if (spawned) {
            npcs.put(id, npc);
            npcIds.add(id);
            return new NPCTuple(id, npc);
        }

        return null;
    }

    // other
    public record NPCTuple(int id, NPC npc) {
        @Override
        public String toString() {
            return "NPCTuple{ id : " + id +
                    ", npc : " + npc + '}';
        }
    }
}
