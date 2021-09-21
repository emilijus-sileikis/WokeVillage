package lt.vu.mif.it.paskui.paskui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCManager {

    private static List<EntityPlayer> NPC = new ArrayList<EntityPlayer>();

    public static void createNPC (Player player, String skin) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorld(player.getWorld().getName())).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "" + skin);
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile);
        PlayerInteractManager mng = new PlayerInteractManager(npc);

        npc.setLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

        String[] name = getSkin(player, skin);
        gameProfile.getProperties().put("textures", new Property("textures", name[0], name[1]));

        addNPCPacket(npc);
        NPC.add(npc);

        // removes the name tag

        ScoreboardTeam team = new ScoreboardTeam(((CraftScoreboard) Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(), player.getName());
        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.b);
        PacketPlayOutScoreboardTeam score1 = PacketPlayOutScoreboardTeam.a(team);
        PacketPlayOutScoreboardTeam score2 = PacketPlayOutScoreboardTeam.a(team, true);
        PacketPlayOutScoreboardTeam score3 = PacketPlayOutScoreboardTeam.a(team, gameProfile.getName(), PacketPlayOutScoreboardTeam.a.a);
        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
        connection.sendPacket(score1);
        connection.sendPacket(score2);
        connection.sendPacket(score3);

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
            Main.getData().set("data." + var + ".tex", name[0]);
            Main.getData().set("data." + var + ".signature", name[1]);
            Main.saveData();
        }

    }

    public static void loadNPC(Location location, GameProfile profile) {

        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        GameProfile gameProfile = profile;
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile);
        PlayerInteractManager mng = new PlayerInteractManager(npc);

        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        addNPCPacket(npc);
        NPC.add(npc);

    }

    private static String[] getSkin(Player player, String name) {

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

    }

    public static void addNPCPacket(EntityPlayer npc) {

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc)); //creates tab list
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc)); //spawns npc
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, npc)); //removes tab list
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.aY * 256 / 360)));
        }
    }

    public static void addJoinPacket(Player player) {

        for (EntityPlayer npc : NPC) {

            PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc)); //creates tab list
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc)); //spawns npc
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, npc)); //removes tab list
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.aY * 256 / 360)));
        }
    }

    public  static void removeNPC(Player player, EntityPlayer npc) {

        PlayerConnection connection = ((CraftPlayer)player).getHandle().b;
        connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));

    }

    public static List<EntityPlayer> getNPCs() {
        return NPC;
    }
}
