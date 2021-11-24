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
        if (x[0] <= 4) {
            if (npc.getCuboid(material) == null) {
                this.cancel();
            }
            else {
                Vec3 pos = npc.getCuboid(material);
                Block block;
                block = new Location(loc.getWorld(), pos.x - 1.3, pos.y, pos.z).getBlock();
                block.setType(Material.AIR);
                ++x[0];
            }
        } else {
            this.cancel();
        }
    }
}
