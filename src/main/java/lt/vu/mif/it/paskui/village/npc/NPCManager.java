package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.server.BroadcastMessageEvent;

import java.util.*;

public class NPCManager {

    private final Map<UUID, NPC> npcs;
    private int var;
    ArrayList<UUID> list = new ArrayList<>();

    public NPCManager() {
        npcs = new HashMap<>();
        var = 1;
    }

    // getters
    public Map<UUID, NPC> getNPCs() {
        return npcs;
    }

    // other
    public void createNPC (Player player, Location loc, EntityType type) { //String skin
        NPC npc = new NPC("", loc);
        list.add(npc.getUUID());

        if (!spawnNPC(npc)) {
            return;
        }

        //Todo:Maybe move this to DataManager class
        Main ref = Main.getInstsance();
        ref.getData().set("data." + var + ".x", (int) player.getLocation().getX());
        ref.getData().set("data." + var + ".y", (int) player.getLocation().getY());
        ref.getData().set("data." + var + ".z", (int) player.getLocation().getZ());
        ref.getData().set("data." + var + ".p", player.getLocation().getPitch());
        ref.getData().set("data." + var + ".yaw", player.getLocation().getYaw());
        ref.getData().set("data." + var + ".world", player.getLocation().getWorld().getName());
        ref.getData().set("data." + var + ".name", ""); //+skin
        //ref.getData().set("data." + var + ".id", list.get(var-1));
        ref.getData().set("data." + var + ".tex", "");
        ref.getData().set("data." + var + ".signature", "");
        ref.saveData();
        ++var;
    }


    /** Loads npc into the world
     * @param location data where npc is located
     */
    public void loadNPC(Location location) {
        NPC npc = new NPC("", location);

        spawnNPC(npc);
        list.add(npc.getUUID());
    }

    //TODO: make this method to delete data.yml data as well
    public void removeNPC(CommandSender sender) {
        Player player = (Player) sender;
        Main ref = Main.getInstsance();
        if ((list.size()-1) < 0) {
            player.sendMessage("There are no npcs to remove!");
            return;
        }

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

    // private
    private boolean spawnNPC(NPC npc) {
        boolean spawned = npc.spawn();

        if (spawned) this.npcs.put(npc.getUUID(), npc);

        return spawned;
    }

    public void despawnAllNPC() {
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
}
