package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.block.base.BlockEntityRenderable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A time display, require better model.
 *
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since beta-1
 */

public class BlockTimeDisplay extends BlockExtension implements DirectionHelper, BlockWithEntity
{
    public BlockTimeDisplay() {
        this(BlockHelper.createBlockSettings(false));
    }

    public BlockTimeDisplay(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(-3, 8.5, 6, 18, 16, 10, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderTimeDisplay
     * @since beta-1
     */
    public static class BlockEntity extends BlockEntityRenderable
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.TIME_DISPLAY.get(), pos, state, 0.21F, 0.63F);
        }
    }
}
