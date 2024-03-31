package ziyue.tjmetro.blocks.base;

import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.IBlockExtends;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.List;

/**
 * @author ZiYueCommentary
 * @since beta-1
 */

public abstract class BlockCustomColorBase extends Block implements EntityBlockMapper
{
    public BlockCustomColorBase(Properties properties) {
        super(properties);
    }

    @Override
    public String getDescriptionId() {
        return super.getDescriptionId().replace("block.tjmetro.custom_color_", "block.minecraft.");
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Text.translatable("tooltip.tjmetro.custom_color").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlockExtends.checkHoldingBrushOrWrench(world, player, () -> {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof CustomColorBlockEntity entity1) {
                entity1.syncData();
                PacketGuiServer.openCustomColorScreenS2C((ServerPlayer) player, pos);
            }
        });
    }

    /**
     * If color equals <code>-1</code>, then use station color.
     *
     * @author ZiYueCommentary
     * @since beta-1
     */
    public static class CustomColorBlockEntity extends BlockEntityClientSerializableMapper implements RenderAttachmentBlockEntity
    {
        final String COLOR_ID = "color";
        public int color = -1;

        public CustomColorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
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
            setChanged();
            syncData();
        }

        @Override
        public @Nullable Object getRenderAttachmentData() {
            return this;
        }
    }
}
