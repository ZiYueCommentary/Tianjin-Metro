package ziyue.tjmetro.client;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.MTR;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.Text;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import ziyue.tjmetro.data.IGuiExtends;
import ziyue.tjmetro.Reference;

import java.util.ArrayList;
import java.util.List;

import static mtr.data.IGui.MAX_LIGHT_GLOWING;

/**
 * Some methods similar to methods in <b>IDrawing</b>.
 *
 * @see mtr.client.IDrawing
 * @since beta-1
 */

public interface IDrawingExtends
{
    /**
     * Drawing string with Tianjin Metro Font.
     *
     * @author ZiYueCommentary
     * @since beta-1
     */
    static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, boolean useMinecraftFont, IDrawing.DrawingCallback drawingCallback) {
        final Style style;
        if (useMinecraftFont) {
            style = Style.EMPTY;
        } else if (Config.USE_TIANJIN_METRO_FONT.get()) {
            style = Style.EMPTY.withFont(new ResourceLocation(Reference.MOD_ID, "tjmetro"));
            y += 0.05f;
        } else if (mtr.client.Config.useMTRFont()) {
            style = Style.EMPTY.withFont(new ResourceLocation(MTR.MOD_ID, "mtr"));
        } else {
            style = Style.EMPTY;
        }

        text = IGuiExtends.filterLanguage(text);
        final String[] stringSplit = text.split("\\|");

        final List<Boolean> isCJKList = new ArrayList<>();
        final List<FormattedCharSequence> orderedTexts = new ArrayList<>();
        int totalHeight = 0, totalWidth = 0;
        for (final String stringSplitPart : stringSplit) {
            final boolean isCJK = IGui.isCjk(stringSplitPart);
            isCJKList.add(isCJK);

            final FormattedCharSequence orderedText = Text.literal(stringSplitPart).withStyle(style).getVisualOrderText();
            orderedTexts.add(orderedText);

            totalHeight += IGui.LINE_HEIGHT * (isCJK ? 2 : 1);
            final int width = textRenderer.width(orderedText) * (isCJK ? 2 : 1);
            if (width > totalWidth) {
                totalWidth = width;
            }
        }

        if (maxHeight >= 0 && totalHeight / scale > maxHeight) {
            scale = totalHeight / maxHeight;
        }

        matrices.pushPose();

        final float totalWidthScaled;
        final float scaleX;
        if (maxWidth >= 0 && totalWidth > maxWidth * scale) {
            totalWidthScaled = maxWidth * scale;
            scaleX = totalWidth / maxWidth;
        } else {
            totalWidthScaled = totalWidth;
            scaleX = scale;
        }
        matrices.scale(1 / scaleX, 1 / scale, 1 / scale);

        float offset = verticalAlignment.getOffset(y * scale, totalHeight);
        for (int i = 0; i < orderedTexts.size(); i++) {
            final boolean isCJK = isCJKList.get(i);
            final int extraScale = isCJK ? 2 : 1;
            if (isCJK) {
                matrices.pushPose();
                matrices.scale(extraScale, extraScale, 1);
            }

            final float xOffset = horizontalAlignment.getOffset(xAlignment.getOffset(x * scaleX, totalWidth), textRenderer.width(orderedTexts.get(i)) * extraScale - totalWidth);

            final float shade = light == MAX_LIGHT_GLOWING ? 1 : Math.min(LightTexture.block(light) / 16F * 0.1F + 0.7F, 1);
            final int a = (textColor >> 24) & 0xFF;
            final int r = (int) (((textColor >> 16) & 0xFF) * shade);
            final int g = (int) (((textColor >> 8) & 0xFF) * shade);
            final int b = (int) ((textColor & 0xFF) * shade);

            if (immediate != null) {
                textRenderer.drawInBatch(orderedTexts.get(i), xOffset / extraScale, offset / extraScale, (a << 24) + (r << 16) + (g << 8) + b, shadow, matrices.last().pose(), immediate, false, 0, light);
            }

            if (isCJK) {
                matrices.popPose();
            }

            offset += (float) (IGui.LINE_HEIGHT * (Config.USE_TIANJIN_METRO_FONT.get() ? 0.8 : 1) * extraScale);
        }

        matrices.popPose();

        if (drawingCallback != null) {
            final float x1 = xAlignment.getOffset(x, totalWidthScaled / scale);
            final float y1 = verticalAlignment.getOffset(y, totalHeight / scale);
            drawingCallback.drawingCallback(x1, y1, x1 + totalWidthScaled / scale, y1 + totalHeight / scale);
        }
    }

    /**
     * @see #drawStringWithFont(PoseStack, Font, MultiBufferSource.BufferSource, String, IGui.HorizontalAlignment, IGui.VerticalAlignment, IGui.HorizontalAlignment, float, float, float, float, float, int, boolean, int, boolean, IDrawing.DrawingCallback)
     */
    static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, float x, float y, int light) {
        drawStringWithFont(matrices, textRenderer, immediate, text, IGui.HorizontalAlignment.CENTER, IGui.VerticalAlignment.CENTER, x, y, -1.0F, -1.0F, 1.0F, -1, true, light, null);
    }

    /**
     * @see #drawStringWithFont(PoseStack, Font, MultiBufferSource.BufferSource, String, IGui.HorizontalAlignment, IGui.VerticalAlignment, IGui.HorizontalAlignment, float, float, float, float, float, int, boolean, int, boolean, IDrawing.DrawingCallback)
     */
    static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, IDrawing.DrawingCallback drawingCallback) {
        drawStringWithFont(matrices, textRenderer, immediate, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, maxWidth, maxHeight, scale, textColor, shadow, light, drawingCallback);
    }

    /**
     * @see #drawStringWithFont(PoseStack, Font, MultiBufferSource.BufferSource, String, IGui.HorizontalAlignment, IGui.VerticalAlignment, IGui.HorizontalAlignment, float, float, float, float, float, int, boolean, int, boolean, IDrawing.DrawingCallback)
     */
    static void drawStringWithFont(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, IDrawing.DrawingCallback drawingCallback) {
        drawStringWithFont(matrices, textRenderer, immediate, text, horizontalAlignment, verticalAlignment, xAlignment, x, y, maxWidth, maxHeight, scale, textColor, shadow, light, false, drawingCallback);
    }
}
