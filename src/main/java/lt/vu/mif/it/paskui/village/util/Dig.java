package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.npc.NPC;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

// TODO: This is only the ,,demo'' variant, change this to something more efficient if possible.
public class Dig extends BukkitRunnable {
    final int[] x = {0};
    NPC npc;
    Material material;
    Location loc;

    public Dig(NPC npc, Material material, Location loc) {
        this.npc = npc;
        this.material = material;
        this.loc = loc;
    }

    @Override
    public void run() {
        if (x[0] <= 999) {
            if (npc.getCuboid(material) == null) {
                this.cancel();
            }
            else {
                Block block = npc.getCuboid(material);
                block.setType(Material.AIR);
                ++x[0];
            }
        } else {
            this.cancel();
        }
    }
}
