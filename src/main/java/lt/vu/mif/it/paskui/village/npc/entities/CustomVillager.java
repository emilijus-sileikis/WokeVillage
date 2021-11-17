package lt.vu.mif.it.paskui.village.npc.entities;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import lt.vu.mif.it.paskui.village.Main;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.ai.CustomVillagerGoalBuilder;
import lt.vu.mif.it.paskui.village.npc.events.NPCDeathEvent;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class CustomVillager extends Villager implements NPCEntity {

    private final NPC npc;

    /** Default constructor.
     * @param npc reference of npc entity will represent.
     * @param loc location of ncp.
     */
    public CustomVillager(NPC npc, Location loc) {
        super(
                EntityType.VILLAGER,
                ((CraftWorld) loc.getWorld()).getHandle()
        );

        this.npc = npc;

        this.setVillagerData(
                this.getVillagerData().setProfession(VillagerProfession.CLERIC)
        );

        /*
        // Clear out initial goals
        GoalSelector[] goals = new GoalSelector[] {
                targetSelector,
                goalSelector
        };

        for (GoalSelector selector : goals) {
            Logging.infoLog("%s", selector);
            for (Goal goal : selector.availableGoals) {
                Logging.infoLog("%s", goal.getClass());
            }
//            selector.availableGoals.clear();
        }
        */

       //initPathfinder();
       //getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3);
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

    /**
     * Makes the NPC go to the nearest Spruce Log block.
     * Then waits some time to simulate chopping.
     */
    public void moveTo() {

        if (Main.getInstance().getNPCManager().getCuboid() == null) {
            Bukkit.broadcast(Component.text("No Spruce Logs found"));
        }

        else {
            Logging.infoLog("Move to called for NPC");
            Location loc = this.npc.getLoc();
            Vec3 pos = Main.getInstance().getNPCManager().getCuboid();
            Block b;
            b = new Location(loc.getWorld(), pos.x - 1.3, pos.y - 1.3, pos.z).getBlock();
            this.brain.removeAllBehaviors();
            this.navigation.moveTo(pos.x, pos.y, pos.z, 0.4D);

            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.broadcast(Component.text("Pause Over"));
                    b.setType(Material.AIR);
                    moveBack(loc);
                }
            }.runTaskLater(Main.getInstance(), 400); //400 ticks = 20 seconds
        }
    }

    /**
     * Makes the NPC to come back to the location where the deal was dealt.
     * @param loc - The location where the deal happened
     */
    public void moveBack(Location loc) {
        this.navigation.moveTo(loc.getX(), loc.getY(), loc.getZ(), 0.4D);

        if (this.npc.getLoc() == loc) {
            refreshBrain(this.portalWorld);
        }
    }

    @Override
    public void stopEntityTrading() {
        this.stopTrading();
    }

    @Override
    public void removeEntity() {
        this.remove(Entity.RemovalReason.DISCARDED);
    }

    // Villager
    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        Brain<Villager> brain = this.brainProvider().makeBrain(dynamic);
        this.initBrainGoals(brain);
        return brain;
    }

    @Override
    public void refreshBrain(ServerLevel world) {
        Brain<Villager> behaviourController = this.getBrain();

        behaviourController.stopAll(world, this);
        this.brain = behaviourController.copyWithoutBehaviors();
        this.initBrainGoals(this.getBrain());
    }

    @Override
    protected void customServerAiStep() {
        // Vanilla Villager brain clearing
//        this.getBrain().removeAllBehaviors();
        super.customServerAiStep();
//        this.moveTo();
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {}

    @Override
    protected void updateTrades() {}

    @Override
    protected void stopTrading() {
        this.setTradingPlayer(null);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        SelectionScreen services = npc.getServices();

        if (services == null) {
            services = new SelectionScreen(npc);
            npc.setServices(services);
        }
        player.getBukkitEntity().openInventory(services.getInventory());
        this.setTradingPlayer(player);

        Logging.infoLog("NPC profession: %s", this.getVillagerData().getProfession()); // Is kept for debug logging.

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
        NPCDeathEvent event = new NPCDeathEvent(npc, reason);
        event.callEvent();
        super.remove(reason);
    }

    // private
    private void initBrainGoals(Brain<Villager> brain) {
        VillagerProfession profession = this.getVillagerData().getProfession();

        // brain package setup
        brain.addActivity(Activity.IDLE,
                CustomVillagerGoalBuilder.getIdlePackage(profession, 0.5f)
        );
        brain.addActivity(Activity.CORE,
                CustomVillagerGoalBuilder.getCorePackage(profession, 0.5f)
        );
        brain.addActivity(Activity.MEET,
                CustomVillagerGoalBuilder.getMeetPackage(profession, 0.5f)
        );

        // brain defaults setup
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        brain.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
    }

    public void initMobGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        //this.goalSelector.addGoal(1, new MoveTowardsRestrictionGoal(this, 0.5));
        //this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.5));
        //this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        //this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, LivingEntity.class, 8.0F));
        //this.goalSelector.addGoal(5, new HurtByTargetGoal(this));
        //this.goalSelector.addGoal(7, new TryFindWaterGoal(this));
        //this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 5, true));
        Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.3);
    }
}