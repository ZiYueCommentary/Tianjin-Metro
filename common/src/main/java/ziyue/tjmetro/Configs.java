package ziyue.tjmetro;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class Configs {
    public static boolean noFallingBlocks;

    public static final Path CONFIG_FILE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tjmetro.json");
    public static final String NO_FALLING_BLOCKS = "no_falling_blocks";

    public static void writeToFile() {
        System.out.println("Wrote Tianjin Metro config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(NO_FALLING_BLOCKS, noFallingBlocks);

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(new GsonBuilder().setPrettyPrinting().create().toJson(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void refreshProperties() {
        System.out.println("Refreshed Tianjin Metro config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
            try {
                noFallingBlocks = jsonConfig.get(NO_FALLING_BLOCKS).getAsBoolean();
            } catch (Exception ignored) {}
        } catch (Exception e) {
            writeToFile();
            e.printStackTrace();
        }
    }
}
