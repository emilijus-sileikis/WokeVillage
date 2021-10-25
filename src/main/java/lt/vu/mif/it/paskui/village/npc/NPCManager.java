package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.Main;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCManager {

    private final Map<UUID, NPC> npcs;
    private int var;

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

    //TODO: implement usage of this method
    public void removeNPC(Player player, ServerPlayer npc) {
        Connection connection = ((CraftPlayer)player).getHandle().networkManager;
        connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
    }

    // private
    private boolean spawnNPC(NPC npc) {
        boolean spawned = npc.spawn();

        if (spawned) this.npcs.put(npc.getUUID(), npc);

        return spawned;
    }
}
