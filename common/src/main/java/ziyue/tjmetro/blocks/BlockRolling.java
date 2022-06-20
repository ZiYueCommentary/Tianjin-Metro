package ziyue.tjmetro.blocks;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockList;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public class BlockRolling extends Block implements SimpleWaterloggedBlock
{
    public BlockRolling() {
        super(Properties.copy(Blocks.IRON_BARS));
    }

    public static final BooleanProperty FACING = BooleanProperty.create("facing");
    public static final BooleanProperty BOTTOM = BooleanProperty.create("bottom");
    public static final BooleanProperty CHANGED = BooleanProperty.create("changed");

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState()
                .setValue(FACING, ctx.getHorizontalDirection().getAxis() == Direction.Axis.X)
                .setValue(CHANGED, false)
                .setValue(BOTTOM, !(ctx.getLevel().getBlockState(ctx.getClickedPos().below()).getBlock() == BlockList.ROLLING.get()))
                .setValue(WATERLOGGED, false);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(FACING))
            return Block.box(6, 0, 0, 10, 16, 16);
        else
            return Block.box(0, 0, 6, 16, 16, 10);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BOTTOM, FACING, CHANGED, WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(level, player, () ->
                level.setBlockAndUpdate(blockPos, blockState.setValue(BOTTOM, !blockState.getValue(BOTTOM)).setValue(CHANGED, true))
        );
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (!blockState.getValue(CHANGED)) {
            if (level.getBlockState(blockPos.below()).getBlock() == BlockList.ROLLING.get())
                level.setBlockAndUpdate(blockPos, blockState.setValue(BOTTOM, false));
            else
                level.setBlockAndUpdate(blockPos, blockState.setValue(BOTTOM, true));
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
