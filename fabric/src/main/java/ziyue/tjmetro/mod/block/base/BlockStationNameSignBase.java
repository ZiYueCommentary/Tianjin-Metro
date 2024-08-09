package ziyue.tjmetro.mod.block.base;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.BlockStationNameBase;
import org.mtr.mod.block.IBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockEntityBase
 * @since 1.0.0-beta-1
 */

public abstract class BlockStationNameSignBase extends BlockStationNameBase
{
    public BlockStationNameSignBase(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(COLOR);
        properties.add(FACING);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(1f, 3.5f, 0f, 15f, 14.5f, 0.5f, IBlock.getStatePropertySafe(state, FACING));
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
    }

    public abstract static class BlockEntityBase extends BlockStationNameBase.BlockEntityBase
    {
        public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state, 0, 0.05F, false);
        }
    }
}
