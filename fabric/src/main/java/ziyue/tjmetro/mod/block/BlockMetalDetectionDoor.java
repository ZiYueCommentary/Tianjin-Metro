package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.SoundEvents;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mapping.DefaultedItemStackList;
import ziyue.tjmetro.mapping.MetalDetectionDoorEntity;
import ziyue.tjmetro.mapping.PlayerInventoryHelper;
import ziyue.tjmetro.mapping.RegistryHelper;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.data.IGuiExtension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A device for clearing specify items from players' inventory.
 *
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since 1.0.0-beta-1
 */

public class BlockMetalDetectionDoor extends BlockExtension implements DirectionHelper, BlockWithEntity, IBlock
{
    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    public BlockMetalDetectionDoor() {
        this(Blocks.createDefaultBlockSettings(true));
    }

    public BlockMetalDetectionDoor(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final BlockState state = getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
        final BlockPos pos = ctx.getBlockPos();
        final World world = ctx.getWorld();
        if (IBlock.isReplaceable(ctx, Direction.UP, 3)) {
            world.setBlockState(pos.up(1), state.with(new Property<>(THIRD.data), EnumThird.MIDDLE));
            world.setBlockState(pos.up(2), state.with(new Property<>(THIRD.data), EnumThird.UPPER));
            return state.with(new Property<>(THIRD.data), EnumThird.LOWER);
        }
        return null;
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> {
            final BlockPos blockPos;
            switch (IBlock.getStatePropertySafe(state, THIRD)) {
                case LOWER:
                    blockPos = pos;
                    break;
                case MIDDLE:
                    blockPos = pos.down(1);
                    break;
                default:
                    blockPos = pos.down(2);
            }
            MetalDetectionDoorEntity entity = new MetalDetectionDoorEntity(world, blockPos, (BlockEntity) world.getBlockEntity(blockPos).data);
            world.spawnEntity(new Entity(entity));
            entity.interact(player.data, hand.data);
            BlockEntity lower = (BlockEntity) world.getBlockEntity(blockPos).data;
            BlockEntity middle = (BlockEntity) world.getBlockEntity(blockPos.up(1)).data;
            BlockEntity upper = (BlockEntity) world.getBlockEntity(blockPos.up(2)).data;
            middle.setData(lower.inventory);
            upper.setData(lower.inventory);
        });
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        if (IBlock.getStatePropertySafe(state, THIRD) == EnumThird.UPPER) {
            return IBlock.getVoxelShapeByDirection(0, 0, 1, 16, 6, 15, direction);
        } else {
            final VoxelShape left = IBlock.getVoxelShapeByDirection(0, 0, 1, 1, 16, 15, direction);
            final VoxelShape right = IBlock.getVoxelShapeByDirection(15, 0, 1, 16, 16, 15, direction);
            return VoxelShapes.union(left, right);
        }
    }

    @Nonnull
    @Override
    public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final VoxelShape shape = this.getOutlineShape2(state, world, pos, context);
        if ((!IBlock.getStatePropertySafe(state, OPEN)) && (IBlock.getStatePropertySafe(state, THIRD) != EnumThird.UPPER)) {
            final VoxelShape barrier = IBlock.getVoxelShapeByDirection(0, 0, 1, 16, 16, 2, IBlock.getStatePropertySafe(state, FACING));
            return VoxelShapes.union(shape, barrier);
        }
        return shape;
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        switch (IBlock.getStatePropertySafe(state, THIRD)) {
            case UPPER:
                IBlockExtension.breakBlock(world, pos.down(1), BlockList.METAL_DETECTION_DOOR.get());
                IBlockExtension.breakBlock(world, pos.down(2), BlockList.METAL_DETECTION_DOOR.get());
                break;
            case MIDDLE:
                IBlockExtension.breakBlock(world, pos.up(1), BlockList.METAL_DETECTION_DOOR.get());
                IBlockExtension.breakBlock(world, pos.down(1), BlockList.METAL_DETECTION_DOOR.get());
                break;
            case LOWER:
                IBlockExtension.breakBlock(world, pos.up(1), BlockList.METAL_DETECTION_DOOR.get());
                IBlockExtension.breakBlock(world, pos.up(2), BlockList.METAL_DETECTION_DOOR.get());
                break;
        }
        super.onBreak2(world, pos, state, player);
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.metal_detection_door"));
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(THIRD);
        properties.add(OPEN);
    }

    /**
     * @author ZiYueCommentary
     * @since 1.0.0-beta-2
     */
    public static class BlockEntity extends BlockEntityExtension
    {
        public final DefaultedItemStackList inventory;

        public BlockEntity(BlockPos blockPos, BlockState blockState) {
            super(BlockEntityTypes.METAL_DETECTION_DOOR.get(), blockPos, blockState);
            this.inventory = DefaultedItemStackList.ofSize(9);
        }

        @Override
        public void blockEntityTick() {
            if (IBlock.getStatePropertySafe(getCachedState2(), THIRD) != EnumThird.LOWER) return;

            final PlayerEntity player = getWorld2().getClosestPlayer(getPos2().getX(), getPos2().getY(), getPos2().getZ(), 1, false);
            if (player != null) {
                if (getPos2().getX() == Math.floor(player.getX()) && getPos2().getY() == Math.round(player.getY()) && getPos2().getZ() == Math.floor(player.getZ())) {
                    List<?> items = this.inventory.data.stream().map(itemStack -> itemStack.getItem()).collect(Collectors.toList()); // Do not use method reference.
                    PlayerInventoryHelper.clearItems(player, items::contains);
                    if (IBlock.getStatePropertySafe(getCachedState2(), OPEN)) return;
                    getWorld2().playSound(null, getPos2(), SoundEvents.TICKET_BARRIER.get(), SoundCategory.BLOCKS, 1, 1);
                    getWorld2().setBlockState(getPos2(), getCachedState2().with(new Property<>(OPEN.data), true));
                }
            } else {
                getWorld2().setBlockState(getPos2(), getCachedState2().with(new Property<>(OPEN.data), false));
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            for (int i = 0; i < this.inventory.size(); i++) {
                compoundTag.data.putString(Integer.toString(i), RegistryHelper.getIdentifierByItem(this.inventory.get(i).getItem()).data.toString());
            }
            super.writeCompoundTag(compoundTag);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            for (int i = 0; i < this.inventory.size(); i++) {
                this.inventory.set(i, RegistryHelper.getItemStackByIdentifier(new Identifier(compoundTag.getString(Integer.toString(i)))));
            }
            super.readCompoundTag(compoundTag);
        }

        public void setData(DefaultedItemStackList list) {
            for (int i = 0; i < this.inventory.size(); i++) {
                this.inventory.set(i, RegistryHelper.cloneSingleItemStack(list.get(i)));
            }
            markDirty2();
        }
    }
}
