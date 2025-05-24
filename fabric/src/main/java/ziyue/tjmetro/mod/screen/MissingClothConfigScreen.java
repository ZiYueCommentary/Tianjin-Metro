package ziyue.tjmetro.mod.screen;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.client.IDrawing;
import ziyue.tjmetro.mod.TianjinMetro;

import static org.mtr.mod.data.IGui.*;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-1
 */

public class MissingClothConfigScreen extends ScreenExtension
{
    protected final Screen parent;
    protected final ButtonWidgetExtension buttonDownload;

    public MissingClothConfigScreen(Screen parent) {
        this.parent = parent;
        buttonDownload = new ButtonWidgetExtension(0, 0, 0, 20, button -> {
            Util.getOperatingSystem().open("https://modrinth.com/mod/cloth-config");
            this.onClose2();
        });
    }

    @Override
    protected void init2() {
        super.init2();
        this.addChild(new ClickableWidget(buttonDownload));
        IDrawing.setPositionAndWidth(buttonDownload, (width - 150) / 2, height / 2 + TEXT_PADDING, 150);

        buttonDownload.setMessage2(new Text(TextHelper.translatable("config.tjmetro.download_cloth_config").data));
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(graphicsHolder);
            graphicsHolder.drawCenteredText(TextHelper.translatable("config.tjmetro.cloth_config_not_found"), width / 2, height / 2 - TEXT_PADDING * 3, ARGB_WHITE);
            graphicsHolder.drawCenteredText(TextHelper.translatable("config.tjmetro.cloth_config_required"), width / 2, height / 2 - TEXT_PADDING, ARGB_WHITE);
            super.render(graphicsHolder, mouseX, mouseY, delta);
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void onClose2() {
        super.onClose2();
        MinecraftClient.getInstance().openScreen(parent);
    }
}
