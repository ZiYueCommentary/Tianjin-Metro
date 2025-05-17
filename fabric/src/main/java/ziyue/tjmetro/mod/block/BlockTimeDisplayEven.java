package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.block.base.BlockEntityRenderable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockTimeDisplay
 * @since 1.0.0-beta-5
 */

public class BlockTimeDisplayEven extends BlockExtension implements DirectionHelper, BlockWithEntity
{
    public BlockTimeDisplayEven() {
        this(Blocks.createDefaultBlockSettings(false));
    }

    public BlockTimeDisplayEven(BlockSettings blockSettings) {
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
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(5, 8.5, 6, 16, 16, 10, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        IBlockExtension.breakBlock(world, pos.offset(IBlock.getStatePropertySafe(state, FACING).rotateYClockwise()), this.asBlock2());
        super.onBreak2(world, pos, state, player);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderTimeDisplay
     * @since 1.0.0-beta-5
     */
    public static class BlockEntity extends BlockTimeDisplay.BlockEntity
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.TIME_DISPLAY_EVEN.get(), pos, state);
        }
    }
}
