package ziyue.tjmetro.mod;

import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.RenderLayer;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Blocks;
import org.mtr.mod.Items;
import org.mtr.mod.render.RenderPSDAPGDoor;
import ziyue.tjmetro.mod.client.Filters;
import ziyue.tjmetro.mod.config.ConfigClient;
import ziyue.tjmetro.mapping.FilterBuilder;
import ziyue.tjmetro.mod.render.*;
import ziyue.tjmetro.mod.screen.ConfigClientScreen;

public final class TianjinMetroClient
{
    public static void init() {
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.LOGO);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.STATION_NAME_SIGN_1);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.STATION_NAME_SIGN_2);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.APG_CORNER);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.PLATFORM_TJ_1);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.PLATFORM_TJ_1_INDENTED);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.PLATFORM_TJ_2);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.PLATFORM_TJ_2_INDENTED);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.EMERGENCY_EXIT_SIGN);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.SERVICE_CORRIDOR_SIGN);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.PSD_DOOR_TIANJIN);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.PSD_GLASS_TIANJIN);
        RegistryClient.registerBlockRenderType(RenderLayer.getCutout(), BlockList.PSD_GLASS_END_TIANJIN);
        RegistryClient.registerBlockRenderType(RenderLayer.getTranslucent(), BlockList.ROLLING);

        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_SIGN_1, RenderStationNameSign::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_SIGN_2, RenderStationNameSign::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.TIME_DISPLAY, RenderTimeDisplay::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.SERVICE_CORRIDOR_SIGN, RenderServiceCorridorSign::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.ROADBLOCK_SIGN, RenderRoadblockSign::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_4, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_6, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_8, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_10, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_2, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_3, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_4, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_5, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_6, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_7, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_8, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_9, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_10, RenderRailwaySignWall::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_ODD, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_ODD, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_ODD, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_ODD, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_ODD, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_2_EVEN, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_EVEN, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_EVEN, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_EVEN, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_EVEN, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_EVEN, RenderRailwaySignTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_4, RenderRailwaySignWallDouble::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_6, RenderRailwaySignWallDouble::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_8, RenderRailwaySignWallDouble::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_10, RenderRailwaySignWallDouble::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN, RenderStationNameEntranceTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_PINYIN, RenderStationNameEntranceTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_BMT, RenderStationNameEntranceTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_BMT_PINYIN, RenderStationNameEntranceTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PSD_TOP_TIANJIN, RenderPSDTopTianjin::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.PSD_DOOR_TIANJIN, dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 0));
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_LEGACY, RenderStationNameWallLegacy::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.STATION_NAME_PLATE, RenderStationNamePlate::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_2_ODD, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_3_ODD, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_4_ODD, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_5_ODD, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_6_ODD, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_7_ODD, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_2_EVEN, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_3_EVEN, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_4_EVEN, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_5_EVEN, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_6_EVEN, RenderRailwaySignTianjinBMT::new);
        RegistryClient.registerBlockEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_7_EVEN, RenderRailwaySignTianjinBMT::new);


        RegistryClient.registerEntityRenderer(EntityTypes.SEAT, RenderSeat::new);

        RegistryClient.registerBlockStationColor(BlockList.STATION_COLOR_CEILING, BlockList.STATION_COLOR_CEILING_LIGHT, BlockList.STATION_COLOR_CEILING_NO_LIGHT, BlockList.STATION_COLOR_CEILING_NOT_LIT);
        RegistryClient.registerBlockStationColor(BlockList.STATION_NAME_SIGN_1);
        RegistryClient.registerBlockCustomColor(BlockList.CUSTOM_COLOR_CONCRETE, BlockList.CUSTOM_COLOR_CONCRETE_SLAB, BlockList.CUSTOM_COLOR_CONCRETE_STAIRS);

        RegistryClient.setupPackets("packet");

        RegistryClient.REGISTRY_CLIENT.init();

        FilterBuilder.setReservedButton(TianjinMetro.CREATIVE_MODE_TAB, TextHelper.translatable("button.tjmetro.tianjin_metro_options"), button ->
                MinecraftClient.getInstance().openScreen(new Screen(new ConfigClientScreen(MinecraftClient.getInstance().getCurrentScreenMapped()))));

        //registerMTRCoreFilters();
        //registerMTRRailwayFacilitiesFilters();
        //registerMTREscalatorsLiftsFilters();
        //registerMTRStationBuildingBlocksFilters();
        
        ConfigClient.refreshProperties();
    }

    private static void registerMTRCoreFilters() {
        Filters.MTR_CORE_DASHBOARDS.addItems(
                Items.RAILWAY_DASHBOARD.get().data,
                Items.CABLE_CAR_DASHBOARD.get().data,
                Items.BOAT_DASHBOARD.get().data,
                Items.AIRPLANE_DASHBOARD.get().data
        );
        Filters.MTR_CORE_RAILS.addItems(
                Items.RAIL_CONNECTOR_20.get().data,
                Items.RAIL_CONNECTOR_20_ONE_WAY.get().data,
                Items.RAIL_CONNECTOR_40.get().data,
                Items.RAIL_CONNECTOR_40_ONE_WAY.get().data,
                Items.RAIL_CONNECTOR_60.get().data,
                Items.RAIL_CONNECTOR_60_ONE_WAY.get().data,
                Items.RAIL_CONNECTOR_80.get().data,
                Items.RAIL_CONNECTOR_80_ONE_WAY.get().data,
                Items.RAIL_CONNECTOR_120.get().data,
                Items.RAIL_CONNECTOR_120_ONE_WAY.get().data,
                Items.RAIL_CONNECTOR_160.get().data,
                Items.RAIL_CONNECTOR_160_ONE_WAY.get().data,
                Items.RAIL_CONNECTOR_200.get().data,
                Items.RAIL_CONNECTOR_200_ONE_WAY.get().data,
                Items.RAIL_CONNECTOR_300.get().data,
                Items.RAIL_CONNECTOR_300_ONE_WAY.get().data,
                Items.RAIL_CONNECTOR_PLATFORM.get().data,
                Items.RAIL_CONNECTOR_SIDING.get().data,
                Items.RAIL_CONNECTOR_TURN_BACK.get().data,
                Items.RAIL_CONNECTOR_CABLE_CAR.get().data,
                Items.RAIL_CONNECTOR_RUNWAY.get().data,
                Items.RAIL_REMOVER.get().data
        );
        Filters.MTR_CORE_SIGNALS.addItems(
                Items.SIGNAL_CONNECTOR_WHITE.get().data,
                Items.SIGNAL_CONNECTOR_ORANGE.get().data,
                Items.SIGNAL_CONNECTOR_MAGENTA.get().data,
                Items.SIGNAL_CONNECTOR_LIGHT_BLUE.get().data,
                Items.SIGNAL_CONNECTOR_YELLOW.get().data,
                Items.SIGNAL_CONNECTOR_GREEN.get().data,
                Items.SIGNAL_CONNECTOR_LIME.get().data,
                Items.SIGNAL_CONNECTOR_PINK.get().data,
                Items.SIGNAL_CONNECTOR_GRAY.get().data,
                Items.SIGNAL_CONNECTOR_LIGHT_GRAY.get().data,
                Items.SIGNAL_CONNECTOR_CYAN.get().data,
                Items.SIGNAL_CONNECTOR_PURPLE.get().data,
                Items.SIGNAL_CONNECTOR_BLUE.get().data,
                Items.SIGNAL_CONNECTOR_BROWN.get().data,
                Items.SIGNAL_CONNECTOR_LIME.get().data,
                Items.SIGNAL_CONNECTOR_RED.get().data,
                Items.SIGNAL_CONNECTOR_BLACK.get().data,
                Items.SIGNAL_REMOVER_WHITE.get().data,
                Items.SIGNAL_REMOVER_ORANGE.get().data,
                Items.SIGNAL_REMOVER_MAGENTA.get().data,
                Items.SIGNAL_REMOVER_LIGHT_BLUE.get().data,
                Items.SIGNAL_REMOVER_YELLOW.get().data,
                Items.SIGNAL_REMOVER_GREEN.get().data,
                Items.SIGNAL_REMOVER_LIME.get().data,
                Items.SIGNAL_REMOVER_PINK.get().data,
                Items.SIGNAL_REMOVER_GRAY.get().data,
                Items.SIGNAL_REMOVER_LIGHT_GRAY.get().data,
                Items.SIGNAL_REMOVER_CYAN.get().data,
                Items.SIGNAL_REMOVER_PURPLE.get().data,
                Items.SIGNAL_REMOVER_BLUE.get().data,
                Items.SIGNAL_REMOVER_BROWN.get().data,
                Items.SIGNAL_REMOVER_LIME.get().data,
                Items.SIGNAL_REMOVER_RED.get().data,
                Items.SIGNAL_REMOVER_BLACK.get().data
        );
        Filters.MTR_CORE_CREATORS.addItems(
                Items.BRIDGE_CREATOR_3.get().data,
                Items.BRIDGE_CREATOR_5.get().data,
                Items.BRIDGE_CREATOR_7.get().data,
                Items.BRIDGE_CREATOR_9.get().data,
                Items.TUNNEL_CREATOR_4_3.get().data,
                Items.TUNNEL_CREATOR_4_5.get().data,
                Items.TUNNEL_CREATOR_4_7.get().data,
                Items.TUNNEL_CREATOR_4_9.get().data,
                Items.TUNNEL_CREATOR_5_3.get().data,
                Items.TUNNEL_CREATOR_5_5.get().data,
                Items.TUNNEL_CREATOR_5_7.get().data,
                Items.TUNNEL_CREATOR_5_9.get().data,
                Items.TUNNEL_CREATOR_6_3.get().data,
                Items.TUNNEL_CREATOR_6_5.get().data,
                Items.TUNNEL_CREATOR_6_7.get().data,
                Items.TUNNEL_CREATOR_6_9.get().data,
                Items.TUNNEL_WALL_CREATOR_4_3.get().data,
                Items.TUNNEL_WALL_CREATOR_4_5.get().data,
                Items.TUNNEL_WALL_CREATOR_4_7.get().data,
                Items.TUNNEL_WALL_CREATOR_4_9.get().data,
                Items.TUNNEL_WALL_CREATOR_5_3.get().data,
                Items.TUNNEL_WALL_CREATOR_5_5.get().data,
                Items.TUNNEL_WALL_CREATOR_5_7.get().data,
                Items.TUNNEL_WALL_CREATOR_5_9.get().data,
                Items.TUNNEL_WALL_CREATOR_6_3.get().data,
                Items.TUNNEL_WALL_CREATOR_6_5.get().data,
                Items.TUNNEL_WALL_CREATOR_6_7.get().data,
                Items.TUNNEL_WALL_CREATOR_6_9.get().data
        );
        Filters.MTR_CORE_NODES.addItems(
                Blocks.RAIL_NODE.get().data.asItem(),
                Blocks.BOAT_NODE.get().data.asItem(),
                Blocks.CABLE_CAR_NODE_LOWER.get().data.asItem(),
                Blocks.CABLE_CAR_NODE_STATION.get().data.asItem(),
                Blocks.CABLE_CAR_NODE_UPPER.get().data.asItem(),
                Blocks.AIRPLANE_NODE.get().data.asItem()
        );
        Filters.MTR_CORE_MISCELLANEOUS.addItems(
                Items.BRUSH.get().data,
                Items.DRIVER_KEY.get().data
        );
    }

    private static void registerMTRRailwayFacilitiesFilters() {
        Filters.MTR_RAILWAY_FACILITIES_GATES.addItems(
                Blocks.APG_DOOR.get().data.asItem(),
                Blocks.APG_GLASS.get().data.asItem(),
                Blocks.APG_GLASS_END.get().data.asItem(),
                Blocks.PSD_DOOR_1.get().data.asItem(),
                Blocks.PSD_DOOR_2.get().data.asItem(),
                Blocks.PSD_GLASS_1.get().data.asItem(),
                Blocks.PSD_GLASS_2.get().data.asItem(),
                Blocks.PSD_GLASS_END_1.get().data.asItem(),
                Blocks.PSD_GLASS_END_2.get().data.asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_PIDS.addItems(
                Blocks.ARRIVAL_PROJECTOR_1_SMALL.get().data.asItem(),
                Blocks.ARRIVAL_PROJECTOR_1_MEDIUM.get().data.asItem(),
                Blocks.ARRIVAL_PROJECTOR_1_LARGE.get().data.asItem(),
                Blocks.PIDS_1.get().data.asItem(),
                Blocks.PIDS_2.get().data.asItem(),
                Blocks.PIDS_3.get().data.asItem(),
                Blocks.PIDS_4.get().data.asItem(),
                Blocks.PIDS_POLE.get().data.asItem(),
                Blocks.PIDS_SINGLE_ARRIVAL_1.get().data.asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_CEILINGS.addItems(
                Blocks.CEILING.get().data.asItem(),
                Blocks.CEILING_LIGHT.get().data.asItem(),
                Blocks.CEILING_NO_LIGHT.get().data.asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_MISCELLANEOUS.addItems(
                Blocks.CLOCK.get().data.asItem(),
                Blocks.CLOCK_POLE.get().data.asItem(),
                Blocks.RUBBISH_BIN_1.get().data.asItem(),
                Blocks.STATION_COLOR_POLE.get().data.asItem(),
                Blocks.TACTILE_MAP.get().data.asItem(),
                Blocks.TRAIN_ANNOUNCER.get().data.asItem(),
                Blocks.TRAIN_CARGO_LOADER.get().data.asItem(),
                Blocks.TRAIN_CARGO_UNLOADER.get().data.asItem(),
                Blocks.TRAIN_REDSTONE_SENSOR.get().data.asItem(),
                Blocks.TRAIN_SCHEDULE_SENSOR.get().data.asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_FENCES.addItems(
                Blocks.GLASS_FENCE_CIO.get().data.asItem(),
                Blocks.GLASS_FENCE_CKT.get().data.asItem(),
                Blocks.GLASS_FENCE_HEO.get().data.asItem(),
                Blocks.GLASS_FENCE_MOS.get().data.asItem(),
                Blocks.GLASS_FENCE_PLAIN.get().data.asItem(),
                Blocks.GLASS_FENCE_SHM.get().data.asItem(),
                Blocks.GLASS_FENCE_STAINED.get().data.asItem(),
                Blocks.GLASS_FENCE_STW.get().data.asItem(),
                Blocks.GLASS_FENCE_TSH.get().data.asItem(),
                Blocks.GLASS_FENCE_WKS.get().data.asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_RAILWAY_SIGNS.addItems(
                Blocks.RAILWAY_SIGN_2_EVEN.get().data.asItem(),
                Blocks.RAILWAY_SIGN_2_ODD.get().data.asItem(),
                Blocks.RAILWAY_SIGN_3_EVEN.get().data.asItem(),
                Blocks.RAILWAY_SIGN_3_ODD.get().data.asItem(),
                Blocks.RAILWAY_SIGN_4_EVEN.get().data.asItem(),
                Blocks.RAILWAY_SIGN_4_ODD.get().data.asItem(),
                Blocks.RAILWAY_SIGN_5_EVEN.get().data.asItem(),
                Blocks.RAILWAY_SIGN_5_ODD.get().data.asItem(),
                Blocks.RAILWAY_SIGN_6_EVEN.get().data.asItem(),
                Blocks.RAILWAY_SIGN_6_ODD.get().data.asItem(),
                Blocks.RAILWAY_SIGN_7_EVEN.get().data.asItem(),
                Blocks.RAILWAY_SIGN_7_ODD.get().data.asItem(),
                Blocks.RAILWAY_SIGN_POLE.get().data.asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_ROUTE_SIGNS.addItems(
                Blocks.ROUTE_SIGN_STANDING_LIGHT.get().data.asItem(),
                Blocks.ROUTE_SIGN_STANDING_METAL.get().data.asItem(),
                Blocks.ROUTE_SIGN_WALL_LIGHT.get().data.asItem(),
                Blocks.ROUTE_SIGN_WALL_METAL.get().data.asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_SIGNAL_LIGHTS.addItems(
                Blocks.SIGNAL_LIGHT_2_ASPECT_1.get().data.asItem(),
                Blocks.SIGNAL_LIGHT_2_ASPECT_2.get().data.asItem(),
                Blocks.SIGNAL_LIGHT_2_ASPECT_3.get().data.asItem(),
                Blocks.SIGNAL_LIGHT_2_ASPECT_4.get().data.asItem(),
                Blocks.SIGNAL_LIGHT_3_ASPECT_1.get().data.asItem(),
                Blocks.SIGNAL_LIGHT_3_ASPECT_2.get().data.asItem(),
                Blocks.SIGNAL_LIGHT_4_ASPECT_1.get().data.asItem(),
                Blocks.SIGNAL_LIGHT_4_ASPECT_2.get().data.asItem(),
                Blocks.SIGNAL_SEMAPHORE_1.get().data.asItem(),
                Blocks.SIGNAL_SEMAPHORE_2.get().data.asItem(),
                Blocks.SIGNAL_POLE.get().data.asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_STATION_NAME_SIGNS.addItems(
                Blocks.STATION_NAME_ENTRANCE.get().data.asItem(),
                Blocks.STATION_NAME_WALL_WHITE.get().data.asItem(),
                Blocks.STATION_NAME_WALL_BLACK.get().data.asItem(),
                Blocks.STATION_NAME_WALL_GRAY.get().data.asItem(),
                Blocks.STATION_NAME_TALL_BLOCK.get().data.asItem(),
                Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.get().data.asItem(),
                Blocks.STATION_NAME_TALL_WALL.get().data.asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_TICKETS.addItems(
                Blocks.TICKET_BARRIER_ENTRANCE_1.get().data.asItem(),
                Blocks.TICKET_BARRIER_EXIT_1.get().data.asItem(),
                Blocks.TICKET_MACHINE.get().data.asItem(),
                Blocks.TICKET_PROCESSOR.get().data.asItem(),
                Blocks.TICKET_PROCESSOR_ENTRANCE.get().data.asItem(),
                Blocks.TICKET_PROCESSOR_EXIT.get().data.asItem(),
                Blocks.TICKET_PROCESSOR_ENQUIRY.get().data.asItem()
        );
    }

    private static void registerMTREscalatorsLiftsFilters() {
        Filters.MTR_ESCALATORS_LIFTS_ESCALATORS.addItems(
                Items.ESCALATOR.get().data
        );
        Filters.MTR_ESCALATORS_LIFTS_LIFTS.addItems(
                Items.LIFT_REFRESHER.get().data,
                Items.LIFT_BUTTONS_LINK_CONNECTOR.get().data,
                Items.LIFT_BUTTONS_LINK_REMOVER.get().data,
                Items.LIFT_DOOR_1.get().data,
                Items.LIFT_DOOR_ODD_1.get().data,
                Blocks.LIFT_BUTTONS_1.get().data.asItem(),
                Blocks.LIFT_TRACK_DIAGONAL_1.get().data.asItem(),
                Blocks.LIFT_TRACK_HORIZONTAL_1.get().data.asItem(),
                Blocks.LIFT_TRACK_VERTICAL_1.get().data.asItem(),
                Blocks.LIFT_TRACK_FLOOR_1.get().data.asItem(),
                Blocks.LIFT_PANEL_EVEN_1.get().data.asItem(),
                Blocks.LIFT_PANEL_EVEN_2.get().data.asItem(),
                Blocks.LIFT_PANEL_ODD_1.get().data.asItem(),
                Blocks.LIFT_PANEL_ODD_2.get().data.asItem()
        );
    }

    private static void registerMTRStationBuildingBlocksFilters() {
        Filters.MTR_STATION_BUILDING_BLOCKS_MISCELLANEOUS.addItems(
                Blocks.LOGO.get().data.asItem(),
                Blocks.METAL.get().data.asItem(),
                Blocks.METAL_SLAB.get().data.asItem()
        );
        Filters.MTR_STATION_BUILDING_BLOCKS_MARBLES.addItems(
                Blocks.MARBLE_BLUE.get().data.asItem(),
                Blocks.MARBLE_BLUE_SLAB.get().data.asItem(),
                Blocks.MARBLE_SANDY.get().data.asItem(),
                Blocks.MARBLE_SANDY_SLAB.get().data.asItem()
        );
        Filters.MTR_STATION_BUILDING_BLOCKS_PLATFORMS.addItems(
                Blocks.PLATFORM.get().data.asItem(),
                Blocks.PLATFORM_INDENTED.get().data.asItem(),
                Blocks.PLATFORM_NA_1.get().data.asItem(),
                Blocks.PLATFORM_NA_2.get().data.asItem(),
                Blocks.PLATFORM_NA_1_INDENTED.get().data.asItem(),
                Blocks.PLATFORM_NA_2_INDENTED.get().data.asItem(),
                Blocks.PLATFORM_UK_1.get().data.asItem(),
                Blocks.PLATFORM_UK_1_INDENTED.get().data.asItem()
        );
        Filters.MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_BLOCKS.addItems(
                Blocks.STATION_COLOR_ANDESITE.get().data.asItem(),
                Blocks.STATION_COLOR_BEDROCK.get().data.asItem(),
                Blocks.STATION_COLOR_BIRCH_WOOD.get().data.asItem(),
                Blocks.STATION_COLOR_BONE_BLOCK.get().data.asItem(),
                Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK.get().data.asItem(),
                Blocks.STATION_COLOR_CHISELED_STONE_BRICKS.get().data.asItem(),
                Blocks.STATION_COLOR_CLAY.get().data.asItem(),
                Blocks.STATION_COLOR_COAL_ORE.get().data.asItem(),
                Blocks.STATION_COLOR_COBBLESTONE.get().data.asItem(),
                Blocks.STATION_COLOR_CONCRETE.get().data.asItem(),
                Blocks.STATION_COLOR_CONCRETE_POWDER.get().data.asItem(),
                Blocks.STATION_COLOR_CRACKED_STONE_BRICKS.get().data.asItem(),
                Blocks.STATION_COLOR_DARK_PRISMARINE.get().data.asItem(),
                Blocks.STATION_COLOR_DIORITE.get().data.asItem(),
                Blocks.STATION_COLOR_GRAVEL.get().data.asItem(),
                Blocks.STATION_COLOR_IRON_BLOCK.get().data.asItem(),
                Blocks.STATION_COLOR_METAL.get().data.asItem(),
                Blocks.STATION_COLOR_PLANKS.get().data.asItem(),
                Blocks.STATION_COLOR_POLISHED_ANDESITE.get().data.asItem(),
                Blocks.STATION_COLOR_POLISHED_DIORITE.get().data.asItem(),
                Blocks.STATION_COLOR_PURPUR_BLOCK.get().data.asItem(),
                Blocks.STATION_COLOR_PURPUR_PILLAR.get().data.asItem(),
                Blocks.STATION_COLOR_QUARTZ_BLOCK.get().data.asItem(),
                Blocks.STATION_COLOR_QUARTZ_BRICKS.get().data.asItem(),
                Blocks.STATION_COLOR_QUARTZ_PILLAR.get().data.asItem(),
                Blocks.STATION_COLOR_SMOOTH_QUARTZ.get().data.asItem(),
                Blocks.STATION_COLOR_SMOOTH_STONE.get().data.asItem(),
                Blocks.STATION_COLOR_SNOW_BLOCK.get().data.asItem(),
                Blocks.STATION_COLOR_STAINED_GLASS.get().data.asItem(),
                Blocks.STATION_COLOR_STONE.get().data.asItem(),
                Blocks.STATION_COLOR_STONE_BRICKS.get().data.asItem(),
                Blocks.STATION_COLOR_WOOL.get().data.asItem()
        );
        Filters.MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_SLABS.addItems(
                Blocks.STATION_COLOR_ANDESITE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_BEDROCK_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_BIRCH_WOOD_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_BONE_BLOCK_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_CHISELED_STONE_BRICKS_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_CLAY_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_COAL_ORE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_COBBLESTONE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_CONCRETE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_CONCRETE_POWDER_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_CRACKED_STONE_BRICKS_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_DARK_PRISMARINE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_DIORITE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_GRAVEL_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_IRON_BLOCK_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_METAL_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_PLANKS_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_POLISHED_ANDESITE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_POLISHED_DIORITE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_PURPUR_BLOCK_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_PURPUR_PILLAR_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_QUARTZ_BRICKS_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_QUARTZ_PILLAR_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_SMOOTH_QUARTZ_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_SMOOTH_STONE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_SNOW_BLOCK_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_STAINED_GLASS_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_STONE_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_STONE_BRICKS_SLAB.get().data.asItem(),
                Blocks.STATION_COLOR_WOOL_SLAB.get().data.asItem()
        );
    }
}
