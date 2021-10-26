package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

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
        Main.getData().set("data." + var + ".x", (int) player.getLocation().getX());
        Main.getData().set("data." + var + ".y", (int) player.getLocation().getY());
        Main.getData().set("data." + var + ".z", (int) player.getLocation().getZ());
        Main.getData().set("data." + var + ".p", player.getLocation().getPitch());
        Main.getData().set("data." + var + ".yaw", player.getLocation().getYaw());
        Main.getData().set("data." + var + ".world", player.getLocation().getWorld().getName());
        Main.getData().set("data." + var + ".name", ""); //+skin
        //Main.getData().set("data." + var + ".id", list.get(var-1));
        Main.getData().set("data." + var + ".tex", "");
        Main.getData().set("data." + var + ".signature", "");
        Main.saveData();
        ++var;
    }


    /** Loads npc into the world
     * @param location data where npc is located
     */
    public void loadNPC(Location location) {
        NPC npc = new NPC("", location);

        spawnNPC(npc);
    }

    //TODO: make this method to delete data.yml data as well
    public void removeNPC(CommandSender sender) {
        Player player = (Player) sender;
        if ((list.size()-1) < 0) {
            player.sendMessage("There are no npcs to remove!");
            return;
        }

        if (sender instanceof Player) {
            UUID npc = list.get(list.size()-1);
            player.sendMessage("NPC REMOVED");
            Bukkit.getWorld("world").getEntity(npc).remove();
            list.remove(list.size()-1);
        }
    }

    // private
    private boolean spawnNPC(NPC npc) {
        boolean spawned = npc.spawn();

        if (spawned) this.npcs.put(npc.getUUID(), npc);

        return spawned;
    }
}
