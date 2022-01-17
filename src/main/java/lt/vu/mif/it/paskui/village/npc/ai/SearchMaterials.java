package lt.vu.mif.it.paskui.village.npc.ai;

import lt.vu.mif.it.paskui.village.npc.NPC;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SearchMaterials {

    public static Block search(Material material, NPC npc) {
        npc.updateLocation();
        Location location = npc.getLoc();
        return around(location.getChunk(), 1, material);
    }

    public static Block around(Chunk origin, int radius, Material material) {
        World world = origin.getWorld();

        int cX = origin.getX();
        int cZ = origin.getZ();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Chunk chunk = world.getChunkAt(cX + x, cZ + z);

                for (int xz = 16 * 16 * 60; xz < 16 * 16 * 100; ++xz) { //256
                    int xx = xz & 15;
                    int zz = xz >> 4 & 15;
                    int yy = xz >> 8;
                    Block block = chunk.getBlock(xx, yy, zz);
                    if (block.getType() == material) {
                        return block;
                    }
                }

            }
        }

        return null;
    }
}
