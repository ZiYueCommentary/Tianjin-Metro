package ziyue.tjmetro.block;

import me.shedaniel.architectury.extensions.BlockEntityExtension;
import mtr.Blocks;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.data.IGuiExtends;
import ziyue.tjmetro.inventory.ContainerMenu;
import ziyue.tjmetro.entity.base.RandomizableContainerBlockEntityMapper;

import java.util.HashSet;
import java.util.List;

import static mtr.block.IBlock.THIRD;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * A device for clearing specify items from players' inventory.
 *
 * @author ZiYueCommentary
 * @since beta-1
 */

public class BlockMetalDetectionDoor extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlockMapper
{
    public BlockMetalDetectionDoor() {
        this(BlockBehaviour.Properties.copy(Blocks.LOGO.get()));
    }

    public BlockMetalDetectionDoor(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, blockPlaceContext.getHorizontalDirection());
        BlockPos clickPos = blockPlaceContext.getClickedPos();
        Level level = blockPlaceContext.getLevel();
        if (IBlock.isReplaceable(blockPlaceContext, Direction.UP, 3)) {
            level.setBlockAndUpdate(clickPos.above(1), blockState.setValue(THIRD, IBlock.EnumThird.MIDDLE));
            level.setBlockAndUpdate(clickPos.above(2), blockState.setValue(THIRD, IBlock.EnumThird.UPPER));
            return blockState.setValue(THIRD, IBlock.EnumThird.LOWER);
        }
        return null;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlockExtends.checkHoldingBrushOrWrench(level, player, () -> {
                    final BlockPos posMenuEntity = switch (blockState.getValue(THIRD)) {
                        case LOWER -> blockPos;
                        case MIDDLE -> blockPos.below(1);
                        default -> blockPos.below(2);
                    };
                    TileEntityMetalDetectionDoor entityDoorUpper = (TileEntityMetalDetectionDoor) level.getBlockEntity(posMenuEntity.above(2));
                    TileEntityMetalDetectionDoor entityDoorMiddle = (TileEntityMetalDetectionDoor) level.getBlockEntity(posMenuEntity.above(1));
                    TileEntityMetalDetectionDoor entityDoorLower = (TileEntityMetalDetectionDoor) level.getBlockEntity(posMenuEntity);
                    player.openMenu(entityDoorLower);
                    entityDoorUpper.setItems(entityDoorLower.getItems());
                    entityDoorMiddle.setItems(entityDoorLower.getItems());
                }
        );
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (blockState.getValue(THIRD) != IBlock.EnumThird.LOWER) return;
        if (entity instanceof Player player) {
            TileEntityMetalDetectionDoor entityMetalDetectionDoor = (TileEntityMetalDetectionDoor) level.getBlockEntity(blockPos);
            HashSet<Item> itemHashSet = entityMetalDetectionDoor.getItemHashSet();
            player.inventory.clearOrCountMatchingItems(itemStack -> itemHashSet.contains(itemStack.getItem()), -1, player.inventoryMenu.getCraftSlots());
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(THIRD) == IBlock.EnumThird.UPPER) {
            return IBlock.getVoxelShapeByDirection(0, 0, 1, 16, 6, 15, blockState.getValue(FACING));
        } else {
            VoxelShape left = IBlock.getVoxelShapeByDirection(0, 0, 1, 1, 16, 15, blockState.getValue(FACING));
            VoxelShape right = IBlock.getVoxelShapeByDirection(15, 0, 1, 16, 16, 15, blockState.getValue(FACING));
            return Shapes.or(left, right);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        switch (blockState.getValue(THIRD)) {
            case UPPER -> {
                IBlockExtends.breakBlock(level, blockPos.below(1), this);
                IBlockExtends.breakBlock(level, blockPos.below(2), this);
            }
            case MIDDLE -> {
                IBlockExtends.breakBlock(level, blockPos.above(), this);
                IBlockExtends.breakBlock(level, blockPos.below(), this);
            }
            case LOWER -> {
                IBlockExtends.breakBlock(level, blockPos.above(1), this);
                IBlockExtends.breakBlock(level, blockPos.above(2), this);
            }
        }
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, THIRD, WATERLOGGED);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityMetalDetectionDoor(blockPos, blockState);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        IGuiExtends.addHoldShiftTooltip(list, Text.translatable("tooltip.tjmetro.metal_detection_door"), true);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public static class TileEntityMetalDetectionDoor extends RandomizableContainerBlockEntityMapper implements BlockEntityExtension
    {
        public TileEntityMetalDetectionDoor(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.METAL_DETECTION_DOOR_TILE_ENTITY.get(), pos, state);
            this.items = NonNullList.withSize(9, ItemStack.EMPTY);
        }

        protected NonNullList<ItemStack> items;

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            super.readCompoundTag(compoundTag);
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            if (!this.tryLoadLootTable(compoundTag)) {
                ContainerHelper.loadAllItems(compoundTag, this.items);
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            super.writeCompoundTag(compoundTag);
            if (!this.trySaveLootTable(compoundTag)) {
                ContainerHelper.saveAllItems(compoundTag, this.items);
            }
        }

        @Override
        protected Component getDefaultName() {
            return Text.translatable("gui.tjmetro.metal_detection_door");
        }

        @Override
        protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
            return new ContainerMenu(MenuType.GENERIC_9x1, i, inventory, this, 1, 1);
        }

        public int getContainerSize() {
            return 9;
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

        @Override
        protected NonNullList<ItemStack> getItems() {
            return items;
        }

        public HashSet<Item> getItemHashSet() {
            HashSet<Item> itemHashSet = new HashSet<>();
            items.forEach(itemStack -> itemHashSet.add(itemStack.getItem()));
            return itemHashSet;
        }

        @Override
        protected void setItems(NonNullList<ItemStack> nonNullList) {
            items = nonNullList;
        }
    }
}
