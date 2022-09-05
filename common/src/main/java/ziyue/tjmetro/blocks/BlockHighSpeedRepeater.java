package ziyue.tjmetro.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Random;

/**
 * No lag repeater.
 *
 * @author ZiYueCommentary
 * @since 1.0b
 */
public class BlockHighSpeedRepeater extends RepeaterBlock
{
    public BlockHighSpeedRepeater() {
        super(Properties.copy(Blocks.REPEATER));
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
        boolean b = level.getSignal(blockPos1, blockState.getValue(FACING)) > 0;
        level.setBlockAndUpdate(blockPos, blockState.setValue(POWERED, b));
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return InteractionResult.PASS;
    }
}
