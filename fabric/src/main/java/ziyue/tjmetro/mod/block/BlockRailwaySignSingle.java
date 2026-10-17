package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.data.IGuiExtension;
import ziyue.tjmetro.mod.packet.PacketOpenBlockRailwaySignSingleScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

/**
 * @author ZiYueCommentary
 * @since 1.1.3
 */

public class BlockRailwaySignSingle extends BlockExtension implements IBlock, DirectionHelper, BlockWithEntity
{
    public BlockRailwaySignSingle() {
        this(Blocks.createDefaultBlockSettings(true, blockState -> 15));
    }

    public BlockRailwaySignSingle(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final Direction hitSide = hit.getSide();
        if (hitSide == facing.rotateYCounterclockwise() || hitSide == facing.rotateYClockwise())
            return ActionResult.PASS;
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> Registry.sendPacketToClient(ServerPlayerEntity.cast(player),
                new PacketOpenBlockRailwaySignSingleScreen(pos, hitSide == facing ? 1 : 0)));
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    @Override
    public @Nonnull VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return VoxelShapes.union(
                IBlock.getVoxelShapeByDirection(3.5, 3, 6, 12.5, 12, 10, facing),
                IBlock.getVoxelShapeByDirection(7.5, 12, 7.5, 8.5, 16, 8.5, facing)
        );
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.railway_sign_single"));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
    }

    public static class BlockEntity extends BlockEntityExtension
    {
        protected String[] signIds = new String[2];
        protected static final String KEY_SIGN_ID = "sign_id";

        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.RAILWAY_SIGN_SINGLE.get(), pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            for (int i = 0; i < signIds.length; i++) {
                final String signId = compoundTag.getString(KEY_SIGN_ID + i);
                signIds[i] = signId.isEmpty() ? null : signId.toLowerCase(Locale.ENGLISH);
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            for (int i = 0; i < signIds.length; i++) {
                compoundTag.putString(KEY_SIGN_ID + i, signIds[i] == null ? "" : signIds[i]);
            }
        }

        public void setData(int index, String signId) {
            this.signIds[index] = signId;
            markDirty2();
        }

        public String getSignId(int index) {
            return signIds[index];
        }
    }
}
