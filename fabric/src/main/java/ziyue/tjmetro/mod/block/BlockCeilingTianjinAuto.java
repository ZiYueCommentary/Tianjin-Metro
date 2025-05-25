package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockWaterloggable;
import org.mtr.mod.block.IBlock;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockCeilingTianjinAuto extends BlockWaterloggable
{
    public static final BooleanProperty LIGHT = BooleanProperty.of("light");

    public BlockCeilingTianjinAuto() {
        this(Blocks.createDefaultBlockSettings(true, state -> 15));
    }

    public BlockCeilingTianjinAuto(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nonnull
    @Override
    public BlockState getPlacementState2(ItemPlacementContext itemPlacementContext) {
        return super.getPlacementState2(itemPlacementContext).with(new Property<>(LIGHT.data), hasLight(itemPlacementContext.getBlockPos()));
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0, 7, 0, 16, 10, 16);
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos).with(new Property<>(LIGHT.data), hasLight(pos));
    }

    @Override
    public void randomDisplayTick2(BlockState state, World world, BlockPos pos, Random random) {
        final boolean light = hasLight(pos);
        if (IBlock.getStatePropertySafe(state, LIGHT) != light) {
            world.setBlockState(pos, state.with(new Property<>(LIGHT.data), light));
        }
    }

    @Override
    public boolean hasRandomTicks2(BlockState state) {
        return true;
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        super.addBlockProperties(properties);
        properties.add(LIGHT);
    }

    protected boolean hasLight(BlockPos pos) {
        return (pos.getZ() % 2 == 0) && (pos.getX() % 2 == 0);
    }
}
