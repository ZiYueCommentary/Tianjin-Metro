package ziyue.tjmetro.mod.block.base;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.block.IBlockExtension;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockCustomColorBase extends BlockExtension implements BlockWithEntity
{
    public BlockCustomColorBase(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return super.getTranslationKey2().replace("block.tjmetro.custom_color_", "block.minecraft.");
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.tjmetro.custom_color").formatted(TextFormatting.GRAY));
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos)));
    }

    public static class BlockEntityBase extends BlockEntityExtension
    {
        public final String COLOR_ID = "color";
        public int color = -1;

        public BlockEntityBase(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
            super(type, blockPos, blockState);
        }

        public int getDefaultColor(BlockPos pos) {
            return InitClient.getStationColor(pos);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            color = compoundTag.getInt(COLOR_ID);
            super.readCompoundTag(compoundTag);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putInt(COLOR_ID, color);
            super.writeCompoundTag(compoundTag);
        }

        public void setData(int color) {
            this.color = color;
            markDirty2();
        }
    }
}
