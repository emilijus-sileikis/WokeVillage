package lt.vu.mif.it.paskui.paskui;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NPCEvent extends Event{
    private final Player player;
    private final EntityPlayer npc;

    private static final HandlerList HANDLERS = new HandlerList();

    public NPCEvent(Player player, EntityPlayer npc) {
        this.player = player;
        this.npc = npc;
    }

    @Override
    public HandlerList getHandlers() {

        return HANDLERS;
    }

    public static HandlerList getHandlerList() {

        return HANDLERS;
    }

    public Player getPlayer() {

        return player;
    }

    public EntityPlayer getNpc() {

        return npc;
    }
}
