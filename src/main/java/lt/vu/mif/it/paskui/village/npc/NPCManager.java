package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
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

    /** Attempts creating NPC and spawning it in Minecraft world.
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


    /** Loads npc into the world from parsed database.
     * @param location data where npc is located
     * @param role NPC role type in String
     * @param personality NPC personality type in String
     */
    public void loadNPC(int id, String name, Location location, UUID uuid, String role, String personality) {
        NPC npc = new NPC(name, location, uuid, role, personality);

        if (spawnNPC(id, npc) == null) {
            Logging.infoLog("Unable to spawn NPC %d", id);
        }
    }

    /** Removes NPC with given id.
     * @param id valid id of npc.
     */
    public void removeNPC(int id) {
        npcs.get(id).remove();

        npcs.remove(id);
        npcIds.removeFirstOccurrence(id);

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

        int i = 0;
        for (NPC npc : npcs.values()) {
            npc.remove();
            ++i;
        }

        Bukkit.broadcast(Component.text("Total of " + i + " NPCs were removed!"));

        npcs.clear();
        npcIds.clear();
    }

    // private

    /** Creates NPCEntity into world.
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

    /** Function for randomly getting NPC personality type.
     * @return randomly selected personality type String.
     */
    private String getRandomPersonality() {
        int a = getRandomNumber(1, 7);
        switch (a) {
            case 1:
                return "Hardworking";
            case 2:
                return "Lazy";
            case 3:
                return "Reliable";
            case 4:
                return "Clumsy";
            case 5:
                return "Generous";
            case 6:
                return "Greedy";
            default:
                return "Error";
        }
    }

    /** Function for randomly getting NPC role type.
     * @return randomly selected role type String.
     */
    private String getRandomRole() {
        int a = getRandomNumber(1, 4);
        switch (a) {
            case 1:
                return "LumberJack";
            case 2:
                return "Miner";
            case 3:
                return "Fisher";
            default:
                return "Error";
        }
    }


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
