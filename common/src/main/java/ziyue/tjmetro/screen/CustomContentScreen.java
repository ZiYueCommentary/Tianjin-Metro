package ziyue.tjmetro.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.IPacket;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import ziyue.tjmetro.block.base.BlockCustomContentBlockBase;
import ziyue.tjmetro.packet.PacketGuiClient;

/**
 * Custom content to display for <b>Custom Content Block</b>.
 *
 * @author ZiYueCommentary
 * @see BlockCustomContentBlockBase
 * @since beta-1
 */

public class CustomContentScreen extends ScreenMapper implements IGui, IPacket
{
    protected final BlockPos pos;
    protected final WidgetBetterTextField textField = new WidgetBetterTextField(null);
    protected String content;

    public CustomContentScreen(BlockPos pos) {
        super(Text.literal(""));
        this.pos = pos;
        minecraft = Minecraft.getInstance();
        final ClientLevel world = minecraft.level;
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof BlockCustomContentBlockBase.CustomContentBlockEntity entity1) {
            content = entity1.content;
        }
        textField.setValue(content);
    }

    @Override
    protected void init() {
        super.init();
        IDrawing.setPositionAndWidth(textField, SQUARE_SIZE, SQUARE_SIZE, width);
        addDrawableChild(textField);
        textField.setValue(textField.getValue());
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        font.draw(matrices, Text.translatable("gui.tjmetro.custom_content"), SQUARE_SIZE, TEXT_PADDING, ARGB_WHITE);
        textField.setWidth(width - (SQUARE_SIZE * 2));
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        textField.tick();
    }

    @Override
    public void onClose() {
        PacketGuiClient.sendCustomContentC2S(pos, textField.getValue());
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
