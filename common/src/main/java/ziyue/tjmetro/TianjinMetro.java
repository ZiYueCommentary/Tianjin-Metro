package ziyue.tjmetro;

import mtr.CreativeModeTabs;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ziyue.filters.Filter;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.function.BiConsumer;

import static ziyue.tjmetro.client.Filters.*;
import static ziyue.tjmetro.packet.IPacket.*;

/**
 * Entry of <b>Tianjin Metro Mod</b>.
 *
 * @since beta-1
 */

public class TianjinMetro
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);

    public static final CreativeModeTabs.Wrapper CREATIVE_MODE_TAB = new CreativeModeTabs.Wrapper(new ResourceLocation(Reference.MOD_ID, "tjmetro_tab"), () -> new ItemStack(BlockList.LOGO.get()));
    public static final GameRules.Key<GameRules.BooleanValue> PREVENT_BLOCK_FALLING = Registry.registerBooleanGameRule("preventBlockFalling", false);

    public static void init(
            BiConsumer<String, RegistryObject<Block>> registerBlock,
            RegisterBlockItem registerBlockItem,
            RegisterItem registerItem,
            RegisterBlockItem registerEnchantedBlockItem,
            BiConsumer<String, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType
    ) {
        /* MISCELLANEOUS */
        registerItem.accept("wrench", ItemList.WRENCH, MISCELLANEOUS);
        registerBlockItem.accept("high_speed_repeater", BlockList.HIGH_SPEED_REPEATER, MISCELLANEOUS);
        registerBlockItem.accept("player_detector", BlockList.PLAYER_DETECTOR, MISCELLANEOUS);
        /* BUILDING */
        registerBlockItem.accept("rolling", BlockList.ROLLING, BUILDING);
        registerBlockItem.accept("platform_tj_1", BlockList.PLATFORM_TJ_1, BUILDING);
        registerBlockItem.accept("platform_tj_1_indented", BlockList.PLATFORM_TJ_1_INDENTED, BUILDING);
        registerBlockItem.accept("platform_tj_2", BlockList.PLATFORM_TJ_2, BUILDING);
        registerBlockItem.accept("platform_tj_2_indented", BlockList.PLATFORM_TJ_2_INDENTED, BUILDING);
        registerBlockItem.accept("marble_gray", BlockList.MARBLE_GRAY, BUILDING);
        registerBlockItem.accept("marble_gray_slab", BlockList.MARBLE_GRAY_SLAB, BUILDING);
        registerBlockItem.accept("marble_gray_stairs", BlockList.MARBLE_GRAY_STAIRS, BUILDING);
        registerBlockItem.accept("marble_yellow", BlockList.MARBLE_YELLOW, BUILDING);
        registerBlockItem.accept("marble_yellow_slab", BlockList.MARBLE_YELLOW_SLAB, BUILDING);
        registerBlockItem.accept("marble_yellow_stairs", BlockList.MARBLE_YELLOW_STAIRS, BUILDING);
        registerBlockItem.accept("metal_detection_door", BlockList.METAL_DETECTION_DOOR, BUILDING);
        registerBlockItem.accept("roadblock", BlockList.ROADBLOCK, BUILDING);
        registerBlockItem.accept("roadblock_sign", BlockList.ROADBLOCK_SIGN, BUILDING);
        registerEnchantedBlockItem.accept("custom_color_concrete", BlockList.CUSTOM_COLOR_CONCRETE, BUILDING);
        registerEnchantedBlockItem.accept("custom_color_concrete_stairs", BlockList.CUSTOM_COLOR_CONCRETE_STAIRS, BUILDING);
        registerEnchantedBlockItem.accept("custom_color_concrete_slab", BlockList.CUSTOM_COLOR_CONCRETE_SLAB, BUILDING);
        /* SIGNS */
        registerBlockItem.accept("station_name_sign_2", BlockList.STATION_NAME_SIGN_2, SIGNS);
        registerBlockItem.accept("station_name_wall_legacy", BlockList.STATION_NAME_WALL_LEGACY, SIGNS);
        registerBlockItem.accept("station_name_entrance_tianjin", BlockList.STATION_NAME_ENTRANCE_TIANJIN, SIGNS);
        registerBlockItem.accept("station_name_entrance_tianjin_pinyin", BlockList.STATION_NAME_ENTRANCE_TIANJIN_PINYIN, SIGNS);
        registerBlockItem.accept("station_name_entrance_tianjin_bmt", BlockList.STATION_NAME_ENTRANCE_TIANJIN_BMT, SIGNS);
        registerBlockItem.accept("station_name_entrance_tianjin_bmt_pinyin", BlockList.STATION_NAME_ENTRANCE_TIANJIN_BMT_PINYIN, SIGNS);
        registerEnchantedBlockItem.accept("station_name_sign_1", BlockList.STATION_NAME_SIGN_1, SIGNS);
//        registerBlockItem.accept("station_name_pole", BlockList.STATION_NAME_POLE, SIGNS);
        /* PLATFORM GATES */
        registerItem.accept("psd_door_tianjin", ItemList.PSD_DOOR_TIANJIN, GATES);
        registerItem.accept("psd_glass_tianjin", ItemList.PSD_GLASS_TIANJIN, GATES);
        registerItem.accept("psd_glass_end_tianjin", ItemList.PSD_GLASS_END_TIANJIN, GATES);
        /* DECORATION */
        registerBlockItem.accept("logo", BlockList.LOGO, DECORATION);
        registerBlockItem.accept("apg_corner", BlockList.APG_CORNER, DECORATION);
        registerBlockItem.accept("bench", BlockList.BENCH, DECORATION);
        registerBlockItem.accept("time_display", BlockList.TIME_DISPLAY, DECORATION);
        registerBlockItem.accept("emergency_exit_sign", BlockList.EMERGENCY_EXIT_SIGN, DECORATION);
        registerBlockItem.accept("service_corridor_sign", BlockList.SERVICE_CORRIDOR_SIGN, DECORATION);
//        registerEnchantedBlockItem.accept("decoration_light", BlockList.DECORATION_LIGHT, DECORATION);
        /* CEILINGS */
        registerBlockItem.accept("ceiling_not_lit", BlockList.CEILING_NOT_LIT, CEILINGS);
        registerEnchantedBlockItem.accept("ceiling", BlockList.STATION_COLOR_CEILING, CEILINGS);
        registerEnchantedBlockItem.accept("ceiling_light", BlockList.STATION_COLOR_CEILING_LIGHT, CEILINGS);
        registerEnchantedBlockItem.accept("ceiling_no_light", BlockList.STATION_COLOR_CEILING_NO_LIGHT, CEILINGS);
        registerEnchantedBlockItem.accept("station_color_ceiling_not_lit", BlockList.STATION_COLOR_CEILING_NOT_LIT, CEILINGS);
        /* RAILWAYS */
        registerBlockItem.accept("railway_sign_wall_4", BlockList.RAILWAY_SIGN_WALL_4, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_6", BlockList.RAILWAY_SIGN_WALL_6, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_8", BlockList.RAILWAY_SIGN_WALL_8, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_10", BlockList.RAILWAY_SIGN_WALL_10, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_double_4", BlockList.RAILWAY_SIGN_WALL_DOUBLE_4, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_double_6", BlockList.RAILWAY_SIGN_WALL_DOUBLE_6, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_double_8", BlockList.RAILWAY_SIGN_WALL_DOUBLE_8, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_double_10", BlockList.RAILWAY_SIGN_WALL_DOUBLE_10, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_big_2", BlockList.RAILWAY_SIGN_WALL_BIG_2, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_big_3", BlockList.RAILWAY_SIGN_WALL_BIG_3, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_big_4", BlockList.RAILWAY_SIGN_WALL_BIG_4, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_big_5", BlockList.RAILWAY_SIGN_WALL_BIG_5, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_big_6", BlockList.RAILWAY_SIGN_WALL_BIG_6, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_big_7", BlockList.RAILWAY_SIGN_WALL_BIG_7, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_big_8", BlockList.RAILWAY_SIGN_WALL_BIG_8, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_big_9", BlockList.RAILWAY_SIGN_WALL_BIG_9, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_wall_big_10", BlockList.RAILWAY_SIGN_WALL_BIG_10, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_3_odd", BlockList.RAILWAY_SIGN_TIANJIN_3_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_4_odd", BlockList.RAILWAY_SIGN_TIANJIN_4_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_5_odd", BlockList.RAILWAY_SIGN_TIANJIN_5_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_6_odd", BlockList.RAILWAY_SIGN_TIANJIN_6_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_7_odd", BlockList.RAILWAY_SIGN_TIANJIN_7_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_2_even", BlockList.RAILWAY_SIGN_TIANJIN_2_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_3_even", BlockList.RAILWAY_SIGN_TIANJIN_3_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_4_even", BlockList.RAILWAY_SIGN_TIANJIN_4_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_5_even", BlockList.RAILWAY_SIGN_TIANJIN_5_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_6_even", BlockList.RAILWAY_SIGN_TIANJIN_6_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_7_even", BlockList.RAILWAY_SIGN_TIANJIN_7_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_3_odd", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_3_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_4_odd", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_4_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_5_odd", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_5_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_6_odd", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_6_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_7_odd", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_7_ODD, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_2_even", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_2_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_3_even", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_3_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_4_even", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_4_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_5_even", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_5_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_6_even", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_6_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_double_7_even", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_7_EVEN, RAILWAY_SIGNS);
        registerBlockItem.accept("railway_sign_tianjin_pole", BlockList.RAILWAY_SIGN_TIANJIN_POLE, RAILWAY_SIGNS);

        registerBlock.accept("railway_sign_wall_middle", BlockList.RAILWAY_SIGN_WALL_MIDDLE);
        registerBlock.accept("railway_sign_wall_big_middle", BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE);
        registerBlock.accept("railway_sign_wall_double_middle", BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE);
        registerBlock.accept("railway_sign_tianjin_middle", BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE);
        registerBlock.accept("railway_sign_tianjin_double_middle", BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_MIDDLE);
        registerBlock.accept("psd_door_tianjin", BlockList.PSD_DOOR_TIANJIN);
        registerBlock.accept("psd_glass_tianjin", BlockList.PSD_GLASS_TIANJIN);
        registerBlock.accept("psd_glass_end_tianjin", BlockList.PSD_GLASS_END_TIANJIN);
        registerBlock.accept("psd_top_tianjin", BlockList.PSD_TOP_TIANJIN);

        registerBlockEntityType.accept("station_name_sign_2", BlockEntityTypes.STATION_NAME_SIGN_ENTITY_2);
        registerBlockEntityType.accept("station_name_sign_1", BlockEntityTypes.STATION_NAME_SIGN_ENTITY_1);
        registerBlockEntityType.accept("roadblock_sign", BlockEntityTypes.ROADBLOCK_SIGN_ENTITY);
        registerBlockEntityType.accept("player_detector", BlockEntityTypes.PLAYER_DETECTOR_TILE_ENTITY);
        registerBlockEntityType.accept("custom_color_concrete", BlockEntityTypes.STATION_COLOR_CONCRETE_TILE_ENTITY);
        registerBlockEntityType.accept("custom_color_concrete_stairs", BlockEntityTypes.STATION_COLOR_CONCRETE_STAIRS_TILE_ENTITY);
        registerBlockEntityType.accept("custom_color_concrete_slab", BlockEntityTypes.STATION_COLOR_CONCRETE_SLAB_TILE_ENTITY);
        registerBlockEntityType.accept("station_name_wall_legacy", BlockEntityTypes.STATION_NAME_WALL_LEGACY_TILE_ENTITY);
        registerBlockEntityType.accept("decoration_light", BlockEntityTypes.DECORATION_LIGHT_TILE_ENTITY);
        registerBlockEntityType.accept("time_display", BlockEntityTypes.TIME_DISPLAY_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_4", BlockEntityTypes.RAILWAY_SIGN_WALL_4_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_6", BlockEntityTypes.RAILWAY_SIGN_WALL_6_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_8", BlockEntityTypes.RAILWAY_SIGN_WALL_8_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_10", BlockEntityTypes.RAILWAY_SIGN_WALL_10_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_double_4", BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_4_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_double_6", BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_6_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_double_8", BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_8_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_double_10", BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_10_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_big_2", BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_2_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_big_3", BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_3_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_big_4", BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_4_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_big_5", BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_5_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_big_6", BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_6_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_big_7", BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_7_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_big_8", BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_8_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_big_9", BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_9_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_wall_big_10", BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_10_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_3_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_4_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_5_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_6_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_7_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_2_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_2_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_3_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_4_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_5_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_6_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_7_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_3_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_3_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_4_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_4_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_5_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_5_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_6_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_6_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_7_odd", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_7_ODD_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_2_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_2_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_3_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_3_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_4_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_4_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_5_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_5_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_6_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_6_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("railway_sign_tianjin_double_7_even", BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_7_EVEN_TILE_ENTITY);
        registerBlockEntityType.accept("metal_detection_door", BlockEntityTypes.METAL_DETECTION_DOOR_TILE_ENTITY);
        registerBlockEntityType.accept("bench", BlockEntityTypes.BENCH_TILE_ENTITY);
        registerBlockEntityType.accept("service_corridor_sign", BlockEntityTypes.SERVICE_CORRIDOR_SIGN_TILE_ENTITY);
        registerBlockEntityType.accept("psd_door_tianjin", BlockEntityTypes.PSD_DOOR_TIANJIN_TILE_ENTITY);
        registerBlockEntityType.accept("psd_top_tianjin", BlockEntityTypes.PSD_TOP_TIANJIN_TILE_ENTITY);
        registerBlockEntityType.accept("station_name_entrance_tianjin", BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_TILE_ENTITY);
        registerBlockEntityType.accept("station_name_entrance_tianjin_pinyin", BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_PINYIN_TILE_ENTITY);
        registerBlockEntityType.accept("station_name_entrance_tianjin_bmt", BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_BMT_TILE_ENTITY);
        registerBlockEntityType.accept("station_name_entrance_tianjin_bmt_pinyin", BlockEntityTypes.STATION_NAME_ENTRANCE_TIANJIN_BMT_PINYIN_TILE_ENTITY);
//        registerBlockEntityType.accept("station_name_pole", BlockEntityTypes.STATION_NAME_POLE_TILE_ENTITY);

        mtr.Registry.registerNetworkReceiver(PACKET_UPDATE_CUSTOM_CONTENT, PacketGuiServer::receiveCustomContentC2S);
        mtr.Registry.registerNetworkReceiver(PACKET_UPDATE_CUSTOM_COLOR, PacketGuiServer::receiveCustomColorC2S);
        mtr.Registry.registerNetworkReceiver(PACKET_SIGN_TYPES, PacketGuiServer::receiveSignIdsC2S);
        mtr.Registry.registerNetworkReceiver(PACKET_SIGN_TYPES_DOUBLE, PacketGuiServer::receiveSignIdsDoubleC2S);
    }

    @FunctionalInterface
    public interface RegisterBlockItem
    {
        void accept(String string, RegistryObject<Block> block, Filter filter);
    }

    @FunctionalInterface
    public interface RegisterItem
    {
        void accept(String string, RegistryObject<Item> item, Filter filter);
    }
}
