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
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @see Property
 * @since 1.0.0-beta-1
 */

public class ConfigClient
{
    private static final HashSet<Property<?>> PROPERTIES = new HashSet<>();

    public static final Property<Boolean> USE_TIANJIN_METRO_FONT = new Property<>("use_tianjin_metro_font", true);
    public static final Property<Boolean> ROTATED_STATION_NAME = new Property<>("rotated_station_name", true);
    public static final Property<Boolean> DISABLE_DYNAMIC_TEXTURES = new Property<>("disable_dynamic_textures", false);
    public static final Property<Boolean> DISABLE_TRAIN_RENDERING = new Property<>("disable_train_rendering", false);
    public static final Property<Integer> DYNAMIC_TEXTURE_MAX_SIZE = new Property<>("dynamic_texture_max_size", 2048);

    protected static final Path CONFIG_FILE_PATH = MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("config/tjmetro.json");
    public static final List<Footer> FOOTERS = Arrays.asList(
            new Footer(() -> TextHelper.translatable("footer.tjmetro.sources"), Reference.GITHUB_REPO),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.contributors"), Reference.CONTRIBUTORS),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.weblate"), Reference.WEBLATE),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.discord"), Reference.DISCORD),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.prevent_block_falling"), Reference.PREVENT_BLOCK_FALLING),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.bde"), Reference.BDE),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.sponsor"), Reference.PATREON),
            new Footer(() -> TextHelper.translatable("footer.tjmetro.tutorial"), Reference.TUTORIAL)
    );

    public static Screen getConfigScreen(Screen parent) {
        if (ModLoaderHelper.hasClothConfig()) {
            return ClientConfigScreen.getClothConfigScreen(parent);
        }
        return new Screen(new MissingClothConfigScreen(parent));
    }

    @SuppressWarnings("unchecked")
    public static void refreshProperties() {
        TianjinMetro.LOGGER.info("Refreshed Tianjin Metro config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();

            for (Property<?> property : PROPERTIES) {
                if (property.get() instanceof Boolean) {
                    Property<Boolean> converted = (Property<Boolean>) property;
                    try {
                        converted.set(jsonConfig.get(converted.getId()).getAsBoolean());
                    } catch (Exception e) {
                        converted.set(converted.getDefault());
                    }
                } else if (property.get() instanceof Number) {
                    Property<Number> converted = (Property<Number>) property;
                    try {
                        converted.set(jsonConfig.get(converted.getId()).getAsNumber().intValue());
                    } catch (Exception e) {
                        converted.set(converted.getDefault());
                    }
                } else if (property.get() instanceof Enum) {
                    Property<Enum<?>> converted = (Property<Enum<?>>) property;
                    try {
                        converted.set(Enum.valueOf(converted.get().getClass(), jsonConfig.get(converted.getId()).getAsString()));
                    } catch (Exception e) {
                        converted.set(converted.getDefault());
                    }
                } else {
                    TianjinMetro.LOGGER.warn("Unrecognized config property: {}, type: {}", property.getId(), property.get().getClass().getName());
                }
            }
        } catch (Exception e) {
            writeToFile();
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }
    }

    public static void writeToFile() {
        TianjinMetro.LOGGER.info("Wrote Tianjin Metro config to file");
        final JsonObject jsonConfig = new JsonObject();

        for (Property<?> property : PROPERTIES) {
            if (property.get() instanceof Boolean) {
                jsonConfig.addProperty(property.getId(), (Boolean) property.get());
            } else if (property.get() instanceof Number) {
                jsonConfig.addProperty(property.getId(), (Number) property.get());
            } else if (property.get() instanceof Enum) {
                jsonConfig.addProperty(property.getId(), property.get().toString());
            } else {
                TianjinMetro.LOGGER.warn("Unrecognized config property: {}, type: {}", property.getId(), property.get().getClass().getName());
            }
        }

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(Utilities.prettyPrint(jsonConfig)));
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e);
        }
    }

    /**
     * A wrapper for settings.
     * A property shouldn't be modified outside the config screen.
     * If you do so, invoke {@link ConfigClient#writeToFile()} after modify.
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

            ConfigClient.PROPERTIES.add(this);
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

    public record Footer(Supplier<MutableText> text, String link)
    {
    }
}
