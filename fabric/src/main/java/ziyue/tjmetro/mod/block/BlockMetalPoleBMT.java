package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.block.base.BlockCustomColorBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @see BlockCustomColorBase
 * @since 1.0.0-beta-2
 */

public class BlockMetalPoleBMT extends BlockCustomColorBase implements DirectionHelper
{
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    public BlockMetalPoleBMT() {
        this(BlockHelper.createBlockSettings(false).nonOpaque());
    }

    public BlockMetalPoleBMT(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction direction = ctx.getSide();
        return getDefaultState2().with(new Property<>(NORTH.data), false).with(new Property<>(EAST.data), false).with(new Property<>(SOUTH.data), false).with(new Property<>(WEST.data), false)
                .with(new Property<>(FACING_NORMAL.data), direction == Direction.UP ? Direction.DOWN.data : direction.data);
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (IBlock.getStatePropertySafe(state, FACING_NORMAL) != Direction.DOWN) return state;

        boolean shouldConnect = false;
        if (IBlockExtension.isBlock(neighborState, BlockList.METAL_POLE_BMT.get())) {
            if (IBlock.getStatePropertySafe(neighborState, FACING_NORMAL) != Direction.DOWN) {
                shouldConnect = true;
            }
        }
        switch (direction) {
            case NORTH:
                return state.with(new Property<>(NORTH.data), shouldConnect);
            case EAST:
                return state.with(new Property<>(EAST.data), shouldConnect);
            case SOUTH:
                return state.with(new Property<>(SOUTH.data), shouldConnect);
            case WEST:
                return state.with(new Property<>(WEST.data), shouldConnect);
        }
        return state;
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction direction = IBlock.getStatePropertySafe(state, FACING_NORMAL);
        if (direction == Direction.DOWN) {
            final VoxelShape pole = Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);
            VoxelShape connect = Block.createCuboidShape(0, 0, 0, 0, 0, 0);
            if (IBlock.getStatePropertySafe(state, NORTH)) {
                connect = VoxelShapes.union(connect, IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 4, 6, Direction.NORTH));
            }
            if (IBlock.getStatePropertySafe(state, EAST)) {
                connect = VoxelShapes.union(connect, IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 4, 6, Direction.EAST));
            }
            if (IBlock.getStatePropertySafe(state, SOUTH)) {
                connect = VoxelShapes.union(connect, IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 4, 6, Direction.SOUTH));
            }
            if (IBlock.getStatePropertySafe(state, WEST)) {
                connect = VoxelShapes.union(connect, IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 4, 6, Direction.WEST));
            }
            return VoxelShapes.union(pole, connect);
        }
        return IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 4, 16, direction);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.metal_pole_bmt";
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING_NORMAL);
        properties.add(NORTH);
        properties.add(EAST);
        properties.add(SOUTH);
        properties.add(WEST);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.screen.ColorPickerScreen
     * @since 1.0.0-beta-2
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(BlockPos blockPos, BlockState blockState) {
            super(BlockEntityTypes.METAL_POLE_BMT.get(), blockPos, blockState);
        }

        @Override
        public int getDefaultColor(BlockPos pos) {
            return 0xfff100;
        }
    }
}
