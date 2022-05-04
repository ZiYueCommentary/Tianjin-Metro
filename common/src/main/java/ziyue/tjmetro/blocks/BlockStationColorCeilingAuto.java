package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.block.BlockCeilingAuto;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public class BlockStationColorCeilingAuto extends BlockCeilingAuto
{
    public BlockStationColorCeilingAuto(Properties settings) {
        super(settings);
    }

    @Override
    public String getDescriptionId() {
        return super.getDescriptionId().replace("block.tjmetro.", "block.mtr.");
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(new TranslatableComponent("tooltip.mtr.station_color").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }
}
