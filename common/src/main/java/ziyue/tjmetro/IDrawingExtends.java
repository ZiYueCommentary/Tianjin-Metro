package ziyue.tjmetro;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.MTR;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Some methods similar to methods in <b>IDrawing</b>.
 *
 * @see mtr.client.IDrawing
 * @since beta-1
 */

public interface IDrawingExtends
{
    /**
     * Get the link style.
     *
     * @since beta-1
     */
    Function<String, Style> LINK_STYLE = link -> Style.EMPTY.withUnderlined(true).withColor(ChatFormatting.BLUE).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(link)));

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

    /**
     * Get multiple lines components by split a component.
     *
     * @param component a string that wait for split
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
     * @param list      hover Text List, just like pointer in C/C++
     * @param component a component that wait for split
     * @param regex     the delimiting regular expression
     * @param limit     the result threshold, as described above
     * @return hover text list
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

    /**
     * Formatting the component with specific styles.
     * A valid format-able string should like this: {@code Hello! <1>This is an example!</>}
     *
     * @author ZiYueCommentary
     * @since beta-1
     */
    static MutableComponent format(Component component, Style... styles) {
        Style componentStyle = component.getStyle();
        MutableComponent result = new TextComponent("");
        String contents = component.getString();
        int firstTagBegin = contents.indexOf('<');
        while (firstTagBegin != -1) {
            if (firstTagBegin != 0) {
                result.append(Text.literal(contents.substring(0, firstTagBegin)).withStyle(componentStyle));
                contents = contents.substring(firstTagBegin);
            } else {
                int firstTagEnd = contents.indexOf('>');
                int tagId = Integer.parseInt(contents.substring(firstTagBegin + 1, firstTagEnd));
                contents = contents.substring(firstTagEnd + 1);
                int lastTagBegin = contents.indexOf("</>");
                result.append(Text.literal(contents.substring(0, lastTagBegin)).withStyle(styles[tagId - 1]));
                contents = contents.substring(lastTagBegin + 3);
            }
            firstTagBegin = contents.indexOf('<');
        }
        result.append(Text.literal(contents).withStyle(componentStyle));
        return result;
    }

    /**
     * @author ZiYueCommentary
     * @see #format(Component, Style...)
     * @since beta-1
     */
    @SafeVarargs
    static MutableComponent format(Component component, UnaryOperator<Style>... styles) {
        List<Style> styleList = new ArrayList<>();
        Style[] styles1 = new Style[styles.length];
        Arrays.stream(styles).forEach(style1 -> styleList.add(style1.apply(component.getStyle())));
        styleList.toArray(styles1);
        return format(component, styles1);
    }
}
