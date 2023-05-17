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
import net.minecraft.network.chat.MutableComponent;
import ziyue.tjmetro.filters.Filter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @see Key
 * @since beta-1
 */

public class Config
{
    public static final Key<Boolean> ENABLE_MTR_FILTERS = new Key<>("enable_mtr_filters", true)
    {
        @Override
        public void setValue(Boolean value) {
            Filter.FILTERS.get(CreativeModeTabs.CORE.get().getId()).enabled = value;
            Filter.FILTERS.get(CreativeModeTabs.ESCALATORS_LIFTS.get().getId()).enabled = value;
            Filter.FILTERS.get(CreativeModeTabs.RAILWAY_FACILITIES.get().getId()).enabled = value;
            Filter.FILTERS.get(CreativeModeTabs.STATION_BUILDING_BLOCKS.get().getId()).enabled = value;
            super.setValue(value);
        }
    };
    public static final Key<Boolean> USE_TIANJIN_METRO_FONT = new Key<>("use_tianjin_metro_font", false);

    protected static final Path CONFIG_FILE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tjmetro.json");
    public static final List<Supplier<MutableComponent>> FOOTERS = Arrays.asList(
            () -> IDrawingExtends.format(Text.translatable("footer.tjmetro.sources"), style -> IDrawingExtends.LINK_STYLE.apply(Reference.GITHUB_REPO).applyTo(style)),
            () -> IDrawingExtends.format(Text.translatable("footer.tjmetro.contributors"), style -> IDrawingExtends.LINK_STYLE.apply(Reference.CONTRIBUTORS).applyTo(style))
    );

    public static Screen getConfigScreen() {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(Text.translatable("gui.tjmetro.options")).transparentBackground();
        ConfigCategory category = builder.getOrCreateCategory(Text.translatable("gui.tjmetro.options"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        BooleanListEntry booleanMTRFilters = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.enable_mtr_filters"), ENABLE_MTR_FILTERS.getValue()).setDefaultValue(ENABLE_MTR_FILTERS.getDefaultValue()).build();
        BooleanListEntry booleanUseTianjinMetroFont = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.use_tianjin_metro_font"), USE_TIANJIN_METRO_FONT.getValue()).setTooltip(Text.translatable("tooltip.tjmetro.use_tianjin_metro_font"), Text.translatable("tooltip.tjmetro.experimental").withStyle(ChatFormatting.YELLOW)).setDefaultValue(USE_TIANJIN_METRO_FONT.getDefaultValue()).build();
        TextListEntry textFooter = entryBuilder.startTextDescription(FOOTERS.get(new Random().nextInt(FOOTERS.size())).get()).build();
        category.addEntry(booleanMTRFilters).addEntry(booleanUseTianjinMetroFont).addEntry(textFooter);
        builder.setSavingRunnable(() -> {
            ENABLE_MTR_FILTERS.setValue(booleanMTRFilters.getValue());
            USE_TIANJIN_METRO_FONT.setValue(booleanUseTianjinMetroFont.getValue());
            writeToFile();
        });
        return builder.build();
    }

    public static void refreshProperties() {
        TianjinMetro.LOGGER.info("Refreshed Tianjin Metro config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
            try {
                ENABLE_MTR_FILTERS.setValue(jsonConfig.get(ENABLE_MTR_FILTERS.getKey()).getAsBoolean());
                USE_TIANJIN_METRO_FONT.setValue(jsonConfig.get(USE_TIANJIN_METRO_FONT.getKey()).getAsBoolean());
            } catch (Exception ignored) {
                ENABLE_MTR_FILTERS.setValue(ENABLE_MTR_FILTERS.getDefaultValue());
                USE_TIANJIN_METRO_FONT.setValue(USE_TIANJIN_METRO_FONT.getDefaultValue());
            }
        } catch (Exception e) {
            writeToFile();
            TianjinMetro.LOGGER.error(e);
        }
    }

    protected static void writeToFile() {
        TianjinMetro.LOGGER.info("Wrote Tianjin Metro config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(ENABLE_MTR_FILTERS.getKey(), ENABLE_MTR_FILTERS.getValue());
        jsonConfig.addProperty(USE_TIANJIN_METRO_FONT.getKey(), USE_TIANJIN_METRO_FONT.getValue());

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(RailwayData.prettyPrint(jsonConfig)));
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e);
        }
    }

    /**
     * A class for settings.
     *
     * @author ZiYueCommentary
     * @see Config
     * @since beta-1
     */
    public static class Key<T>
    {
        protected T value;
        protected final T defaultValue;
        protected final String key;

        public Key(String key, T defaultValue) {
            this.key = key;
            this.value = defaultValue;
            this.defaultValue = defaultValue;
        }

        public String getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public T getDefaultValue() {
            return defaultValue;
        }
    }
}
