package ziyue.tjmetro.mod.client;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import ziyue.filters.Filter;
import ziyue.tjmetro.mapping.FilterBuilder;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.CreativeModeTabs;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.screen.ConfigClientScreen;

import static ziyue.tjmetro.mod.BlockList.*;
import static ziyue.tjmetro.mod.ItemList.*;

public interface Filters
{
    PressAction OPTION_BUTTON_ACTION = button -> MinecraftClient.getInstance().openScreen(new Screen(new ConfigClientScreen(MinecraftClient.getInstance().getCurrentScreenMapped())));

    Filter TIANJIN_MISCELLANEOUS = FilterBuilder.registerFilter(CreativeModeTabs.TIANJIN_METRO, TextHelper.translatable("filter.tjmetro.tianjin_miscellaneous"), () -> new ItemStack(new ItemConvertible(ItemList.WRENCH.get().data)));
    Filter TIANJIN_BUILDING = FilterBuilder.registerFilter(CreativeModeTabs.TIANJIN_METRO, TextHelper.translatable("filter.tjmetro.tianjin_building"), () -> new ItemStack(new ItemConvertible(BlockList.ROLLING.get().data)));
    Filter TIANJIN_SIGNS = FilterBuilder.registerFilter(CreativeModeTabs.TIANJIN_METRO, TextHelper.translatable("filter.tjmetro.tianjin_signs"), () -> new ItemStack(new ItemConvertible(BlockList.STATION_NAME_SIGN_1.get().data)));
    Filter TIANJIN_GATES = FilterBuilder.registerFilter(CreativeModeTabs.TIANJIN_METRO, TextHelper.translatable("filter.tjmetro.tianjin_gates"), () -> new ItemStack(new ItemConvertible(ItemList.PSD_DOOR_TIANJIN.get().data)));
    Filter TIANJIN_DECORATION = FilterBuilder.registerFilter(CreativeModeTabs.TIANJIN_METRO, TextHelper.translatable("filter.tjmetro.tianjin_decoration"), () -> new ItemStack(new ItemConvertible(BlockList.LOGO.get().data)));
    Filter TIANJIN_RAILWAY_SIGNS = FilterBuilder.registerFilter(CreativeModeTabs.TIANJIN_METRO, TextHelper.translatable("filter.tjmetro.tianjin_railway_signs"), () -> new ItemStack(new ItemConvertible(BlockList.RAILWAY_SIGN_TIANJIN_3_EVEN.get().data)));
    //Filter TIANJIN_UNCATEGORIZED = FilterBuilder.registerUncategorizedItemsFilter(CreativeModeTabs.TIANJIN_METRO);

    static void init() {
        FilterBuilder.addItems(Filters.TIANJIN_MISCELLANEOUS, WRENCH);
        FilterBuilder.addBlocks(Filters.TIANJIN_MISCELLANEOUS, PLAYER_DETECTOR, HIGH_SPEED_REPEATER);
        FilterBuilder.addBlocks(Filters.TIANJIN_BUILDING,
                ROLLING,
                PLATFORM_TJ_1, PLATFORM_TJ_2, PLATFORM_TJ_1_INDENTED, PLATFORM_TJ_2_INDENTED, PLATFORM_TJ_LINE_11_1, PLATFORM_TJ_LINE_11_1_INDENTED, PLATFORM_TJ_LINE_11_2, PLATFORM_TJ_LINE_11_2_INDENTED,
                MARBLE_GRAY, MARBLE_GRAY_SLAB, MARBLE_GRAY_STAIRS, MARBLE_YELLOW, MARBLE_YELLOW_SLAB, MARBLE_YELLOW_STAIRS,
                ROADBLOCK, ROADBLOCK_SIGN,
                CUSTOM_COLOR_CONCRETE, CUSTOM_COLOR_CONCRETE_SLAB, CUSTOM_COLOR_CONCRETE_STAIRS);
        FilterBuilder.addBlocks(Filters.TIANJIN_SIGNS,
                STATION_NAME_SIGN_1, STATION_NAME_SIGN_2,
                STATION_NAME_ENTRANCE_TIANJIN, STATION_NAME_ENTRANCE_TIANJIN_PINYIN, STATION_NAME_ENTRANCE_TIANJIN_BMT, STATION_NAME_ENTRANCE_TIANJIN_BMT_PINYIN, STATION_NAME_ENTRANCE_TIANJIN_JINJING, STATION_NAME_ENTRANCE_TIANJIN_JINJING_PINYIN,
                STATION_NAME_WALL_LEGACY, STATION_NAME_PROJECTOR,
                STATION_NAME_PLATE);
        FilterBuilder.addItems(Filters.TIANJIN_GATES,
                PSD_DOOR_TIANJIN, PSD_GLASS_TIANJIN, PSD_GLASS_END_TIANJIN,
                APG_DOOR_TIANJIN, APG_GLASS_TIANJIN, APG_GLASS_END_TIANJIN,
                APG_DOOR_TIANJIN_BMT, APG_GLASS_TIANJIN_BMT, APG_GLASS_END_TIANJIN_BMT,
                PSD_DOOR_TIANJIN_BMT, PSD_GLASS_TIANJIN_BMT, PSD_GLASS_END_TIANJIN_BMT
                //APG_DOOR_TIANJIN_TRT, APG_GLASS_TIANJIN_TRT, APG_GLASS_END_TIANJIN_TRT
        );
        FilterBuilder.addBlocks(Filters.TIANJIN_DECORATION,
                CEILING_NOT_LIT, STATION_COLOR_CEILING, STATION_COLOR_CEILING_LIGHT, STATION_COLOR_CEILING_NO_LIGHT, STATION_COLOR_CEILING_NOT_LIT,
                LOGO, APG_CORNER, TIME_DISPLAY, EMERGENCY_EXIT_SIGN, SERVICE_CORRIDOR_SIGN, BENCH, METAL_DETECTION_DOOR, METAL_POLE_BMT, SECURITY_CHECK_SIGN,
                STATION_NAVIGATOR_POLE, STATION_NAVIGATOR_3, STATION_NAVIGATOR_4, STATION_NAVIGATOR_5);
        FilterBuilder.addBlocks(Filters.TIANJIN_RAILWAY_SIGNS,
                RAILWAY_SIGN_WALL_4, RAILWAY_SIGN_WALL_6, RAILWAY_SIGN_WALL_8, RAILWAY_SIGN_WALL_10,
                RAILWAY_SIGN_WALL_DOUBLE_4, RAILWAY_SIGN_WALL_DOUBLE_6, RAILWAY_SIGN_WALL_DOUBLE_8, RAILWAY_SIGN_WALL_DOUBLE_10,
                RAILWAY_SIGN_WALL_BIG_2, RAILWAY_SIGN_WALL_BIG_3, RAILWAY_SIGN_WALL_BIG_4, RAILWAY_SIGN_WALL_BIG_5, RAILWAY_SIGN_WALL_BIG_6, RAILWAY_SIGN_WALL_BIG_7, RAILWAY_SIGN_WALL_BIG_8, RAILWAY_SIGN_WALL_BIG_9, RAILWAY_SIGN_WALL_BIG_10,
                RAILWAY_SIGN_TIANJIN_3_ODD, RAILWAY_SIGN_TIANJIN_4_ODD, RAILWAY_SIGN_TIANJIN_5_ODD, RAILWAY_SIGN_TIANJIN_6_ODD, RAILWAY_SIGN_TIANJIN_7_ODD, RAILWAY_SIGN_TIANJIN_2_EVEN, RAILWAY_SIGN_TIANJIN_3_EVEN, RAILWAY_SIGN_TIANJIN_4_EVEN, RAILWAY_SIGN_TIANJIN_5_EVEN, RAILWAY_SIGN_TIANJIN_6_EVEN, RAILWAY_SIGN_TIANJIN_7_EVEN, RAILWAY_SIGN_TIANJIN_POLE,
                RAILWAY_SIGN_TIANJIN_BMT_2_ODD, RAILWAY_SIGN_TIANJIN_BMT_3_ODD, RAILWAY_SIGN_TIANJIN_BMT_4_ODD, RAILWAY_SIGN_TIANJIN_BMT_5_ODD, RAILWAY_SIGN_TIANJIN_BMT_6_ODD, RAILWAY_SIGN_TIANJIN_BMT_7_ODD, RAILWAY_SIGN_TIANJIN_BMT_2_EVEN, RAILWAY_SIGN_TIANJIN_BMT_3_EVEN, RAILWAY_SIGN_TIANJIN_BMT_4_EVEN, RAILWAY_SIGN_TIANJIN_BMT_5_EVEN, RAILWAY_SIGN_TIANJIN_BMT_6_EVEN, RAILWAY_SIGN_TIANJIN_BMT_7_EVEN);

        FilterBuilder.setReservedButton(CreativeModeTabs.TIANJIN_METRO, TextHelper.translatable("button.tjmetro.tianjin_metro_options"), OPTION_BUTTON_ACTION);
    }
}
