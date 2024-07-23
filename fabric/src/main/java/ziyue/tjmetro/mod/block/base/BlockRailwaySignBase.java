package ziyue.tjmetro.mod.block.base;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.block.IBlockExtension;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @see org.mtr.mod.block.BlockRailwaySign
 * @author ZiYueCommentary
 * @since beta-1
 */

public abstract class BlockRailwaySignBase extends BlockExtension implements IBlock, DirectionHelper, BlockWithEntity
{
    public final int length;
    public final boolean isOdd;

    public BlockRailwaySignBase(BlockSettings settings, int length, boolean isOdd) {
        super(settings);
        this.length = length;
        this.isOdd = isOdd;
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> {
            final Direction facing = IBlock.getStatePropertySafe(state, FACING);
            final Direction hitSide = hit.getSide();
            if (hitSide == facing || hitSide == facing.getOpposite()) {
                final BlockPos checkPos = findEndWithDirection(world, pos, hitSide.getOpposite(), false);
                if (checkPos != null) {
                    Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(checkPos));
                }
            }
        });
    }

    @Nonnull
    @Override
    public abstract BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos);

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction facing = ctx.getPlayerFacing();
        return IBlock.isReplaceable(ctx, facing.rotateYClockwise(), getMiddleLength() + 2) ? getDefaultState2().with(new Property<>(FACING.data), facing.data) : null;
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);

        final BlockPos checkPos = findEndWithDirection(world, pos, facing, true);
        if (checkPos != null) {
            IBlock.onBreakCreative(world, player, checkPos);
        }

        super.onBreak2(world, pos, state, player);
    }

    @Override
    public abstract void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack);

    @Nonnull
    @Override
    public abstract VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context);

    @Nonnull
    @Override
    public abstract String getTranslationKey2();

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.mtr.railway_sign_length", length).formatted(TextFormatting.GRAY));
        tooltip.add(TextHelper.translatable(isOdd ? "tooltip.mtr.railway_sign_odd" : "tooltip.mtr.railway_sign_even").formatted(TextFormatting.GRAY));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
    }

    public int getXStart() {
        return switch (length % 4) {
            default -> isOdd ? 8 : 16;
            case 1 -> isOdd ? 4 : 12;
            case 2 -> isOdd ? 16 : 8;
            case 3 -> isOdd ? 12 : 4;
        };
    }

    protected int getMiddleLength() {
        return (length - (4 - getXStart() / 4)) / 2;
    }

    protected abstract BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite);

    public abstract static class BlockEntityBase extends BlockEntityExtension
    {
        protected final LongAVLTreeSet selectedIds;
        protected final String[] signIds;
        protected static final String KEY_SELECTED_IDS = "selected_ids";
        protected static final String KEY_SIGN_LENGTH = "sign_length";

        public BlockEntityBase(BlockEntityType<?> type, int length, BlockPos pos, BlockState state) {
            super(type, pos, state);
            signIds = new String[length];
            selectedIds = new LongAVLTreeSet();
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            selectedIds.clear();
            Arrays.stream(compoundTag.getLongArray(KEY_SELECTED_IDS)).forEach(selectedIds::add);
            for (int i = 0; i < signIds.length; i++) {
                final String signId = compoundTag.getString(KEY_SIGN_LENGTH + i);
                signIds[i] = signId.isEmpty() ? null : signId.toLowerCase(Locale.ENGLISH);
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putLongArray(KEY_SELECTED_IDS, new ArrayList<>(selectedIds));
            for (int i = 0; i < signIds.length; i++) {
                compoundTag.putString(KEY_SIGN_LENGTH + i, signIds[i] == null ? "" : signIds[i]);
            }
        }

        public void setData(LongAVLTreeSet selectedIds, String[] signTypes) {
            this.selectedIds.clear();
            this.selectedIds.addAll(selectedIds);
            if (signIds.length == signTypes.length) {
                System.arraycopy(signTypes, 0, signIds, 0, signTypes.length);
            }
            markDirty2();
        }

        public LongAVLTreeSet getSelectedIds() {
            return selectedIds;
        }

        public String[] getSignIds() {
            return signIds;
        }
    }
}
