package ziyue.tjmetro.mod.block;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.block.base.BlockEntityRenderable;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

/**
 * Roadblock with signs.
 *
 * @author ZiYueCommentary
 * @see BlockRoadblock
 * @since beta-1
 */

public class BlockRoadblockSign extends BlockRoadblock implements BlockWithEntity
{
    @NotNull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos)));
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @since beta-1
     * @see ziyue.tjmetro.mod.screen.RoadblockContentScreen
     * @see ziyue.tjmetro.mod.render.RenderRoadblockSign
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
            if (blockEntity.data instanceof BlockEntity entity) {
                entity.content = this.content;
                entity.markDirty2();
            } else {
                TianjinMetro.LOGGER.error("RoadBlockSign.BlockEntity: Unable to set data for block entity at " + pos.toShortString());
            }
            markDirty2();
        }
    }
}
