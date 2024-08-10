package ziyue.tjmetro.mod;

import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.Blocks;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.BlockItemExtension;
import org.mtr.mapping.mapper.SlabBlockExtension;
import org.mtr.mapping.registry.BlockRegistryObject;
import org.mtr.mod.block.BlockCeiling;
import org.mtr.mod.block.BlockPlatform;
import org.mtr.mod.item.ItemBlockEnchanted;
import ziyue.tjmetro.mod.block.*;
import ziyue.tjmetro.mod.block.base.StairBlock;

/**
 * @since 1.0.0-beta-1
 */

public interface BlockList
{
    BlockRegistryObject LOGO = Registry.registerBlockWithBlockItem("logo", () -> new Block(new BlockLogo()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject PLAYER_DETECTOR = Registry.registerBlockWithBlockItem("player_detector", () -> new Block(new BlockPlayerDetector()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject ROLLING = Registry.registerBlockWithBlockItem("rolling", () -> new Block(new BlockRolling()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject CEILING_NOT_LIT = Registry.registerBlockWithBlockItem("ceiling_not_lit", () -> new Block(new BlockCeiling(BlockHelper.createBlockSettings(false))), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_COLOR_CEILING = Registry.registerBlockWithBlockItem("station_color_ceiling", () -> new Block(new BlockStationColorCeilingAuto()), ItemBlockEnchanted::new, CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_COLOR_CEILING_LIGHT = Registry.registerBlockWithBlockItem("station_color_ceiling_light", () -> new Block(new BlockStationColorCeiling()), ItemBlockEnchanted::new, CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_COLOR_CEILING_NO_LIGHT = Registry.registerBlockWithBlockItem("station_color_ceiling_no_light", () -> new Block(new BlockStationColorCeiling()), ItemBlockEnchanted::new, CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_COLOR_CEILING_NOT_LIT = Registry.registerBlockWithBlockItem("station_color_ceiling_not_lit", () -> new Block(new BlockStationColorCeiling(BlockHelper.createBlockSettings(false))), ItemBlockEnchanted::new, CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_NAME_SIGN_1 = Registry.registerBlockWithBlockItem("station_name_sign_1", () -> new Block(new BlockStationNameSign1()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_NAME_SIGN_2 = Registry.registerBlockWithBlockItem("station_name_sign_2", () -> new Block(new BlockStationNameSign2()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject APG_CORNER = Registry.registerBlockWithBlockItem("apg_corner", () -> new Block(new BlockAPGCorner()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject PLATFORM_TJ_1 = Registry.registerBlockWithBlockItem("platform_tj_1", () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject PLATFORM_TJ_1_INDENTED = Registry.registerBlockWithBlockItem("platform_tj_1_indented", () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject PLATFORM_TJ_2 = Registry.registerBlockWithBlockItem("platform_tj_2", () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject PLATFORM_TJ_2_INDENTED = Registry.registerBlockWithBlockItem("platform_tj_2_indented", () -> new Block(new BlockPlatform(BlockHelper.createBlockSettings(false), true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject MARBLE_GRAY = Registry.registerBlockWithBlockItem("marble_gray", () -> new Block(BlockHelper.createBlockSettings(false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject MARBLE_GRAY_SLAB = Registry.registerBlockWithBlockItem("marble_gray_slab", () -> new Block(new SlabBlockExtension(BlockHelper.createBlockSettings(false))), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject MARBLE_GRAY_STAIRS = Registry.registerBlockWithBlockItem("marble_gray_stairs", () -> new Block(new StairBlock(Blocks.getBricksMapped())), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject MARBLE_YELLOW = Registry.registerBlockWithBlockItem("marble_yellow", () -> new Block(BlockHelper.createBlockSettings(false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject MARBLE_YELLOW_SLAB = Registry.registerBlockWithBlockItem("marble_yellow_slab", () -> new Block(new SlabBlockExtension(BlockHelper.createBlockSettings(false))), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject MARBLE_YELLOW_STAIRS = Registry.registerBlockWithBlockItem("marble_yellow_stairs", () -> new Block(new StairBlock(Blocks.getBricksMapped())), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject TIME_DISPLAY = Registry.registerBlockWithBlockItem("time_display", () -> new Block(new BlockTimeDisplay()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject EMERGENCY_EXIT_SIGN = Registry.registerBlockWithBlockItem("emergency_exit_sign", () -> new Block(new BlockEmergencyExitSign()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject SERVICE_CORRIDOR_SIGN = Registry.registerBlockWithBlockItem("service_corridor_sign", () -> new Block(new BlockServiceCorridorSign()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject ROADBLOCK = Registry.registerBlockWithBlockItem("roadblock", () -> new Block(new BlockRoadblock()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject ROADBLOCK_SIGN = Registry.registerBlockWithBlockItem("roadblock_sign", () -> new Block(new BlockRoadblockSign()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject BENCH = Registry.registerBlockWithBlockItem("bench", () -> new Block(new BlockBench()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject CUSTOM_COLOR_CONCRETE = Registry.registerBlockWithBlockItem("custom_color_concrete", () -> new Block(new BlockCustomColorConcrete()), ItemBlockEnchanted::new, CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject CUSTOM_COLOR_CONCRETE_SLAB = Registry.registerBlockWithBlockItem("custom_color_concrete_slab", () -> new Block(new BlockCustomColorConcreteSlab()), ItemBlockEnchanted::new, CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject CUSTOM_COLOR_CONCRETE_STAIRS = Registry.registerBlockWithBlockItem("custom_color_concrete_stairs", () -> new Block(new BlockCustomColorConcreteStairs()), ItemBlockEnchanted::new, CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_4 = Registry.registerBlockWithBlockItem("railway_sign_wall_4", () -> new Block(new BlockRailwaySignWall(4)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_6 = Registry.registerBlockWithBlockItem("railway_sign_wall_6", () -> new Block(new BlockRailwaySignWall(6)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_8 = Registry.registerBlockWithBlockItem("railway_sign_wall_8", () -> new Block(new BlockRailwaySignWall(8)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_10 = Registry.registerBlockWithBlockItem("railway_sign_wall_10", () -> new Block(new BlockRailwaySignWall(10)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_MIDDLE = Registry.registerBlock("railway_sign_wall_middle", () -> new Block(new BlockRailwaySignWall(0)));
    BlockRegistryObject RAILWAY_SIGN_WALL_DOUBLE_4 = Registry.registerBlockWithBlockItem("railway_sign_wall_double_4", () -> new Block(new BlockRailwaySignWallDouble(4)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_DOUBLE_6 = Registry.registerBlockWithBlockItem("railway_sign_wall_double_6", () -> new Block(new BlockRailwaySignWallDouble(6)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_DOUBLE_8 = Registry.registerBlockWithBlockItem("railway_sign_wall_double_8", () -> new Block(new BlockRailwaySignWallDouble(8)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_DOUBLE_10 = Registry.registerBlockWithBlockItem("railway_sign_wall_double_10", () -> new Block(new BlockRailwaySignWallDouble(10)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_DOUBLE_MIDDLE = Registry.registerBlock("railway_sign_wall_double_middle", () -> new Block(new BlockRailwaySignWallDouble(0)));
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_2 = Registry.registerBlockWithBlockItem("railway_sign_wall_big_2", () -> new Block(new BlockRailwaySignWallBig(2)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_3 = Registry.registerBlockWithBlockItem("railway_sign_wall_big_3", () -> new Block(new BlockRailwaySignWallBig(3)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_4 = Registry.registerBlockWithBlockItem("railway_sign_wall_big_4", () -> new Block(new BlockRailwaySignWallBig(4)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_5 = Registry.registerBlockWithBlockItem("railway_sign_wall_big_5", () -> new Block(new BlockRailwaySignWallBig(5)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_6 = Registry.registerBlockWithBlockItem("railway_sign_wall_big_6", () -> new Block(new BlockRailwaySignWallBig(6)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_7 = Registry.registerBlockWithBlockItem("railway_sign_wall_big_7", () -> new Block(new BlockRailwaySignWallBig(7)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_8 = Registry.registerBlockWithBlockItem("railway_sign_wall_big_8", () -> new Block(new BlockRailwaySignWallBig(8)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_9 = Registry.registerBlockWithBlockItem("railway_sign_wall_big_9", () -> new Block(new BlockRailwaySignWallBig(9)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_10 = Registry.registerBlockWithBlockItem("railway_sign_wall_big_10", () -> new Block(new BlockRailwaySignWallBig(10)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_WALL_BIG_MIDDLE = Registry.registerBlock("railway_sign_wall_big_middle", () -> new Block(new BlockRailwaySignWallBig(0)));
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_3_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_3_odd", () -> new Block(new BlockRailwaySignTianjin(3, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_4_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_4_odd", () -> new Block(new BlockRailwaySignTianjin(4, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_5_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_5_odd", () -> new Block(new BlockRailwaySignTianjin(5, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_6_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_6_odd", () -> new Block(new BlockRailwaySignTianjin(6, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_7_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_7_odd", () -> new Block(new BlockRailwaySignTianjin(7, true)), CreativeModeTabs.TIANJIN_METRO);
//    BlockRegistryObject RAILWAY_SIGN_TIANJIN_8_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_8_odd", () -> new Block(new BlockRailwaySignTianjin(8, true)), CreativeModeTabs.TIANJIN_METRO);
//    BlockRegistryObject RAILWAY_SIGN_TIANJIN_9_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_9_odd", () -> new Block(new BlockRailwaySignTianjin(9, true)), CreativeModeTabs.TIANJIN_METRO);
//    BlockRegistryObject RAILWAY_SIGN_TIANJIN_10_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_10_odd", () -> new Block(new BlockRailwaySignTianjin(10, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_2_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_2_even", () -> new Block(new BlockRailwaySignTianjin(2, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_3_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_3_even", () -> new Block(new BlockRailwaySignTianjin(3, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_4_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_4_even", () -> new Block(new BlockRailwaySignTianjin(4, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_5_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_5_even", () -> new Block(new BlockRailwaySignTianjin(5, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_6_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_6_even", () -> new Block(new BlockRailwaySignTianjin(6, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_7_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_7_even", () -> new Block(new BlockRailwaySignTianjin(7, false)), CreativeModeTabs.TIANJIN_METRO);
//    BlockRegistryObject RAILWAY_SIGN_TIANJIN_8_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_8_even", () -> new Block(new BlockRailwaySignTianjin(8, false)), CreativeModeTabs.TIANJIN_METRO);
//    BlockRegistryObject RAILWAY_SIGN_TIANJIN_9_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_9_even", () -> new Block(new BlockRailwaySignTianjin(9, false)), CreativeModeTabs.TIANJIN_METRO);
//    BlockRegistryObject RAILWAY_SIGN_TIANJIN_10_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_10_even", () -> new Block(new BlockRailwaySignTianjin(10, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_MIDDLE = Registry.registerBlock("railway_sign_tianjin_middle", () -> new Block(new BlockRailwaySignTianjin(0, false)));
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_POLE = Registry.registerBlockWithBlockItem("railway_sign_tianjin_pole", () -> new Block(new BlockRailwaySignTianjinPole()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_NAME_ENTRANCE_TIANJIN = Registry.registerBlockWithBlockItem("station_name_entrance_tianjin", () -> new Block(new BlockStationNameEntranceTianjin(false, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_NAME_ENTRANCE_TIANJIN_PINYIN = Registry.registerBlockWithBlockItem("station_name_entrance_tianjin_pinyin", () -> new Block(new BlockStationNameEntranceTianjin(true, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_NAME_ENTRANCE_TIANJIN_BMT = Registry.registerBlockWithBlockItem("station_name_entrance_tianjin_bmt", () -> new Block(new BlockStationNameEntranceTianjin(false, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_NAME_ENTRANCE_TIANJIN_BMT_PINYIN = Registry.registerBlockWithBlockItem("station_name_entrance_tianjin_bmt_pinyin", () -> new Block(new BlockStationNameEntranceTianjin(true, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject PSD_DOOR_TIANJIN_BLOCK = Registry.registerBlock("psd_door_tianjin", () -> new Block(new BlockPSDDoorTianjin()));
    BlockRegistryObject PSD_GLASS_TIANJIN_BLOCK = Registry.registerBlock("psd_glass_tianjin", () -> new Block(new BlockPSDGlassTianjin()));
    BlockRegistryObject PSD_GLASS_END_TIANJIN_BLOCK = Registry.registerBlock("psd_glass_end_tianjin", () -> new Block(new BlockPSDGlassEndTianjin()));
    BlockRegistryObject PSD_TOP_TIANJIN = Registry.registerBlock("psd_top_tianjin", () -> new Block(new BlockPSDTopTianjin()));
    BlockRegistryObject METAL_DETECTION_DOOR = Registry.registerBlockWithBlockItem("metal_detection_door", () -> new Block(new BlockMetalDetectionDoor()), CreativeModeTabs.TIANJIN_METRO);
//    BlockRegistryObject HIGH_SPEED_REPEATER = Registry.registerBlockWithBlockItem("high_speed_repeater", () -> new Block(new BlockHighSpeedRepeater()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_NAME_WALL_LEGACY = Registry.registerBlockWithBlockItem("station_name_wall_legacy", () -> new Block(new BlockStationNameWallLegacy()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_NAME_PLATE = Registry.registerBlockWithBlockItem("station_name_plate", () -> new Block(new BlockStationNamePlate()), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject STATION_NAME_PLATE_MIDDLE = Registry.registerBlock("station_name_plate_middle", () -> new Block(new BlockStationNamePlate()));
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_2_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_2_odd", () -> new Block(new BlockRailwaySignTianjinBMT(2, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_3_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_3_odd", () -> new Block(new BlockRailwaySignTianjinBMT(3, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_4_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_4_odd", () -> new Block(new BlockRailwaySignTianjinBMT(4, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_5_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_5_odd", () -> new Block(new BlockRailwaySignTianjinBMT(5, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_6_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_6_odd", () -> new Block(new BlockRailwaySignTianjinBMT(6, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_7_ODD = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_7_odd", () -> new Block(new BlockRailwaySignTianjinBMT(7, true)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_2_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_2_even", () -> new Block(new BlockRailwaySignTianjinBMT(2, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_3_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_3_even", () -> new Block(new BlockRailwaySignTianjinBMT(3, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_4_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_4_even", () -> new Block(new BlockRailwaySignTianjinBMT(4, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_5_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_5_even", () -> new Block(new BlockRailwaySignTianjinBMT(5, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_6_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_6_even", () -> new Block(new BlockRailwaySignTianjinBMT(6, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_7_EVEN = Registry.registerBlockWithBlockItem("railway_sign_tianjin_bmt_7_even", () -> new Block(new BlockRailwaySignTianjinBMT(7, false)), CreativeModeTabs.TIANJIN_METRO);
    BlockRegistryObject RAILWAY_SIGN_TIANJIN_BMT_MIDDLE = Registry.registerBlock("railway_sign_tianjin_bmt_middle", () -> new Block(new BlockRailwaySignTianjinBMT(0, false)));
    BlockRegistryObject APG_DOOR_TIANJIN_BLOCK = Registry.registerBlock("apg_door_tianjin", () -> new Block(new BlockAPGDoorTianjin()));
    BlockRegistryObject APG_GLASS_TIANJIN_BLOCK = Registry.registerBlock("apg_glass_tianjin", () -> new Block(new BlockAPGGlassTianjin()));
    BlockRegistryObject APG_GLASS_END_TIANJIN_BLOCK = Registry.registerBlock("apg_glass_end_tianjin", () -> new Block(new BlockAPGGlassEndTianjin()));
    BlockRegistryObject APG_DOOR_TIANJIN_BMT_BLOCK = Registry.registerBlock("apg_door_tianjin_bmt", () -> new Block(new BlockAPGDoorTianjinBMT()));
    BlockRegistryObject APG_GLASS_TIANJIN_BMT_BLOCK = Registry.registerBlock("apg_glass_tianjin_bmt", () -> new Block(new BlockAPGGlassTianjinBMT()));
    BlockRegistryObject APG_GLASS_END_TIANJIN_BMT_BLOCK = Registry.registerBlock("apg_glass_end_tianjin_bmt", () -> new Block(new BlockAPGGlassEndTianjinBMT()));
    BlockRegistryObject METAL_POLE_BMT = Registry.registerBlockWithBlockItem("metal_pole_bmt", () -> new Block(new BlockMetalPoleBMT()), BlockItemExtension::new, CreativeModeTabs.TIANJIN_METRO);

    static void registerBlocks() {
        // Calling this class to initialize constants
        TianjinMetro.LOGGER.info("Registering blocks");
    }
}
