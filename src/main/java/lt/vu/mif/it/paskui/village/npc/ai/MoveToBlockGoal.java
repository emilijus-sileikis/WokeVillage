package lt.vu.mif.it.paskui.village.npc.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;

public class MoveToBlockGoal extends net.minecraft.world.entity.ai.goal.MoveToBlockGoal {

    private static final int WAIT_AFTER_BLOCK_FOUND = 20;

    private final Block targetBlock;
//    private final Mob executerMob;
    private int ticksAfterGoalReach;

    public MoveToBlockGoal(Block targetBlock, PathfinderMob mob, double speed, int maxYDifference) {
        super(mob, speed, 24, maxYDifference);
        this.targetBlock = targetBlock;
//        this.executerMob = mob;
    }

    @Override
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        ChunkAccess iChunkAccess = world.getChunkIfLoadedImmediately(pos.getX(), pos.getZ());

        return iChunkAccess != null &&
                iChunkAccess.getBlockState(pos).is(this.targetBlock) &&
                iChunkAccess.getBlockState(pos.above(2)).isAir();
    }

    @Override
    public void start() {
        super.start();
        this.ticksAfterGoalReach = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.fallDistance = 1.0f;
    }

    @Override
    public boolean canUse() {
        if (!this.mob.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        }

        this.nextStartTick = this.nextStartTick(this.mob);
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }
}
