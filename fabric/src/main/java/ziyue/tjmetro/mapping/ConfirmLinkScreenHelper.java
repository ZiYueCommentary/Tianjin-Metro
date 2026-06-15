package ziyue.tjmetro.mapping;

import net.minecraft.client.MinecraftClient;
#if MC_VERSION > "11802"
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
#else
import net.minecraft.client.gui.screen.ConfirmChatLinkScreen;
#endif
import net.minecraft.util.Util;
import org.mtr.mapping.holder.Screen;

/**
 * @author ZiYueCommentary
 * @since 1.1.1
 */

public interface ConfirmLinkScreenHelper
{
    static void open(Screen parent, String url, boolean trusted) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
#if MC_VERSION > "11802"
        minecraftClient.setScreen(new ConfirmLinkScreen(confirmed -> {
#elif MC_VERSION > "11605"
        minecraftClient.setScreen(new ConfirmChatLinkScreen(confirmed -> {
#else
        minecraftClient.openScreen(new ConfirmChatLinkScreen(confirmed -> {
#endif
            if (confirmed) {
                Util.getOperatingSystem().open(url);
            }
#if MC_VERSION > "11605"
            minecraftClient.setScreen(parent.data);
#else
            minecraftClient.openScreen(parent.data);
#endif
        }, url, trusted));
    }
}
