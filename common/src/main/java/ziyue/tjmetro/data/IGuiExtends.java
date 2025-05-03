package ziyue.tjmetro.data;

import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Some methods similar to methods in <b>IGui</b>.
 *
 * @see mtr.data.IGui
 * @since beta-1
 */

public interface IGuiExtends
{
    /**
     * Get the link style.
     *
     * @since beta-1
     */
    Function<String, Style> LINK_STYLE = link -> Style.EMPTY.withUnderlined(true).withColor(ChatFormatting.BLUE).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(link)));

    /**
     * Remains the language string that the language option specified.
     *
     * @return filtered string
     * @author ZiYueCommentary
     * @see mtr.client.Config#languageOptions()
     * @since beta-1
     */
    static String filterLanguage(String text) {
        final StringBuilder noCommentString = new StringBuilder(text);
        final int commentIndex = noCommentString.indexOf("||");
        final int separatorIndex = noCommentString.indexOf("|");
        if (commentIndex != -1)
            noCommentString.delete(commentIndex, noCommentString.length());
        if (separatorIndex != -1) {
            switch (mtr.client.Config.languageOptions()) {
                case 1 -> noCommentString.delete(separatorIndex, noCommentString.length());
                case 2 -> noCommentString.delete(0, separatorIndex);
            }
        }
        return noCommentString.toString();
    }

    /**
     * Returns the language string that the language option specified.
     *
     * @param keyCJK key of CJK string
     * @param key    key of English string
     * @return filtered string
     * @author ZiYueCommentary
     * @see mtr.client.Config#languageOptions()
     * @since beta-1
     */
    static String mergeTranslation(String keyCJK, String key) {
        return switch (mtr.client.Config.languageOptions()) {
            case 1 -> Text.translatable(keyCJK).getString();
            case 2 -> Text.translatable(key).getString();
            default -> Text.translatable(keyCJK).getString() + "|" + Text.translatable(key).getString();
        };
    }

    /**
     * Splits the translation string into English and CJK. This function requires there is only a "|" separator.
     *
     * @param text the translation string
     * @return a tuple of CJK (A) and English (B)
     * @author ZiYueCommentary
     * @since beta-1
     */
    static Tuple<String, String> splitTranslation(String text) {
        final int separatorIndex = text.lastIndexOf("|");
        return new Tuple<>(text.substring(0, separatorIndex), text.substring(separatorIndex + 1));
    }

    /**
     * Get multiple lines components by split a component.
     *
     * @param component a string that wait for split
     * @param regex     the delimiting regular expression
     * @param limit     the result threshold, as described above
     * @return Multiple line component
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
     * A simple formatter for formatting the component with specific styles. Not compatible with tag-nesting.
     * A valid format-able string should be like this: {@code Hello! <1>This is an example!</>}
     *
     * @author ZiYueCommentary
     * @since beta-1
     */
    static MutableComponent format(Component component, Style... styles) {
        Style componentStyle = component.getStyle();
        MutableComponent result = Component.empty();
        StringBuilder contents = new StringBuilder(component.getString());
        int firstTagBegin = contents.indexOf("<");
        while (firstTagBegin != -1) {
            if (firstTagBegin != 0) {
                result.append(Text.literal(contents.substring(0, firstTagBegin)).withStyle(componentStyle));
                contents.delete(0, firstTagBegin);
            } else {
                int firstTagEnd = contents.indexOf(">");
                int tagId = Integer.parseInt(contents.substring(firstTagBegin + 1, firstTagEnd));
                contents.delete(0, firstTagEnd + 1);
                int lastTagBegin = contents.indexOf("</>");
                result.append(Text.literal(contents.substring(0, lastTagBegin)).withStyle(styles[tagId - 1]));
                contents.delete(0, lastTagBegin + 3);
            }
            firstTagBegin = contents.indexOf("<");
        }
        result.append(Text.literal(contents.toString()).withStyle(componentStyle));
        return result;
    }

    /**
     * @author ZiYueCommentary
     * @see #format(Component, Style...)
     * @since beta-1
     * @deprecated
     */
    @Deprecated
    @SafeVarargs
    static MutableComponent format(Component component, UnaryOperator<Style>... styles) {
        List<Style> styleList = new ArrayList<>();
        Style[] styles1 = new Style[styles.length];
        Arrays.stream(styles).forEach(style1 -> styleList.add(style1.apply(component.getStyle())));
        styleList.toArray(styles1);
        return format(component, styles1);
    }
}
