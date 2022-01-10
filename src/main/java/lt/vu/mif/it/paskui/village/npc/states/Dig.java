package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

// TODO: This is only the "demo" variant, change this to something more efficient if possible.
public class Dig extends NPCLocState {
    private final Material material;

    public Dig(NPC npc, Location loc, Material material) {
        super(npc, loc);
        this.material = material;
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
