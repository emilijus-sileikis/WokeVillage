package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Role;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Move extends BukkitRunnable {
    NPC npc;
    int timeElapsed;

    public Move(NPC npc, int timeElapsed) {
        this.npc = npc;
        this.timeElapsed = timeElapsed;
    }

    @Override
    public void run() {
            Location location = this.npc.getLocation();
            npc.moveFurther(location);

            if (npc.getRole() != Role.MINER) {
                new Invisible(npc).runTaskLater(Main.getInstance(), 100);
            }
            new Pause(npc, location)
                    .runTaskLater(
                            Main.getInstance(), (timeElapsed * 20L)
                    );
    }
}
