package ziyue.tjmetro.blocks.base;

import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public abstract class CustomContentBlockBase extends Block implements EntityBlockMapper
{
    public CustomContentBlockBase(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, blockGetter, tooltip, tooltipFlag);
        tooltip.add(Text.translatable("tooltip.tjmetro.station_name").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    /**
     * @author ZiYueCommentary
     * @since 1.0b
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
