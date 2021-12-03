package lt.vu.mif.it.paskui.village.npc.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.behavior.AcquirePoi;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.GateBehavior;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
import net.minecraft.world.entity.ai.behavior.HarvestFarmland;
import net.minecraft.world.entity.ai.behavior.InteractWith;
import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
import net.minecraft.world.entity.ai.behavior.JumpOnBed;
import net.minecraft.world.entity.ai.behavior.LookAndFollowTradingPlayerSink;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.PoiCompetitorScan;
import net.minecraft.world.entity.ai.behavior.ReactToBell;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
import net.minecraft.world.entity.ai.behavior.SetRaidStatus;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromBlockMemory;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.ShowTradesToPlayer;
import net.minecraft.world.entity.ai.behavior.SocializeAtBell;
import net.minecraft.world.entity.ai.behavior.StrollAroundPoi;
import net.minecraft.world.entity.ai.behavior.StrollToPoi;
import net.minecraft.world.entity.ai.behavior.StrollToPoiList;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.behavior.TradeWithVillager;
import net.minecraft.world.entity.ai.behavior.UpdateActivityFromSchedule;
import net.minecraft.world.entity.ai.behavior.UseBonemeal;
import net.minecraft.world.entity.ai.behavior.ValidateNearbyPoi;
import net.minecraft.world.entity.ai.behavior.VillageBoundRandomStroll;
import net.minecraft.world.entity.ai.behavior.VillagerMakeLove;
import net.minecraft.world.entity.ai.behavior.WakeUp;
import net.minecraft.world.entity.ai.behavior.WorkAtPoi;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;

import java.util.Optional;

public class CustomVillagerGoalBuilder {

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getIdlePackage(
            VillagerProfession profession,
            float speed
    ) {
        return ImmutableList.of(
                Pair.of(2, new RunOne<>(ImmutableList.of(
                        Pair.of(InteractWith.of(
                                        EntityType.VILLAGER, 8,
                                        MemoryModuleType.INTERACTION_TARGET, speed, 2),
                                2
                        ),
                        Pair.of(
                                InteractWith.of(
                                        EntityType.CAT, 8,
                                        MemoryModuleType.INTERACTION_TARGET, speed, 2
                                ),
                                1
                        ),
                        Pair.of(new VillageBoundRandomStroll(speed), 1),
                        Pair.of(new SetWalkTargetFromLookTarget(speed, 2), 1),
                        Pair.of(new JumpOnBed(speed), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                ))),
                Pair.of(3, new GiveGiftToHero(100)),
                Pair.of(3, new SetLookAndInteract(EntityType.PLAYER, 4)),
                Pair.of(3, new ShowTradesToPlayer(400, 1600)),
                Pair.of(3, new GateBehavior<>(
                        ImmutableMap.of(),
                        ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET),
                        GateBehavior.OrderPolicy.ORDERED,
                        GateBehavior.RunningPolicy.RUN_ONE,
                        ImmutableList.of(Pair.of(new TradeWithVillager(), 1))
                )),
                Pair.of(3, new GateBehavior<>(
                        ImmutableMap.of(),
                        ImmutableSet.of(MemoryModuleType.BREED_TARGET),
                        GateBehavior.OrderPolicy.ORDERED,
                        GateBehavior.RunningPolicy.RUN_ONE,
                        ImmutableList.of(Pair.of(new VillagerMakeLove(), 1))
                )),
                getFullLookBehavior(),
                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getCorePackage(
            VillagerProfession profession,
            float speed
    ) {
        return ImmutableList.of(
                Pair.of(0, new Swim(0.8F)),
                Pair.of(0, new InteractWithDoor()),
                Pair.of(0, new LookAtTargetSink(45, 90)),
                Pair.of(0, new WakeUp()),
                Pair.of(0, new ReactToBell()),
                Pair.of(0, new SetRaidStatus()),
                Pair.of(1, new MoveToTargetSink()),
                Pair.of(2, new PoiCompetitorScan(profession)),
                Pair.of(3, new LookAndFollowTradingPlayerSink(speed)),
                Pair.of(5, new GoToWantedItem<>(speed, false, 4)),
                Pair.of(10, new AcquirePoi(
                        PoiType.HOME, MemoryModuleType.HOME,
                        false, Optional.of((byte)14)
                )),
                Pair.of(10, new AcquirePoi(
                        PoiType.MEETING, MemoryModuleType.MEETING_POINT,
                        true, Optional.of((byte)14)
                ))
        );
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getMeetPackage(
            VillagerProfession profession, float speed
    ) {
        return ImmutableList.of(
                Pair.of(2, new RunOne<>(ImmutableList.of(
                        Pair.of(new StrollAroundPoi(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2),
                        Pair.of(new SocializeAtBell(), 2)))),
                Pair.of(10, new ShowTradesToPlayer(400, 1600)),
                Pair.of(10, new SetLookAndInteract(EntityType.PLAYER, 4)),
                Pair.of(2, new SetWalkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, speed, 6, 100, 200)),
                Pair.of(3, new GiveGiftToHero(100)),
                Pair.of(3, new ValidateNearbyPoi(PoiType.MEETING, MemoryModuleType.MEETING_POINT)),
                Pair.of(3, new GateBehavior<>(
                        ImmutableMap.of(),
                        ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET),
                        GateBehavior.OrderPolicy.ORDERED,
                        GateBehavior.RunningPolicy.RUN_ONE,
                        ImmutableList.of(Pair.of(new TradeWithVillager(), 1))
                )),
                getFullLookBehavior(),
                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getWorkPackage(
            VillagerProfession profession,
            float speed
    ) {
        WorkAtPoi workAtPoi = new WorkAtPoi();

        return ImmutableList.of(
                getMinimalLookBehavior(),
                Pair.of(5, new RunOne<>(
                        ImmutableList.of(Pair.of(workAtPoi, 7),
                        Pair.of(new StrollAroundPoi(MemoryModuleType.JOB_SITE, 0.4F, 4), 2),
                        Pair.of(new StrollToPoi(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5),
                        Pair.of(new StrollToPoiList(MemoryModuleType.SECONDARY_JOB_SITE, speed, 1, 6, MemoryModuleType.JOB_SITE), 5),
                        Pair.of(new HarvestFarmland(), profession == VillagerProfession.FARMER ? 2 : 5),
                        Pair.of(new UseBonemeal(), profession == VillagerProfession.FARMER ? 4 : 7))
                )),
                Pair.of(10, new ShowTradesToPlayer(400, 1600)), Pair.of(10, new SetLookAndInteract(EntityType.PLAYER, 4)),
                Pair.of(2, new SetWalkTargetFromBlockMemory(MemoryModuleType.JOB_SITE, speed, 9, 100, 1200)),
                Pair.of(3, new GiveGiftToHero(100)),
                Pair.of(99, new UpdateActivityFromSchedule())
        );
    }

    private static Pair<Integer, Behavior<LivingEntity>> getFullLookBehavior() {
        return Pair.of(5, new RunOne<>(ImmutableList.of(
                Pair.of(new SetEntityLookTarget(EntityType.CAT, 8.0F), 8),
                Pair.of(new SetEntityLookTarget(EntityType.VILLAGER, 8.0F), 2),
                Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 2),
                Pair.of(new SetEntityLookTarget(MobCategory.CREATURE, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(MobCategory.WATER_CREATURE, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(MobCategory.UNDERGROUND_WATER_CREATURE, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(MobCategory.WATER_AMBIENT, 8.0F), 1),
                Pair.of(new SetEntityLookTarget(MobCategory.MONSTER, 8.0F), 1),
                Pair.of(new DoNothing(30, 60), 2)
        )));
    }

    private static Pair<Integer, Behavior<LivingEntity>> getMinimalLookBehavior() {
        return Pair.of(5, new RunOne<>(ImmutableList.of(
                Pair.of(new SetEntityLookTarget(EntityType.VILLAGER, 8.0F), 2),
                Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 2),
                Pair.of(new DoNothing(30, 60), 8))
        ));
    }
/*
Pair.of(0, new ValidateNearbyPoi(profession.getJobPoiType(), MemoryModuleType.JOB_SITE)),
Pair.of(0, new ValidateNearbyPoi(profession.getJobPoiType(), MemoryModuleType.POTENTIAL_JOB_SITE)),
Pair.of(0, new ValidateNearbyPoi(profession.getJobPoiType(), MemoryModuleType.JOB_SITE)),
Pair.of(0, new ValidateNearbyPoi(profession.getJobPoiType(), MemoryModuleType.POTENTIAL_JOB_SITE)),
Pair.of(10, new AssignProfessionFromJobSite()),
Pair.of(10, new ResetProfession())
*/
}
