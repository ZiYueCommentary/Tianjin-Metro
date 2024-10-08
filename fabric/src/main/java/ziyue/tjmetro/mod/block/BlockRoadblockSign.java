package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.block.base.BlockEntityRenderable;
import ziyue.tjmetro.mod.data.IGuiExtension;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Roadblock with signs.
 *
 * @author ZiYueCommentary
 * @see BlockRoadblock
 * @see BlockEntity
 * @since 1.0.0-beta-1
 */

public class BlockRoadblockSign extends BlockRoadblock implements BlockWithEntity
{
    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos)));
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.roadblock_sign"));
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.screen.RoadblockContentScreen
     * @see ziyue.tjmetro.mod.render.RenderRoadblockSign
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityRenderable
    {
        public final String CONTENT_ID = "content";
        public String content = "";

        public BlockEntity(BlockPos blockPos, BlockState state) {
            super(BlockEntityTypes.ROADBLOCK_SIGN.get(), blockPos, state, 0, 0.535F);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            content = compoundTag.getString(CONTENT_ID);
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putString(CONTENT_ID, content);
            super.writeCompoundTag(compoundTag);
        }

        public void setData(String content) {
            this.content = content;
            BlockPos pos = this.getPos2().offset(IBlock.getStatePropertySafe(this.getCachedState2(), FACING).rotateYClockwise());
            org.mtr.mapping.holder.BlockEntity blockEntity = this.getWorld2().getBlockEntity(pos);
            if (blockEntity.data instanceof BlockEntity) {
                final BlockEntity entity = (BlockEntity) blockEntity.data;
                entity.content = this.content;
                entity.markDirty2();
            } else {
                TianjinMetro.LOGGER.error("RoadBlockSign.BlockEntity: Unable to set data for block entity at " + pos.toShortString());
            }
            markDirty2();
        }
    }
}
