package ziyue.tjmetro.mapping;

import net.minecraft.client.Minecraft;
#if MC_VERSION > "11605"
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
#else
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
#endif
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.holder.Util;

/**
 * @author ZiYueCommentary
 * @since 1.1.1
 */

public interface ConfirmLinkScreenHelper
{
    static void open(Screen parent, String url, boolean trusted) {
        Minecraft minecraftClient = Minecraft.getInstance();
#if MC_VERSION > "11605"
        minecraftClient.setScreen(new ConfirmLinkScreen(confirmed -> {
#else
        minecraftClient.setScreen(new ConfirmOpenLinkScreen(confirmed -> {
#endif
            if (confirmed) {
                Util.getOperatingSystem().open(url);
            }
            minecraftClient.setScreen(parent.data);
        }, url, trusted));
    }
}
