package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

public class Chop extends BukkitRunnable {
    final int[] x = {0};
    NPC npc;
    Material material;
    Location loc;
    CustomVillager villager;
    Block block;

    public Chop(NPC npc, Material material, Location loc, CustomVillager villager, Block block) {
        this.npc = npc;
        this.material = material;
        this.loc = loc;
        this.villager = villager;
        this.block = block;
    }

    @Override
    public void run() {
        if (x[0] <= 6) {
            if (block == null || npc.getEntity().isDead()) {
                return;
            }
            else {
                block.setType(Material.AIR);
                block = block.getRelative(BlockFace.UP);
                ++x[0];
            }
        } else {
            return;
        }
    }
}
