package lt.vu.mif.it.paskui.paskui;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
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
import org.bukkit.entity.Entity;

import java.util.UUID;

public class NPCManager {

    public void createNPC (Entity player, String npcName) {
        Location location = player.getLocation();
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npcName);
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld,gameProfile);

        npc.setLocation(location.getX(), location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

        PlayerConnection connection = ((CraftPlayer) player).getHandle().b; //creates player connection
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc)); //creates tab list
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc)); //spawns npc
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, npc)); //removes tab list
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.aY * 256 / 360)));

        // removes the name tag

        ScoreboardTeam team = new ScoreboardTeam(
                ((CraftScoreboard)Bukkit.getScoreboardManager().getMainScoreboard()).getHandle(),player.getName());
        team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.b);

        PacketPlayOutScoreboardTeam score1 = PacketPlayOutScoreboardTeam.a(team);
        PacketPlayOutScoreboardTeam score2 = PacketPlayOutScoreboardTeam.a(team, true);
        PacketPlayOutScoreboardTeam score3 = PacketPlayOutScoreboardTeam.a(
                team, npc.getName(), PacketPlayOutScoreboardTeam.a.a);

        connection.sendPacket(score1);
        connection.sendPacket(score2);
        connection.sendPacket(score3);
    }

}
