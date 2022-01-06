package lt.vu.mif.it.paskui.village.npc.ai;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.util.Vector3;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Checks if there is a specific block in a certain radius.
 * @return returns the block which the NPC will use to locate materials.
 */
public class SearchMaterials {
    private final NPC npc;
    private Location loc;

    public SearchMaterials(NPC npc, Location loc) {
        this.npc = npc;
        this.loc = loc;
    }

    public Block searchMaterials(final @NotNull Material material) {
        npc.updateLocation();

        final int RADIUS = 8;
        final World WORLD = this.loc.getWorld();
        final Vector3 START = new Vector3(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        final Vector3 MIN = START.subtract(RADIUS);
        final Vector3 MAX = START.add(RADIUS);

        LinkedList<Block> opened = new LinkedList<>(List.of(
                this.loc.getBlock(),
                this.loc.toBlockLocation().add(0, 1, 0).getBlock()
        ));
        LinkedList<Block> closed = new LinkedList<>();
        int y, z, x;
        Vector3 fn, ln; // firstNeighbour, lastNeighbour

        while (!opened.isEmpty()) {
            Block block = opened.pop();

            // firstNeighbour
            fn = new Vector3(
                    block.getX() - 1,
                    block.getY() - 1,
                    block.getZ() - 1
            );
            fn = fn.add(
                    Math.max(0, MIN.x() - fn.x()),
                    Math.max(0, MIN.y() - fn.y()),
                    Math.max(0, MIN.z() - fn.z())
            );

            // lastNeighbour
            ln = new Vector3(
                    block.getX() + 1,
                    block.getY() + 1,
                    block.getZ() + 1
            );
            ln = ln.subtract(
                    Math.max(0, ln.x() - MAX.x()),
                    Math.max(0, ln.y() - MAX.y()),
                    Math.max(0, ln.z() - MAX.z())
            );

            for (y = fn.y(); y <= ln.y(); ++y) {
                for (z = fn.z(); z <= ln.z(); ++z) {
                    for (x = fn.x(); x <= ln.x(); ++x) {
                        Block newBlock = WORLD.getBlockAt(x, y, z);

                        if (newBlock.equals(block)
                                || opened.contains(newBlock)
                                || closed.contains(newBlock)) {
                            continue;
                        }

                        if (newBlock.getType() == Material.AIR
                                || newBlock.getType() == Material.CAVE_AIR) {
                            opened.add(newBlock);
                        } else if (newBlock.getType() == material) {
                            return newBlock;
                        } else {
                            closed.push(newBlock);
                        }
                    }
                }
            }

            closed.add(block);
        }

        return null;
    }
}
