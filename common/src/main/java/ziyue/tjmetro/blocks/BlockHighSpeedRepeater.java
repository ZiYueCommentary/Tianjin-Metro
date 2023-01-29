package ziyue.tjmetro.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Random;

/**
 * Repeater without delay.
 *
 * @author ZiYueCommentary
 * @since 1.0b
 */

public class BlockHighSpeedRepeater extends RepeaterBlock
{
    public BlockHighSpeedRepeater() {
        this(BlockBehaviour.Properties.copy(Blocks.REPEATER));
    }

    public BlockHighSpeedRepeater(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        this.update(level, blockPos, blockState);
    }

    @Override
    protected int getDelay(BlockState blockState) {
        return 0;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        this.update(level, blockPos, blockState);
    }

    protected void update(Level level, BlockPos blockPos, BlockState blockState) {
        if (isLocked(level, blockPos, blockState)) return;
        BlockPos blockPos1 = blockPos.relative(blockState.getValue(FACING));
        boolean powered = level.getSignal(blockPos1, blockState.getValue(FACING)) > 0;
        level.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, powered));
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
    }

    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        if (!blockState.getValue(POWERED)) return;

        double d = (double) blockPos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        double e = (double) blockPos.getY() + 0.4 + (random.nextDouble() - 0.5) * 0.2;
        double f = (double) blockPos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
        float g = -5.0F;
        if (random.nextBoolean()) {
            g = 1.0F;
        }
        g /= 16.0F;

        Direction direction = blockState.getValue(FACING);
        double h = g * (float) direction.getStepX();
        double i = g * (float) direction.getStepZ();
        DustParticleOptions blueParticle = new DustParticleOptions(0.196F, 0.909F, 0.933F, 1.0F); // 50, 232, 238
        level.addParticle(blueParticle, d + h, e, f + i, 0.0, 0.0, 0.0);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return InteractionResult.PASS;
    }
}
