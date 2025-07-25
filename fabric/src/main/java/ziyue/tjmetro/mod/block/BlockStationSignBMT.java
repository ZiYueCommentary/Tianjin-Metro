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
 * @author ZiYueCommentary
 * @since 1.0.0
 */

public class BlockStationSignBMT extends BlockExtension implements DirectionHelper
{
    public BlockStationSignBMT() {
        this(Blocks.createDefaultBlockSettings(false, state -> 10));
    }

    public BlockStationSignBMT(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().rotateYClockwise().data);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(0, 1, 6, 15, 15, 10, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
    }
}
