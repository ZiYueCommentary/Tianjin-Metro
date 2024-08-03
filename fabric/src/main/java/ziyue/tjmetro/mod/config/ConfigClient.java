package ziyue.tjmetro.mod.config;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.TextHelper;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.TianjinMetro;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @see Property
 * @since 1.0.0-beta-1
 */

public class ConfigClient
{
    public static final Property<Boolean> ENABLE_MTR_FILTERS = new Property<>("enable_mtr_filters", false);
    public static final Property<Boolean> USE_TIANJIN_METRO_FONT = new Property<>("use_tianjin_metro_font", true);

    protected static final Path CONFIG_FILE_PATH = MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("config/tjmetro.json");
    public static final List<Footer> FOOTERS = Arrays.asList(
            new Footer(() -> TextHelper.translatable("footer.tjmetro.sources"), Reference.GITHUB_REPO),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.forum"), Reference.FORUM),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.contributors"), Reference.CONTRIBUTORS),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.weblate"), Reference.WEBLATE),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.discord"), Reference.DISCORD)
    );

    /**
     * @deprecated Cloth Config does not support Forge 1.20.4
     */
    @Deprecated
    public static Screen getConfigScreen(Screen parent) {
        //ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent.data).setTitle(TextHelper.translatable("gui.tjmetro.options").data).transparentBackground();
        //ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        //ConfigCategory categoryTianjinMetro = builder.getOrCreateCategory(TextHelper.translatable("config.category.tjmetro").data);
        //BooleanListEntry booleanMTRFilters = entryBuilder.startBooleanToggle(TextHelper.translatable("config.tjmetro.enable_mtr_filters").data, ENABLE_MTR_FILTERS.get()).setDefaultValue(ENABLE_MTR_FILTERS.getDefault()).build();
        //BooleanListEntry booleanUseTianjinMetroFont = entryBuilder.startBooleanToggle(TextHelper.translatable("config.tjmetro.use_tianjin_metro_font").data, USE_TIANJIN_METRO_FONT.get()).setTooltip(TextHelper.translatable("tooltip.tjmetro.use_tianjin_metro_font").data, TextHelper.translatable("tooltip.tjmetro.experimental").formatted(TextFormatting.YELLOW).data).setDefaultValue(ENABLE_MTR_FILTERS.getDefault()).build();
        //TextListEntry textFooter = entryBuilder.startTextDescription(FOOTERS.get(new Random().nextInt(FOOTERS.size())).get().data).build();
        //categoryTianjinMetro.addEntry(booleanMTRFilters).addEntry(booleanUseTianjinMetroFont).addEntry(textFooter);

        //builder.setSavingRunnable(() -> {
        //    ENABLE_MTR_FILTERS.set(booleanMTRFilters.getValue());
        //    USE_TIANJIN_METRO_FONT.set(booleanUseTianjinMetroFont.getValue());
        //});
        //return new Screen(builder.build());
        throw new UnsupportedOperationException();
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
     * @since 1.0.0-beta-1
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

    public static final class Footer
    {
        private final Supplier<MutableText> footer;
        private final String link;

        public Footer(Supplier<MutableText> footer, String link) {
            this.footer = footer;
            this.link = link;
        }

        public Supplier<MutableText> footer() {
            return footer;
        }

        public String link() {
            return link;
        }
    }
}
