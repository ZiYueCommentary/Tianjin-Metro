package ziyue.tjmetro.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.packet.IPacket;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;
import ziyue.tjmetro.blocks.BlockStationNameSignBase;
import ziyue.tjmetro.packet.PacketGuiClient;

/**
 * Custom content to display for <b>Station Name Sign Block</b>.
 * @author ZiYueCommentary
 * @since 1.0b
 * @see ziyue.tjmetro.blocks.BlockStationNameSignBase
 * @see ziyue.tjmetro.render.RenderStationNameSign
 */

public class CustomContentScreen extends ScreenMapper implements IGui, IPacket
{
    protected final BlockPos pos;
    protected final WidgetBetterTextField textField = new WidgetBetterTextField(null, "");
    protected String content;

    public CustomContentScreen(BlockPos pos) {
        super(new TextComponent(""));
        this.pos = pos;

        minecraft = Minecraft.getInstance();
        final ClientLevel world = minecraft.level;
        if (world != null) {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof BlockStationNameSignBase.TileEntityStationNameWall)
                content = ((BlockStationNameSignBase.TileEntityStationNameWall) entity).content;
        } else {
            content = "";
        }
        textField.setValue(content);
    }

    @Override
    protected void init() {
        super.init();
        addDrawableChild(textField);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(matrices);
            font.draw(matrices, new TranslatableComponent("gui.tjmetro.custom_content"), SQUARE_SIZE, TEXT_PADDING, ARGB_WHITE);
            IDrawing.setPositionAndWidth(textField, SQUARE_SIZE, SQUARE_SIZE, width);
            textField.setValue(textField.getValue());
            super.render(matrices, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        textField.tick();
    }

    @Override
    public void onClose() {
        PacketGuiClient.sendTrainSensorC2S(pos, textField.getValue());
        if(!textField.getValue().equals("")) ziyue.tjmetro.Main.LOGGER.info("Use custom content!");
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
