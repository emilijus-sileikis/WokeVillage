package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class NPCManager {

    private final HashMap<Integer, NPC> npcs;
    private final LinkedList<Integer> npcIds;

    public NPCManager() {
        npcs = new HashMap<>();
        npcIds = new LinkedList<>();
    }

    // getters
    /** Checks whether there is any NPCs existing.
     * @return True if npc hashmap and npc id list are not empty.
     */
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
    /**
     * creates NPC and attempts to spawn it.
     *
     * @param player Player that spawns the NPC
     * @param loc    initial location of NPC and in which world.
     * @param type   NPC entity type
     * @return Spawned NPC instance on success, null on fail.
     */
    public NPCTuple createNPC(Player player, Location loc, EntityType type) { //String skin
        NPC npc = new NPC("", loc, getRandomRole(), getRandomPersonality());
        int id = npcIds.isEmpty() ? 0 : npcIds.getLast() + 1;

        return spawnNPC(id, npc);
    }

    /**
     * Loads npc into the world
     *
     * @param id unique npc id number
     * @param name name npc is called
     * @param location data where npc is located
     * @param uuid unique npc id in NMS World
     * @param role npc occupation
     * @param personality npc persona type
     */
    public void loadNPC(int id, String name, Location location, UUID uuid, Role role, Personality personality) {
        NPC npc = new NPC(name, location, uuid, role, personality);

        if (spawnNPC(id, npc) == null) {
            Logging.infoLog("Unable to spawn NPC %d", id);
        }
    }

    /**
     * removes NPC with given id.
     *
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

        Bukkit.broadcast(Component.text("Total of " + npcs.size() + " NPCs were removed!"));

        for (NPC npc : npcs.values()) {
            npc.remove();
        }

        npcs.clear();
        npcIds.clear();
    }

    // private

    /**
     * Creates NPCEntity into world.
     *
     * @param id  int type identifier on NPC.
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

    private Personality getRandomPersonality() {
        int r = new Random().nextInt(6);
        return switch (r) {
            case 0 -> Personality.HARDWORKING;
            case 1 -> Personality.LAZY;
            case 2 -> Personality.RELIABLE;
            case 3 -> Personality.CLUMSY;
            case 4 -> Personality.GENEROUS;
            default -> Personality.GREEDY;
        };
    }

    private Role getRandomRole() {
        int r = new Random().nextInt(3);
        return switch (r) {
            case 0 -> Role.LUMBERJACK;
            case 1 -> Role.MINER;
            default -> Role.FISHER;
        };
    }
}
