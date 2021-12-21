package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.level.ServerLevel;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.concurrent.ScheduledExecutorService;

public class Failure extends NPCLocState {
    private final Player p;
    private final ScheduledExecutorService scheduler;

    public Failure(NPC npc, Location loc, Player p, ScheduledExecutorService scheduler) {
        super(npc, loc);
        this.p = p;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        if (npc.getEntity().isDead()) {
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spawnParticle(Particle.CRIT_MAGIC, loc, 100);
        }
        p.sendMessage(Component.text("Your items have been lost! The trader suffered an accident...")
                .color(NamedTextColor.RED));
        scheduler.shutdown();
        ServerLevel world = ((CraftWorld) loc.getWorld()).getHandle();
        npc.refreshBrains(world);
        npc.setKillable();
        npc.setCollidable();
        npc.setVisible();
        npc.itemReset();
    }
}