package lt.vu.mif.it.paskui.village.npc.entities;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Dynamic;
import lt.vu.mif.it.paskui.village.npc.NPC;
import lt.vu.mif.it.paskui.village.npc.ai.CustomVillagerGoalBuilder;
import lt.vu.mif.it.paskui.village.npc.ai.SearchMaterials;
import lt.vu.mif.it.paskui.village.npc.events.NPCDeathEvent;
import lt.vu.mif.it.paskui.village.npc.services.SelectionScreen;
import lt.vu.mif.it.paskui.village.npc.states.Check;
import lt.vu.mif.it.paskui.village.util.Logging;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * {@link NPC} interface into minecraft world.
 */
public class CustomVillager extends Villager implements NPCEntity {

    /**
     * {@link NPC} instance that this entity is associated to.
     */
    private final NPC npc;
    int count = 0;

    /** Default constructor.
     * @param npc reference of npc entity will represent.
     * @param loc location of ncp.
     */
    public CustomVillager(final NPC npc, final Location loc) {
        super(
                EntityType.VILLAGER,
                ((CraftWorld) loc.getWorld()).getHandle()
        );

        this.npc = npc;

        this.setVillagerData(
                this.getVillagerData().setProfession(VillagerProfession.CLERIC)
        );

        Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED))
                .setBaseValue(0.5F);
    }

    // NPCEntity
    @Override
    public String getEntityName() {
        return Objects.requireNonNullElse(
                this.getCustomName(),
                net.kyori.adventure.text.Component.text("CustomVillager")
        ).toString();
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
    public Vec3 getNPCPos() { return this.position(); }

    @Override
    public void setEntityName(final @NotNull String name) {
        this.setCustomName(new TextComponent(name));
    }

    @Override
    public void setEntityUUID(final @NotNull UUID npcUUID) {
        this.uuid = npcUUID;
    }

    @Override
    public void setEntityPos(final @NotNull Location loc) {
        this.setPos(loc.getX(), loc.getY(), loc.getZ());
    }

    /**
     * Makes the NPC move to the desired block.
     * @param timeElapsed time for collecting resources.
     * @param destination material to find and collect.
     */
    public void moveTo(final int timeElapsed, Material destination) {
        new Check(npc, destination, this, timeElapsed).runTaskLater(20);
    }

    /**
     * Makes the NPC to come back to the location where the deal was made.
     * @param loc - The location where the deal happened
     */
    public void moveBack(final Location loc) { this.navigation.moveTo(loc.getX(), loc.getY(), loc.getZ(), 0.5D); }

    /**
     * Calculates the distance between the starting point and the end point.
     * @param material - required material.
     */
    public double distanceTo(Material material) {
        //Block block = npc.searchMaterials(material);
        SearchMaterials search = new SearchMaterials(npc, npc.getLoc());
        Block block = search.searchMaterials(material);

        if (block == null) {
            return 0;
        }
        else {
            Vec3 p1 = new Vec3(this.getBlockX(), this.getBlockY(), this.getBlockZ());
            Vector p2 = block.getLocation().toVector();
            return Math.sqrt(p1.distanceToSqr(p2.getX(), p2.getY(), p2.getZ()));
        }
    }

    @Override
    public void stopEntityTrading() {
        this.stopTrading();
    }

    @Override
    public void removeBrain() {this.brain.removeAllBehaviors();}

    @Override
    public void refreshBrains(final @NotNull ServerLevel world) {
        Brain<Villager> behaviourController = this.getBrain();

        behaviourController.stopAll(world, this);
        this.brain = behaviourController.copyWithoutBehaviors();
        this.initBrainGoals(this.getBrain());
    }

    @Override
    public void moveFurther(Location location) {
        double X = location.getX();
        double Y = location.getY();
        double Z = location.getZ();
        X += 30;
        Z += 5;
        this.navigation.moveTo(X, Y, Z, 0.5D);
    }

    @Override
    public void setInvisible() { this.setInvisible(true); }

    @Override
    public void setVisible() { this.setInvisible(false); }

    @Override
    public void setKillable() { this.setInvulnerable(false); }

    @Override
    public void setCollidable(boolean state) {
        this.getBukkitLivingEntity().setCollidable(state);
        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            player.setCollidable(state);
        }
    }

    @Override
    public void removeEntity() {
        this.remove(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public void setHandItem(org.bukkit.inventory.ItemStack item) {
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.fromBukkitCopy(item));
    }

    // Villager
    @Override
    protected @NotNull Brain<?> makeBrain(final @NotNull Dynamic<?> dynamic) {
        Brain<Villager> brain = this.brainProvider().makeBrain(dynamic);
        this.initBrainGoals(brain);
        return brain;
    }

    @Override
    public void refreshBrain(final @NotNull ServerLevel world) {
        Brain<Villager> behaviourController = this.getBrain();

        behaviourController.stopAll(world, this);
        this.brain = behaviourController.copyWithoutBehaviors();
        this.initBrainGoals(this.getBrain());
    }

    public void setName(net.minecraft.network.chat.Component name) {
        this.setCustomName(name);
    }

    @Override
    protected void customServerAiStep() {
        // TODO : research what can be used in this method.
        super.customServerAiStep();
    }

    @Override
    protected void rewardTradeXp(final @NotNull MerchantOffer offer) { }

    @Override
    protected void updateTrades() { }

    @Override
    protected void stopTrading() {
        this.setTradingPlayer(null);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull final Player player,
                                                  @NotNull final InteractionHand hand) {
        SelectionScreen services = npc.getServices();
        if (this.isInvulnerable()) {
            player.sendMessage(Component.nullToEmpty("The NPC is busy"), UUID.randomUUID());
            return InteractionResult.FAIL;
        }

        ++count;

        if (count == 6) {
            final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            final Runnable runnable = new Runnable() {
                int countdownStarter = 20; //change to 1200 (minecraft day cycle is 20m)
                final Component name = getName();

                public void run() {

                    countdownStarter--;
                    setName(Component.nullToEmpty("Time Left: " + countdownStarter));

                    if (countdownStarter < 0) {
                        scheduler.shutdown();
                        setName(name);
                        count=0;

                    }
                }
            };
            scheduler.scheduleAtFixedRate(runnable, 0, 1, SECONDS);
        }

        if (count > 5) {
            player.sendMessage(Component.nullToEmpty("You have reached the limit of trades for now!"), UUID.randomUUID());
            player.sendMessage(Component.nullToEmpty("(Please wait a whole Minecraft day to continue)"), UUID.randomUUID());
            return InteractionResult.FAIL;
        }

        if (services == null) {
            services = SelectionScreen.createScreen(npc);
            npc.setServices(services);
        }
        player.getBukkitEntity().openInventory(services.getInventory());
        this.setTradingPlayer(player);
        this.setInvulnerable(true);

        return InteractionResult.SUCCESS;
    }

    @Override
    public void travel(@NotNull final Vec3 movementInput) {
        super.travel(movementInput);
    }

    @Override
    public void remove(final @NotNull RemovalReason reason) {
        Logging.infoLog("NPC %s removed for %s", uuid, reason);
        NPCDeathEvent event = new NPCDeathEvent(npc, reason);
        event.callEvent();
        super.remove(reason);
    }

    // private
    /**
     * To initialize and structure {@link CustomVillager} behaviour.
     * @param brain brain to add behaviours to.
     */
    private void initBrainGoals(@NotNull final Brain<Villager> brain) {
        final float speed = 0.5f;
        VillagerProfession profession = this.getVillagerData().getProfession();

        // brain package setup
        brain.addActivity(Activity.IDLE,
                CustomVillagerGoalBuilder.getIdlePackage(profession, speed)
        );
        brain.addActivity(Activity.CORE,
                CustomVillagerGoalBuilder.getCorePackage(profession, speed)
        );
        brain.addActivity(Activity.MEET,
                CustomVillagerGoalBuilder.getMeetPackage(profession, speed)
        );

        // brain defaults setup
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.setActiveActivityIfPossible(Activity.IDLE);
        brain.updateActivityFromSchedule(
                this.level.getDayTime(),
                this.level.getGameTime()
        );
    }

    /**
     * To add basic MobGoals to {@link CustomVillager}.
     */
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
    }
}
