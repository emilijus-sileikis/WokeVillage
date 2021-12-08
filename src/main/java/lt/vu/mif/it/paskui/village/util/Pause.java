package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
        Bukkit.broadcast(Component.text("Pause Over"));
        npc.setVisible();
        npc.moveBack(loc);
    }
}
