package lt.vu.mif.it.paskui.village.npc.states;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.Role;
import lt.vu.mif.it.paskui.village.npc.ai.SearchMaterials;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.schedule.Activity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Check extends NPCLocState {
    private final CustomVillager villager;
    private final Material material;
    private final int timeElapsed;
    SearchMaterials search = new SearchMaterials(npc, loc);

    public Check(NPC npc, Material material, CustomVillager villager, int timeElapsed) {
        super(npc, npc.getLoc());
        this.villager = villager;
        this.material = material;
        this.timeElapsed = timeElapsed;
    }

    @Override
    public void run() {
        this.villager.setInvulnerable(true);
        this.npc.setCollidable(false);
        this.npc.setWorkHand();

        villager.removeBrain();
        //Block block = npc.searchMaterials(material);
        Block block = search.searchMaterials(material);
        villager.getBrain().addActivity(Activity.CORE, ImmutableList.of(
                Pair.of(0, new Swim(0.8f)))
        );

        Location newLoc = this.npc.getLoc();
        if (npc.role == Role.MINER) {
            villager.getNavigation().moveTo(
                    newLoc.getX() + 5, newLoc.getY(), newLoc.getZ() + 3,
                    0.5D
            );

            new Dig(npc, newLoc, material).runTaskLater(timeElapsed * 20L);
        }

        if (block != null) {
            Logging.infoLog("Move to called for NPC");
            villager.getNavigation().moveTo(block.getX(), block.getY(), block.getZ(), 0.5D);
            double dist = villager.distanceTo(material); //10 blocks ~= 10 seconds

            new Chop(this.npc, villager, material, block).runTaskTimer(
                    40 + ((long)dist * 20L),
                    (timeElapsed * 20L) / 7
            );

            new Pause(npc, this.loc).runTaskLater(timeElapsed * 20L);
        }
        else {
            new Move(npc, this.loc, timeElapsed).runTaskLater(40);
        }
    }
}
