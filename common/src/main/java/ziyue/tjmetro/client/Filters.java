package ziyue.tjmetro.client;

import mtr.CreativeModeTabs;
import mtr.Items;
import mtr.mappings.Text;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.ItemList;
import ziyue.tjmetro.TianjinMetro;

/**
 * @since beta-1
 */

public interface Filters
{
    Object MISCELLANEOUS = new Object();//.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.miscellaneous"), () -> new ItemStack(ItemList.WRENCH.get()));
    Object BUILDING = new Object();//.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.building"), () -> new ItemStack(BlockList.ROLLING.get()));
    Object SIGNS = new Object();//.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.signs"), () -> new ItemStack(BlockList.STATION_NAME_SIGN_1.get()));
    Object GATES = new Object();//.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.gates"), () -> new ItemStack(BlockList.PSD_DOOR_TIANJIN.get()));
    Object DECORATION = new Object();//.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.decoration"), () -> new ItemStack(BlockList.LOGO.get()));
    Object CEILINGS = new Object();//.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.ceilings"), () -> new ItemStack(BlockList.STATION_COLOR_CEILING.get()));
    Object RAILWAY_SIGNS = new Object();//.registerFilter(TianjinMetro.CREATIVE_MODE_TAB.get(), Text.translatable("filter.tjmetro.railway_signs"), () -> new ItemStack(BlockList.RAILWAY_SIGN_WALL_4.get()));

    /* MTR filters */
    Object MTR_CORE_MISCELLANEOUS = new Object();//.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_miscellaneous"), () -> new ItemStack(Items.BRUSH.get()));
    Object MTR_CORE_DASHBOARDS = new Object();//.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_dashboards"), () -> new ItemStack(Items.RAILWAY_DASHBOARD.get()));
    Object MTR_CORE_NODES = new Object();//.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_nodes"), () -> new ItemStack(mtr.Blocks.RAIL_NODE.get()));
    Object MTR_CORE_RAILS = new Object();//.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_rails"), () -> new ItemStack(Items.RAIL_CONNECTOR_PLATFORM.get()));
    Object MTR_CORE_CREATORS = new Object();//.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_creators"), () -> new ItemStack(Items.TUNNEL_CREATOR_4_3.get()));
    Object MTR_CORE_SIGNALS = new Object();//.registerFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_core_signals"), () -> new ItemStack(Items.SIGNAL_CONNECTOR_WHITE.get()));
    Object MTR_CORE_UNCATEGORIZED = new Object();//.registerUncategorizedItemsFilter(CreativeModeTabs.CORE.get(), Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));

    Object MTR_RAILWAY_FACILITIES_GATES = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_gates"), () -> new ItemStack(mtr.Blocks.APG_DOOR.get()));
    Object MTR_RAILWAY_FACILITIES_PIDS = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_pids"), () -> new ItemStack(mtr.Blocks.PIDS_1.get()));
    Object MTR_RAILWAY_FACILITIES_CEILINGS = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_ceilings"), () -> new ItemStack(mtr.Blocks.CEILING.get()));
    Object MTR_RAILWAY_FACILITIES_MISCELLANEOUS = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_miscellaneous"), () -> new ItemStack(mtr.Blocks.CLOCK.get()));
    Object MTR_RAILWAY_FACILITIES_FENCES = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_fences"), () -> new ItemStack(mtr.Blocks.GLASS_FENCE_CIO.get()));
    Object MTR_RAILWAY_FACILITIES_RAILWAY_SIGNS = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_railway_signs"), () -> new ItemStack(mtr.Blocks.RAILWAY_SIGN_2_EVEN.get()));
    Object MTR_RAILWAY_FACILITIES_ROUTE_SIGNS = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_route_signs"), () -> new ItemStack(mtr.Blocks.ROUTE_SIGN_STANDING_LIGHT.get()));
    Object MTR_RAILWAY_FACILITIES_SIGNAL_LIGHTS = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_signal_lights"), () -> new ItemStack(mtr.Blocks.SIGNAL_LIGHT_2_ASPECT_1.get()));
    Object MTR_RAILWAY_FACILITIES_STATION_NAME_SIGNS = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_station_name_signs"), () -> new ItemStack(mtr.Blocks.STATION_NAME_WALL_WHITE.get()));
    Object MTR_RAILWAY_FACILITIES_TICKETS = new Object();//.registerFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_railway_facilities_tickets"), () -> new ItemStack(mtr.Blocks.TICKET_MACHINE.get()));
    Object MTR_RAILWAY_FACILITIES_UNCATEGORIZED = new Object();//.registerUncategorizedItemsFilter(CreativeModeTabs.RAILWAY_FACILITIES.get(), Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));

    Object MTR_ESCALATORS_LIFTS_ESCALATORS = new Object();//.registerFilter(CreativeModeTabs.ESCALATORS_LIFTS.get(), Text.translatable("filter.tjmetro.mtr_escalators_lifts_escalators"), () -> new ItemStack(Items.ESCALATOR.get()));
    Object MTR_ESCALATORS_LIFTS_LIFTS = new Object();//.registerFilter(CreativeModeTabs.ESCALATORS_LIFTS.get(), Text.translatable("filter.tjmetro.mtr_escalators_lifts_lifts"), () -> new ItemStack(Items.LIFT_REFRESHER.get()));
    Object MTR_ESCALATORS_LIFTS_UNCATEGORIZED = new Object();//.registerUncategorizedItemsFilter(CreativeModeTabs.ESCALATORS_LIFTS.get(), Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));

    Object MTR_STATION_BUILDING_BLOCKS_MISCELLANEOUS = new Object();//.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_miscellaneous"), () -> new ItemStack(mtr.Blocks.LOGO.get()));
    Object MTR_STATION_BUILDING_BLOCKS_MARBLES = new Object();//.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_marbles"), () -> new ItemStack(mtr.Blocks.MARBLE_BLUE.get()));
    Object MTR_STATION_BUILDING_BLOCKS_PLATFORMS = new Object();//.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_platforms"), () -> new ItemStack(mtr.Blocks.PLATFORM.get()));
    Object MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_BLOCKS = new Object();//.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_station_color_blocks"), () -> new ItemStack(mtr.Blocks.STATION_COLOR_QUARTZ_BLOCK.get()));
    Object MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_SLABS = new Object();//.registerFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_station_building_blocks_station_color_slabs"), () -> new ItemStack(mtr.Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get()));
    Object MTR_STATION_BUILDING_BLOCKS_UNCATEGORIZED = new Object();//.registerUncategorizedItemsFilter(CreativeModeTabs.STATION_BUILDING_BLOCKS.get(), Text.translatable("filter.tjmetro.mtr_uncategorized"), () -> new ItemStack(Blocks.BARRIER));
}
