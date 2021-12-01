package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import net.kyori.adventure.text.Component;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Move extends BukkitRunnable {
    NPC npc;
    Material material;
    CustomVillager villager;
    int timeElapsed;

    public Move(NPC npc, Material material, CustomVillager villager, int timeElapsed) {
        this.npc = npc;
        this.material = material;
        this.villager = villager;
        this.timeElapsed = timeElapsed;
    }

    @Override
    public void run() {
        // Lumberjack - logs
        // fisher - water
        // Use loot tables to get the target block

        if (npc.getCuboid(material) != null) {
            cancel();
            Location loc = this.npc.getLoc();
            Vec3 finish = this.npc.getCuboid(material);
            Logging.infoLog("Move to called for NPC");
            villager.removeBrain();
            villager.getNavigation().moveTo(finish.x, finish.y, finish.z, 0.5D);
            Double dist = villager.distanceTo(material); //10 blocks ~= 10 seconds

            Bukkit.broadcast(Component.text("Distance: " + dist));

            BukkitTask chop = new Chop(npc, material, loc).runTaskTimer(Main.getInstance(), 60 + (dist.longValue() * 20L), 80);

            if (!(Bukkit.getScheduler().isCurrentlyRunning(chop.getTaskId()))) { //ifas neveikia
                BukkitTask wait = new Pause(npc, loc).runTaskLater(Main.getInstance(), (timeElapsed * 20L) + (dist.longValue() * 20L));
            }
        } else {
            Bukkit.broadcast(Component.text("Moving further..."));
            Location location = this.npc.getLoc();
            Bukkit.broadcast(Component.text("Start Location: " + location.toString()));
            npc.moveFurther(location);
        }
    }
}
