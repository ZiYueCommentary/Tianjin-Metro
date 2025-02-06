package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A roadblock-style fence.
 *
 * @author ZiYueCommentary
 * @since 1.0.0-beta-1
 */

public class BlockRoadblock extends BlockExtension implements DirectionHelper
{
    public BlockRoadblock() {
        this(Blocks.createDefaultBlockSettings(false));
    }

    public BlockRoadblock(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final BlockState state = getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        if (IBlock.isReplaceable(ctx, direction.rotateYClockwise(), 2)) {
            ctx.getWorld().setBlockState(ctx.getBlockPos().offset(direction.rotateYClockwise()), state.with(new Property<>(FACING.data), direction.getOpposite().data));
            return state;
        }
        return null;
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        IBlockExtension.breakBlock(world, pos.offset(IBlock.getStatePropertySafe(state, FACING).rotateYClockwise()), this.asBlock2());
        super.onBreak2(world, pos, state, player);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(0, 0, 5, 16, 17, 11, IBlock.getStatePropertySafe(state, FACING));
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(0, 0, 5, 16, 24, 11, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
    }
}
