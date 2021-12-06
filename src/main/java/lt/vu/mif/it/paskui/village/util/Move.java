package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

    // TODO: Change this so it could work with all roles (lumberjack chops trees and moves further, fisher goes to a water source and stays there, miner mines a hole).
    @Override
    public void run() {
        final Location back = this.npc.getLocation();

        /*
        if (npc.getRole() == Role.MINER) {
            cancel();
            Location loc = this.npc.getLoc();
            Vec3 finish = new Vec3(loc.getX() + 5, loc.getY(), loc.getZ() + 3);
            villager.removeBrain();
            villager.getNavigation().moveTo(finish.x, finish.y, finish.z, 0.5D);
            // TODO: Make the NPC dig a 10x10x10 hole
            //BukkitTask dig = new Dig();
        }
        */

        Block block = npc.getCuboid(material);

        if (block != null) {
            cancel();
            Location loc = this.npc.getLoc();
            Logging.infoLog("Move to called for NPC");
            villager.removeBrain();
            villager.getNavigation().moveTo(block.getX(), block.getY(), block.getZ(), 0.5D);
            double dist = villager.distanceTo(material); //10 blocks ~= 10 seconds

            BukkitTask chop = new Chop(npc, material, loc, villager)
                    .runTaskTimer(
                            Main.getInstance(),
                            60 + ((long)dist * 20L),
                            (timeElapsed * 20L) / 6
                    ); //period 80

            BukkitTask wait = new Pause(npc, back).runTaskLater(Main.getInstance(), (timeElapsed * 20L));
        } else {
            Location location = this.npc.getLoc();
            npc.moveFurther(location);
        }
    }
}
