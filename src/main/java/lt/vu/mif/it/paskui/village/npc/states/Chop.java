package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitRunnable;

public class Chop extends BukkitRunnable {

    private final CustomVillager npcEntity;
    private final Material material;
    private Block block;
    private int y = 0;

    public Chop(CustomVillager villager, Material material, Block block) {
        this.npcEntity = villager;
        this.material = material;
        this.block = block;
    }

    @Override
    public void run() {
        if (y >= 6 || block == null || npcEntity.getBukkitEntity().isDead()) {
            this.cancel();
            return;
        }

        if (block.getType() == material) {
            block.setType(Material.AIR);
        }

        block = block.getRelative(BlockFace.UP);
        ++y;
    }
}
