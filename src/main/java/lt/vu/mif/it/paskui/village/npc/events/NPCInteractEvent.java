package lt.vu.mif.it.paskui.village.npc.events;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.entity.Player;

public class NPCInteractEvent extends NPCEvent {

    private final Player player;

    public NPCInteractEvent(Player player, NPC npc) {
        super(npc);

        this.player = player;
    }

    // public
    public Player getPlayer() {
        return player;
    }
}
