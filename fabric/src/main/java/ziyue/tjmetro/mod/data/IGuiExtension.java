package ziyue.tjmetro.mod.data;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.holder.TextFormatting;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Some methods similar to methods in IGui.
 *
 * @see org.mtr.mod.data.IGui
 * @since 1.0.0-beta-1
 */

public interface IGuiExtension
{
    /**
     * Remains the language string that the language option specified.
     *
     * @return filtered string
     * @author ZiYueCommentary
     * @see org.mtr.mod.config.LanguageDisplay
     * @since 1.0.0-beta-1
     */
    static String filterLanguage(String text) {
        final StringBuilder noCommentString = new StringBuilder(text);
        final int commentIndex = noCommentString.indexOf("||");
        final int separatorIndex = noCommentString.indexOf("|");
        if (commentIndex != -1)
            noCommentString.delete(commentIndex, noCommentString.length());
        if (separatorIndex != -1) {
            switch (Config.getClient().getLanguageDisplay()) {
                case CJK_ONLY:
                    noCommentString.delete(separatorIndex, noCommentString.length());
                    break;
                case NON_CJK_ONLY:
                    noCommentString.delete(0, separatorIndex);
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
     * @see org.mtr.mod.config.LanguageDisplay
     * @since 1.0.0-beta-1
     */
    static String mergeTranslation(String keyCJK, String key) {
        switch (Config.getClient().getLanguageDisplay()) {
            default:
                return TextHelper.translatable(keyCJK).getString() + "|" + TextHelper.translatable(key).getString();
            case CJK_ONLY:
                return TextHelper.translatable(keyCJK).getString();
            case NON_CJK_ONLY:
                return TextHelper.translatable(key).getString();
        }
    }

    /**
     * Splits the translation string into English and CJK. This function requires there is only a "|" separator.
     *
     * @param text the translation string
     * @return a tuple of CJK (left) and English (right)
     * @author ZiYueCommentary
     * @since 1.0.0-beta-1
     */
    static ImmutablePair<String, String> splitTranslation(String text) {
        final int separatorIndex = text.indexOf("|");
        return new ImmutablePair<>(text.substring(0, separatorIndex), text.substring(separatorIndex + 1));
    }

    /**
     * Formatting the string.
     *
     * @author ZiYueCommentary
     * @see IGui#insertTranslation(TranslationProvider.TranslationHolder, TranslationProvider.TranslationHolder, String, int, String...)
     * @since 1.0.0-beta-2
     */
    static String insertTranslation(String keyCJK, String key, @Nullable String overrideFirst, int expectedArguments, String... arguments) {
        if (arguments.length < expectedArguments) {
            return "";
        }

        final List<String[]> dataCJK = new ArrayList<>();
        final List<String[]> data = new ArrayList<>();
        for (int i = 0; i < arguments.length; i++) {
            final String[] argumentSplit = arguments[i].split("\\|");

            int indexCJK = 0;
            int index = 0;
            for (final String text : argumentSplit) {
                if (IGui.isCjk(text)) {
                    if (indexCJK == dataCJK.size()) {
                        dataCJK.add(new String[expectedArguments]);
                    }
                    dataCJK.get(indexCJK)[i] = text;
                    indexCJK++;
                } else {
                    if (index == data.size()) {
                        data.add(new String[expectedArguments]);
                    }
                    data.get(index)[i] = text;
                    index++;
                }
            }
        }

        final StringBuilder result = new StringBuilder();
        dataCJK.forEach(combinedArguments -> {
            if (Arrays.stream(combinedArguments).allMatch(Objects::nonNull)) {
                result.append("|");
                if (overrideFirst == null) {
                    result.append(TextHelper.translatable(keyCJK, (Object[]) combinedArguments).getString());
                } else {
                    final String[] newCombinedArguments = new String[expectedArguments + 1];
                    System.arraycopy(combinedArguments, 0, newCombinedArguments, 1, expectedArguments);
                    newCombinedArguments[0] = overrideFirst;
                    result.append((TextHelper.translatable(keyCJK, (Object[]) newCombinedArguments).getString()));
                }
            }
        });
        data.forEach(combinedArguments -> {
            if (Arrays.stream(combinedArguments).allMatch(Objects::nonNull)) {
                result.append("|");
                if (overrideFirst == null) {
                    result.append((TextHelper.translatable(key, (Object[]) combinedArguments).getString()));
                } else {
                    final String[] newCombinedArguments = new String[expectedArguments + 1];
                    System.arraycopy(combinedArguments, 0, newCombinedArguments, 1, expectedArguments);
                    newCombinedArguments[0] = overrideFirst;
                    result.append((TextHelper.translatable(key, (Object[]) newCombinedArguments).getString()));
                }
            }
        });

        if (result.length() > 0) {
            return result.substring(1);
        } else {
            return "";
        }
    }

    /**
     * @author ZiYueCommentary
     * @see #insertTranslation(String, String, String, int, String...)
     * @since 1.0.0-beta-2
     */
    static String insertTranslation(String keyCJK, String key, int expectedArguments, String... arguments) {
        return insertTranslation(keyCJK, key, null, expectedArguments, arguments);
    }

    /**
     * Adding a hold-shift-tooltip to the tooltips.
     *
     * @param list      hover text List, just like pointer in C/C++
     * @param component a component that waits for split
     * @param regex     the delimiting regular expression
     * @param limit     the result threshold, as described above
     * @return hover text lists
     * @author ZiYueCommentary
     * @since 1.0.0-beta-2
     */
    static List<MutableText> addHoldShiftTooltip(List<MutableText> list, MutableText component, boolean wordWarp, String regex, int limit) {
        if (Screen.hasShiftDown()) {
            // Adding a space in the end is the simplest way to fix the bug. Do not ask me why.
            String[] texts = (component.getString() + " ").split(regex, limit);
            if (wordWarp) {
                for (String text : texts) {
                    int start = 0;
                    while (start < text.length()) {
                        int end = start;
                        int currentWidth = 0;
                        int lastSpace = -1;

                        while (end < text.length() && currentWidth + GraphicsHolder.getTextWidth(text.substring(start, end + 1)) <= 380) { // 380 is the maximum width
                            if (text.charAt(end) == ' ') {
                                lastSpace = end;
                            }
                            currentWidth += GraphicsHolder.getTextWidth(text.substring(end, end + 1));
                            end++;
                        }

                        if (lastSpace != -1 && lastSpace > start) {
                            list.add(TextHelper.literal(text.substring(start, lastSpace)));
                            start = lastSpace + 1;
                        } else {
                            list.add(TextHelper.literal(text.substring(start, end)));
                            start = end;
                        }
                    }
                }
            } else {
                for (String text : texts) {
                    list.add(TextHelper.literal(text));
                }
            }
        } else {
            list.add(TextHelper.translatable("tooltip.tjmetro.shift").formatted(TextFormatting.YELLOW));
        }
        return list;
    }

    /**
     * @author ZiYueCommentary
     * @see #addHoldShiftTooltip(List, MutableText, boolean, String, int)
     * @since 1.0.0-beta-2
     */
    static List<MutableText> addHoldShiftTooltip(List<MutableText> list, MutableText component, boolean wordWarp, String regex) {
        return addHoldShiftTooltip(list, component, wordWarp, regex, 0);
    }

    /**
     * @author ZiYueCommentary
     * @see #addHoldShiftTooltip(List, MutableText, boolean, String, int)
     * @since 1.0.0-beta-2
     */
    static List<MutableText> addHoldShiftTooltip(List<MutableText> list, MutableText component, boolean wordWarp) {
        return addHoldShiftTooltip(list, component, wordWarp, "\n", 0);
    }

    /**
     * @author ZiYueCommentary
     * @see #addHoldShiftTooltip(List, MutableText, boolean, String, int)
     * @since 1.0.0-beta-2
     */
    static List<MutableText> addHoldShiftTooltip(List<MutableText> list, MutableText component) {
        return addHoldShiftTooltip(list, component, true, "\n", 0);
    }
}
