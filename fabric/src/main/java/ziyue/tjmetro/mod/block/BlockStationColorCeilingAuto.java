package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.block.BlockCeilingAuto;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Ceiling with station color.
 *
 * @author ZiYueCommentary
 * @see BlockCeilingAuto
 * @since 1.0.0-beta-1
 */

public class BlockStationColorCeilingAuto extends BlockCeilingAuto
{
    public BlockStationColorCeilingAuto() {
        this(BlockHelper.createBlockSettings(false, state -> 15));
    }

    public BlockStationColorCeilingAuto(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return super.getTranslationKey2().replace("block.tjmetro.station_color_", "block.mtr.");
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.mtr.station_color").formatted(TextFormatting.GRAY));
    }
}
