package ziyue.tjmetro.blocks;

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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.entity.SeatEntity;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class BlockBench extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public static final IntegerProperty POS = IntegerProperty.create("pos", 0, 3);

    public BlockBench() {
        super(Properties.copy(Blocks.OAK_PLANKS));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        SeatEntity seatEntity = new SeatEntity(world, pos.getX(), pos.getY(), pos.getZ());
        seatEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
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
        boolean[] blockSame = new boolean[2];
        switch (direction){
            case NORTH:
            case SOUTH:
                blockSame[0] = world.getBlockState(blockPos.west()).getBlock() == BlockList.BENCH.get();
                blockSame[1] = world.getBlockState(blockPos.east()).getBlock() == BlockList.BENCH.get();
                break;
            case WEST:
            case EAST:
                blockSame[0] = world.getBlockState(blockPos.north()).getBlock() == BlockList.BENCH.get();
                blockSame[1] = world.getBlockState(blockPos.south()).getBlock() == BlockList.BENCH.get();
                break;
        }
        int pos = blockSame[0] && blockSame[1] ? 3 : blockSame[0] ? 1 : blockSame[1] ? 2 : 0;
        return defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, direction).setValue(POS, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape top = Block.box(0.0, 8.0, 1.0, 16.0, 10.0, 15.0);
        VoxelShape left = Block.box(1.0, 8.0, 0.0, 15.0, 10.0, 16.0);
        return Shapes.join(top, left, BooleanOp.OR);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
