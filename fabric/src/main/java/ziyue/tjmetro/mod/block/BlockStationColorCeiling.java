package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockCeiling;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Ceiling with station color.
 *
 * @author ZiYueCommentary
 * @see BlockCeiling
 * @since 1.0.0-beta-1
 */

public class BlockStationColorCeiling extends BlockCeiling
{
    public BlockStationColorCeiling(int light) {
        this(Blocks.createDefaultBlockSettings(true, state -> light));
    }

    public BlockStationColorCeiling(BlockSettings blockSettings) {
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
