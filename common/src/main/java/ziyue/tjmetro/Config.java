package ziyue.tjmetro;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import me.shedaniel.clothconfig2.gui.entries.TextListEntry;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import mtr.CreativeModeTabs;
import mtr.Patreon;
import mtr.client.ClientData;
import mtr.data.RailwayData;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import ziyue.tjmetro.filters.Filter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @see Key
 * @since beta-1
 */

public class Config
{
    public static final Key<Boolean> ENABLE_MTR_FILTERS = new Key<>("enable_mtr_filters", true)
    {
        @Override
        public void set(Boolean value) {
            Filter.FILTERS.get(CreativeModeTabs.CORE.get().getId()).enabled = value;
            Filter.FILTERS.get(CreativeModeTabs.ESCALATORS_LIFTS.get().getId()).enabled = value;
            Filter.FILTERS.get(CreativeModeTabs.RAILWAY_FACILITIES.get().getId()).enabled = value;
            Filter.FILTERS.get(CreativeModeTabs.STATION_BUILDING_BLOCKS.get().getId()).enabled = value;
            super.set(value);
        }
    };
    public static final Key<Boolean> USE_TIANJIN_METRO_FONT = new Key<>("use_tianjin_metro_font", false);

    protected static final Path CONFIG_FILE_PATH = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve("tjmetro.json");
    public static final List<Supplier<MutableComponent>> FOOTERS = Arrays.asList(
            () -> IDrawingExtends.format(Text.translatable("footer.tjmetro.sources"), style -> IDrawingExtends.LINK_STYLE.apply(Reference.GITHUB_REPO).applyTo(style)),
            () -> IDrawingExtends.format(Text.translatable("footer.tjmetro.contributors"), style -> IDrawingExtends.LINK_STYLE.apply(Reference.CONTRIBUTORS).applyTo(style)),
            () -> IDrawingExtends.format(Text.translatable("footer.tjmetro.forum"), style -> IDrawingExtends.LINK_STYLE.apply(Reference.FORUM).applyTo(style).withColor(TextColor.fromRgb(15946322)))
    );

    public static Screen getConfigScreen() {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(Text.translatable("gui.tjmetro.options")).transparentBackground();
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        // Category: Minecraft Transit Railway
        ConfigCategory categoryMTR = builder.getOrCreateCategory(Text.translatable("config.category.mtr"));
        TextListEntry textHeader = entryBuilder.startTextDescription(Text.translatable("config.gui.mtr.experimental").withStyle(ChatFormatting.YELLOW)).build();
        BooleanListEntry booleanUseMTRFont = entryBuilder.startBooleanToggle(Text.translatable("options.mtr.use_mtr_font"), mtr.client.Config.useMTRFont()).build();
        BooleanListEntry booleanShowAnnouncementMessages = entryBuilder.startBooleanToggle(Text.translatable("options.mtr.show_announcement_messages"), mtr.client.Config.showAnnouncementMessages()).build();
        BooleanListEntry booleanUseTTSAnnouncements = entryBuilder.startBooleanToggle(Text.translatable("options.mtr.use_tts_announcements"), mtr.client.Config.useTTSAnnouncements()).build();
        BooleanListEntry booleanHideSpecialRailColors = entryBuilder.startBooleanToggle(Text.translatable("options.mtr.hide_special_rail_colors"), mtr.client.Config.hideSpecialRailColors()).build();
        BooleanListEntry booleanHideTranslucentParts = entryBuilder.startBooleanToggle(Text.translatable("options.mtr.hide_translucent_parts"), mtr.client.Config.hideTranslucentParts()).build();
        BooleanListEntry booleanShiftToToggleSitting = entryBuilder.startBooleanToggle(Text.translatable("options.mtr.shift_to_toggle_sitting", Minecraft.getInstance().options.keyShift.getTranslatedKeyMessage()), mtr.client.Config.shiftToToggleSitting()).build();
        EnumListEntry<LanguageOptions> enumLanguageOptions = entryBuilder.startEnumSelector(Text.translatable("options.mtr.language_options"), LanguageOptions.class, LanguageOptions.getEnum(mtr.client.Config.languageOptions())).build();
        BooleanListEntry booleanUseDynamicFPS = entryBuilder.startBooleanToggle(Text.translatable("options.mtr.use_dynamic_fps"), mtr.client.Config.useDynamicFPS()).build();
        IntegerSliderEntry sliderTrackTextureOffset = entryBuilder.startIntSlider(Text.translatable("options.mtr.track_texture_offset"), mtr.client.Config.trackTextureOffset(), 0, mtr.client.Config.TRACK_OFFSET_COUNT - 1).build();
        IntegerSliderEntry sliderDynamicTextureResolution = entryBuilder.startIntSlider(Text.translatable("options.mtr.dynamic_texture_resolution"), mtr.client.Config.dynamicTextureResolution(), 0, mtr.client.Config.DYNAMIC_RESOLUTION_COUNT - 1).build();
        IntegerSliderEntry sliderTrainRenderDistanceRatio = entryBuilder.startIntSlider(Text.translatable("options.mtr.vehicle_render_distance_ratio"), mtr.client.Config.trainRenderDistanceRatio(), 0, mtr.client.Config.TRAIN_RENDER_DISTANCE_RATIO_COUNT - 1).build();
        SubCategoryBuilder subCategorySupporter = entryBuilder.startSubCategory(Text.translatable("config.gui.mtr.supporters"));
        String tierTitle = "";
        List<SubCategoryBuilder> listSupporters = new ArrayList<>();
        SubCategoryBuilder current = null;
        for (final Patreon patreon : mtr.client.Config.PATREON_LIST) {
            if (!patreon.tierTitle.equals(tierTitle)) {
                final Component text = Text.literal(patreon.tierTitle);
                if (current != null) listSupporters.add(current);
                current = entryBuilder.startSubCategory(text);
            }
            tierTitle = patreon.tierTitle;
            final Component text = patreon.tierAmount < 1000 ? Text.translatable("options.mtr.anonymous") : Text.literal(patreon.name);
            current.add(entryBuilder.startTextDescription(Text.literal(text.getString()).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(patreon.tierColor)))).build());
        }
        listSupporters.add(current);
        for (final SubCategoryBuilder patreon : listSupporters) {
            subCategorySupporter.add(patreon.build());
        }
        TextListEntry textMTRFooter = entryBuilder.startTextDescription(Text.translatable("options.mtr.support_patreon").withStyle(IDrawingExtends.LINK_STYLE.apply("https://www.patreon.com/minecraft_transit_railway"))).build();
        categoryMTR.addEntry(textHeader).addEntry(booleanUseMTRFont).addEntry(booleanShowAnnouncementMessages).addEntry(booleanUseTTSAnnouncements).addEntry(booleanHideSpecialRailColors).addEntry(booleanHideTranslucentParts).addEntry(booleanShiftToToggleSitting).addEntry(enumLanguageOptions).addEntry(booleanUseDynamicFPS).addEntry(sliderTrackTextureOffset).addEntry(sliderDynamicTextureResolution).addEntry(sliderTrainRenderDistanceRatio).addEntry(subCategorySupporter.build()).addEntry(textMTRFooter);
        // Category: Tianjin Metro
        ConfigCategory categoryTianjinMetro = builder.getOrCreateCategory(Text.translatable("config.category.tjmetro"));
        BooleanListEntry booleanMTRFilters = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.enable_mtr_filters"), ENABLE_MTR_FILTERS.get()).setDefaultValue(ENABLE_MTR_FILTERS.getDefault()).build();
        BooleanListEntry booleanUseTianjinMetroFont = entryBuilder.startBooleanToggle(Text.translatable("config.tjmetro.use_tianjin_metro_font"), USE_TIANJIN_METRO_FONT.get()).setTooltip(Text.translatable("tooltip.tjmetro.use_tianjin_metro_font"), Text.translatable("tooltip.tjmetro.experimental").withStyle(ChatFormatting.YELLOW)).build();
        TextListEntry textFooter = entryBuilder.startTextDescription(FOOTERS.get(new Random().nextInt(FOOTERS.size())).get()).build();
        categoryTianjinMetro.addEntry(booleanMTRFilters).addEntry(booleanUseTianjinMetroFont).addEntry(textFooter);

        builder.setFallbackCategory(categoryTianjinMetro);
        builder.setSavingRunnable(() -> {
            mtr.client.Config.setUseMTRFont(booleanUseMTRFont.getValue());
            mtr.client.Config.setShowAnnouncementMessages(booleanShowAnnouncementMessages.getValue());
            mtr.client.Config.setUseTTSAnnouncements(booleanUseTTSAnnouncements.getValue());
            mtr.client.Config.setHideSpecialRailColors(booleanHideSpecialRailColors.getValue());
            mtr.client.Config.setHideTranslucentParts(booleanHideTranslucentParts.getValue());
            mtr.client.Config.setShiftToToggleSitting(booleanShiftToToggleSitting.getValue());
            mtr.client.Config.setLanguageOptions(enumLanguageOptions.getValue().id);
            mtr.client.Config.setUseDynamicFPS(booleanUseDynamicFPS.getValue());
            mtr.client.Config.setTrackTextureOffset(sliderTrackTextureOffset.getValue());
            mtr.client.Config.setDynamicTextureResolution(sliderDynamicTextureResolution.getValue());
            mtr.client.Config.setTrainRenderDistanceRatio(sliderTrainRenderDistanceRatio.getValue());
            ClientData.DATA_CACHE.sync();
            ClientData.DATA_CACHE.refreshDynamicResources();
            ClientData.SIGNAL_BLOCKS.writeCache();

            ENABLE_MTR_FILTERS.set(booleanMTRFilters.getValue());
            USE_TIANJIN_METRO_FONT.set(booleanUseTianjinMetroFont.getValue());
        });
        return builder.build();
    }

    public static void refreshProperties() {
        TianjinMetro.LOGGER.info("Refreshed Tianjin Metro config");
        try {
            final JsonObject jsonConfig = new JsonParser().parse(String.join("", Files.readAllLines(CONFIG_FILE_PATH))).getAsJsonObject();
            try {
                ENABLE_MTR_FILTERS.set(jsonConfig.get(ENABLE_MTR_FILTERS.getId()).getAsBoolean());
                USE_TIANJIN_METRO_FONT.set(jsonConfig.get(USE_TIANJIN_METRO_FONT.getId()).getAsBoolean());
            } catch (Exception ignored) {
                ENABLE_MTR_FILTERS.set(ENABLE_MTR_FILTERS.getDefault());
                USE_TIANJIN_METRO_FONT.set(USE_TIANJIN_METRO_FONT.getDefault());
            }
        } catch (Exception e) {
            writeToFile();
            TianjinMetro.LOGGER.error(e);
        }
    }

    protected static void writeToFile() {
        TianjinMetro.LOGGER.info("Wrote Tianjin Metro config to file");
        final JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty(ENABLE_MTR_FILTERS.getId(), ENABLE_MTR_FILTERS.get());
        jsonConfig.addProperty(USE_TIANJIN_METRO_FONT.getId(), USE_TIANJIN_METRO_FONT.get());

        try {
            Files.write(CONFIG_FILE_PATH, Collections.singleton(RailwayData.prettyPrint(jsonConfig)));
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e);
        }
    }

    /**
     * A class for settings.
     *
     * @author ZiYueCommentary
     * @see Config
     * @since beta-1
     */
    public static class Key<T>
    {
        protected T value;
        protected final T defaultValue;
        protected final String id;

        public Key(String id, T defaultValue) {
            this.id = id;
            this.value = defaultValue;
            this.defaultValue = defaultValue;
        }

        public String getId() {
            return id;
        }

        public T get() {
            return value;
        }

        public void set(T value) {
            this.value = value;
            writeToFile();
        }

        public T getDefault() {
            return defaultValue;
        }
    }

    public enum LanguageOptions
    {
        DEFAULT(0),
        CJK(1),
        NOT_CJK(2);

        public final int id;

        LanguageOptions(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return Text.translatable("options.mtr.language_options_" + id).getString();
        }

        public static LanguageOptions getEnum(int id) {
            return switch (id) {
                case 0 -> DEFAULT;
                case 1 -> CJK;
                case 2 -> NOT_CJK;
                default -> throw new AssertionError();
            };
        }
    }
}
