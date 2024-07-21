package ziyue.tjmetro.mod.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.CreativeModeTabs;
import ziyue.tjmetro.mapping.FilterBuilder;
import ziyue.tjmetro.mod.TianjinMetro;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @see Property
 * @since beta-1
 */

public class ConfigClient
{
    public static final Property<Boolean> ENABLE_MTR_FILTERS = new Property<>("enable_mtr_filters", true)
    {
        @Override
        public void set(Boolean value) {
            FilterBuilder.filtersVisibility(CreativeModeTabs.CORE, value);
            FilterBuilder.filtersVisibility(CreativeModeTabs.ESCALATORS_LIFTS, value);
            FilterBuilder.filtersVisibility(CreativeModeTabs.RAILWAY_FACILITIES, value);
            FilterBuilder.filtersVisibility(CreativeModeTabs.STATION_BUILDING_BLOCKS, value);
            super.set(value);
        }
    };
    public static final Property<Boolean> USE_TIANJIN_METRO_FONT = new Property<>("use_tianjin_metro_font", true);

    protected static final Path CONFIG_FILE_PATH = MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("config/tjmetro.json");
    public static final List<Supplier<MutableText>> FOOTERS = Arrays.asList(
            //() -> IGuiExtension.format(Text.translatable("footer.tjmetro.sources"), IGuiExtension.LINK_STYLE.apply(Reference.GITHUB_REPO)),
            //() -> IGuiExtension.format(Text.translatable("footer.tjmetro.contributors"), IGuiExtension.LINK_STYLE.apply(Reference.CONTRIBUTORS)),
            //() -> IGuiExtension.format(Text.translatable("footer.tjmetro.forum"), IGuiExtension.LINK_STYLE.apply(Reference.FORUM).withColor(TextColor.fromRgb(15946322))),
            () -> TextHelper.translatable("footer.tjmetro.sources", "https://url.zip/cfe7ea9"),
            () -> TextHelper.translatable("footer.tjmetro.forum", "https://url.zip/92ccb56"),
            () -> TextHelper.translatable("footer.tjmetro.weblate", "https://url.zip/891a2ce")
    );

    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent.data).setTitle(TextHelper.translatable("gui.tjmetro.options").data).transparentBackground();
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory categoryTianjinMetro = builder.getOrCreateCategory(TextHelper.translatable("config.category.tjmetro").data);
        BooleanListEntry booleanMTRFilters = entryBuilder.startBooleanToggle(TextHelper.translatable("config.tjmetro.enable_mtr_filters").data, ENABLE_MTR_FILTERS.get()).setDefaultValue(ENABLE_MTR_FILTERS.getDefault()).build();
        BooleanListEntry booleanUseTianjinMetroFont = entryBuilder.startBooleanToggle(TextHelper.translatable("config.tjmetro.use_tianjin_metro_font").data, USE_TIANJIN_METRO_FONT.get()).setTooltip(TextHelper.translatable("tooltip.tjmetro.use_tianjin_metro_font").data, TextHelper.translatable("tooltip.tjmetro.experimental").formatted(TextFormatting.YELLOW).data).setDefaultValue(ENABLE_MTR_FILTERS.getDefault()).build();
        TextListEntry textFooter = entryBuilder.startTextDescription(FOOTERS.get(new Random().nextInt(FOOTERS.size())).get().data).build();
        categoryTianjinMetro.addEntry(booleanMTRFilters).addEntry(booleanUseTianjinMetroFont).addEntry(textFooter);

        builder.setSavingRunnable(() -> {
            ENABLE_MTR_FILTERS.set(booleanMTRFilters.getValue());
            USE_TIANJIN_METRO_FONT.set(booleanUseTianjinMetroFont.getValue());
        });
        return new Screen(builder.build());
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
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }
    }

    protected static void writeToFile() {
        TianjinMetro.LOGGER.info("Wrote Tianjin Metro config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(ENABLE_MTR_FILTERS.getId(), ENABLE_MTR_FILTERS.get());
        jsonConfig.addProperty(USE_TIANJIN_METRO_FONT.getId(), USE_TIANJIN_METRO_FONT.get());

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(Utilities.prettyPrint(jsonConfig)));
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e);
        }
    }

    /**
     * A wrapper for settings.
     *
     * @author ZiYueCommentary
     * @see ConfigClient
     * @since beta-1
     */
    public static class Property<T>
    {
        protected T value;
        protected final T defaultValue;
        protected final String id;

        public Property(String id, T defaultValue) {
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
