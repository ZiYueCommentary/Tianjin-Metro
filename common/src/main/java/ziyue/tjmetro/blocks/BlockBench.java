package ziyue.tjmetro.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;

import java.util.HashSet;
import java.util.Set;

import static mtr.block.IBlock.SIDE_EXTENDED;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Oak bench, model based on <b>Adorn mod</b>.
 *
 * @author ZiYueCommentary
 * @since beta-1
 */

public class BlockBench extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlockMapper
{
    public static final Set<Minecart> SeatSet = new HashSet<>();
    public static final String ENTITY_SEAT_NAME = "RW50aXR5VGlhbmppbk1ldHJvU2VhdA==";

    public BlockBench() {
        this(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_PLANKS));
    }

    public BlockBench(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        TileEntityBench entityBench = (TileEntityBench) world.getBlockEntity(pos);
        if (entityBench.seat == null) {
            entityBench.ride(world, pos, player);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction direction = blockPlaceContext.getHorizontalDirection();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        Level world = blockPlaceContext.getLevel();
        return defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, direction).setValue(SIDE_EXTENDED, getPos(direction, blockPos, world));
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        level.setBlockAndUpdate(blockPos, blockState.setValue(SIDE_EXTENDED, getPos(blockState.getValue(FACING), blockPos, level)));
    }

    public IBlock.EnumSide getPos(Direction direction, BlockPos blockPos, Level world) {
        BlockState counterClockWiseState = world.getBlockState(blockPos.relative(direction.getCounterClockWise()));
        BlockState clockWiseState = world.getBlockState(blockPos.relative(direction.getClockWise()));
        boolean[] blockSame = {
                (counterClockWiseState.getBlock() == BlockList.BENCH.get()) && (counterClockWiseState.getValue(FACING) == direction),
                (clockWiseState.getBlock() == BlockList.BENCH.get()) && (clockWiseState.getValue(FACING) == direction)
        };
        return blockSame[0] && blockSame[1] ? IBlock.EnumSide.MIDDLE : blockSame[0] ? IBlock.EnumSide.LEFT : blockSame[1] ? IBlock.EnumSide.RIGHT : IBlock.EnumSide.SINGLE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, FACING, SIDE_EXTENDED);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape top = IBlock.getVoxelShapeByDirection(0.0, 8.0, 1.0, 16.0, 9.5, 15.0, blockState.getValue(FACING));
        VoxelShape left = IBlock.getVoxelShapeByDirection(12.0, 0.0, 2.0, 14.0, 8.0, 14.0, blockState.getValue(FACING));
        VoxelShape right = IBlock.getVoxelShapeByDirection(2.0, 0.0, 2.0, 4.0, 8.0, 14.0, blockState.getValue(FACING));
        return switch (blockState.getValue(SIDE_EXTENDED)) {
            case SINGLE -> Shapes.or(top, left, right);
            case LEFT -> Shapes.or(top, left);
            case RIGHT -> Shapes.or(top, right);
            default -> top;
        };
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityBench(blockPos, blockState);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public static class TileEntityBench extends BlockEntityMapper implements TickableBlockEntity
    {
        Minecart seat;

        public TileEntityBench(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.BENCH_TILE_ENTITY.get(), pos, state);
        }

        @Override
        public void tick() {
            if ((seat != null) && (seat.getPassengers().isEmpty())) {
                SeatSet.remove(seat);
                seat.kill();
                seat = null;
            }
        }

        /**
         * Create a minecart as the seat.
         * For some unknown reason, Fabric 1.16.5 was unable to register entities properly.
         * The minecart will be the seat of the bench so that players can sit down.
         *
         * @see ziyue.tjmetro.mixin.mixins.EntityRenderDispatcherMixin
         * @see ziyue.tjmetro.mixin.mixins.MinecartRendererMixin
         * @see ziyue.tjmetro.mixin.mixins.MinecraftServerMixin
         */
        public void ride(Level world, BlockPos pos, Player player) {
            seat = new Minecart(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
            {
                @Override
                public boolean isPushable() {
                    return false;
                }
            };
            seat.setCustomName(Text.literal(ENTITY_SEAT_NAME));
            seat.setNoGravity(true);
            world.addFreshEntity(seat);
            SeatSet.add(seat);
            player.startRiding(seat);
        }
    }
}
