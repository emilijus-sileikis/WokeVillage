package lt.vu.mif.it.paskui.paskui;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.List;

public class PacketReader {

    private final Player player;
    private int count = 0;

    public PacketReader(Player player) {

        this.player = player;

    }

    public boolean inject() {

        CraftPlayer nmsPlayer = (CraftPlayer) player;
        Channel channel = nmsPlayer.getHandle().b.a.k;

        if (channel.pipeline().get("PacketInjector") != null)
            return false;

        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<PacketPlayInUseEntity>() {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, PacketPlayInUseEntity packetPlayInUseEntity,
                                  List<Object> list) throws Exception {
                list.add(packetPlayInUseEntity);
                read(packetPlayInUseEntity);

            }
        });
        return true;
    }

    private void read(PacketPlayInUseEntity packetPlayInUseEntity) {
        count++;
        if (count == 4) {
            count = 0;
            int entityID = (int) getValue(packetPlayInUseEntity, "a");
            //call event
            //if (!NPCManager.npcs.containsKey(entityID))
            //return;

            new BukkitRunnable() {

                @Override
                public void run() {
                    Bukkit.getPluginManager().callEvent(new NPCEvent(player, NPCManager.npcs.get(entityID)));
                }

            }.runTask(Main.getPlugin(Main.class));
        }
    }

    private Object getValue(Object instance, String name) {
        Object results = null;
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            results = field.get(instance);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
