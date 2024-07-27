package org.mtr.mod;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jonafanho.apitools.ModId;
import com.jonafanho.apitools.ModLoader;
import com.jonafanho.apitools.ModProvider;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.gradle.api.Project;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class BuildTools
{
    public final String minecraftVersion;
    public final String loader;
    public final int javaLanguageVersion;
    private final Path path;
    private final String version;
    private final String mtrVersion;

    public BuildTools(String minecraftVersion, String loader, String mtrVersion, Project project) {
        this.minecraftVersion = minecraftVersion;
        this.loader = loader;
        this.path = project.getProjectDir().toPath();
        this.version = project.getVersion().toString();
        this.mtrVersion = mtrVersion;
        int majorVersion = Integer.parseInt(minecraftVersion.split("\\.")[1]);
        javaLanguageVersion = majorVersion <= 16 ? 8 : majorVersion == 17 ? 16 : 17;
    }

    public String getFabricVersion() {
        String fabricVersion = getJson("https://meta.fabricmc.net/v2/versions/loader/" + minecraftVersion).getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject("loader").get("version").getAsString();
        System.out.println("Fabric loader version: " + fabricVersion);
        return fabricVersion;
    }

    public String getYarnVersion() {
        String yarnVersion = getJson("https://meta.fabricmc.net/v2/versions/yarn/" + minecraftVersion).getAsJsonArray().get(0).getAsJsonObject().get("version").getAsString();
        System.out.println("Yarn version: " + yarnVersion);
        return yarnVersion;
    }

    public String getFabricApiVersion() {
        final String modIdString = "fabric-api";
        String fabricApiVersion = new ModId(modIdString, ModProvider.MODRINTH).getModFiles(minecraftVersion, ModLoader.FABRIC, "").get(0).fileName.split(".jar")[0].replace(modIdString + "-", "");
        System.out.println("Fabric API version: " + fabricApiVersion);
        return fabricApiVersion;
    }

    public String getModMenuVersion() {
        if (minecraftVersion.equals("1.20.4")) {
            return "9.0.0"; // TODO latest version not working
        }
        final String modIdString = "modmenu";
        String modMenuVersion = new ModId(modIdString, ModProvider.MODRINTH).getModFiles(minecraftVersion, ModLoader.FABRIC, "").get(0).fileName.split(".jar")[0].replace(modIdString + "-", "");
        System.out.println("ModMenu version: " + modMenuVersion);
        return modMenuVersion;
    }

    public String getForgeVersion() {
        String forgeVersion = getJson("https://files.minecraftforge.net/net/minecraftforge/forge/promotions_slim.json").getAsJsonObject().getAsJsonObject("promos").get(minecraftVersion + "-latest").getAsString();
        System.out.println("Forge version: " + forgeVersion);
        return forgeVersion;
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