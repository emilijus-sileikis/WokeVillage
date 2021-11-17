package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
     * @param player Player that spawns the NPC
     * @param loc    initial location of NPC and in which world.
     * @param type   NPC entity type
     * @return Spawned NPC instance on success, null on fail.
     */
    public NPC createNPC(Player player, Location loc, EntityType type) { //String skin
        int id = npcIds.isEmpty() ? 0 : npcIds.getLast() + 1;
        NPC npc = new NPC(id,"", loc, Role.getRandomRole(), Personality.getRandomPersonality());

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

    // other
    /**
     * Checks if there is a specific block in a radius
     * @return returns the vector which the NPC will use for walking to the log.
     */
    public Vec3 getCuboid(Material material, Role role) {
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

        NPC npc = npcs.get(npcIds.getLast());
        Location center = npc.getLoc();
        float radius = 16;
        Location minimum = new Location(center.getWorld(), center.getX() - (radius / 2), center.getY() - (radius / 2), center.getZ() - (radius / 2));
        Location maximum = new Location(center.getWorld(), center.getX() + (radius / 2), center.getY() + (radius / 2), center.getZ() + (radius / 2));
        Block b;
        Vec3 v;

        for(int x = minimum.getBlockX(); x <= maximum.getBlockX(); x++) {
            for(int y = minimum.getBlockY(); y <= maximum.getBlockY(); y++) {
                for(int z = minimum.getBlockZ(); z <= maximum.getBlockZ(); z++) {
                    b = new Location(center.getWorld(), x, y, z).getBlock();
                    if (b.getType() == material) {
                        Bukkit.broadcast(Component.text(":D Found at: X=" + x + " " + "Y=" + y + " " + "Z=" + z));
                        v = new Vec3((x + 1.3), (y + 1.3), z);
                        Bukkit.broadcast(Component.text("Move to: " + v));
                        return v;
                    }
                }
            }
        }
        return null;
    }
}
