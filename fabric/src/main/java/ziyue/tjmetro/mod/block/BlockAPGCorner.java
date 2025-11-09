package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static org.mtr.mod.block.IBlock.HALF;

/**
 * Corner for Automatic Platform Gates.
 *
 * @author ZiYueCommentary
 * @see org.mtr.mod.block.BlockAPGGlass
 * @since 1.0.0-beta-1
 */

public class BlockAPGCorner extends BlockExtension implements DirectionHelper
{
    public BlockAPGCorner() {
        this(Blocks.createDefaultBlockSettings(true));
    }

    public BlockAPGCorner(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        BlockState state = getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
        ctx.getWorld().setBlockState(ctx.getBlockPos().up(), state.with(new Property<>(HALF.data), IBlock.DoubleBlockHalf.UPPER));
        return state.with(new Property<>(HALF.data), IBlock.DoubleBlockHalf.LOWER);
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (IBlock.getStatePropertySafe(state, HALF) == IBlock.DoubleBlockHalf.UPPER)
            IBlockExtension.breakBlock(world, pos.down(), BlockList.APG_CORNER.get());
        else
            IBlockExtension.breakBlock(world, pos.up(), BlockList.APG_CORNER.get());

        super.onBreak2(world, pos, state, player);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(HALF);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (IBlock.getStatePropertySafe(state, HALF)) { // What am I doing?
            case UPPER -> IBlock.getVoxelShapeByDirection(0, 0, 0, 4, 9, 4, IBlock.getStatePropertySafe(state, FACING));
            case LOWER ->
                    IBlock.getVoxelShapeByDirection(0, 0, 0, 4, 16, 4, IBlock.getStatePropertySafe(state, FACING));
        };
    }
}
