package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * BukkitRunnable implemented as NPC state.
 */
public abstract class NPCState extends BukkitRunnable {

    protected final NPC npc;

    public NPCState(final NPC npc) {
        this.npc = npc;
    }

    /**
     * Abstraction of {@link #runTaskLater(Plugin, long)} which defaults
     * Plugin to {@link Main}.
     * @param delay the ticks to wait before running the task.
     * @return a NPCState that contains the id number.
     * @throws IllegalArgumentException if plugin is null.
     * @throws IllegalStateException if this was already scheduled.
     */
    public synchronized NPCState runTaskLater(final long delay)
            throws IllegalArgumentException, IllegalStateException {
        return (NPCState) this.runTaskLater(Main.getInstance(), delay);
    }

    /**
     * Abstraction of {@link #runTaskTimer(Plugin, long, long)} which defaults
     * Plugin to {@link Main}.
     * @param delay the ticks to wait before running the task.
     * @param period the ticks to wait between runs.
     * @return a NPCState that contains the id number.
     * @throws IllegalArgumentException if plugin is null.
     * @throws IllegalStateException if this was already scheduled.
     */
    public synchronized NPCState runTaskTimer(final long delay, final long period)
            throws IllegalArgumentException, IllegalStateException {
        return (NPCState) this.runTaskTimer(Main.getInstance(), delay, period);
    }
}
