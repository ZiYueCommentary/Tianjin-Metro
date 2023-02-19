package ziyue.tjmetro;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.CreativeModeTabs;
import mtr.data.RailwayData;
import net.minecraft.client.Minecraft;
import ziyue.tjmetro.filters.Filter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

public class Options
{
    protected static boolean enableMTRFilters;
    protected static final Path CONFIG_FILE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tjmetro.json");
    protected static final String ENABLE_MTR_FILTERS_KEY = "enable_mtr_filters";

    public static boolean enableMTRFilters() {
        return enableMTRFilters;
    }

    public static boolean enableMTRFilters(boolean value) {
        enableMTRFilters = value;
        Filter.FILTERS.get(CreativeModeTabs.CORE.get().getId()).enabled = value;
        Filter.FILTERS.get(CreativeModeTabs.ESCALATORS_LIFTS.get().getId()).enabled = value;
        Filter.FILTERS.get(CreativeModeTabs.RAILWAY_FACILITIES.get().getId()).enabled = value;
        Filter.FILTERS.get(CreativeModeTabs.STATION_BUILDING_BLOCKS.get().getId()).enabled = value;

        writeToFile();
        return enableMTRFilters;
    }

    public static void refreshProperties() {
        TianjinMetro.LOGGER.info("Refreshed Tianjin Metro options");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
            try {
                enableMTRFilters(jsonConfig.get(ENABLE_MTR_FILTERS_KEY).getAsBoolean());
            } catch (Exception ignored) {
            }
        } catch (Exception e) {
            writeToFile();
            e.printStackTrace();
        }
    }

    protected static void writeToFile() {
        TianjinMetro.LOGGER.info("Wrote Tianjin Metro options to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(ENABLE_MTR_FILTERS_KEY, enableMTRFilters);

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(RailwayData.prettyPrint(jsonConfig)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
