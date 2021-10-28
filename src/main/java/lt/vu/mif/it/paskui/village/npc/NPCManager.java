package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCManager {

    private final HashMap<Integer, NPC> npcs;
    private int var;
    ArrayList<UUID> list = new ArrayList<>();

    public NPCManager() {
        npcs = new HashMap<>();
        var = 1;
    }

    // getters
    public Map<Integer, NPC> getNPCs() {
        return npcs;
    }

    // other
    public void createNPC (Player player, Location loc, EntityType type) { //String skin
        NPC npc = new NPC("", loc);

        if (!spawnNPC(var, npc)) {
            return;
        }

        //Todo: move this to DataManager class
        Main ref = Main.getInstsance();
        String npcData = "data." + var;
        ref.getData().set(npcData + ".id", var);
        ref.getData().set(npcData + ".uuid", npc.getUUID().toString());
        ref.getData().set(npcData + ".name", npc.getName());
        ref.getData().set(npcData + ".x", (int) npc.getLoc().getX());
        ref.getData().set(npcData + ".y", (int) npc.getLoc().getY());
        ref.getData().set(npcData + ".z", (int) npc.getLoc().getZ());
        ref.getData().set(npcData + ".p", npc.getLoc().getPitch());
        ref.getData().set(npcData + ".yaw", npc.getLoc().getYaw());
        ref.getData().set(npcData + ".world", npc.getLoc().getWorld().getName());
//        ref.getData().set(npcData + ".tex", "");
//        ref.getData().set(npcData + ".signature", "");
        ref.saveData();
    }


    /** Loads npc into the world
     * @param location data where npc is located
     */
    public void loadNPC(int id, String name, Location location, UUID uuid) {
        NPC npc = new NPC(name, location, uuid);

        spawnNPC(id, npc);
    }

    //TODO: make this method to delete data.yml data as well
    public void removeNPC(CommandSender sender) {
        Player player = (Player) sender;
        Main ref = Main.getInstsance();
        if ((list.size()-1) < 0) {
            player.sendMessage("There are no npcs to remove!");
            return;
        }

        // TODO: read this through and clean it up.
        if (sender instanceof Player) {
            int i = list.size();
            UUID npc = list.get(list.size()-1);
            player.sendMessage("NPC REMOVED");
            Bukkit.getWorld("world").getEntity(npc).remove();
            list.remove(list.size()-1);
            npcs.remove(list.size()-1);
            ref.getData().set("data." + i, null);
            ref.saveData();
            if (i == 1) {
                ref.data.clearConfig();
            }
            --i;
        }
    }

    public void removeAllNPC() {
        Collection<NPC> npcs = getNPCs().values();

        if (list.isEmpty()) {
            Bukkit.broadcast(Component.text("There are no NPCs to remove!"));
            return;
        }

        for (NPC npc : npcs) {
            Logging.infoLog("Removed npc: %s", npc.toString());
            npc.remove();
            list.clear();
        }
        Bukkit.broadcast(Component.text("All NPCs were removed!"));
        Main.getInstsance().data.clearConfig();
    }

    // private
    private boolean spawnNPC(int id, NPC npc) {
        boolean spawned = npc.spawn();

        if (!spawned) {
            npcs.put(id, npc);
            list.add(npc.getUUID());
            var = id + 1;
        }

        return spawned;
    }
}
