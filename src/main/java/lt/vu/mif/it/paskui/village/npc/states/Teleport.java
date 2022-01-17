package lt.vu.mif.it.paskui.village.npc.states;

import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.entities.CustomVillager;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.bukkit.Location;

import java.util.Objects;

public class Teleport extends NPCLocState {
    CustomVillager villager;

    public Teleport(NPC npc, Location loc, CustomVillager villager) {
        super(npc, loc);
        this.villager = villager;
    }

    @Override
    public void run() {
        if (npc.getEntity().isInWaterOrBubbleColumn()
                || npc.getEntity().getVelocity().getX() == 0.0D
                || npc.getEntity().getVelocity().getZ() == 0.0D) {
            npc.getEntity().teleport(loc);
            Objects.requireNonNull(villager.getAttribute(Attributes.MOVEMENT_SPEED))
                    .setBaseValue(0);
        }
    }
}