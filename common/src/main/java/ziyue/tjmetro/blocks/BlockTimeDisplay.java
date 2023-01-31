package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * A time display, require better model.
 *
 * @author ZiYueCommentary
 * @since beta-1
 */

public class BlockTimeDisplay extends HorizontalDirectionalBlock implements EntityBlockMapper, SimpleWaterloggedBlock
{
    public BlockTimeDisplay() {
        this(BlockBehaviour.Properties.copy(Blocks.CLOCK.get()).lightLevel(state -> 5));
    }

    public BlockTimeDisplay(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection()).setValue(WATERLOGGED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityTimeDisplay(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(-3, 8.5, 6, 18, 16, 10, blockState.getValue(FACING));
    }

    public static class TileEntityTimeDisplay extends BlockEntityClientSerializableMapper
    {
        public final float yOffset;
        public final float zOffset;

        public TileEntityTimeDisplay(BlockPos pos, BlockState state) {
            this(BlockEntityTypes.TIME_DISPLAY_TILE_ENTITY.get(), pos, state, 0.21f, 0.63f);
        }

        public TileEntityTimeDisplay(BlockEntityType<?> entityType, BlockPos pos, BlockState state, float yOffset, float zOffset) {
            super(entityType, pos, state);
            this.yOffset = yOffset;
            this.zOffset = zOffset;
        }

        public boolean shouldRender() {
            return true;
        }
    }
}
