package lt.vu.mif.it.paskui.village.npc;

import lt.vu.mif.it.paskui.village.util.Logging;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;

public class CustomVillager extends Villager implements NPCAttach {

    //private final GoalSelector[] goals;
    private final NPC npc;

    public CustomVillager(NPC npc, Location loc) {
        super(
                EntityType.VILLAGER,
                ((CraftWorld) loc.getWorld()).getHandle()
        );

        this.npc = npc;
        //goals = new GoalSelector[] {
                //targetSelector,
                //goalSelector
        //};

        //for (GoalSelector selector : goals) {
            //selector.availableGoals.clear();
            //Logging.infoLog("%s : %d", selector.toString(), selector.availableGoals.size());
       // }

       // getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
    }

    @Override
    public void initPathfinder() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MoveTowardsRestrictionGoal(this, 0.5));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
        this.goalSelector.addGoal(5, new HurtByTargetGoal(this));
        //this.goalSelector.addGoal(7, new TryFindWaterGoal(this));
        //this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 5, true));
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