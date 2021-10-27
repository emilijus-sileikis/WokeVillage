package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
