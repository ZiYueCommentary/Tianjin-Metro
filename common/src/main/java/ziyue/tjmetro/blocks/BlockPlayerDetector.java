package ziyue.tjmetro.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Player detector.
 *
 * @author ZiYueCommentary
 * @since 1.0b
 */

//todo
public class BlockPlayerDetector extends Block implements SimpleWaterloggedBlock, EntityBlock
{
    public static final BooleanProperty UP = BooleanProperty.create("up");

    public BlockPlayerDetector() {
        super(Properties.copy(Blocks.STONE_PRESSURE_PLATE));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState()
                .setValue(POWERED, false)
                .setValue(WATERLOGGED, false)
                .setValue(FACING, blockPlaceContext.getHorizontalDirection())
                .setValue(UP, blockPlaceContext.getHorizontalDirection() == Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, WATERLOGGED, FACING, UP);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return getSignal(blockState, blockGetter, blockPos, direction);
    }

    @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new TileEntityPlayerDetector();
    }

    /*
     * 方块的tick方法不会在每刻都调用，animateTick方法虽然每刻都调用，但是个客户端方法，所以没法用setBlock方法
     * 方块实体的tick方法会在实现TickableBlockEntity接口后在每刻被调用
     * 此外，实体的tick方法也会在每刻被调用
     */
    public static class TileEntityPlayerDetector extends BlockEntity implements TickableBlockEntity
    {
        public TileEntityPlayerDetector() {
            super(BlockEntityTypes.PLAYER_DETECTOR_TILE_ENTITY.get());
        }

        @Override
        public void tick() {
            boolean[] hasPlayer = {
                    this.level.hasNearbyAlivePlayer(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 2),
                    this.level.hasNearbyAlivePlayer(getBlockPos().getX(), getBlockPos().below().getY(), getBlockPos().getZ(), 2),
                    this.level.hasNearbyAlivePlayer(getBlockPos().getX(), getBlockPos().above().getY(), getBlockPos().getZ(), 2)
            };
            boolean powered = hasPlayer[0] || hasPlayer[1] || hasPlayer[2];
            if (powered != getBlockState().getValue(POWERED)) {
                this.level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(POWERED, powered));
            }
        }
    }
}
