package ziyue.tjmetro.block;

import mtr.Blocks;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class BlockStationNamePlate extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlockMapper
{
    public static final IntegerProperty POS = IntegerProperty.create("pos", 0, 3);

    public BlockStationNamePlate() {
        this(BlockBehaviour.Properties.copy(Blocks.STATION_NAME_TALL_WALL.get()));
    }

    public BlockStationNamePlate(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection()).setValue(WATERLOGGED, false);
        if (IBlock.isReplaceable(blockPlaceContext, blockState.getValue(FACING).getClockWise(), 4)) {
            for (int i = 1; i < 4; i++) {
                blockPlaceContext.getLevel().setBlock(blockPlaceContext.getClickedPos().relative(blockState.getValue(FACING).getClockWise(), i), blockState.setValue(POS, i), 2);
            }
            return blockState.setValue(POS, 0);
        }
        return null;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        for (int i = 1; i < blockState.getValue(POS) + 1; i++) {
            IBlockExtends.breakBlock(level, blockPos.relative(blockState.getValue(FACING).getCounterClockWise(), i), this);
        }
        for (int i = 1; i < 4 - blockState.getValue(POS); i++) {
            IBlockExtends.breakBlock(level, blockPos.relative(blockState.getValue(FACING).getClockWise(), i), this);
        }
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
        final int pos = IBlock.getStatePropertySafe(blockState, POS);
        if ((pos % 3) != 0) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
        } else {
            final VoxelShape main = IBlock.getVoxelShapeByDirection(3.25, 0, 7, 16, 12, 9, (pos == 3) ? facing.getOpposite() : facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(2, 0, 7, 3.25, 16, 9, (pos == 3) ? facing.getOpposite() : facing);
            return Shapes.or(main, pole);
        }
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityStationNamePlate(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, POS);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public static class TileEntityStationNamePlate extends BlockEntityClientSerializableMapper
    {
        public final float yOffset;
        public final float zOffset;
        protected Long selectedId;
        protected static final String KEY_SELECTED_ID = "selected_id";

        public TileEntityStationNamePlate(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_PLATE_TILE_ENTITY.get(), pos, state);
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
                while (level.getBlockEntity(offsetPos) instanceof TileEntityStationNamePlate entity) {
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
