package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockStationNameBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.block.base.BlockEntityRenderable;
import ziyue.tjmetro.mod.data.IGuiExtension;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author ZiYueCommentary
 * @see org.mtr.mod.block.BlockStationNameEntrance
 * @see BlockEntity
 * @since 1.0.0-beta-1
 */

public class BlockStationNameEntranceTianjin extends BlockStationNameBase implements IBlock
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
    public static final IntegerProperty STYLE = IntegerProperty.of("style", 0, 7);

    public final boolean pinyin;
    public final Type type;

    public BlockStationNameEntranceTianjin(boolean pinyin, Type type) {
        this(Blocks.createDefaultBlockSettings(false, state -> 15).noCollision(), pinyin, type);
    }

    public BlockStationNameEntranceTianjin(BlockSettings blockSettings, boolean pinyin, Type type) {
        super(blockSettings);
        this.pinyin = pinyin;
        this.type = type;
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final BlockPos pos = ctx.getBlockPos();
        final Direction side = ctx.getSide();
        final Direction facing = side.getOpposite();

        if (side != Direction.UP && side != Direction.DOWN) {
            final BlockState leftState = ctx.getWorld().getBlockState(pos.offset(facing.rotateYCounterclockwise()));
            final BlockState rightState = ctx.getWorld().getBlockState(pos.offset(facing.rotateYClockwise()));

            final int nearbyStyle;
            if (leftState.getBlock().data instanceof BlockStationNameEntranceTianjin) {
                nearbyStyle = IBlock.getStatePropertySafe(leftState, new Property<>(STYLE.data));
            } else if (rightState.getBlock().data instanceof BlockStationNameEntranceTianjin) {
                nearbyStyle = IBlock.getStatePropertySafe(rightState, new Property<>(STYLE.data));
            } else {
                nearbyStyle = 0;
            }

            return getDefaultState2()
                    .with(new Property<>(FACING.data), facing.data)
                    .with(new Property<>(STYLE.data), nearbyStyle);
        } else {
            return null;
        }
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingItem(world, player, item -> {
            if (item.data == Items.BRUSH.get().data) {
                world.setBlockState(pos, state.cycle(new Property<>(STYLE.data)));
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), new Property<>(STYLE.data), 1);
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), new Property<>(STYLE.data), 1);
            } else {
                Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos));
            }
        }, null, Items.BRUSH.get(), ItemList.WRENCH.get());
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final boolean tall = IBlock.getStatePropertySafe(state, STYLE) % 2 == 1;
        return IBlock.getVoxelShapeByDirection(0, tall ? 0 : 4, 0, 16, tall ? 16 : 12, 1, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.station_name_entrance_tianjin"));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(STYLE);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(pinyin, type, blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderStationNameEntranceTianjin
     * @see ziyue.tjmetro.mod.screen.RailwaySignScreen
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityRenderable
    {
        protected long selectedId = -1;
        public static final String KEY_SELECTED_ID = "selected_id";

        public BlockEntity(boolean pinyin, Type type, BlockPos pos, BlockState state) {
            super(getType(pinyin, type), pos, state, 0, 0.00625F);
        }

        public static BlockEntityType<?> getType(boolean pinyin, Type type) {
            switch (type) {
                case TRT:
                    if (pinyin) return BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_PINYIN.get();
                    else return BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN.get();
                case BMT:
                    if (pinyin) return BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_BMT_PINYIN.get();
                    else return BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_BMT.get();
                case JINJING:
                    if (pinyin) return BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_JINJING_PINYIN.get();
                    else return BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_JINJING.get();
                default:
                    return null;
            }
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            selectedId = compoundTag.getLong(KEY_SELECTED_ID);
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putLong(KEY_SELECTED_ID, selectedId);
            super.writeCompoundTag(compoundTag);
        }

        public void setData(long selectedId) {
            Consumer<Direction> setStyle = direction -> {
                BlockPos offsetPos = getPos2();
                org.mtr.mapping.holder.BlockEntity blockEntity = getWorld2().getBlockEntity(offsetPos);
                while ((blockEntity != null) && (blockEntity.data instanceof BlockEntity)) {
                    final BlockEntity entity = (BlockEntity) blockEntity.data;
                    entity.selectedId = selectedId;
                    entity.markDirty2();
                    offsetPos = offsetPos.offset(direction);
                    blockEntity = getWorld2().getBlockEntity(offsetPos);
                }
            };
            setStyle.accept(IBlock.getStatePropertySafe(getCachedState2(), FACING).rotateYClockwise());
            setStyle.accept(IBlock.getStatePropertySafe(getCachedState2(), FACING).rotateYCounterclockwise());
            this.selectedId = selectedId;
            markDirty2();
        }

        public long getSelectedId() {
            return selectedId;
        }
    }

    public enum Type
    {
        TRT,
        BMT,
        JINJING
    }
}
