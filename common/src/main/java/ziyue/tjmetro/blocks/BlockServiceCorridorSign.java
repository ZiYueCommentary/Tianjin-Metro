package ziyue.tjmetro.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * @author ZiYueCommentary
 * @since beta-1
 */

public class BlockServiceCorridorSign extends HorizontalDirectionalBlock implements EntityBlockMapper, SimpleWaterloggedBlock
{
    public BlockServiceCorridorSign() {
        this(Properties.copy(BlockList.LOGO.get()));
    }

    public BlockServiceCorridorSign(Properties properties) {
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
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(1f, 3.5f, 0f, 15f, 14.5f, 0.5f, IBlock.getStatePropertySafe(blockState, FACING));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityServiceCorridorSign(blockPos, blockState);
    }

    public static class TileEntityServiceCorridorSign extends BlockEntityClientSerializableMapper
    {
        public final float yOffset;
        public final float zOffset;

        public TileEntityServiceCorridorSign(BlockPos pos, BlockState state) {
            this(BlockEntityTypes.SERVICE_CORRIDOR_SIGN_TILE_ENTITY.get(), pos, state, 0, 0.05f);
        }

        public TileEntityServiceCorridorSign(BlockEntityType<?> entityType, BlockPos pos, BlockState state, float yOffset, float zOffset) {
            super(entityType, pos, state);
            this.yOffset = yOffset;
            this.zOffset = zOffset;
        }

        public boolean shouldRender() {
            return true;
        }
    }
}
