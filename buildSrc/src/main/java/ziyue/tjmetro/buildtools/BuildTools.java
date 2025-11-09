package ziyue.tjmetro.buildtools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BuildTools
{
    public final String minecraftVersion;
    public final int javaLanguageVersion;
    public final Dependencies dependencies;

    public BuildTools(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
        int majorVersion = Integer.parseInt(minecraftVersion.split("\\.")[1]);
        javaLanguageVersion = majorVersion == 17 ? 16 : 17;
        JsonObject json = getJson("https://files.ziyuesinicization.site/tjmetro/dependencies.json").getAsJsonObject();
        JsonObject versionSpecificJson = json.get(minecraftVersion).getAsJsonObject();
        dependencies = new Dependencies(
                json.get("fabric-loom").getAsString(),
                json.get("fabric-loader").getAsString(),
                versionSpecificJson.get("yarn").getAsString(),
                versionSpecificJson.get("fabric-api").getAsString(),
                versionSpecificJson.get("modmenu").getAsString(),
                versionSpecificJson.get("filters-api").getAsString(),
                versionSpecificJson.get("cloth-config").getAsString(),
                versionSpecificJson.get("forge").getAsString()
        );
        System.out.println(dependencies);
    }

    private record Dependencies(String fabricLoom, String fabricLoader, String yarn, String fabricApi, String modMenu,
                                String filtersApi, String clothConfig, String forge)
    {
    }

    private static JsonElement getJson(String url) {
        for (int i = 0; i < 5; i++) {
            try {
                return JsonParser.parseString(IOUtils.toString(new URL(url), StandardCharsets.UTF_8));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new JsonObject();
    }
}