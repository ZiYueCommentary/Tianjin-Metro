package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockList;

import javax.annotation.Nullable;
import java.util.List;

public class BlockRouteMapBMT extends BlockExtension implements DirectionHelper
{
    public static final IntegerProperty POSITION = IntegerProperty.of("position", 1, 6);

    public BlockRouteMapBMT() {
        this(Blocks.createDefaultBlockSettings(true, state -> 10));
    }

    public BlockRouteMapBMT(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public @Nullable BlockState getPlacementState2(ItemPlacementContext ctx) {
        // Verify
        for (int i = 0; i < 3; ++i) {
            final BlockPos pos = ctx.getBlockPos().offset(ctx.getPlayerFacing().rotateYClockwise(), i);
            if (!ctx.getWorld().getBlockState(pos).canReplace(ctx) || !ctx.getWorld().getBlockState(pos.down()).canReplace(ctx)) {
                return null;
            }
        }

        final BlockState state = getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
        for (int i = 0; i < 3; ++i) {
            final BlockPos pos = ctx.getBlockPos().offset(ctx.getPlayerFacing().rotateYClockwise(), i);
            if (i != 0) ctx.getWorld().setBlockState(pos, state.with(new Property<>(POSITION.data), i + 1));
            ctx.getWorld().setBlockState(pos.down(), state.with(new Property<>(POSITION.data), i + 4));
        }
        return state;
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        final int position = IBlock.getStatePropertySafe(state, POSITION);
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        final BlockPos first;

        if (position > 3) {
            first = pos.offset(direction.rotateYCounterclockwise(), position - 4).up();
        } else {
            first = pos.offset(direction.rotateYCounterclockwise(), position - 1);
        }

        for (int i = 0; i < 3; ++i) {
            final BlockPos blockPos = first.offset(direction.rotateYClockwise(), i);
            if (!blockPos.equals(pos)) IBlockExtension.breakBlock(world, blockPos);
            IBlockExtension.breakBlock(world, blockPos.down(), BlockList.ROUTE_MAP_BMT.get());
        }

        super.onBreak2(world, pos, state, player);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(POSITION);
    }
}
