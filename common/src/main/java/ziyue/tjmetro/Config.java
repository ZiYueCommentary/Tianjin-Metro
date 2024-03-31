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
import net.minecraft.network.chat.TextColor;
import ziyue.tjmetro.client.ClientCache;
import ziyue.tjmetro.filter.Filter;

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
        public void set(Boolean value) {
            Filter.FILTERS.get(CreativeModeTabs.CORE.get().getId()).enabled = value;
            Filter.FILTERS.get(CreativeModeTabs.ESCALATORS_LIFTS.get().getId()).enabled = value;
            Filter.FILTERS.get(CreativeModeTabs.RAILWAY_FACILITIES.get().getId()).enabled = value;
            Filter.FILTERS.get(CreativeModeTabs.STATION_BUILDING_BLOCKS.get().getId()).enabled = value;
            super.set(value);
        }
    };
    public static final Key<Boolean> USE_TIANJIN_METRO_FONT = new Key<>("use_tianjin_metro_font", false);

    protected static final Path CONFIG_FILE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tjmetro.json");
    public static final List<Supplier<MutableComponent>> FOOTERS = Arrays.asList(
            () -> IDrawingExtends.format(Text.translatable("footer.tjmetro.sources"), IDrawingExtends.LINK_STYLE.apply(Reference.GITHUB_REPO)),
            () -> IDrawingExtends.format(Text.translatable("footer.tjmetro.contributors"), IDrawingExtends.LINK_STYLE.apply(Reference.CONTRIBUTORS)),
            () -> IDrawingExtends.format(Text.translatable("footer.tjmetro.forum"), IDrawingExtends.LINK_STYLE.apply(Reference.FORUM).withColor(TextColor.fromRgb(15946322))),
            () -> IDrawingExtends.format(Text.translatable("footer.tjmetro.weblate"), IDrawingExtends.LINK_STYLE.apply(Reference.WEBLATE))
    );

    public static Screen getConfigScreen() {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(Text.translatable("gui.tjmetro.options")).transparentBackground();
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory categoryTianjinMetro = builder.getOrCreateCategory(Text.translatable("config.category.tjmetro"));
        BooleanListEntry booleanMTRFilters = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.enable_mtr_filters"), ENABLE_MTR_FILTERS.get()).setDefaultValue(ENABLE_MTR_FILTERS.getDefault()).build();
        BooleanListEntry booleanUseTianjinMetroFont = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.use_tianjin_metro_font"), USE_TIANJIN_METRO_FONT.get()).setTooltip(Text.translatable("tooltip.tjmetro.use_tianjin_metro_font"), Text.translatable("tooltip.tjmetro.experimental").withStyle(ChatFormatting.YELLOW)).build();
        TextListEntry textFooter = entryBuilder.startTextDescription(FOOTERS.get(new Random().nextInt(FOOTERS.size())).get()).build();
        categoryTianjinMetro.addEntry(booleanMTRFilters).addEntry(booleanUseTianjinMetroFont).addEntry(textFooter);

        builder.setSavingRunnable(() -> {
            ENABLE_MTR_FILTERS.set(booleanMTRFilters.getValue());
            USE_TIANJIN_METRO_FONT.set(booleanUseTianjinMetroFont.getValue());
            ClientCache.DATA_CACHE.resetFonts();
        });
        return builder.build();
    }

    public static void refreshProperties() {
        TianjinMetro.LOGGER.info("Refreshed Tianjin Metro config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
            try {
                ENABLE_MTR_FILTERS.set(jsonConfig.get(ENABLE_MTR_FILTERS.getId()).getAsBoolean());
                USE_TIANJIN_METRO_FONT.set(jsonConfig.get(USE_TIANJIN_METRO_FONT.getId()).getAsBoolean());
            } catch (Exception ignored) {
                ENABLE_MTR_FILTERS.set(ENABLE_MTR_FILTERS.getDefault());
                USE_TIANJIN_METRO_FONT.set(USE_TIANJIN_METRO_FONT.getDefault());
            }
        } catch (Exception e) {
            writeToFile();
            TianjinMetro.LOGGER.error(e);
        }
    }

    protected static void writeToFile() {
        TianjinMetro.LOGGER.info("Wrote Tianjin Metro config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(ENABLE_MTR_FILTERS.getId(), ENABLE_MTR_FILTERS.get());
        jsonConfig.addProperty(USE_TIANJIN_METRO_FONT.getId(), USE_TIANJIN_METRO_FONT.get());

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(RailwayData.prettyPrint(jsonConfig)));
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e);
        }
    }

    /**
     * A wrapper for settings.
     *
     * @author ZiYueCommentary
     * @see Config
     * @since beta-1
     */
    public static class Key<T>
    {
        protected T value;
        protected final T defaultValue;
        protected final String id;

        public Key(String id, T defaultValue) {
            this.id = id;
            this.value = defaultValue;
            this.defaultValue = defaultValue;
        }

        public String getId() {
            return id;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
            writeToFile();
        }

        public T getDefault() {
            return defaultValue;
        }
    }
}
