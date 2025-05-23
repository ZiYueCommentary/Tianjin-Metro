package ziyue.tjmetro.mod.screen;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.TextHelper;
import ziyue.tjmetro.mapping.TextFormatter;
import ziyue.tjmetro.mod.client.DynamicTextureCache;
import ziyue.tjmetro.mod.config.ConfigClient;

import java.util.Random;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-5
 */

public class ClientConfigScreen
{
    // TODO public config screen api
    public static Screen getClothConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent.data).setTitle(TextHelper.translatable("gui.tjmetro.options").data).transparentBackground();
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory categoryTianjinMetro = builder.getOrCreateCategory(TextHelper.translatable("config.category.tjmetro").data);
        BooleanListEntry booleanUseTianjinMetroFont = entryBuilder.startBooleanToggle(TextHelper.translatable("config.tjmetro.use_tianjin_metro_font").data, ConfigClient.USE_TIANJIN_METRO_FONT.get()).setTooltip(TextHelper.translatable("tooltip.tjmetro.use_tianjin_metro_font").data).setDefaultValue(ConfigClient.USE_TIANJIN_METRO_FONT.getDefault()).build();
        BooleanListEntry booleanRotatedStationName = entryBuilder.startBooleanToggle(TextHelper.translatable("config.tjmetro.rotated_station_name").data, ConfigClient.ROTATED_STATION_NAME.get()).setDefaultValue(ConfigClient.ROTATED_STATION_NAME.getDefault()).build();
        TextListEntry textFooter = entryBuilder.startTextDescription(TextFormatter.FOOTER_LINK.apply(ConfigClient.FOOTERS.get(new Random().nextInt(ConfigClient.FOOTERS.size())))).build();
        builder.setSavingRunnable(() -> {
            if (DynamicTextureCache.instance != null) {
                DynamicTextureCache.instance.reload();
            }
            ConfigClient.USE_TIANJIN_METRO_FONT.set(booleanUseTianjinMetroFont.getValue());
            ConfigClient.ROTATED_STATION_NAME.set(booleanRotatedStationName.getValue());
            ConfigClient.writeToFile();
        });
        return new Screen(builder.build());
    }
}
