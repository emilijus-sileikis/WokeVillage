package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.npc.NPC;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class Chop extends BukkitRunnable {
    final int[] x = {0};
    NPC npc;
    Material material;
    Location loc;

    public Chop(NPC npc, Material material, Location loc) {
        this.npc = npc;
        this.material = material;
        this.loc = loc;
    }

    @Override
    public void run() {
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
