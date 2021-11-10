package lt.vu.mif.it.paskui.village.npc.entities;

import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class CustomVillager extends Villager implements NPCEntity {

    private final NPC npc;

    public CustomVillager(NPC npc, Location loc) {
        super(
                EntityType.VILLAGER,
                ((CraftWorld) loc.getWorld()).getHandle()
        );

        this.npc = npc;

        // Clear out initial goals
        GoalSelector[] goals = new GoalSelector[] {
                targetSelector,
                goalSelector
        };

        for (GoalSelector selector : goals) {
            selector.availableGoals.clear();
        }

       // getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
    }

    // NPCEntity
    @Override
    public String getEntityName() {
        return Objects.requireNonNullElse(getCustomName().getString(), "");
    }

    @Override
    public UUID getEntityUUID() {
        return this.uuid;
    }

    @Override
    public Entity getNMSEntity() {
        return this;
    }

    @Override
    public NPC getNPC() {
        return npc;
    }

    @Override
    public void setEntityName(String name) {
        if ( !(name.isBlank() || name.isEmpty()) ) {
            this.setCustomName(new TextComponent(name));
        }
    }

    @Override
    public void setEntityUUID(UUID npcUUID) {
        this.uuid = npcUUID;
    }

    @Override
    public void setEntityPos(Location loc) {
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
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
        Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.3);
    }

    public void moveTo() {
        this.goalSelector.removeAllGoals();
        this.moveTo(Main.getInstance().getNPCManager().getCuboid());
    }

    @Override
    public void removeEntity() {
        this.remove(Entity.RemovalReason.DISCARDED);
    }

    // Villager
    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        // TODO: Current implementation works, but might need more logic later on.
//        NPCInteractEvent event = new NPCInteractEvent(
//                ((CraftPlayer) player.getBukkitEntity()).getPlayer(),
//                npc
//        );
//        event.callEvent();

        SelectionScreen gui = new SelectionScreen(npc);
        player.getBukkitEntity().openInventory(gui.getInventory());

        //TODO: add something to check if inventory is closed and then use initPathfinder(); maybe

        return InteractionResult.SUCCESS;
    }

    @Override
    public void travel(@NotNull Vec3 movementInput) {
        super.travel(movementInput);
    }

    @Override
    public void remove(RemovalReason reason) {
        Logging.infoLog("NPC %s removed for %s", uuid, reason);
        super.remove(reason);
    }
}