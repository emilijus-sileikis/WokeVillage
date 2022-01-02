package lt.vu.mif.it.paskui.village.util;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

public class Dig extends BukkitRunnable {
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
        if (npc.getEntity().isDead()) {
            return;
        }

        Location center = new Location(loc.getWorld(), npc.getLoc().getX(), npc.getLoc().getY(), npc.getLoc().getZ() + 7);
        float range = 2;
        float height = 10;
        Location min = new Location(center.getWorld(), center.getX() - range, center.getY() - height, center.getZ() - range);
        Location max = new Location(center.getWorld(), center.getX() + range, center.getY(), center.getZ() + range);
        Block b;

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    b = new Location(loc.getWorld(), x, y, z).getBlock();
                    b.setType(Material.AIR);
                    if (x == max.getBlockX() && z == max.getBlockZ()) {
                        b = new Location(loc.getWorld(), x, y, z).getBlock();
                        for (int a = 0; a < 9; a++) {
                            b.getRelative(BlockFace.DOWN).setType(Material.LADDER);
                        }
                    }
                }
            }
        }
    }
}
