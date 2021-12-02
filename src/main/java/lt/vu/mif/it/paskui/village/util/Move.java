package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import lt.vu.mif.it.paskui.village.npc.services.tables.FisherLootTable;
import lt.vu.mif.it.paskui.village.npc.services.tables.LumberjackLootTable;
import lt.vu.mif.it.paskui.village.npc.services.tables.MinerLootTable;
import net.kyori.adventure.text.Component;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import static lt.vu.mif.it.paskui.village.EventListen.random_int;

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

    // TODO: Change this so it could work with all roles (lumberjack chops trees and moves further, fisher goes to a water source and stays there, miner mines a hole).
    @Override
    public void run() {

        if (npc.getRole() == Role.MINER) {
            cancel();
            Location loc = this.npc.getLoc();
            Vec3 finish = new Vec3(loc.getX() + 5, loc.getY(), loc.getZ() + 3);
            villager.removeBrain();
            villager.getNavigation().moveTo(finish.x, finish.y, finish.z, 0.5D);
            // TODO: Make the NPC dig a 10x10x10 hole
            //BukkitTask dig = new Dig();
        }

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
            BukkitTask wait = new Pause(npc, loc).runTaskLater(Main.getInstance(), (timeElapsed * 20L) + (dist.longValue() * 20L));

        } else {
            Bukkit.broadcast(Component.text("Moving further..."));
            Location location = this.npc.getLoc();
            Bukkit.broadcast(Component.text("Start Location: " + location.toString()));
            npc.moveFurther(location);
        }
    }
}
