package ziyue.tjmetro;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import mtr.CreativeModeTabs;
import mtr.data.RailwayData;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import ziyue.tjmetro.filters.Filter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author ZiYueCommentary
 * @since beta-1
 */

public class Config
{
    protected static boolean enableMTRFilters;
    protected static boolean useTianjinMetroFont;
    protected static final Path CONFIG_FILE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tjmetro.json");
    protected static final String ENABLE_MTR_FILTERS_KEY = "enable_mtr_filters";
    protected static final String USE_TIANJIN_METRO_FONT_KEY = "use_tianjin_metro_font";
    protected static final List<IDrawingExtends.FormatSupplier> FOOTERS = Arrays.asList(
            new IDrawingExtends.FormatSupplier(() -> Text.translatable("footer.tjmetro.sources").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Reference.GITHUB_REPO))), style -> style.withBold(true).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(Reference.GITHUB_REPO)))),
            new IDrawingExtends.FormatSupplier(() -> Text.translatable("footer.tjmetro.contributors").withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Reference.CONTRIBUTORS))), style -> style.withBold(true).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(Reference.CONTRIBUTORS))))
    );

    public static Screen getConfigScreen() {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(Text.translatable("gui.tjmetro.options")).transparentBackground();
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("gui.tjmetro.options"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        BooleanListEntry booleanMTRFilters = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.enable_mtr_filters"), getEnableMTRFilters()).setDefaultValue(true).build();
        BooleanListEntry booleanUseTianjinMetroFont = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.use_tianjin_metro_font"), getUseTianjinMetroFont()).setTooltip(Text.translatable("tooltip.tjmetro.use_tianjin_metro_font"), Text.translatable("tooltip.tjmetro.experimental").withStyle(ChatFormatting.YELLOW)).setDefaultValue(false).build();
        TextListEntry textFooter = entryBuilder.startTextDescription(FOOTERS.get(new Random().nextInt(FOOTERS.size())).get()).build();
        category.addEntry(booleanMTRFilters).addEntry(booleanUseTianjinMetroFont).addEntry(textFooter);
        builder.setSavingRunnable(() -> {
            setEnableMTRFilters(booleanMTRFilters.getValue());
            setUseTianjinMetroFont(booleanUseTianjinMetroFont.getValue());
            writeToFile();
        });
        return builder.build();
    }

    public static boolean getEnableMTRFilters() {
        return enableMTRFilters;
    }

    public static void setEnableMTRFilters(boolean value) {
        enableMTRFilters = value;
        Filter.FILTERS.get(CreativeModeTabs.CORE.get().getId()).enabled = value;
        Filter.FILTERS.get(CreativeModeTabs.ESCALATORS_LIFTS.get().getId()).enabled = value;
        Filter.FILTERS.get(CreativeModeTabs.RAILWAY_FACILITIES.get().getId()).enabled = value;
        Filter.FILTERS.get(CreativeModeTabs.STATION_BUILDING_BLOCKS.get().getId()).enabled = value;
    }

    public static boolean getUseTianjinMetroFont() {
        return useTianjinMetroFont;
    }

    public static void setUseTianjinMetroFont(boolean value) {
        useTianjinMetroFont = value;
    }

    public static void refreshProperties() {
        TianjinMetro.LOGGER.info("Refreshed Tianjin Metro config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
            try {
                setEnableMTRFilters(jsonConfig.get(ENABLE_MTR_FILTERS_KEY).getAsBoolean());
                setUseTianjinMetroFont(jsonConfig.get(USE_TIANJIN_METRO_FONT_KEY).getAsBoolean());
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            writeToFile();
            TianjinMetro.LOGGER.error(e);
        }
    }

    protected static void writeToFile() {
        TianjinMetro.LOGGER.info("Wrote Tianjin Metro config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(ENABLE_MTR_FILTERS_KEY, enableMTRFilters);
        jsonConfig.addProperty(USE_TIANJIN_METRO_FONT_KEY, useTianjinMetroFont);

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(RailwayData.prettyPrint(jsonConfig)));
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e);
        }
    }
}
