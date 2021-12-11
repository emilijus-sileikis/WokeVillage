package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

public class Teleport extends BukkitRunnable {
    NPC npc;
    Location loc;

    public Teleport(NPC npc, Location loc) {
        this.npc = npc;
        this.loc = loc;
    }

    @Override
    public void run() {
        if (npc.getEntity().isInWaterOrBubbleColumn()) { npc.getEntity().teleport(loc); }
    }
}