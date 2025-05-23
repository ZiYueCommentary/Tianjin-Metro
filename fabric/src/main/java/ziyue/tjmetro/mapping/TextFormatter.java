package ziyue.tjmetro.mapping;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import ziyue.tjmetro.mod.config.ConfigClient;

import java.util.function.Function;

public interface TextFormatter
{
    /**
     * Stylize a config screen footer as a link.
     *
     * @since 1.0.0-beta-5
     */
    Function<ConfigClient.Footer, MutableText> FOOTER_LINK = footer -> footer.text().get().data.setStyle(Style.EMPTY.withColor(Formatting.BLUE).withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, footer.link())));
}
