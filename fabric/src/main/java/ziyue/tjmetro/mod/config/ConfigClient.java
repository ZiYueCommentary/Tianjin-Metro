package ziyue.tjmetro.mod.config;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.TextHelper;
import ziyue.tjmetro.mapping.ModLoaderHelper;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.screen.ClientConfigScreen;
import ziyue.tjmetro.mod.screen.MissingClothConfigScreen;

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
    public static final Property<Boolean> USE_TIANJIN_METRO_FONT = new Property<>("use_tianjin_metro_font", true);
    public static final Property<Boolean> ROTATED_STATION_NAME = new Property<>("rotated_station_name", true);
    public static final Property<Boolean> DISABLE_DYNAMIC_TEXTURES = new Property<>("disable_dynamic_textures", false);
    public static final Property<Boolean> DISABLE_TRAIN_RENDERING = new Property<>("disable_train_rendering", false);

    protected static final Path CONFIG_FILE_PATH = MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("config/tjmetro.json");
    public static final List<Footer> FOOTERS = Arrays.asList(
            new Footer(() -> TextHelper.translatable("footer.tjmetro.sources"), Reference.GITHUB_REPO),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.forum"), Reference.FORUM),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.contributors"), Reference.CONTRIBUTORS),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.weblate"), Reference.WEBLATE),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.discord"), Reference.DISCORD),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.prevent_block_falling"), Reference.PREVENT_BLOCK_FALLING)
    );

    public static Screen getConfigScreen(Screen parent) {
        if (ModLoaderHelper.hasClothConfig()) {
            return ClientConfigScreen.getClothConfigScreen(parent);
        }
        return new Screen(new MissingClothConfigScreen(parent));
    }

    public static void refreshProperties() {
        TianjinMetro.LOGGER.info("Refreshed Tianjin Metro config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
            try {
                USE_TIANJIN_METRO_FONT.set(jsonConfig.get(USE_TIANJIN_METRO_FONT.getId()).getAsBoolean());
            } catch (Exception e) {
                USE_TIANJIN_METRO_FONT.set(USE_TIANJIN_METRO_FONT.getDefault());
            }
            try {
                ROTATED_STATION_NAME.set(jsonConfig.get(ROTATED_STATION_NAME.getId()).getAsBoolean());
            } catch (Exception e) {
                ROTATED_STATION_NAME.set(ROTATED_STATION_NAME.getDefault());
            }
            try {
                DISABLE_DYNAMIC_TEXTURES.set(jsonConfig.get(DISABLE_DYNAMIC_TEXTURES.getId()).getAsBoolean());
            } catch (Exception e) {
                DISABLE_DYNAMIC_TEXTURES.set(DISABLE_DYNAMIC_TEXTURES.getDefault());
            }
            try {
                DISABLE_TRAIN_RENDERING.set(jsonConfig.get(DISABLE_TRAIN_RENDERING.getId()).getAsBoolean());
            } catch (Exception e) {
                DISABLE_TRAIN_RENDERING.set(DISABLE_TRAIN_RENDERING.getDefault());
            }
        } catch (Exception e) {
            writeToFile();
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }
    }

    public static void writeToFile() {
        TianjinMetro.LOGGER.info("Wrote Tianjin Metro config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(USE_TIANJIN_METRO_FONT.getId(), USE_TIANJIN_METRO_FONT.get());
        jsonConfig.addProperty(ROTATED_STATION_NAME.getId(), ROTATED_STATION_NAME.get());
        jsonConfig.addProperty(DISABLE_DYNAMIC_TEXTURES.getId(), DISABLE_DYNAMIC_TEXTURES.get());
        jsonConfig.addProperty(DISABLE_TRAIN_RENDERING.getId(), DISABLE_TRAIN_RENDERING.get());

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
        }

        public T getDefault() {
            return defaultValue;
        }
    }

    public static final class Footer
    {
        private final Supplier<MutableText> text;
        private final String link;

        public Footer(Supplier<MutableText> text, String link) {
            this.text = text;
            this.link = link;
        }

        public Supplier<MutableText> text() {
            return text;
        }

        public String link() {
            return link;
        }
    }
}
