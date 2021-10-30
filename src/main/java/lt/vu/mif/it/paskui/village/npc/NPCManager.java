package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

public class NPCManager {

    private final HashMap<Integer, NPC> npcs;
    private final LinkedList<Integer> npcKeys;

    public NPCManager() {
        npcs = new HashMap<>();
        npcKeys = new LinkedList<>();
    }

    // getters
    public Map<Integer, NPC> getNPCs() {
        return npcs;
    }

    // other
    public void createNPC (Player player, Location loc, EntityType type) { //String skin
        NPC npc = new NPC("", loc);
        int id = npcKeys.isEmpty() ? 0 : npcKeys.getLast() + 1;

        if (!spawnNPC(id, npc)) {
            return;
        }
        Main.getInstsance().data.writeData(npc, id);
    }


    /** Loads npc into the world
     * @param location data where npc is located
     */
    public void loadNPC(int id, String name, Location location, UUID uuid) {
        NPC npc = new NPC(name, location, uuid);

        spawnNPC(id, npc);
    }

    /**
     * removes newest NPC
     */
    public void removeNPC(CommandSender sender) {
        Main ref = Main.getInstsance();

        if (npcKeys.isEmpty() || npcs.isEmpty()) {
            sender.sendMessage("There are no NPCs to remove!");
            return;
        }

        int id = npcKeys.getLast();
        npcs.get(id).remove();

        ref.getData().set("data." + id, null);
        ref.saveData();

        npcs.remove(id);
        npcKeys.removeLast();

        sender.sendMessage(String.format("NPC %d was removed.", id));
    }

    /**
     * Removes all NPC entities.
     */
    public void removeAllNPC() {
        if (npcKeys.isEmpty() || this.npcs.isEmpty()) {
            Bukkit.broadcast(Component.text("There are no NPCs to remove!"));
            return;
        }

        while (!npcKeys.isEmpty()) {
            removeNPC(Bukkit.getConsoleSender());
            Bukkit.broadcast(Component.text("All NPCs have been removed!"));
        }
    }

    // private
    private boolean spawnNPC(int id, NPC npc) {
        boolean spawned = npc.spawn();

        if (spawned) {
            npcs.put(id, npc);
            npcKeys.add(id);
        }

        return spawned;
    }
}
