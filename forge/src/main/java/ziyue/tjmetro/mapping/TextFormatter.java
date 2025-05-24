package ziyue.tjmetro.mapping;

#if MC_VERSION > "11605"

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import ziyue.tjmetro.mod.config.ConfigClient;

import java.util.function.Function;

public interface TextFormatter
{
    /**
     * Stylize a config screen footer as a link.
     *
     * @since 1.0.0-beta-5
     */
    Function<ConfigClient.Footer, MutableComponent> FOOTER_LINK = footer -> footer.text().get().data.setStyle(Style.EMPTY.withColor(ChatFormatting.BLUE).withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, footer.link())));
}

#else

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import ziyue.tjmetro.mod.config.ConfigClient;

import java.util.function.Function;

public interface TextFormatter
{
    /**
     * Stylize a config screen footer as a link.
     *
     * @since 1.0.0-beta-5
     */
    Function<ConfigClient.Footer, IFormattableTextComponent> FOOTER_LINK = footer -> footer.text().get().data.setStyle(Style.EMPTY.withColor(TextFormatting.BLUE).withUnderlined(true).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, footer.link())));
}

#endif