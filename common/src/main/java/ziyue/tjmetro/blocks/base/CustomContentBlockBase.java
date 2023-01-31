package ziyue.tjmetro.blocks.base;

import mtr.block.IBlock;
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
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.List;

/**
 * @author ZiYueCommentary
 * @since beta-1
 */

public abstract class CustomContentBlockBase extends Block implements EntityBlockMapper
{
    public CustomContentBlockBase(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, blockGetter, tooltip, tooltipFlag);
        tooltip.add(Text.translatable("tooltip.tjmetro.station_name").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof BlockCustomColorBase.CustomColorBlockEntity entity1) {
                entity1.syncData();
                PacketGuiServer.openCustomContentScreenS2C((ServerPlayer) player, pos);
            }
        });
    }

    /**
     * @author ZiYueCommentary
     * @since beta-1
     */
    public abstract static class CustomContentBlockEntity extends BlockEntityClientSerializableMapper
    {
        public final float yOffset;
        public final float zOffset;

        public final String CONTENT_ID = "content";
        public String content = "";

        public CustomContentBlockEntity(BlockEntityType<?> entity, BlockPos pos, BlockState state, float yOffset, float zOffset) {
            super(entity, pos, state);
            this.yOffset = yOffset;
            this.zOffset = zOffset;
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
            setChanged();
            syncData();
        }

        public boolean shouldRender() {
            return true;
        }
    }
}
