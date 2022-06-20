package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ziyue.tjmetro.IBlockExtends;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public class BlockRoadblock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public static final BooleanProperty IS_RIGHT = BooleanProperty.create("is_right");

    public BlockRoadblock() {
        super(Properties.copy(Blocks.LOGO.get()).lightLevel((state) -> 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockState state = defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, ctx.getHorizontalDirection()).setValue(IS_RIGHT, false);
        Direction direction = state.getValue(FACING);
        if (IBlock.isReplaceable(ctx, direction == Direction.NORTH ? Direction.EAST : direction == Direction.SOUTH ? Direction.WEST : direction == Direction.WEST ? Direction.NORTH : Direction.SOUTH, 2)) {
            ctx.getLevel().setBlock(direction == Direction.NORTH ? pos.east() : direction == Direction.SOUTH ? pos.west() : direction == Direction.WEST ? pos.north() : pos.south(), state.setValue(IS_RIGHT, true), 2);
            return state;
        }
        return null;
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        boolean isRight = state.getValue(IS_RIGHT);
        switch (state.getValue(FACING)) {
            case NORTH:
                IBlockExtends.onBreakCreative(world, player, isRight ? pos.west() : pos.east(), this);
                break;
            case WEST:
                IBlockExtends.onBreakCreative(world, player, isRight ? pos.south() : pos.north(), this);
                break;
            case SOUTH:
                IBlockExtends.onBreakCreative(world, player, isRight ? pos.east() : pos.west(), this);
                break;
            case EAST:
                IBlockExtends.onBreakCreative(world, player, isRight ? pos.north() : pos.south(), this);
                break;
        }
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(0, 0, 5, 16, 17, 11, blockState.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(0, 0, 5, 16, 24, 11, blockState.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, IS_RIGHT, WATERLOGGED);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
