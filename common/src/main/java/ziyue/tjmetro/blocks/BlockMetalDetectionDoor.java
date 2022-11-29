package ziyue.tjmetro.blocks;

import me.shedaniel.architectury.extensions.BlockEntityExtension;
import mtr.Blocks;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.commands.arguments.item.ItemPredicateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.IExtends;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

//todo too complex
public class BlockMetalDetectionDoor extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock
{
    public BlockMetalDetectionDoor() {
        this(BlockBehaviour.Properties.copy(Blocks.LOGO.get()).noCollission());
    }

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
        if (entity instanceof Player player) {

        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (blockState.getValue(TOP)) IExtends.breakBlock(level, blockPos.below(), this);
        else IExtends.breakBlock(level, blockPos.above(), this);
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TOP, WATERLOGGED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new TileEntityMetalDetectionDoor();
    }

    public static class TileEntityMetalDetectionDoor extends RandomizableContainerBlockEntity implements BlockEntityExtension
    {
        public TileEntityMetalDetectionDoor() {
            super(BlockEntityTypes.METAL_DETECTION_DOOR_TILE_ENTITY.get());
            this.items = NonNullList.withSize(27, ItemStack.EMPTY);
        }

        private NonNullList<ItemStack> items;

        @Override
        public final void load(BlockState blockState, CompoundTag compoundTag) {
            super.load(blockState, compoundTag);
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            if (!this.tryLoadLootTable(compoundTag)) {
                ContainerHelper.loadAllItems(compoundTag, this.items);
            }
        }

        @Override
        public final CompoundTag save(CompoundTag compoundTag) {
            super.save(compoundTag);
            return compoundTag;
        }

        @Override
        protected Component getDefaultName() {
            return Text.translatable("gui.tjmetro.metal_detection_door");
        }

        @Override
        protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
            return null;
        }

        public int getContainerSize() {
            return 27;
        }

        @Override
        public void loadClientData(BlockState pos, CompoundTag tag) {
            load(getBlockState(), tag);
        }

        @Override
        public CompoundTag saveClientData(CompoundTag tag) {
            save(tag);
            return tag;
        }

        public void setData() {
            setChanged();
            syncData();
        }

        @Override
        protected NonNullList<ItemStack> getItems() {
            return items;
        }

        @Override
        protected void setItems(NonNullList<ItemStack> nonNullList) {
            items = nonNullList;
        }
    }
}
