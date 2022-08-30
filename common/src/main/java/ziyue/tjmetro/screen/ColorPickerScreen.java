package ziyue.tjmetro.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.screen.WidgetBetterCheckbox;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.StringUtils;
import ziyue.tjmetro.packet.PacketGuiClient;

import java.awt.*;

/**
 * Color Picker, based on <i>Widget Color Selector</i>.<br>
 * All methods are <b>protected</b>, different with <b>private</b> in <i>Widget Color Selector</i>.
 *
 * @author ZiYueCommentary
 * @see mtr.screen.WidgetColorSelector
 * @since 1.0b
 */

public class ColorPickerScreen extends ScreenMapper implements IGui, IPacket
{
    protected float hue;
    protected float saturation;
    protected float brightness;
    protected DraggingState draggingState = DraggingState.NONE;
    protected final BlockPos pos;
    protected final int oldColor;
    protected final WidgetBetterTextField textFieldColor;
    protected final WidgetBetterTextField textFieldRed;
    protected final WidgetBetterTextField textFieldGreen;
    protected final WidgetBetterTextField textFieldBlue;
    protected final WidgetBetterCheckbox defaultColorCheckBox;
    protected static final int RIGHT_WIDTH = 60;

    public ColorPickerScreen(BlockPos pos, int oldColor) {
        super(Text.literal(""));
        this.pos = pos;
        this.oldColor = oldColor;
        textFieldColor = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.HEX, Text.literal(Integer.toHexString(oldColor).toUpperCase()).getString(), 6);
        textFieldRed = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, Text.literal(String.valueOf((oldColor >> 16) & 0xFF)).getString(), 3);
        textFieldGreen = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, Text.literal(String.valueOf((oldColor >> 8) & 0xFF)).getString(), 3);
        textFieldBlue = new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.POSITIVE_INTEGER, Text.literal(String.valueOf(oldColor & 0xFF)).getString(), 3);
        defaultColorCheckBox = new WidgetBetterCheckbox(0, 0, 10, 100, Text.translatable("gui.tjmetro.default_color"), button -> textFieldBlue.active = false);
    }

    @Override
    protected void init() {
        super.init();
        final int startX = SQUARE_SIZE * 4 + getMainWidth() + 2;
        final int startY = SQUARE_SIZE + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING / 2 + 2;
        IDrawing.setPositionAndWidth(textFieldColor, startX + TEXT_FIELD_PADDING / 2, startY, RIGHT_WIDTH - TEXT_FIELD_PADDING);
        IDrawing.setPositionAndWidth(textFieldRed, startX + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, RIGHT_WIDTH - TEXT_FIELD_PADDING);
        IDrawing.setPositionAndWidth(textFieldGreen, startX + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE * 3 + TEXT_FIELD_PADDING * 2, RIGHT_WIDTH - TEXT_FIELD_PADDING);
        IDrawing.setPositionAndWidth(textFieldBlue, startX + TEXT_FIELD_PADDING / 2, startY + SQUARE_SIZE * 4 + TEXT_FIELD_PADDING * 3, RIGHT_WIDTH - TEXT_FIELD_PADDING);
        setHsb(oldColor, true);
        textFieldColor.setResponder(text -> textCallback(text, -1));
        textFieldRed.setResponder(text -> textCallback(text, 16));
        textFieldGreen.setResponder(text -> textCallback(text, 8));
        textFieldBlue.setResponder(text -> textCallback(text, 0));
        addDrawableChild(textFieldColor);
        addDrawableChild(textFieldRed);
        addDrawableChild(textFieldGreen);
        addDrawableChild(textFieldBlue);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(matrices);
            super.render(matrices, mouseX, mouseY, delta);
            final int mainWidth = getMainWidth();
            final int mainHeight = getMainHeight();
            drawCenteredString(matrices, font, Text.translatable("gui.mtr.color"), SQUARE_SIZE * 4 + mainWidth + RIGHT_WIDTH / 2, SQUARE_SIZE, ARGB_WHITE);
            drawCenteredString(matrices, font, "RGB", SQUARE_SIZE * 4 + mainWidth + RIGHT_WIDTH / 2, SQUARE_SIZE * 3 + TEXT_FIELD_PADDING, ARGB_WHITE);
            final Tesselator tesselator = Tesselator.getInstance();
            final BufferBuilder buffer = tesselator.getBuilder();
            UtilitiesClient.beginDrawingRectangle(buffer);
            final int selectedColor = Color.HSBtoRGB(hue, saturation, brightness);
            IDrawing.drawRectangle(buffer, SQUARE_SIZE * 4 + mainWidth + 3, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING * 4 + 1, SQUARE_SIZE * 4 + mainWidth + RIGHT_WIDTH + 1, mainHeight - 1, selectedColor);
            for (int drawHue = 0; drawHue < mainHeight; drawHue++) {
                final int color = Color.HSBtoRGB((float) drawHue / (mainHeight - 1), 1, 1);
                IDrawing.drawRectangle(buffer, SQUARE_SIZE * 2 + mainWidth, SQUARE_SIZE + drawHue, SQUARE_SIZE * 3 + mainWidth, SQUARE_SIZE + drawHue + 1, color);
            }
            for (int drawSaturation = 0; drawSaturation < mainWidth; drawSaturation++) {
                for (int drawBrightness = 0; drawBrightness < mainHeight; drawBrightness++) {
                    final int color = Color.HSBtoRGB(hue, (float) drawSaturation / (mainWidth - 1), (float) drawBrightness / (mainHeight - 1));
                    IDrawing.drawRectangle(buffer, SQUARE_SIZE + drawSaturation, SQUARE_SIZE + mainHeight - drawBrightness - 1, SQUARE_SIZE + drawSaturation + 1, SQUARE_SIZE + mainHeight - drawBrightness, color);
                }
            }
            final int selectedHueInt = Math.round(hue * (mainHeight - 1));
            final int selectedSaturationInt = Math.round(saturation * (mainWidth - 1));
            final int selectedBrightnessInt = Math.round(brightness * (mainHeight - 1));
            IDrawing.drawRectangle(buffer, SQUARE_SIZE * 2 + mainWidth, SQUARE_SIZE + selectedHueInt - 1, SQUARE_SIZE * 3 + mainWidth, SQUARE_SIZE + selectedHueInt + 2, ARGB_BLACK);
            IDrawing.drawRectangle(buffer, SQUARE_SIZE * 2 + mainWidth, SQUARE_SIZE + selectedHueInt, SQUARE_SIZE * 3 + mainWidth, SQUARE_SIZE + selectedHueInt + 1, ARGB_WHITE);
            IDrawing.drawRectangle(buffer, SQUARE_SIZE + selectedSaturationInt - 1, SQUARE_SIZE + mainHeight - selectedBrightnessInt - 1, SQUARE_SIZE + selectedSaturationInt + 2, SQUARE_SIZE + mainHeight - selectedBrightnessInt, ARGB_BLACK);
            IDrawing.drawRectangle(buffer, SQUARE_SIZE + selectedSaturationInt, SQUARE_SIZE + mainHeight - selectedBrightnessInt - 2, SQUARE_SIZE + selectedSaturationInt + 1, SQUARE_SIZE + mainHeight - selectedBrightnessInt + 1, ARGB_BLACK);
            IDrawing.drawRectangle(buffer, SQUARE_SIZE + selectedSaturationInt, SQUARE_SIZE + mainHeight - selectedBrightnessInt - 1, SQUARE_SIZE + selectedSaturationInt + 1, SQUARE_SIZE + mainHeight - selectedBrightnessInt, ARGB_WHITE);
            tesselator.end();
            UtilitiesClient.finishDrawingRectangle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tick() {
        textFieldRed.tick();
        textFieldGreen.tick();
        textFieldBlue.tick();
        textFieldColor.tick();
    }

    @Override
    public void onClose() {
        PacketGuiClient.sendCustomColorC2S(pos, Color.HSBtoRGB(hue, saturation, brightness) & RGB_WHITE);
        super.onClose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        final int mainWidth = getMainWidth();
        final int mainHeight = getMainHeight();
        draggingState = DraggingState.NONE;
        if (mouseY >= SQUARE_SIZE && mouseY < SQUARE_SIZE + mainHeight) {
            if (mouseX >= SQUARE_SIZE && mouseX < SQUARE_SIZE + mainWidth) {
                draggingState = DraggingState.SATURATION_BRIGHTNESS;
            } else if (mouseX >= SQUARE_SIZE * 2 + mainWidth && mouseX < SQUARE_SIZE * 3 + mainWidth) {
                draggingState = DraggingState.HUE;
            }
        }
        selectColor(mouseX, mouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        selectColor(mouseX, mouseY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    protected void selectColor(double mouseX, double mouseY) {
        final int mainWidth = getMainWidth();
        final int mainHeight = getMainHeight();
        switch (draggingState) {
            case SATURATION_BRIGHTNESS:
                saturation = (float) Mth.clamp((mouseX - SQUARE_SIZE) / mainWidth, 0, 1);
                brightness = 1 - (float) Mth.clamp((mouseY - SQUARE_SIZE) / mainHeight, 0, 1);
                setColorText(Color.HSBtoRGB(hue, saturation, brightness), true);
                break;
            case HUE:
                hue = (float) Mth.clamp((mouseY - SQUARE_SIZE) / mainHeight, 0, 1);
                setColorText(Color.HSBtoRGB(hue, saturation, brightness), true);
                break;
        }
    }

    protected void setHsb(int color, boolean padZero) {
        final float[] hsb = Color.RGBtoHSB((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, null);
        hue = hsb[0];
        saturation = hsb[1];
        brightness = hsb[2];
        setColorText(color, padZero);
    }

    protected void setColorText(int color, boolean padZero) {
        final String colorString = Integer.toHexString(color & RGB_WHITE).toUpperCase();
        textFieldColor.setValue(padZero ? StringUtils.leftPad(colorString, 6, "0") : colorString);
        textFieldRed.setValue(String.valueOf((color >> 16) & 0xFF));
        textFieldGreen.setValue(String.valueOf((color >> 8) & 0xFF));
        textFieldBlue.setValue(String.valueOf(color & 0xFF));
    }

    protected void textCallback(String text, int shift) {
        try {
            final boolean isHex = shift < 0;
            final int compare = Integer.parseInt(text, isHex ? 16 : 10);
            final int currentColor = Color.HSBtoRGB(hue, saturation, brightness) & RGB_WHITE;
            if ((isHex ? currentColor : ((currentColor >> shift) & 0xFF)) != compare) {
                setHsb(isHex ? compare : (currentColor & ~(0xFF << shift)) + (compare << shift), !isHex);
            }
        } catch (Exception ignored) {
        }
    }

    protected int getMainWidth() {
        return width - SQUARE_SIZE * 5 - RIGHT_WIDTH;
    }

    protected int getMainHeight() {
        return height - SQUARE_SIZE * 2;
    }

    protected enum DraggingState
    {
        NONE, SATURATION_BRIGHTNESS, HUE
    }
}