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
import net.minecraft.core.Direction;
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
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
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
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

/**
 * {@link NPC} interface into minecraft world.
 */
public class CustomVillager extends Villager implements NPCEntity {

    /**
     * {@link NPC} instance that this entity is associated to.
     */
    private final NPC npc;

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
                .setBaseValue(0.4);
    }

    // NPCEntity
    @Override
    public String getEntityName() {
        return Objects.requireNonNullElse(
                this.getCustomName(),
                Component.text("CustomVillager")
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

    public void moveTo(final int timeElapsed, final Material material, final Vec3 start, final Vec3 pos) {

        if (npc.getCuboid(material) == null) {
            Bukkit.broadcast(Component.text("No " + material.toString() + " found"));
        }

        else {
            final int[] x = {0};
            Logging.infoLog("Move to called for NPC");
            Location loc = this.npc.getLoc();
            Block b;
            b = new Location(loc.getWorld(), pos.x - 1.3, pos.y, pos.z).getBlock();
            this.brain.removeAllBehaviors();
            this.navigation.moveTo(pos.x, pos.y, pos.z, 0.5D);
            Double dist = distanceTo(start, pos); //10 blocks ~= 10 seconds

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcast(Component.text("Pause Over"));
                        moveBack(loc);
                    }
                }.runTaskLater(Main.getInstance(), (timeElapsed * 20L) + (dist.longValue() * 20L)); //400 ticks = 20 seconds

                Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
                    public void run() {
                        if (x[0] <= 4) {
                            Vec3 pos = npc.getCuboid(material);
                            Block block;
                            block = new Location(loc.getWorld(), pos.x - 1.3, pos.y, pos.z).getBlock();
                            block.setType(Material.AIR);
                            ++x[0];
                        } else {
                            Bukkit.getScheduler().cancelTask(1);
                        }
                    }
                }, 60 + (dist.longValue() * 20L), 80);
        }
    }

    /**
     * Makes the NPC to come back to the location where the deal was dealt.
     * @param loc - The location where the deal happened
     */
    public void moveBack(final Location loc) {
        this.navigation.moveTo(loc.getX(), loc.getY(), loc.getZ(), 0.4D);

        if (this.npc.getLoc() == loc) {
            refreshBrain(this.portalWorld);
        }
    }

    public static double distanceTo(Vec3 p1, Vec3 p2) {
        return Math.sqrt(Math.round(p1.x - p2.x) + Math.round(p1.y - p2.y) + Math.round(p1.z - p2.z));
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
    public @NotNull InteractionResult mobInteract(
            @NotNull final Player player,
            @NotNull final InteractionHand hand
    ) {
        SelectionScreen services = npc.getServices();

        if (services == null) {
            services = SelectionScreen.createScreen(npc);
            npc.setServices(services);
        }
        player.getBukkitEntity().openInventory(services.getInventory());
        this.setTradingPlayer(player);

        //TODO: add something to check if inventory is closed and then use initPathfinder(); maybe

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
