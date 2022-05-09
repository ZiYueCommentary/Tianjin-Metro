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

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public class BlockRoadblock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public static final BooleanProperty IS_RIGHT = BooleanProperty.create("is_right");
    public static final BooleanProperty WATERLOGGED = BooleanProperty.create("waterlogged");

    public BlockRoadblock() {
        super(Properties.copy(Blocks.LOGO.get()).lightLevel((state) -> 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockState state = defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, ctx.getHorizontalDirection()).setValue(IS_RIGHT, false);
        Direction direction = state.getValue(FACING);
        if (IBlock.isReplaceable(ctx, direction == Direction.NORTH ? Direction.EAST : direction == Direction.SOUTH ? Direction.WEST : direction == Direction.WEST ? Direction.NORTH : Direction.SOUTH, 2)) {
            ctx.getLevel().setBlock(direction == Direction.NORTH ? pos.east() : direction == Direction.SOUTH ? pos.west() : direction == Direction.WEST ? pos.north() : pos.south(),
                    state.setValue(IS_RIGHT, true), 2);
            return state;
        }
        return null;
    }
    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        boolean isRight = state.getValue(IS_RIGHT);
        switch (state.getValue(FACING)){
            case NORTH:
                onBreakCreative(world, player, isRight ? pos.west() : pos.east());
                break;
            case WEST:
                onBreakCreative(world, player, isRight ? pos.south() : pos.north());
                break;
            case SOUTH:
                onBreakCreative(world, player, isRight ? pos.east() : pos.west());
                break;
            case EAST:
                onBreakCreative(world, player, isRight ? pos.north() : pos.south());
                break;
        }
        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(0,0,5,16,17,11, blockState.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(0,0,5,16,24,11, blockState.getValue(FACING));
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

    static void onBreakCreative(Level world, Player player, BlockPos pos) {
        if (!world.isClientSide && player == null || player.isCreative()) {
            try {
                world.setBlock(pos, world.getBlockState(pos).getValue(WATERLOGGED) ? net.minecraft.world.level.block.Blocks.WATER.defaultBlockState() : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            } catch(Exception exception) {
                ziyue.tjmetro.Main.LOGGER.info("Get WATERLOGGED state failed: replace with air");
                world.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 35);
            }
            final BlockState state = world.getBlockState(pos);
            world.levelEvent(player, 2001, pos, Block.getId(state));
        }
    }
}
