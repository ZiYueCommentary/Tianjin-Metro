package ziyue.tjmetro.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;

import java.util.Random;

import static ziyue.tjmetro.client.Config.*;

public class ConfigScreen
{
    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.translatable("gui.tjmetro.options")).transparentBackground();
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory categoryTianjinMetro = builder.getOrCreateCategory(Text.translatable("config.category.tjmetro"));
        BooleanListEntry booleanMTRFilters = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.enable_mtr_filters"), ENABLE_MTR_FILTERS.get()).setDefaultValue(ENABLE_MTR_FILTERS.getDefault()).build();
        BooleanListEntry booleanUseTianjinMetroFont = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.use_tianjin_metro_font"), USE_TIANJIN_METRO_FONT.get()).setTooltip(Text.translatable("tooltip.tjmetro.use_tianjin_metro_font"), Text.translatable("tooltip.tjmetro.experimental").withStyle(ChatFormatting.YELLOW)).build();
        TextListEntry textFooter = entryBuilder.startTextDescription(FOOTERS.get(new Random().nextInt(FOOTERS.size())).get()).build();
        categoryTianjinMetro.addEntry(booleanMTRFilters).addEntry(booleanUseTianjinMetroFont).addEntry(textFooter);

        builder.setSavingRunnable(() -> {
            ENABLE_MTR_FILTERS.set(booleanMTRFilters.getValue());
            USE_TIANJIN_METRO_FONT.set(booleanUseTianjinMetroFont.getValue());
            ClientCache.DATA_CACHE.resetFonts();
        });
        return builder.build();
    }
}
