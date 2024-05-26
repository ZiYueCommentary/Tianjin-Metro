package ziyue.tjmetro.client;

import mtr.CreativeModeTabs;
import mtr.Items;
import mtr.mappings.Text;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import ziyue.filters.Filter;
import ziyue.filters.FilterBuilder;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.ItemList;
import ziyue.tjmetro.TianjinMetro;

/**
 * @see Filter
 * @since beta-1
 */

public interface Filters
{
    Filter MISCELLANEOUS = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.miscellaneous"), () -> new ItemStack(ItemList.WRENCH.get()));
    Filter BUILDING = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.building"), () -> new ItemStack(BlockList.ROLLING.get()));
    Filter SIGNS = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.signs"), () -> new ItemStack(BlockList.STATION_NAME_SIGN_1.get()));
    Filter GATES = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.gates"), () -> new ItemStack(BlockList.PSD_DOOR_TIANJIN.get()));
    Filter DECORATION = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.decoration"), () -> new ItemStack(BlockList.LOGO.get()));
    Filter CEILINGS = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.ceilings"), () -> new ItemStack(BlockList.STATION_COLOR_CEILING.get()));
    Filter RAILWAY_SIGNS = FilterBuilder.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.railway_signs"), () -> new ItemStack(BlockList.RAILWAY_SIGN_WALL_4.get()));

    /* MTR filters */
    Filter MTR_CORE_MISCELLANEOUS = FilterBuilder.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_miscellaneous"), () -> new ItemStack(Items.BRUSH.get()));
    Filter MTR_CORE_DASHBOARDS = FilterBuilder.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_dashboards"), () -> new ItemStack(Items.RAILWAY_DASHBOARD.get()));
    Filter MTR_CORE_NODES = FilterBuilder.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_nodes"), () -> new ItemStack(mtr.Blocks.RAIL_NODE.get()));
    Filter MTR_CORE_RAILS = FilterBuilder.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_rails"), () -> new ItemStack(Items.RAIL_CONNECTOR_PLATFORM.get()));
    Filter MTR_CORE_CREATORS = FilterBuilder.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_creators"), () -> new ItemStack(Items.TUNNEL_CREATOR_4_3.get()));
    Filter MTR_CORE_SIGNALS = FilterBuilder.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_signals"), () -> new ItemStack(Items.SIGNAL_CONNECTOR_WHITE.get()));
    Filter MTR_CORE_UNCATEGORIZED = FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));

    Filter MTR_RAILWAY_FACILITIES_GATES = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_gates"), () -> new ItemStack(mtr.Blocks.APG_DOOR.get()));
    Filter MTR_RAILWAY_FACILITIES_PIDS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_pids"), () -> new ItemStack(mtr.Blocks.PIDS_1.get()));
    Filter MTR_RAILWAY_FACILITIES_CEILINGS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_ceilings"), () -> new ItemStack(mtr.Blocks.CEILING.get()));
    Filter MTR_RAILWAY_FACILITIES_MISCELLANEOUS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_miscellaneous"), () -> new ItemStack(mtr.Blocks.CLOCK.get()));
    Filter MTR_RAILWAY_FACILITIES_FENCES = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_fences"), () -> new ItemStack(mtr.Blocks.GLASS_FENCE_CIO.get()));
    Filter MTR_RAILWAY_FACILITIES_RAILWAY_SIGNS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_railway_signs"), () -> new ItemStack(mtr.Blocks.RAILWAY_SIGN_2_EVEN.get()));
    Filter MTR_RAILWAY_FACILITIES_ROUTE_SIGNS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_route_signs"), () -> new ItemStack(mtr.Blocks.ROUTE_SIGN_STANDING_LIGHT.get()));
    Filter MTR_RAILWAY_FACILITIES_SIGNAL_LIGHTS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_signal_lights"), () -> new ItemStack(mtr.Blocks.SIGNAL_LIGHT_2_ASPECT_1.get()));
    Filter MTR_RAILWAY_FACILITIES_STATION_NAME_SIGNS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_station_name_signs"), () -> new ItemStack(mtr.Blocks.STATION_NAME_WALL_WHITE.get()));
    Filter MTR_RAILWAY_FACILITIES_TICKETS = FilterBuilder.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_tickets"), () -> new ItemStack(mtr.Blocks.TICKET_MACHINE.get()));
    Filter MTR_RAILWAY_FACILITIES_UNCATEGORIZED = FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));

    Filter MTR_ESCALATORS_LIFTS_ESCALATORS = FilterBuilder.registerFilter(CreativeModeTabs.ESCALATORS_LIFTS.get(), Text.translatable("filter.tjmetro.mtr_escalators_lifts_escalators"), () -> new ItemStack(Items.ESCALATOR.get()));
    Filter MTR_ESCALATORS_LIFTS_LIFTS = FilterBuilder.registerFilter(CreativeModeTabs.ESCALATORS_LIFTS.get(), Text.translatable("filter.tjmetro.mtr_escalators_lifts_lifts"), () -> new ItemStack(Items.LIFT_REFRESHER.get()));
    Filter MTR_ESCALATORS_LIFTS_UNCATEGORIZED = FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTabs.ESCALATORS_LIFTS.get(), Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));

    Filter MTR_STATION_BUILDING_BLOCKS_MISCELLANEOUS = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_miscellaneous"), () -> new ItemStack(mtr.Blocks.LOGO.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_MARBLES = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_marbles"), () -> new ItemStack(mtr.Blocks.MARBLE_BLUE.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_PLATFORMS = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_platforms"), () -> new ItemStack(mtr.Blocks.PLATFORM.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_BLOCKS = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_station_color_blocks"), () -> new ItemStack(mtr.Blocks.STATION_COLOR_QUARTZ_BLOCK.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_SLABS = FilterBuilder.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_station_color_slabs"), () -> new ItemStack(mtr.Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get()));
    Filter MTR_STATION_BUILDING_BLOCKS_UNCATEGORIZED = FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));
}
