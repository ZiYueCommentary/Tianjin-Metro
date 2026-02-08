package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since 1.1.0
 */

public class BlockPIDSTianjinSingle extends BlockPIDSTianjin
{
    public static final BooleanProperty SHOULD_RENDER = BooleanProperty.of("should_render"); // it just works...

    @Override
    public @Nonnull BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state;
    }

    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction direction = ctx.getPlayerFacing();
        return IBlock.isReplaceable(ctx, direction, 2) ? this.getDefaultState2().with(new Property<>(FACING.data), direction.data).with(new Property<>(SHOULD_RENDER.data), true) : null;
    }

    @Override
    public boolean canStoreData(World world, BlockPos blockPos) {
        return IBlock.getStatePropertySafe(world, blockPos, SHOULD_RENDER);
    }

    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            Direction direction = IBlock.getStatePropertySafe(state, FACING);
            world.setBlockState(pos.offset(direction.rotateYClockwise()), this.getDefaultState2().with(new Property<>(FACING.data), direction.data).with(new Property<>(SHOULD_RENDER.data), false), 3);
            world.updateNeighbors(pos, Blocks.getAirMapped());
            state.updateNeighbors(new WorldAccess(world.data), pos, 3);
        }
    }

    @Override
    public BlockPos getBlockPosWithData(World world, BlockPos blockPos) {
        if (canStoreData(world, blockPos)) {
            return blockPos;
        } else {
            final Direction direction = IBlock.getStatePropertySafe(world, blockPos, FACING);
            return blockPos.offset(IBlock.getStatePropertySafe(world, blockPos, SHOULD_RENDER) ? direction.rotateYClockwise() : direction.rotateYCounterclockwise());
        }
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        IBlockExtension.breakBlock(world, pos.offset(IBlock.getStatePropertySafe(state, SHOULD_RENDER) ? direction.rotateYClockwise() : direction.rotateYCounterclockwise()), this.asBlock2());
        super.onBreak2(world, pos, state, player);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        super.addBlockProperties(properties);
        properties.add(SHOULD_RENDER);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState, true);
    }
}
