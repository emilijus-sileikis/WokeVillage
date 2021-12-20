package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Role;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Move extends BukkitRunnable {
    NPC npc;
    int timeElapsed;
    Location back;

    public Move(NPC npc, int timeElapsed, Location back) {
        this.npc = npc;
        this.timeElapsed = timeElapsed;
        this.back = back;
    }

    @Override
    public void run() {
        Location location = this.npc.getLoc();
        npc.moveFurther(location);

        if (npc.getRole() != Role.MINER) {
            new Invisible(npc).runTaskLater(Main.getInstance(), 100);
            npc.itemReset();
        }

        new Pause(npc, back).runTaskLater(
                Main.getInstance(), (timeElapsed * 20L)
        );
    }
}
