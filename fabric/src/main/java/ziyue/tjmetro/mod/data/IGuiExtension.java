package ziyue.tjmetro.mod.data;

import org.mtr.libraries.kotlin.Pair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.config.Config;

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
     * @return a tuple of CJK (A) and English (B)
     * @author ZiYueCommentary
     * @since 1.0.0-beta-1
     */
    static Pair<String, String> splitTranslation(String text) {
        final int separatorIndex = text.indexOf("|");
        return new Pair<>(text.substring(0, separatorIndex), text.substring(separatorIndex + 1));
    }
}
