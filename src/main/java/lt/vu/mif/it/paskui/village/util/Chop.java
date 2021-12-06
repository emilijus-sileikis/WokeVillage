package lt.vu.mif.it.paskui.village.util;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import net.kyori.adventure.text.Component;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.schedule.Activity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Chop extends BukkitRunnable {
    final int[] x = {0};
    NPC npc;
    Material material;
    Location loc;
    CustomVillager villager;

    public Chop(NPC npc, Material material, Location loc, CustomVillager villager) {
        this.npc = npc;
        this.material = material;
        this.loc = loc;
        this.villager = villager;
    }

    @Override
    public void run() {
        if (this.villager.isInWaterOrBubble()) {
            this.villager.getBrain().addActivity(Activity.CORE, ImmutableList.of(
                    Pair.of(0, new Swim(0.8F))));
        }
        if (x[0] <= 6) {
            Block block = npc.getCuboid(material);
            if (block == null) {
                this.cancel();
            }
            else {
                block.setType(Material.AIR);
                ++x[0];
            }
        } else {
            this.cancel();
        }
    }
}
