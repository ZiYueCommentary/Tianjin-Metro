package ziyue.tjmetro.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import org.mtr.mapping.holder.Screen;
import ziyue.tjmetro.mod.client.Filters;
import ziyue.tjmetro.mod.config.ConfigClient;

public class MainForgeClient
{
    public static void registerConfigMenu() {
#if MC_VERSION <= "11605"
         ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.fml.ExtensionPoint.CONFIGGUIFACTORY, () -> (client, parent) -> ConfigClient.getConfigScreen(new Screen(parent)).data);
#elif MC_VERSION <= "11701"
         ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.fmlclient.ConfigGuiHandler.ConfigGuiFactory.class, () -> new net.minecraftforge.fmlclient.ConfigGuiHandler.ConfigGuiFactory((client, parent) -> ConfigClient.getConfigScreen(new Screen(parent)).data));
#elif MC_VERSION <= "11802"
         ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory.class, () -> new net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory((client, parent) -> ConfigClient.getConfigScreen(new Screen(parent)).data));
#elif MC_VERSION <= "12004"
         ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory.class, () -> new net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ConfigClient.getConfigScreen(new Screen(parent)).data));
#endif
    }
}
