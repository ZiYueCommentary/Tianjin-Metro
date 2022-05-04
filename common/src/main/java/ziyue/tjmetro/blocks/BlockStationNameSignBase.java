package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public abstract class BlockStationNameSignBase extends BlockStationNameBase implements SimpleWaterloggedBlock
{
    public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    public BlockStationNameSignBase(){
        super(Properties.copy(Blocks.STATION_NAME_TALL_WALL.get()));
    }

    public abstract BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, FACING, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(1f, 3.5f, 0f, 15f, 14.5f, 0.5f, IBlock.getStatePropertySafe(blockState, FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final Direction side = ctx.getClickedFace();
        if (side != Direction.UP && side != Direction.DOWN)
            return defaultBlockState().setValue(FACING, side.getOpposite()).setValue(WATERLOGGED, false);
        else
            return null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return world.getBlockState(pos.relative(facing)).isFaceSturdy(world, pos.relative(facing), facing.getOpposite());
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
