package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ScheduledExecutorService;

public class Failure extends BukkitRunnable {
    Player p;
    ScheduledExecutorService scheduler;
    Location loc;
    NPC npc;

    public Failure(Player p, ScheduledExecutorService scheduler, Location loc, NPC npc) {
        this.p = p;
        this.scheduler = scheduler;
        this.loc = loc;
        this.npc = npc;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
        }
        p.sendMessage(Component.text("Your items have been lost! The trader suffered an accident...")
                .color(NamedTextColor.RED));
        scheduler.shutdown();
        ServerLevel world = ((CraftWorld) loc.getWorld()).getHandle();
        npc.refreshBrains(world);
        npc.setKillable();
        npc.setVisible();
    }
}