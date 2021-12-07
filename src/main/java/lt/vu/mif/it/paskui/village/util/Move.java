package lt.vu.mif.it.paskui.village.util;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.schedule.Activity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

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
        if (npc.getEntity().isDead()) {
            this.cancel();
        }

        final Location back = this.npc.getLocation();
        villager.removeBrain();
        Block block = npc.getCuboid(material);

        if (npc.getRole() == Role.MINER) {
            this.cancel();
            Location loc = this.npc.getLoc();
            villager.getNavigation().moveTo(loc.getX() + 5, loc.getY(), loc.getZ() + 3, 0.5D);

            new Dig(npc, material, loc)
                    .runTaskLater(
                            Main.getInstance(),
                            timeElapsed * 20L
                    );
        }

        if (block != null) {
            this.cancel();
            Location loc = this.npc.getLoc();
            Logging.infoLog("Move to called for NPC");
            villager.getBrain().addActivity(Activity.CORE, ImmutableList.of(
                    Pair.of(0, new Swim(0.8F))));
            villager.getNavigation().moveTo(block.getX(), block.getY(), block.getZ(), 0.5D);
            double dist = villager.distanceTo(material); //10 blocks ~= 10 seconds

            new Chop(npc, material, loc, villager)
                    .runTaskTimer(
                            Main.getInstance(),
                            60 + ((long)dist * 20L),
                            (timeElapsed * 20L) / 6
                    ); //period 80

            new Pause(npc, back).runTaskLater(Main.getInstance(), (timeElapsed * 20L));

        } else {
            Location location = this.npc.getLoc();
            npc.moveFurther(location);
        }
    }
}
