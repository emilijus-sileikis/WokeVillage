package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Pause extends BukkitRunnable {
    NPC npc;
    Location loc;

    public Pause(NPC npc, Location loc) {
        this.npc = npc;
        this.loc = loc;
    }

    @Override
    public void run() {
        npc.setVisible();
        npc.moveBack(loc);
        npc.goods(npc.getRole());
        new Teleport(npc, loc).runTaskLater(Main.getInstance(), 80);
    }
}