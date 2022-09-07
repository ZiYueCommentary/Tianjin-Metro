package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
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

    public BlockRoadblock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, ctx.getHorizontalDirection()).setValue(IS_RIGHT, false);
        if (IBlock.isReplaceable(ctx, IBlockExtends.getRightDirection(state.getValue(FACING)), 2)) {
            ctx.getLevel().setBlock(IBlockExtends.getRightPos(ctx.getClickedPos(), ctx.getHorizontalDirection()), state.setValue(IS_RIGHT, true), 2);
            return state;
        }
        return null;
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        if (state.getValue(IS_RIGHT))
            IBlockExtends.breakBlock(world, IBlockExtends.getLeftPos(pos, state.getValue(FACING)), this);
        else
            IBlockExtends.breakBlock(world, IBlockExtends.getRightPos(pos, state.getValue(FACING)), this);
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(0, 0, 5, 16, 17, 11, blockState.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(0, 0, 5, 16, IBlockExtends.FENCE_HEIGHT, 11, blockState.getValue(FACING));
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
