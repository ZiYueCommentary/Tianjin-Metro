package ziyue.tjmetro.mod.client;

import org.mtr.mapping.holder.ItemConvertible;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Blocks;
import org.mtr.mod.CreativeModeTabs;
import org.mtr.mod.Items;
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

    /* MTR filters */
    Filter MTR_CORE_MISCELLANEOUS = FilterBuilder.registerFilter(CreativeModeTabs.CORE, TextHelper.translatable("filter.tjmetro.mtr_core_miscellaneous"), () -> new ItemStack(new ItemConvertible(Items.BRUSH.get().data)));
    Filter MTR_CORE_DASHBOARDS = FilterBuilder.registerFilter(CreativeModeTabs.CORE, TextHelper.translatable("filter.tjmetro.mtr_core_dashboards"), () -> new ItemStack(new ItemConvertible(Items.RAILWAY_DASHBOARD.get().data)));
    Filter MTR_CORE_NODES = FilterBuilder.registerFilter(CreativeModeTabs.CORE, TextHelper.translatable("filter.tjmetro.mtr_core_nodes"), () -> new ItemStack(new ItemConvertible(Blocks.RAIL_NODE.get().data)));
    Filter MTR_CORE_RAILS = FilterBuilder.registerFilter(CreativeModeTabs.CORE, TextHelper.translatable("filter.tjmetro.mtr_core_rails"), () -> new ItemStack(new ItemConvertible(Items.RAIL_CONNECTOR_PLATFORM.get().data)));
    Filter MTR_CORE_CREATORS = FilterBuilder.registerFilter(CreativeModeTabs.CORE, TextHelper.translatable("filter.tjmetro.mtr_core_creators"), () -> new ItemStack(new ItemConvertible(Items.TUNNEL_CREATOR_4_3.get().data)));
    Filter MTR_CORE_SIGNALS = FilterBuilder.registerFilter(CreativeModeTabs.CORE, TextHelper.translatable("filter.tjmetro.mtr_core_signals"), () -> new ItemStack(new ItemConvertible(Items.SIGNAL_CONNECTOR_WHITE.get().data)));
    Filter MTR_CORE_UNCATEGORIZED = FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTabs.CORE);

    Filter MTR_RAILWAY_FACILITIES_GATES = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_gates"), () -> new ItemStack(new ItemConvertible(Blocks.APG_DOOR.get().data)));
    Filter MTR_RAILWAY_FACILITIES_PIDS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_pids"), () -> new ItemStack(new ItemConvertible(Blocks.PIDS_1.get().data)));
    Filter MTR_RAILWAY_FACILITIES_CEILINGS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_ceilings"), () -> new ItemStack(new ItemConvertible(Blocks.CEILING.get().data)));
    Filter MTR_RAILWAY_FACILITIES_MISCELLANEOUS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_miscellaneous"), () -> new ItemStack(new ItemConvertible(Blocks.CLOCK.get().data)));
    Filter MTR_RAILWAY_FACILITIES_FENCES = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_fences"), () -> new ItemStack(new ItemConvertible(Blocks.GLASS_FENCE_CIO.get().data)));
    Filter MTR_RAILWAY_FACILITIES_RAILWAY_SIGNS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_railway_signs"), () -> new ItemStack(new ItemConvertible(Blocks.RAILWAY_SIGN_2_EVEN.get().data)));
    Filter MTR_RAILWAY_FACILITIES_ROUTE_SIGNS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_route_signs"), () -> new ItemStack(new ItemConvertible(Blocks.ROUTE_SIGN_STANDING_LIGHT.get().data)));
    Filter MTR_RAILWAY_FACILITIES_SIGNAL_LIGHTS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_signal_lights"), () -> new ItemStack(new ItemConvertible(Blocks.SIGNAL_LIGHT_2_ASPECT_1.get().data)));
    Filter MTR_RAILWAY_FACILITIES_STATION_NAME_SIGNS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_station_name_signs"), () -> new ItemStack(new ItemConvertible(Blocks.STATION_NAME_WALL_WHITE.get().data)));
    Filter MTR_RAILWAY_FACILITIES_TICKETS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, TextHelper.translatable("filter.tjmetro.mtr_railway_facilities_tickets"), () -> new ItemStack(new ItemConvertible(Blocks.TICKET_MACHINE.get().data)));
    Filter MTR_RAILWAY_FACILITIES_UNCATEGORIZED = FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTabs.RAILWAY_FACILITIES);

    Filter MTR_ESCALATORS_LIFTS_ESCALATORS = FilterBuilder.registerFilter(CreativeModeTabs.ESCALATORS_LIFTS, TextHelper.translatable("filter.tjmetro.mtr_escalators_lifts_escalators"), () -> new ItemStack(new ItemConvertible(Items.ESCALATOR.get().data)));
    Filter MTR_ESCALATORS_LIFTS_LIFTS = FilterBuilder.registerFilter(CreativeModeTabs.ESCALATORS_LIFTS, TextHelper.translatable("filter.tjmetro.mtr_escalators_lifts_lifts"), () -> new ItemStack(new ItemConvertible(Items.LIFT_REFRESHER.get().data)));
    Filter MTR_ESCALATORS_LIFTS_UNCATEGORIZED = FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTabs.ESCALATORS_LIFTS);

    Filter MTR_STATION_BUILDING_BLOCKS_MISCELLANEOUS = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, TextHelper.translatable("filter.tjmetro.mtr_station_building_blocks_miscellaneous"), () -> new ItemStack(new ItemConvertible(Blocks.LOGO.get().data)));
    Filter MTR_STATION_BUILDING_BLOCKS_MARBLES = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, TextHelper.translatable("filter.tjmetro.mtr_station_building_blocks_marbles"), () -> new ItemStack(new ItemConvertible(Blocks.MARBLE_BLUE.get().data)));
    Filter MTR_STATION_BUILDING_BLOCKS_PLATFORMS = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, TextHelper.translatable("filter.tjmetro.mtr_station_building_blocks_platforms"), () -> new ItemStack(new ItemConvertible(Blocks.PLATFORM.get().data)));
    Filter MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_BLOCKS = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, TextHelper.translatable("filter.tjmetro.mtr_station_building_blocks_station_color_blocks"), () -> new ItemStack(new ItemConvertible(Blocks.STATION_COLOR_QUARTZ_BLOCK.get().data)));
    Filter MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_SLABS = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, TextHelper.translatable("filter.tjmetro.mtr_station_building_blocks_station_color_slabs"), () -> new ItemStack(new ItemConvertible(Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get().data)));
    Filter MTR_STATION_BUILDING_BLOCKS_UNCATEGORIZED = FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS);
}
