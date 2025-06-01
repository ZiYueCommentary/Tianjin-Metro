package ziyue.tjmetro.mod.screen;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.client.DynamicTextureCache;
import ziyue.tjmetro.mapping.TextFormatter;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.config.ConfigClient;

import java.util.Random;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-5
 */

public class ClientConfigScreen
{
    public static final MasterCategory TIANJIN_METRO_CATEGORY = new MasterCategory(Reference.MOD_ID, () -> TextHelper.translatable("config.category.tjmetro"), (entryBuilder, categoryTianjinMetro) -> {
        BooleanListEntry booleanUseTianjinMetroFont = entryBuilder
                .startBooleanToggle(TextHelper.translatable("config.tjmetro.use_tianjin_metro_font").data, ConfigClient.USE_TIANJIN_METRO_FONT.get())
                .setTooltip(TextHelper.translatable("tooltip.tjmetro.use_tianjin_metro_font").data)
                .setDefaultValue(ConfigClient.USE_TIANJIN_METRO_FONT.getDefault())
                .setSaveConsumer(ConfigClient.USE_TIANJIN_METRO_FONT::set)
                .build();
        BooleanListEntry booleanRotatedStationName = entryBuilder
                .startBooleanToggle(TextHelper.translatable("config.tjmetro.rotated_station_name").data, ConfigClient.ROTATED_STATION_NAME.get())
                .setDefaultValue(ConfigClient.ROTATED_STATION_NAME.getDefault())
                .setSaveConsumer(ConfigClient.ROTATED_STATION_NAME::set)
                .build();
        SubCategoryBuilder subCategoryDebugging = entryBuilder.startSubCategory(TextHelper.translatable("config.tjmetro.debugging").data);
        BooleanListEntry booleanDisableDynamicTextures = entryBuilder
                .startBooleanToggle(TextHelper.translatable("config.tjmetro.disable_dynamic_textures").data, ConfigClient.DISABLE_DYNAMIC_TEXTURES.get())
                .setDefaultValue(ConfigClient.DISABLE_DYNAMIC_TEXTURES.getDefault())
                .setTooltip(TextHelper.translatable("tooltip.tjmetro.disable_dynamic_textures").data)
                .setSaveConsumer(ConfigClient.DISABLE_DYNAMIC_TEXTURES::set)
                .build();
        BooleanListEntry booleanDisableTrainRendering = entryBuilder
                .startBooleanToggle(TextHelper.translatable("config.tjmetro.disable_train_rendering").data, ConfigClient.DISABLE_TRAIN_RENDERING.get())
                .setDefaultValue(ConfigClient.DISABLE_TRAIN_RENDERING.getDefault())
                .setSaveConsumer(ConfigClient.DISABLE_TRAIN_RENDERING::set)
                .build();
        subCategoryDebugging.add(booleanDisableDynamicTextures);
        subCategoryDebugging.add(booleanDisableTrainRendering);
        TextListEntry textFooter = entryBuilder.startTextDescription(TextFormatter.FOOTER_LINK.apply(ConfigClient.FOOTERS.get(new Random().nextInt(ConfigClient.FOOTERS.size())))).build();
        categoryTianjinMetro.addEntry(booleanUseTianjinMetroFont).addEntry(booleanRotatedStationName).addEntry(subCategoryDebugging.build()).addEntry(textFooter);
    }, () -> ConfigBuilder.create()
            .setTitle(TextHelper.translatable("gui.tjmetro.options").data)
            .setSavingRunnable(() -> {
                if (DynamicTextureCache.instance != null) {
                    DynamicTextureCache.instance.reload();
                }
                ConfigClient.writeToFile();
            }));

    public static final CentralConfig TIANJIN_METRO_CENTRAL_CONFIG = new CentralConfig(TIANJIN_METRO_CATEGORY);

    public static Screen getClothConfigScreen(Screen parent) {
        return TIANJIN_METRO_CENTRAL_CONFIG.getConfigScreen(parent);
    }
}
