package ziyue.tjmetro.mod.screen;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import ziyue.tjmetro.mod.RegistryClient;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.packet.PacketUpdateRoadblockContent;

/**
 * Custom content to display for Custom Content Block.
 *
 * @author ZiYueCommentary
 * @see PacketUpdateRoadblockContent
 * @since 1.0.0-beta-1
 */

public class RoadblockContentScreen extends ScreenExtension implements IGui
{
    protected final BlockPos pos;
    protected final TextFieldWidgetExtension textField;
    protected String content;
    private static final int MAX_MESSAGE_LENGTH = 256;

    public RoadblockContentScreen(BlockPos pos, String content) {
        this.pos = pos;
        this.content = content;
        textField = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, null);
    }

    @Override
    protected void init2() {
        super.init2();
        this.addChild(new ClickableWidget(textField));
        IDrawing.setPositionAndWidth(textField, SQUARE_SIZE, SQUARE_SIZE, width); // Why clamp?
        textField.setText2(content);
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(graphicsHolder);
            graphicsHolder.drawText(TextHelper.translatable("gui.tjmetro.custom_content"), SQUARE_SIZE, TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
            super.render(graphicsHolder, mouseX, mouseY, delta);
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }
    }

    @Override
    public void tick2() {
        textField.tick2();
        super.tick2();
    }

    @Override
    public void onClose2() {
        RegistryClient.sendPacketToServer(new PacketUpdateRoadblockContent(pos, textField.getText2()));
        super.onClose2();
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }
}
