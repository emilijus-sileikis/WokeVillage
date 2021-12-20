package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.scheduler.BukkitRunnable;

public class Invisible extends BukkitRunnable {
    NPC npc;

    public Invisible(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void run() {
        npc.setInvisible();
    }
}
