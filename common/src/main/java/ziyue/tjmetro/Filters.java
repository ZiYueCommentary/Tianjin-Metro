package ziyue.tjmetro;

import mtr.CreativeModeTabs;
import mtr.Items;
import mtr.mappings.Text;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
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

    /* MTR filters */
    Filter MTR_CORE_MISCELLANEOUS = Filter.registerFilter(CreativeModeTabs.CORE, Text.translatable("filter.tjmetro.mtr_core_miscellaneous"), () -> new ItemStack(Items.BRUSH.get()));
    Filter MTR_CORE_DASHBOARDS = Filter.registerFilter(CreativeModeTabs.CORE, Text.translatable("filter.tjmetro.mtr_core_dashboards"), () -> new ItemStack(Items.RAILWAY_DASHBOARD.get()));
    Filter MTR_CORE_NODES = Filter.registerFilter(CreativeModeTabs.CORE, Text.translatable("filter.tjmetro.mtr_core_nodes"), () -> new ItemStack(mtr.Blocks.RAIL_NODE.get()));
    Filter MTR_CORE_RAILS = Filter.registerFilter(CreativeModeTabs.CORE, Text.translatable("filter.tjmetro.mtr_core_rails"), () -> new ItemStack(Items.RAIL_CONNECTOR_PLATFORM.get()));
    Filter MTR_CORE_CREATORS = Filter.registerFilter(CreativeModeTabs.CORE, Text.translatable("filter.tjmetro.mtr_core_creators"), () -> new ItemStack(Items.TUNNEL_CREATOR_4_3.get()));
    Filter MTR_CORE_SIGNALS = Filter.registerFilter(CreativeModeTabs.CORE, Text.translatable("filter.tjmetro.mtr_core_signals"), () -> new ItemStack(Items.SIGNAL_CONNECTOR_WHITE.get()));
    Filter MTR_CORE_UNCATEGORIZED = Filter.registerUncategorizedItemsFilter(CreativeModeTabs.CORE, Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));

    Filter MTR_RAILWAY_FACILITIES_GATES = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_gates"), () -> new ItemStack(mtr.Blocks.APG_DOOR.get()));
    Filter MTR_RAILWAY_FACILITIES_PIDS = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_pids"), () -> new ItemStack(mtr.Blocks.PIDS_1.get()));
    Filter MTR_RAILWAY_FACILITIES_CEILINGS = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_ceilings"), () -> new ItemStack(mtr.Blocks.CEILING.get()));
    Filter MTR_RAILWAY_FACILITIES_MISCELLANEOUS = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_miscellaneous"), () -> new ItemStack(mtr.Blocks.CLOCK.get()));
    Filter MTR_RAILWAY_FACILITIES_FENCES = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_fences"), () -> new ItemStack(mtr.Blocks.GLASS_FENCE_CIO.get()));
    Filter MTR_RAILWAY_FACILITIES_RAILWAY_SIGNS = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_railway_signs"), () -> new ItemStack(mtr.Blocks.RAILWAY_SIGN_2_EVEN.get()));
    Filter MTR_RAILWAY_FACILITIES_ROUTE_SIGNS = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_route_signs"), () -> new ItemStack(mtr.Blocks.ROUTE_SIGN_STANDING_LIGHT.get()));
    Filter MTR_RAILWAY_FACILITIES_SIGNAL_LIGHTS = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_signal_lights"), () -> new ItemStack(mtr.Blocks.SIGNAL_LIGHT_2_ASPECT_1.get()));
    Filter MTR_RAILWAY_FACILITIES_STATION_NAME_SIGNS = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_station_name_signs"), () -> new ItemStack(mtr.Blocks.STATION_NAME_WALL_WHITE.get()));
    Filter MTR_RAILWAY_FACILITIES_TICKETS = Filter.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_railway_facilities_tickets"), () -> new ItemStack(mtr.Blocks.TICKET_MACHINE.get()));
    Filter MTR_RAILWAY_FACILITIES_UNCATEGORIZED = Filter.registerUncategorizedItemsFilter(CreativeModeTabs.RAILWAY_FACILITIES, Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));

    Filter MTR_ESCALATORS_LIFTS_ESCALATORS = Filter.registerFilter(CreativeModeTabs.ESCALATORS_LIFTS, Text.translatable("filter.tjmetro.mtr_escalators_lifts_escalators"), () -> new ItemStack(Items.ESCALATOR.get()));
    Filter MTR_ESCALATORS_LIFTS_LIFTS = Filter.registerFilter(CreativeModeTabs.ESCALATORS_LIFTS, Text.translatable("filter.tjmetro.mtr_escalators_lifts_lifts"), () -> new ItemStack(Items.LIFT_REFRESHER.get()));
    Filter MTR_ESCALATORS_LIFTS_UNCATEGORIZED = Filter.registerUncategorizedItemsFilter(CreativeModeTabs.ESCALATORS_LIFTS, Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));

    Filter MTR_STATION_BUILDING_BLOCKS_MISCELLANEOUS = Filter.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, Text.translatable("filter.tjmetro.mtr_station_building_blocks_miscellaneous"), () -> new ItemStack(mtr.Blocks.LOGO.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_MARBLES = Filter.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, Text.translatable("filter.tjmetro.mtr_station_building_blocks_marbles"), () -> new ItemStack(mtr.Blocks.MARBLE_BLUE.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_PLATFORMS = Filter.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, Text.translatable("filter.tjmetro.mtr_station_building_blocks_platforms"), () -> new ItemStack(mtr.Blocks.PLATFORM.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_BLOCKS = Filter.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, Text.translatable("filter.tjmetro.mtr_station_building_blocks_station_color_blocks"), () -> new ItemStack(mtr.Blocks.STATION_COLOR_QUARTZ_BLOCK.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_SLABS = Filter.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, Text.translatable("filter.tjmetro.mtr_station_building_blocks_station_color_slabs"), () -> new ItemStack(mtr.Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_UNCATEGORIZED = Filter.registerUncategorizedItemsFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS, Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));
}
