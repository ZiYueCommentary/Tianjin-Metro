package ziyue.tjmetro.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.Items;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.block.base.IRailwaySign;
import ziyue.tjmetro.mod.data.IGuiExtension;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A railway-sign-like block.
 *
 * @author ZiYueCommentary
 * @see BlockEntity
 * @see BlockRailwaySignBase
 * @since 1.0.0-beta-4
 */

public class BlockStationNavigator extends BlockRailwaySignBase
{
    public static final BooleanProperty ARROW_LEFT = BooleanProperty.of("arrow_left");

    public final int length;

    public BlockStationNavigator(int length) {
        this(length, Blocks.createDefaultBlockSettings(true, state -> 15));
    }

    public BlockStationNavigator(int length, BlockSettings blockSettings) {
        super(blockSettings, length, true);
        this.length = length;
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return IRailwaySign.getStateForNeighborUpdate(state, direction, neighborState, BlockList.STATION_NAVIGATOR_MIDDLE.get());
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        // This block should take three blocks, no matter how longs it is.
        // Navigator, which takes more blocks, is too long.
        final Direction facing = ctx.getPlayerFacing();
        if (IBlock.isReplaceable(ctx, facing.rotateYClockwise(), 2) && IBlock.isReplaceable(ctx, facing.rotateYCounterclockwise(), 2)) {
            final World world = ctx.getWorld();
            final BlockPos pos = ctx.getBlockPos();
            world.setBlockState(pos.offset(facing.rotateYCounterclockwise()), getDefaultState2().with(new Property<>(FACING.data), facing.data));
            world.setBlockState(pos.offset(facing.rotateYClockwise()), getDefaultState2().with(new Property<>(FACING.data), facing.getOpposite().data));
            world.updateNeighbors(pos, org.mtr.mapping.holder.Blocks.getAirMapped());
            getDefaultState2().updateNeighbors(new WorldAccess(world.data), pos, 3);
            return BlockList.STATION_NAVIGATOR_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data);
        }
        return null;
    }

    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingItem(world, player, item -> {
            if (item.data == ItemList.WRENCH.get().data) {
                final BlockPos checkPos = findEndWithDirection(world, pos, hit.getSide().getOpposite(), false);
                Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(checkPos));
            } else {
                final BlockPos checkPos = findEndWithDirection(world, pos, hit.getSide().getOpposite(), false);
                world.setBlockState(checkPos, world.getBlockState(checkPos).cycle(new Property<>(ARROW_LEFT.data)));
            }
        }, null, ItemList.WRENCH.get(), Items.BRUSH.get());
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {

    }

    @Override
    protected int getMiddleLength() {
        return 1;
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.mtr.railway_sign_length", length).formatted(TextFormatting.GRAY));
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.station_navigator"));
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (IBlockExtension.isBlock(state, BlockList.STATION_NAVIGATOR_MIDDLE.get())) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 9, 9, facing);
        } else {
            return IBlock.getVoxelShapeByDirection(getXStart() - 0.5, 0, 7, 16, 9, 9, facing);
        }
    }

    @Override
    protected BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.STATION_NAVIGATOR_MIDDLE.get());
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(ARROW_LEFT);
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.station_navigator";
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (length != 0)
            return new BlockEntity(length, blockPos, blockState);
        else
            return null;
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderStationNavigator
     * @since 1.0.0-beta-4
     */
    public static class BlockEntity extends BlockEntityExtension
    {
        public final int length;
        protected LongAVLTreeSet selectedRoutes;
        protected static final String KEY_SELECTED_ROUTES = "selected_routes";

        public BlockEntity(int length, BlockPos pos, BlockState state) {
            super(getType(length), pos, state);
            this.length = length;
            this.selectedRoutes = new LongAVLTreeSet();
        }

        public static BlockEntityType<?> getType(int length) {
            switch (length) {
                case 3:
                    return BlockEntityTypes.STATION_NAVIGATOR_3.get();
                case 4:
                    return BlockEntityTypes.STATION_NAVIGATOR_4.get();
                case 5:
                    return BlockEntityTypes.STATION_NAVIGATOR_5.get();
                default:
                    throw new InvalidParameterException();
            }
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            this.selectedRoutes = new LongAVLTreeSet();
            Arrays.stream(compoundTag.getLongArray(KEY_SELECTED_ROUTES)).forEach(selectedRoutes::add);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putLongArray(KEY_SELECTED_ROUTES, new ArrayList<>(selectedRoutes));
        }

        public void setData(LongAVLTreeSet selectedIds) {
            this.selectedRoutes = new LongAVLTreeSet();
            this.selectedRoutes.addAll(selectedIds);
            markDirty2();
        }

        public LongAVLTreeSet getSelectedRoutes() {
            return selectedRoutes;
        }
    }
}
