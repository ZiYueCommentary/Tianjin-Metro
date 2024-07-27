package ziyue.tjmetro.mod.screen;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.client.IDrawing;
import ziyue.tjmetro.mod.config.ConfigClient;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.client.DynamicTextureCache;

import java.util.Random;

import static org.mtr.mod.data.IGui.*;

public class ConfigClientScreen extends ScreenExtension
{
    protected final Screen parent;
    protected final ButtonWidgetExtension buttonEnableMTRFilters;
    protected final ButtonWidgetExtension buttonTianjinMetroFont;
    protected final ButtonWidgetExtension buttonFooterLink;
    protected final ButtonWidgetExtension buttonDone;
    protected final ConfigClient.Footer footer;
    protected static final int BUTTON_WIDTH = 60;
    protected static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

    public ConfigClientScreen(Screen parent) {
        this.parent = parent;
        this.footer = ConfigClient.FOOTERS.get(new Random().nextInt(ConfigClient.FOOTERS.size()));
        buttonEnableMTRFilters = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
            ConfigClient.ENABLE_MTR_FILTERS.set(!ConfigClient.ENABLE_MTR_FILTERS.get());
            setButtonText(button, ConfigClient.ENABLE_MTR_FILTERS.get());
        });
        buttonTianjinMetroFont = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
            ConfigClient.USE_TIANJIN_METRO_FONT.set(!ConfigClient.USE_TIANJIN_METRO_FONT.get());
            setButtonText(button, ConfigClient.USE_TIANJIN_METRO_FONT.get());
        });
        buttonFooterLink = new ButtonWidgetExtension(0, 0, 0, BUTTON_HEIGHT, button -> Util.getOperatingSystem().open(footer.link()));
        buttonDone = new ButtonWidgetExtension(0, 0, 0, 20, button -> this.onClose2());
    }

    @Override
    protected void init2() {
        super.init2();
        //this.addChild(new ClickableWidget(buttonEnableMTRFilters));
        this.addChild(new ClickableWidget(buttonTianjinMetroFont));
        this.addChild(new ClickableWidget(buttonFooterLink));
        this.addChild(new ClickableWidget(buttonDone));
        int i = 0;
        IDrawing.setPositionAndWidth(buttonEnableMTRFilters, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
        IDrawing.setPositionAndWidth(buttonTianjinMetroFont, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);
        i++;
        IDrawing.setPositionAndWidth(buttonFooterLink, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (i++) + SQUARE_SIZE, BUTTON_WIDTH);

        IDrawing.setPositionAndWidth(buttonDone, (width - 150) / 2, height - 20 - SQUARE_SIZE, 150);

        setButtonText(new ButtonWidget(buttonEnableMTRFilters), ConfigClient.ENABLE_MTR_FILTERS.get());
        setButtonText(new ButtonWidget(buttonTianjinMetroFont), ConfigClient.USE_TIANJIN_METRO_FONT.get());
        buttonFooterLink.setMessage2(new Text(TextHelper.translatable("button.tjmetro.click_here").data));
        buttonDone.setMessage2(new Text(TextHelper.translatable("gui.done").data));
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(graphicsHolder);
            graphicsHolder.drawCenteredText(TextHelper.translatable("button.tjmetro.tianjin_metro_options"), width / 2, TEXT_PADDING, ARGB_WHITE);
            final int yStart1 = SQUARE_SIZE + TEXT_PADDING / 2;

            int i = 0;
            i++;//graphicsHolder.drawText(TextHelper.translatable("config.tjmetro.enable_mtr_filters"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
            graphicsHolder.drawText(TextHelper.translatable("config.tjmetro.use_tianjin_metro_font"), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
            i++;
            graphicsHolder.drawText(this.footer.footer().get(), SQUARE_SIZE, BUTTON_HEIGHT * (i++) + yStart1, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
            super.render(graphicsHolder, mouseX, mouseY, delta);
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void onClose2() {
        DynamicTextureCache.instance.reload();
        ConfigClient.refreshProperties();
        super.onClose2();
        MinecraftClient.getInstance().openScreen(parent);
    }

    protected static void setButtonText(ButtonWidget button, boolean state) {
        button.setMessage(state ? new Text(TextHelper.translatable("options.mtr.on").data) : new Text(TextHelper.translatable("options.mtr.off").data));
    }
}
