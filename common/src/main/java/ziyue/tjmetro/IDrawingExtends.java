package ziyue.tjmetro;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.IGui;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * @see mtr.client.IDrawing
 * @since 1.0b
 */

public interface IDrawingExtends
{
    /**
     * Drawing string with minecraft font.
     *
     * @author ZiYueCommentary
     * @since 1.0b
     */
    static void drawString(PoseStack matrices, Font textRenderer, MultiBufferSource.BufferSource immediate, String text, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, IGui.HorizontalAlignment xAlignment, float x, float y, float maxWidth, float maxHeight, float scale, int textColor, boolean shadow, int light, mtr.client.IDrawing.DrawingCallback drawingCallback) {
        while (text.contains("||")) {
            text = text.replace("||", "|");
        }
        final String[] stringSplit = text.split("\\|");

        final List<Boolean> isCJKList = new ArrayList<>();
        final List<FormattedCharSequence> orderedTexts = new ArrayList<>();
        int totalHeight = 0, totalWidth = 0;
        for (final String stringSplitPart : stringSplit) {
            final boolean isCJK = IGui.isCjk(stringSplitPart);
            isCJKList.add(isCJK);

            final FormattedCharSequence orderedText = Text.literal(stringSplitPart).getVisualOrderText();
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

            final float shade = light == IGui.MAX_LIGHT_GLOWING ? 1 : Math.min(LightTexture.block(light) / 16F * 0.1F + 0.7F, 1);
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

            offset += IGui.LINE_HEIGHT * extraScale;
        }

        matrices.popPose();

        if (drawingCallback != null) {
            final float x1 = xAlignment.getOffset(x, totalWidthScaled / scale);
            final float y1 = verticalAlignment.getOffset(y, totalHeight / scale);
            drawingCallback.drawingCallback(x1, y1, x1 + totalWidthScaled / scale, y1 + totalHeight / scale);
        }
    }
}
