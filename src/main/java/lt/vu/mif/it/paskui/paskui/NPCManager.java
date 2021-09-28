package lt.vu.mif.it.paskui.paskui;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCManager {

    public static Map<Integer, ServerPlayer> npcs = new HashMap<>();

    public static void createNPC (Player player, String skin) {
        Location location = player.getLocation();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");

        // NPC setup
        ServerPlayer npc = new ServerPlayer(
                ((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) player.getWorld()).getHandle(),
                gameProfile
        );

        npc.setPos(location.getX(), location.getY(), location.getZ());

        addNPCPacket(npc);
        npcs.put(npc.getId(), npc);
    }

    public static void addNPCPacket(ServerPlayer npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Connection connection = ((CraftPlayer) player).getHandle().networkManager; //creates player connection
            connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc)); //creates tab list
            connection.send(new ClientboundAddPlayerPacket(npc)); //spawns npc
            connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc)); //removes tab list
        }
    }
}