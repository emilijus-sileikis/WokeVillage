package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

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
    /**
     * Checks whether there is any NPCs existing.
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
     * Creates NPC and attempts to spawn it.
     * @param name        name given to NPC.
     * @param loc         initial location of NPC and in which world.
     * @param role        given {@link Role} to {@link NPC}.
     * @param personality given {@link Personality} to {@link NPC}.
     * @return Spawned NPC instance on success, null on fail.
     */
    public NPC createNPC(
            final @NotNull String name,
            final @NotNull Location loc,
            final @NotNull Role role,
            final @NotNull Personality personality
    ) {
        int id = npcIds.isEmpty() ? 0 : npcIds.getLast() + 1;
        NPC npc = new NPC(id, name, loc, role, personality);

        return spawnNPC(npc);
    }

    /**
     * Loads npc into the world.
     * @param id unique npc id number
     * @param name name npc is called
     * @param location data where npc is located
     * @param uuid unique npc id in NMS World
     * @param role npc occupation
     * @param personality npc persona type
     */
    public void loadNPC(int id, String name, Location location, UUID uuid, Role role, Personality personality) {
        NPC npc = new NPC(id, name, location, uuid, role, personality);

        if (spawnNPC(npc) == null) {
            Logging.severeLog("Unable to spawn NPC %d", id);
        }
    }

    /**
     * Deletes stored {@link NPC} from {@link NPCManager} entries.
     * @param npc {@link NPC} instance existing in {@link NPCManager}.
     */
    public void deleteNPC(NPC npc) {
        npcs.remove(npc.getId());
        npcIds.removeFirstOccurrence(npc.getId());

        Bukkit.broadcast(Component.text("NPC was removed!"));
    }

    /**
     * Removes {@link NPC}, with given id, from the world and then deletes
     * it from {@link NPCManager} entries.
     * @param id valid id of npc.
     */
    public void removeNPC(int id) {
        npcs.get(id).remove();

        this.deleteNPC(npcs.get(id));
    }

    /**
     * Removes all {@link NPC} entities from world then deletes them
     * from {@link NPCManager} entries.
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
     * @param npc NPC to spawn.
     * @return Instance of NPC on success, null on failure.
     */
    private NPC spawnNPC(NPC npc) {
        boolean spawned = npc.spawn();

        if (spawned) {
            npcs.put(npc.getId(), npc);
            npcIds.add(npc.getId());
            return npc;
        }

        return null;
    }
}
