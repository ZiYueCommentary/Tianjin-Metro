package ziyue.tjmetro.mod.mixin;

import org.mtr.libraries.com.google.gson.JsonArray;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.config.Config;
import org.mtr.mod.resource.SignResource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.block.BlockPIDSTianjin;
import ziyue.tjmetro.mod.block.base.IRailwaySign;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZiYueCommentary
 * @see CustomResourceLoader
 * @since 1.0.0-beta-1
 */

@Mixin(CustomResourceLoader.class)
public abstract class CustomResourceLoaderMixin
{
    @Shadow(remap = false)
    @Final
    private static Object2ObjectAVLTreeMap<String, SignResource> SIGNS_CACHE;

    @Shadow(remap = false)
    @Final
    private static ObjectArrayList<SignResource> SIGNS;

    @Inject(at = @At("TAIL"), method = "reload", remap = false)
    private static void afterReload(CallbackInfo ci) {
        for (IRailwaySign.SignType value : IRailwaySign.SignType.values()) {
            SIGNS.add(value.sign);
            SIGNS_CACHE.put(value.signId, value.sign);
        }

        BlockPIDSTianjin.CATEGORIES.clear();

        ResourceManagerHelper.readAllResources(new Identifier("tjmetro", "pids_tianjin_ads.json"), (inputStream) -> {
            try {
                final JsonObject config = Config.readResource(inputStream).getAsJsonObject();
                config.entrySet().forEach(entry -> {
                    if (entry.getValue().isJsonObject()) {
                        long id = entry.getKey().hashCode();
                        if (BlockPIDSTianjin.CATEGORIES.containsKey(id)) {
                            TianjinMetro.LOGGER.warn("Duplicate category key: {}. Skipping!", entry.getKey());
                        } else {
                            final JsonObject adDefinition = entry.getValue().getAsJsonObject();
                            BlockPIDSTianjin.CATEGORIES.put(id, new BlockPIDSTianjin.Category(
                                    id,
                                    new Color(Integer.parseInt(adDefinition.get("color").getAsString(), 16)),
                                    TextHelper.translatable(adDefinition.get("name").getAsString()),
                                    TextHelper.translatable(adDefinition.get("description").getAsString()))
                            );

                            BlockPIDSTianjin.Category array = BlockPIDSTianjin.CATEGORIES.get(id);
                            if (!adDefinition.get("advertisements").isJsonArray()) {
                                TianjinMetro.LOGGER.warn("Bad advertisement config at {}: missing advertisement array. Skipping!", entry.getKey());
                            } else {
                                JsonArray advertisements = adDefinition.get("advertisements").getAsJsonArray();
                                AtomicInteger index = new AtomicInteger();
                                advertisements.forEach(ad -> {
                                    try {
                                        if (ad.isJsonObject()) {
                                            JsonObject slide = ad.getAsJsonObject();
                                            array.add(new BlockPIDSTianjin.Advertisement(new Identifier(slide.get("image").getAsString()), TextHelper.translatable(slide.get("text").getAsString())));
                                        }
                                    } catch (Exception e) {
                                        TianjinMetro.LOGGER.warn("Bad advertisement config at {}, index {}. Skipping!", entry.getKey(), index.get());
                                    }
                                    index.getAndIncrement();
                                });
                            }
                        }
                    }
                });
            } catch (Exception e) {
                TianjinMetro.LOGGER.warn("Error when initializing advertisements!", e);
            }
        });
        TianjinMetro.LOGGER.info("Found {} categories for PIDS Tianjin", BlockPIDSTianjin.CATEGORIES.size());
    }
}
