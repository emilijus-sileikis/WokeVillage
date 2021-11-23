package lt.vu.mif.it.paskui.village.npc.events;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class NPCEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final NPC npc;

    public NPCEvent(NPC npc) {
        super();

        this.npc = npc;
    }

    public NPC getNpc() {
        return npc;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
