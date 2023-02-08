package ziyue.tjmetro;

import mtr.mappings.Text;
import net.minecraft.world.item.ItemStack;
import ziyue.tjmetro.filters.Filter;

/**
 * @see Filter
 * @since beta-1
 */

public interface Filters
{
    Filter BUILDING = Filter.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, Text.translatable("filter.tjmetro.building"), () -> new ItemStack(BlockList.ROLLING.get()));
    Filter SIGNS = Filter.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, Text.translatable("filter.tjmetro.signs"), () -> new ItemStack(BlockList.STATION_NAME_SIGN_1.get()));
    Filter DECORATION = Filter.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, Text.translatable("filter.tjmetro.decoration"), () -> new ItemStack(BlockList.LOGO.get()));
    Filter CEILINGS = Filter.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, Text.translatable("filter.tjmetro.ceilings"), () -> new ItemStack(BlockList.STATION_COLOR_CEILING.get()));
    Filter RAILWAYS = Filter.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, Text.translatable("filter.tjmetro.railway_signs"), () -> new ItemStack(BlockList.RAILWAY_SIGN_WALL_4.get()));
}
