package ziyue.tjmetro.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.TianjinMetroClient;
import ziyue.tjmetro.mod.client.Filters;

/**
 * @since 1.0.0-beta-1
 */

@Mod(Reference.MOD_ID)
public final class MainForge
{
    public static boolean filterInitialized = false;

    public MainForge() {
        TianjinMetro.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            TianjinMetroClient.init();
            MainForgeClient.registerConfigMenu();
        });
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void afterRegistry(
#if MC_VERSION <= "11701"
        net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post event
#elif MC_VERSION <= "11802"
        net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent.Post event
#elif MC_VERSION <= "12004"
        net.minecraftforge.client.event.ScreenEvent.Init.Post event
#endif
    ) {
        if (filterInitialized) return;
        Filters.init();
        filterInitialized = true;
    }
}
