package lt.vu.mif.it.paskui.paskui;

import com.mojang.authlib.GameProfile;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.Player;

import java.util.*;

public class NPCManager {

    public static Map<Integer, ServerPlayer> npcs = new HashMap<>();

    public static void createNPC (Player player, String skin) {

        Location location = player.getLocation();
        ServerPlayer craftPlayer = ((CraftPlayer)player).getHandle();

        // NPC textures
        //Property textures = (Property) craftPlayer.getProfile().getProperties().get("textures").toArray()[0];
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ""); //+ skin
        //gameProfile.getProperties().put("textures", new Property("textures", textures.getValue(), textures.getSignature()));

       ServerPlayer npc = new ServerPlayer(

               ((CraftServer) Bukkit.getServer()).getServer(),
               ((CraftWorld) player.getWorld()).getHandle(),
               gameProfile) {
       };

        npc.setPos(location.getX(), location.getY(), location.getZ());

        //String[] name = getSkin(player, skin);
        //gameProfile.getProperties().put("textures", new Property("textures", name[0], name[1]));

        Arrays.stream(player.getInventory().getContents()).forEach(itemStack -> {

            npc.getInventory().setPickedItem(CraftItemStack.asNMSCopy(itemStack));

        });

        // removes the name tag

        /*for (Player player1 : Bukkit.getOnlinePlayers()) {

            Scoreboard team = new Scoreboard(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard().getScores()));
            team.setDisplayObjective(Scoreboard.EnumNameTagVisibility.b);
            PacketPlayOutScoreboardTeam score1 = PacketPlayOutScoreboardTeam.a(team);
            PacketPlayOutScoreboardTeam score2 = PacketPlayOutScoreboardTeam.a(team, true);
            PacketPlayOutScoreboardTeam score3 = PacketPlayOutScoreboardTeam.a(team, gameProfile.getName(), PacketPlayOutScoreboardTeam.a.a);
            PlayerConnection connection = ((CraftPlayer) player1).getHandle().b;
            connection.sendPacket(score1);
            connection.sendPacket(score2);
            connection.sendPacket(score3);

        }*/

        int var = 1;
        if ( Main.data.getDataConfig().contains("data")) {

            var = Main.data.getDataConfig().getConfigurationSection("data").getKeys(false).size() + 1;

            Main.getData().set("data." + var + ".x", (int) player.getLocation().getX());
            Main.getData().set("data." + var + ".y", (int) player.getLocation().getY());
            Main.getData().set("data." + var + ".z", (int) player.getLocation().getZ());
            Main.getData().set("data." + var + ".p", (int) player.getLocation().getPitch());
            Main.getData().set("data." + var + ".y", player.getLocation().getYaw());
            Main.getData().set("data." + var + ".world", player.getLocation().getWorld().getName());
            Main.getData().set("data." + var + ".name", skin);
            Main.getData().set("data." + var + ".tex", "");
            Main.getData().set("data." + var + ".signature", "");
            Main.saveData();
        }

        addNPCPacket(npc);
        npcs.put(npc.getId(), npc);




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

    /*private static String[] getSkin(Player player, String name) {

        try {

            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = new JsonParser().parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();
            return new String[] {texture, signature};
        } catch (Exception e) {

                EntityPlayer p = ((CraftPlayer) player).getHandle();
                GameProfile profile = p.getProfile();
                Property property = profile.getProperties().get("textures").iterator().next();
                String texture = property.getValue();
                String signature = property.getSignature();
                return new String[] {texture, signature};

        }

    }*/

    public static void addNPCPacket(ServerPlayer npc) {

        for (Player player : Bukkit.getOnlinePlayers()) {
            Connection connection = ((CraftPlayer) player).getHandle().networkManager; //creates player connection
            connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc)); //creates tab list
            connection.send(new ClientboundAddPlayerPacket(npc)); //spawns npc
            connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc)); //removes tab list
        }
    }

    public static void addJoinPacket(Player player) {

        for (ServerPlayer npc : npcs.values()) {

            Connection connection = ((CraftPlayer) player).getHandle().networkManager; //creates player connection
            connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc)); //creates tab list
            connection.send(new ClientboundAddPlayerPacket(npc)); //spawns npc
            connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc)); //removes tab list

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

    public  static void removeNPC(Player player, ServerPlayer npc) {

        Connection connection = ((CraftPlayer)player).getHandle().networkManager;
        connection.send(new ClientboundRemoveEntitiesPacket(npc.getId()));

    }

    public static Map<Integer, ServerPlayer> getNPCs() {
        return npcs;
    }
}
