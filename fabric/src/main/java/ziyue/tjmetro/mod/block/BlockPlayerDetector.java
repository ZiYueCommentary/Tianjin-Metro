package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Player detector.
 *
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since 1.0.0-beta-1
 */

public class BlockPlayerDetector extends BlockExtension implements DirectionHelper, BlockWithEntity
{
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");

    public BlockPlayerDetector() {
        this(BlockHelper.createBlockSettings(false).breakInstantly());
    }

    public BlockPlayerDetector(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(POWERED.data), false).with(new Property<>(FACING_NORMAL.data), ctx.getSide().data);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(POWERED);
        properties.add(FACING_NORMAL);
    }

    @Override
    public int getStrongRedstonePower2(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return IBlock.getStatePropertySafe(state, POWERED) ? 15 : 0;
    }

    @Override
    public int getWeakRedstonePower2(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return IBlock.getStatePropertySafe(state, POWERED) ? 15 : 0;
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = IBlock.getStatePropertySafe(state, FACING_NORMAL);
        switch (direction) {
            case UP:
                return Block.createCuboidShape(6, 0, 6, 10, 1, 10);
            case DOWN:
                return Block.createCuboidShape(6, 15, 6, 10, 16, 10);
            default:
                return IBlock.getVoxelShapeByDirection(1.7, 2, 14.8, 14.3, 4, 17, direction);
        }
    }

    @Override
    public boolean emitsRedstonePower2(BlockState state) {
        return true;
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see BlockPlayerDetector
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityExtension
    {
        public BlockEntity(BlockPos blockPos, BlockState blockState) {
            super(BlockEntityTypes.PLAYER_DETECTOR.get(), blockPos, blockState);
        }

        @Override
        public void blockEntityTick() {
            final PlayerEntity player = this.getWorld2().getClosestPlayer(this.getPos2().getX(), this.getPos2().getY(), this.getPos2().getZ(), 3, false);
            final boolean powered = player != null;
            if (powered != IBlock.getStatePropertySafe(this.getCachedState2(), POWERED)) {
                this.getWorld2().setBlockState(this.getPos2(), this.getCachedState2().with(new Property<>(POWERED.data), powered));
                this.getWorld2().updateNeighbors(this.getPos2(), this.getCachedState2().getBlock());
                this.getWorld2().updateNeighbors(this.getPos2().down(), this.getCachedState2().getBlock());
            }
        }
    }
}
