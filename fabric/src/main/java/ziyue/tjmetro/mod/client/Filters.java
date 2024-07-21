package ziyue.tjmetro.mod.client;

import org.mtr.mapping.holder.ItemConvertible;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.mapper.TextHelper;
import ziyue.filters.Filter;
import ziyue.tjmetro.mapping.FilterBuilder;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.TianjinMetro;

public interface Filters
{
    Filter MISCELLANEOUS = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, TextHelper.translatable("filter.tjmetro.miscellaneous"), () -> new ItemStack(new ItemConvertible(ItemList.WRENCH.get().data)));
    Filter BUILDING = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, TextHelper.translatable("filter.tjmetro.building"), () -> new ItemStack(new ItemConvertible(BlockList.ROLLING.get().data)));
    Filter SIGNS = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, TextHelper.translatable("filter.tjmetro.signs"), () -> new ItemStack(new ItemConvertible(BlockList.STATION_NAME_SIGN_1.get().data)));
    Filter GATES = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, TextHelper.translatable("filter.tjmetro.gates"), () -> new ItemStack(new ItemConvertible(ItemList.PSD_DOOR_TIANJIN.get().data)));
    Filter DECORATION = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, TextHelper.translatable("filter.tjmetro.decoration"), () -> new ItemStack(new ItemConvertible(BlockList.LOGO.get().data)));
    Filter CEILINGS = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, TextHelper.translatable("filter.tjmetro.ceilings"), () -> new ItemStack(new ItemConvertible(BlockList.STATION_COLOR_CEILING.get().data)));
    Filter RAILWAY_SIGNS = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB, TextHelper.translatable("filter.tjmetro.railway_signs"), () -> new ItemStack(new ItemConvertible(BlockList.RAILWAY_SIGN_TIANJIN_3_EVEN.get().data)));
}
