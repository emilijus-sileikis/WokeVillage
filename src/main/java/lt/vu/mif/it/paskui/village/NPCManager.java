package lt.vu.mif.it.paskui.village;

import com.mojang.authlib.GameProfile;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class NPCManager {

    //TODO: fix error
    public static Map<Integer, ServerPlayer> npcs = new HashMap<>();

    public static void createNPC (Player player, String skin) {
        Location location = player.getLocation();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ""); //+ skin

        // NPC setup
        ServerPlayer npc = new ServerPlayer(
               ((CraftServer) Bukkit.getServer()).getServer(),
               ((CraftWorld) player.getWorld()).getHandle(),
               gameProfile
        );

        npc.setPos(location.getX(), location.getY(), location.getZ());

        //
        Arrays.stream(player.getInventory().getContents()).forEach(
                itemStack -> npc.getInventory().setPickedItem(CraftItemStack.asNMSCopy(itemStack))
        );
        // Instantiates NPC
        addNPCPacket(npc);
        npcs.put(npc.getId(), npc);

        int var = 1;
        if ( Main.getData().contains("data"))
            var = Objects.requireNonNull(Main.getData().getConfigurationSection("data")).
                    getKeys(false).size() + 1;

            Main.getData().set("data." + var + ".x", (int) player.getLocation().getX());
            Main.getData().set("data." + var + ".y", (int) player.getLocation().getY());
            Main.getData().set("data." + var + ".z", (int) player.getLocation().getZ());
            Main.getData().set("data." + var + ".p", player.getLocation().getPitch());
            Main.getData().set("data." + var + ".yaw", player.getLocation().getYaw());
            Main.getData().set("data." + var + ".world", player.getLocation().getWorld().getName());
            Main.getData().set("data." + var + ".name", skin);
            Main.getData().set("data." + var + ".tex", "");
            Main.getData().set("data." + var + ".signature", "");
            Main.saveData();
    }

    public static void loadNPC(Location location, GameProfile profile) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = profile;
        ServerPlayer npc = new ServerPlayer(nmsServer, nmsWorld, gameProfile);

        npc.setPos(location.getX(), location.getY(), location.getZ());

        addNPCPacket(npc);
        npcs.put(npc.getId(), npc);
    }

    public static void addNPCPacket(ServerPlayer npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Connection connection = ((CraftPlayer) player).getHandle().networkManager; //creates player connection

            //creates tab list
            connection.send( new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc) );
            //spawns npc
            connection.send( new ClientboundAddPlayerPacket(npc) );
            //removes tab list
            connection.send( new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc) );
        }
    }

    public static void addJoinPacket(Player player) {
        for (ServerPlayer npc : npcs.values()) {
            //creates connection to player
            Connection connection = ((CraftPlayer) player).getHandle().networkManager;

            //creates tab list
            connection.send( new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc) );
            //spawns npc
            connection.send( new ClientboundAddPlayerPacket(npc) );
            //removes tab list
            connection.send( new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc) );

            //TODO: Code below might be relevant for 'onJoin/rejoin' event:
            /*ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), player.getName());
            team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.b);
            PacketPlayOutScoreboardTeam score1 = PacketPlayOutScoreboardTeam.a(team);
            PacketPlayOutScoreboardTeam score2 = PacketPlayOutScoreboardTeam.a(team, true);
            PacketPlayOutScoreboardTeam score3 = PacketPlayOutScoreboardTeam.a(team, player.getName(), PacketPlayOutScoreboardTeam.a.a);
            PlayerConnection connection1 = ((CraftPlayer) player).getHandle().b;
            connection1.sendPacket(score1);
            connection1.sendPacket(score2);
            connection1.sendPacket(score3);*/
        }
    }

    //TODO: implement this function in command
    public  static void removeNPC(Player player, ServerPlayer npc) {
        Connection connection = ((CraftPlayer)player).getHandle().networkManager;
        connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));
    }

    public static Map<Integer, ServerPlayer> getNPCs() {
        return npcs;
    }
}
