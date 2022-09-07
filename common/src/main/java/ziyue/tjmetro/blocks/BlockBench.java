package ziyue.tjmetro.blocks;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.entity.SeatEntity;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Oak bench, model based on <b>Adorn mod</b>.
 *
 * @author ZiYueCommentary
 * @see SeatEntity
 * @since 1.0b
 */

public class BlockBench extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public static final IntegerProperty POS = IntegerProperty.create("pos", 0, 3);

    public BlockBench(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        SeatEntity seatEntity = new SeatEntity(world, pos.getX(), pos.getY(), pos.getZ());
        seatEntity.noCulling = true;
        seatEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
        seatEntity.setInvisible(true);
        world.addFreshEntity(seatEntity);
        player.startRiding(seatEntity, true);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction direction = blockPlaceContext.getHorizontalDirection();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        Level world = blockPlaceContext.getLevel();
        int pos = getPos(direction, blockPos, world);
        return defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, direction).setValue(POS, pos);
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        level.setBlockAndUpdate(blockPos, blockState.setValue(POS, getPos(blockState.getValue(FACING), blockPos, level)));
    }

    public int getPos(Direction direction, BlockPos blockPos, Level world) {
        boolean[] blockSame = new boolean[2];
        switch (direction) {
            case NORTH:
                blockSame[0] = (world.getBlockState(blockPos.west()).getBlock() == BlockList.BENCH.get()) && (world.getBlockState(blockPos.west()).getValue(FACING) == direction);
                blockSame[1] = (world.getBlockState(blockPos.east()).getBlock() == BlockList.BENCH.get()) && (world.getBlockState(blockPos.east()).getValue(FACING) == direction);
                break;
            case EAST:
                blockSame[0] = (world.getBlockState(blockPos.north()).getBlock() == BlockList.BENCH.get()) && (world.getBlockState(blockPos.north()).getValue(FACING) == direction);
                blockSame[1] = (world.getBlockState(blockPos.south()).getBlock() == BlockList.BENCH.get()) && (world.getBlockState(blockPos.south()).getValue(FACING) == direction);
                break;
            case WEST:
                blockSame[1] = (world.getBlockState(blockPos.north()).getBlock() == BlockList.BENCH.get()) && (world.getBlockState(blockPos.north()).getValue(FACING) == direction);
                blockSame[0] = (world.getBlockState(blockPos.south()).getBlock() == BlockList.BENCH.get()) && (world.getBlockState(blockPos.south()).getValue(FACING) == direction);
                break;
            case SOUTH:
                blockSame[1] = (world.getBlockState(blockPos.west()).getBlock() == BlockList.BENCH.get()) && (world.getBlockState(blockPos.west()).getValue(FACING) == direction);
                blockSame[0] = (world.getBlockState(blockPos.east()).getBlock() == BlockList.BENCH.get()) && (world.getBlockState(blockPos.east()).getValue(FACING) == direction);
                break;
        }
        return blockSame[0] && blockSame[1] ? 3 : blockSame[0] ? 1 : blockSame[1] ? 2 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, POS);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape top = IBlock.getVoxelShapeByDirection(0.0, 8.0, 1.0, 16.0, 9.5, 15.0, blockState.getValue(FACING));
        VoxelShape left = IBlock.getVoxelShapeByDirection(12.0, 0.0, 2.0, 14.0, 8.0, 14.0, blockState.getValue(FACING));
        VoxelShape right = IBlock.getVoxelShapeByDirection(2.0, 0.0, 2.0, 4.0, 8.0, 14.0, blockState.getValue(FACING));
        switch (blockState.getValue(POS)) {
            case 0:
                return Shapes.or(top, left, right);
            case 1:
                return Shapes.or(top, left);
            case 2:
                return Shapes.or(top, right);
            default:
                return top;
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
