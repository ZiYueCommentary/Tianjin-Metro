package ziyue.tjmetro.block;

import mtr.Items;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.ItemList;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * @author ZiYueCommentary
 * @see mtr.block.BlockStationNameEntrance
 * @since beta-1
 */

public class BlockStationNameEntranceTianjin extends BlockStationNameBase implements IBlock, SimpleWaterloggedBlock
{
    /*
     * 0 - short
     * 1 - tall
     * 2 - short, no routes
     * 3 - tall, no routes
     * 4 - short, no background
     * 5 - tall, no background
     * 6 - short, no routes, no background
     * 7 - tall, no routes, no background
     */
    public static final IntegerProperty STYLE = IntegerProperty.create("style", 0, 7);

    public final boolean pinyin;
    public final boolean isBMT;

    public BlockStationNameEntranceTianjin(boolean pinyin, boolean isBMT) {
        this(Properties.copy(mtr.Blocks.STATION_NAME_ENTRANCE.get()), pinyin, isBMT);
    }

    public BlockStationNameEntranceTianjin(Properties properties, boolean pinyin, boolean isBMT) {
        super(properties);
        this.pinyin = pinyin;
        this.isBMT = isBMT;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingItem(world, player, item -> {
            if (item == Items.BRUSH.get()) {
                world.setBlockAndUpdate(pos, state.cycle(STYLE));
                this.propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), STYLE, 1);
                this.propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), STYLE, 1);
            } else {
                PacketGuiServer.openRailwaySignScreenS2C((ServerPlayer) player, pos);
            }
        }, null, Items.BRUSH.get(), ItemList.WRENCH.get());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return world.getBlockState(pos.relative(facing)).getMaterial().isSolid();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction side = ctx.getClickedFace();
        return side != Direction.UP && side != Direction.DOWN ? this.defaultBlockState().setValue(FACING, side.getOpposite()).setValue(WATERLOGGED, false) : null;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        return direction.getOpposite() == IBlock.getStatePropertySafe(state, FACING).getOpposite() && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        boolean tall = IBlock.getStatePropertySafe(state, STYLE) % 2 == 1;
        return IBlock.getVoxelShapeByDirection(0.0, tall ? 0.0 : 4.0, 0.0, 16.0, tall ? 16.0 : 12.0, 1.0, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityStationNameEntranceTianjin(pinyin, isBMT, pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STYLE, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public static class TileEntityStationNameEntranceTianjin extends BlockEntityClientSerializableMapper
    {
        public final float yOffset;
        public final float zOffset;
        protected Long selectedId;
        protected static final String KEY_SELECTED_ID = "selected_id";

        public TileEntityStationNameEntranceTianjin(boolean pinyin, boolean isBMT, BlockPos pos, BlockState state) {
            super(isBMT ?
                    (pinyin ? BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_BMT_PINYIN_TILE_ENTITY.get() : BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_BMT_TILE_ENTITY.get()) :
                    (pinyin ? BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_PINYIN_TILE_ENTITY.get() : BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_TILE_ENTITY.get()), pos, state);
            this.yOffset = 0.0F;
            this.zOffset = 0.00625F;
            this.selectedId = -1L;
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            selectedId = compoundTag.getLong(KEY_SELECTED_ID);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putLong(KEY_SELECTED_ID, selectedId);
        }

        public void setData(Long selectedId) {
            Consumer<Direction> setStyle = direction -> {
                BlockPos offsetPos = getBlockPos();
                while (level.getBlockState(offsetPos).getBlock() instanceof BlockStationNameEntranceTianjin) {
                    TileEntityStationNameEntranceTianjin entity = (TileEntityStationNameEntranceTianjin) level.getBlockEntity(offsetPos);
                    entity.selectedId = selectedId;
                    entity.setChanged();
                    entity.syncData();
                    offsetPos = offsetPos.relative(direction);
                }
            };
            setStyle.accept(IBlock.getStatePropertySafe(getBlockState(), FACING).getClockWise());
            setStyle.accept(IBlock.getStatePropertySafe(getBlockState(), FACING).getCounterClockWise());
            this.selectedId = selectedId;
            setChanged();
            syncData();
        }

        public Long getSelectedId() {
            return selectedId;
        }
    }
}
