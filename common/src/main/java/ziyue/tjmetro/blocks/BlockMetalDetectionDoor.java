package ziyue.tjmetro.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.IExtends;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

//todo
public class BlockMetalDetectionDoor extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public BlockMetalDetectionDoor(Properties properties) {
        super(properties);
    }

    public static final BooleanProperty TOP = BooleanProperty.create("top");

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, blockPlaceContext.getHorizontalDirection());
        if (IBlock.isReplaceable(blockPlaceContext, Direction.UP, 2)) {
            blockPlaceContext.getLevel().setBlockAndUpdate(blockPlaceContext.getClickedPos().above(), blockState.setValue(TOP, true));
            return blockState.setValue(TOP, false);
        }
        return null;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (blockState.getValue(TOP)) return;
        if (!(entity instanceof Player)) return;
        Inventory inventory = ((Player) entity).inventory;
        inventory.removeItem(BlockList.BENCH.get().asItem().getDefaultInstance());
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (blockState.getValue(TOP)) IExtends.breakBlock(level, blockPos.below());
        else IExtends.breakBlock(level, blockPos.above());
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TOP, WATERLOGGED);
    }

    public static class TileEntityMetalDetectionDoor extends BlockEntityClientSerializableMapper
    {
        public TileEntityMetalDetectionDoor(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            super.writeCompoundTag(compoundTag);
        }

        public void setData() {
            setChanged();
            syncData();
        }
    }
}
