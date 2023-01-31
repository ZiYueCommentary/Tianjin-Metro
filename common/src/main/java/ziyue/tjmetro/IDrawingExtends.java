package ziyue.tjmetro;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.IGui;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

/**
 * Some methods similar to methods in <b>IDrawing</b>.
 *
 * @see mtr.client.IDrawing
 * @since beta-1
 */

public interface IDrawingExtends
{
    /**
     * Drawing string with minecraft font.
     *
     * @author ZiYueCommentary
     * @since beta-1
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

    /**
     * Get multi-line components by split a component.
     *
     * @param component A string that wait for split.
     * @param regex     the delimiting regular expression
     * @param limit     the result threshold, as described above
     * @return Multi-line component
     * @author ZiYueCommentary
     * @since beta-1
     */
    static List<MutableComponent> getComponentLines(MutableComponent component, String regex, int limit) {
        String[] lines = component.getString().split(regex, limit);
        ArrayList<MutableComponent> components = new ArrayList<>();
        for (String line : lines) components.add(Text.literal(line).withStyle(component.getStyle()));
        return components;
    }

    /**
     * @author ZiYueCommentary
     * @see #getComponentLines(MutableComponent, String, int)
     * @since beta-1
     */
    static List<MutableComponent> getComponentLines(MutableComponent component, String regex) {
        return getComponentLines(component, regex, 0);
    }

    /**
     * @author ZiYueCommentary
     * @see #getComponentLines(MutableComponent, String, int)
     * @since beta-1
     */
    static List<MutableComponent> getComponentLines(MutableComponent component) {
        return getComponentLines(component, "\n", 0);
    }

    /**
     * Add a <b>hold shift tooltip</b> to a tooltip, no need to re-assignment.
     *
     * @param list      Hover Text List, just like pointer in C/C++.
     * @param component A component that wait for split.
     * @param regex     the delimiting regular expression
     * @param limit     the result threshold, as described above
     * @return Hover Text List
     * @author ZiYueCommentary
     * @since beta-1
     */
    static List<Component> addHoldShiftTooltip(List<Component> list, MutableComponent component, boolean wordWarp, String regex, int limit) {
        if (Screen.hasShiftDown()) {
            if (wordWarp)
                getComponentLines(component, regex, limit).forEach(component1 -> Minecraft.getInstance().font.getSplitter().splitLines(component1, 150, component.getStyle()).forEach(component2 -> list.add(Text.literal(component2.getString()).withStyle(component.getStyle()))));
            else
                list.addAll(getComponentLines(component, regex, limit));
        } else {
            list.add(Text.translatable("tooltip.tjmetro.shift").withStyle(ChatFormatting.YELLOW));
        }
        return list;
    }

    /**
     * @author ZiYueCommentary
     * @see #addHoldShiftTooltip(List, MutableComponent, boolean, String, int)
     * @since beta-1
     */
    static List<Component> addHoldShiftTooltip(List<Component> list, MutableComponent component, boolean wordWarp, String regex) {
        return addHoldShiftTooltip(list, component, wordWarp, regex, 0);
    }

    /**
     * @author ZiYueCommentary
     * @see #addHoldShiftTooltip(List, MutableComponent, boolean, String, int)
     * @since beta-1
     */
    static List<Component> addHoldShiftTooltip(List<Component> list, MutableComponent component, boolean wordWarp) {
        return addHoldShiftTooltip(list, component, wordWarp, "\n", 0);
    }
}
