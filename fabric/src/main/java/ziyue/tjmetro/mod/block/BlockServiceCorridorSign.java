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
 * A decoration.
 *
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since beta-1
 */

public class BlockServiceCorridorSign extends BlockExtension implements DirectionHelper, BlockWithEntity
{
    public BlockServiceCorridorSign() {
        this(BlockHelper.createBlockSettings(true));
    }

    public BlockServiceCorridorSign(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(1f, 3.5f, 0f, 15f, 14.5f, 0.5f, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderServiceCorridorSign
     * @since beta-1
     */
    public static class BlockEntity extends BlockEntityRenderable
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SERVICE_CORRIDOR_SIGN.get(), pos, state, 0, 0.03F);
        }
    }
}
