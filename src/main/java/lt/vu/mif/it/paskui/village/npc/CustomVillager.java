package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.util.Logging;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class CustomVillager extends Villager implements NPCAttach {

    private final GoalSelector[] goals;
    private final NPC npc;

    public CustomVillager(NPC npc, Location loc) {
        super(
                EntityType.VILLAGER,
                ((CraftWorld) loc.getWorld()).getHandle()
        );

        this.npc = npc;
        goals = new GoalSelector[] {
                targetSelector,
                goalSelector
        };

        for (GoalSelector selector : goals) {
            selector.availableGoals.clear();
            Logging.infoLog("%s : %d", selector.toString(), selector.availableGoals.size());
        }

        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
    }

    // NPCAttach
    @Override
    public NPC getNPC() {
        return npc;
    }

    // Villager
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        return super.mobInteract(player, hand);
    }

    @Override
    public void travel(Vec3 movementInput) {
        super.travel(movementInput);
    }
}