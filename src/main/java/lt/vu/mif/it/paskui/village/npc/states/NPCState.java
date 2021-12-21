package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * BukkitRunnable implemented as NPC state.
 */
public abstract class NPCState extends BukkitRunnable {

    protected final NPC npc;

    public NPCState(final NPC npc) {
        this.npc = npc;
    }
}
