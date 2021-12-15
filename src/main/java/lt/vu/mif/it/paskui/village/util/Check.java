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

public class Check extends BukkitRunnable {
    NPC npc;
    Material material;
    CustomVillager villager;
    int timeElapsed;

    public Check(NPC npc, Material material, CustomVillager villager, int timeElapsed) {
        this.npc = npc;
        this.material = material;
        this.villager = villager;
        this.timeElapsed = timeElapsed;
    }

    @Override
    public void run() {
        this.villager.setInvulnerable(true);
        this.npc.setNonCollidable();
        Location back = this.npc.getLoc();
        this.npc.setWorkHand();
        villager.removeBrain();
        Block block = npc.searchMaterials(material);
        villager.getBrain().addActivity(Activity.CORE, ImmutableList.of(
                Pair.of(0, new Swim(0.8f))));

        if (npc.getRole() == Role.MINER) {
            Location loc = this.npc.getLoc();
            villager.getNavigation().moveTo(loc.getX() + 5, loc.getY(), loc.getZ() + 3, 0.5D);

            new Dig(npc, material, loc)
                    .runTaskLater(
                            Main.getInstance(),
                            timeElapsed * 20L
                    );
        }

        if (block != null) {
            Location loc = this.npc.getLoc();
            Logging.infoLog("Move to called for NPC");
            villager.getNavigation().moveTo(block.getX(), block.getY(), block.getZ(), 0.5D);
            double dist = villager.distanceTo(material); //10 blocks ~= 10 seconds

            new Chop(villager, material, block)
                    .runTaskTimer(
                            Main.getInstance(),
                            60 + ((long)dist * 20L),
                            (timeElapsed * 20L) / 7
                    );

            new Pause(npc, back)
                    .runTaskLater(
                            Main.getInstance(), (timeElapsed * 20L)
                    );
        }
        else {
            new Move(npc, timeElapsed, back)
                    .runTaskLater(
                            Main.getInstance(), 60
                    );
        }
    }
}
