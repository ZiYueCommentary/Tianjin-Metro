package ziyue.tjmetro.mod.data;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.IGui;

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

    static String insertTranslation(String keyCJK, String key, int expectedArguments, String... arguments) {
        return insertTranslation(keyCJK, key, null, expectedArguments, arguments);
    }

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
}
