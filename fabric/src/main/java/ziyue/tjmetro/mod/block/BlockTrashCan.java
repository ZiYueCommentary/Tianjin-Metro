package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockRubbishBin;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Work in progress.
 *
 * @author ZiYueCommentary
 * @since 1.0.0
 */

public class BlockTrashCan extends BlockExtension
{
    public BlockTrashCan() {
        this(Blocks.createDefaultBlockSettings(true));
    }

    public BlockTrashCan(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public @Nullable BlockState getPlacementState2(ItemPlacementContext ctx) {
        if (!IBlock.isReplaceable(ctx, Direction.UP, 2)) return null;
        ctx.getWorld().setBlockState(ctx.getBlockPos().up(), super.getPlacementState2(ctx).with(new Property<>(IBlock.HALF.data), IBlock.DoubleBlockHalf.UPPER));
        return super.getPlacementState2(ctx).with(new Property<>(IBlock.HALF.data), IBlock.DoubleBlockHalf.LOWER);
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (IBlock.getStatePropertySafe(state, IBlock.HALF) == IBlock.DoubleBlockHalf.UPPER) {
            IBlockExtension.breakBlock(world, pos.down(), BlockList.TRASH_CAN.get());
        } else {
            IBlockExtension.breakBlock(world, pos.up(), BlockList.TRASH_CAN.get());
        }
        super.onBreak2(world, pos, state, player);
    }

    @Override
    public @Nonnull VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (IBlock.getStatePropertySafe(state, IBlock.HALF) == IBlock.DoubleBlockHalf.UPPER) {
            return IBlock.getVoxelShapeByDirection(2, 0, 2, 14, 3, 14, Direction.NORTH);
        } else {
            return IBlock.getVoxelShapeByDirection(2, 0, 2, 14, 16, 14, Direction.NORTH);
        }
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(BlockRubbishBin.FILLED);
        properties.add(IBlock.HALF);
    }
}
