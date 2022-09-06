package ziyue.tjmetro;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static ziyue.tjmetro.TianjinMetro.LOGGER;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

@Environment(EnvType.CLIENT)
public class Configs
{
    public static final Path CONFIG_FILE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tjmetro.json");

    public static boolean noColoredLights;
    public static final String NO_COLORED_LIGHTS = "no_colored_lights";

    public static void writeToFile() {
        LOGGER.info("Wrote Tianjin Metro config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(NO_COLORED_LIGHTS, noColoredLights);

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refreshProperties() {
        LOGGER.info("Refreshed Tianjin Metro config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
            try {
                noColoredLights = jsonConfig.get(NO_COLORED_LIGHTS).getAsBoolean();
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            writeToFile();
            e.printStackTrace();
        }
    }
}
