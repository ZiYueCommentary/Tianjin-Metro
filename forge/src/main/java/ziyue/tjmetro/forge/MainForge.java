package ziyue.tjmetro.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.mtr.mapping.holder.Screen;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.TianjinMetroClient;
import ziyue.tjmetro.mod.screen.ConfigClientScreen;

/**
 * @since 1.0.0-beta-1
 */

@Mod(Reference.MOD_ID)
public final class MainForge
{
    public static boolean filterInitialized = false;

    public MainForge() {
        TianjinMetro.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> TianjinMetroClient::init);
        MinecraftForge.EVENT_BUS.register(this);

#if MC_VERSION <= "11605"
        ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.fml.ExtensionPoint.CONFIGGUIFACTORY, () -> (client, parent) -> new ConfigClientScreen(new Screen(parent)));
#elif MC_VERSION <= "11701"
        ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.fmlclient.ConfigGuiHandler.ConfigGuiFactory.class, () -> new net.minecraftforge.fmlclient.ConfigGuiHandler.ConfigGuiFactory((client, parent) -> new ConfigClientScreen(new Screen(parent))));
#elif MC_VERSION <= "11802"
        ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory.class, () -> new net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory((client, parent) -> new ConfigClientScreen(new Screen(parent))));
#elif MC_VERSION <= "12004"
        ModLoadingContext.get().registerExtensionPoint(net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory.class, () -> new net.minecraftforge.client.ConfigScreenHandler.ConfigScreenFactory((client, parent) -> new ConfigClientScreen(new Screen(parent))));
#endif
    }

    @SubscribeEvent
    public void afterRegistry(
#if MC_VERSION <= "11701"
        net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post event
#elif MC_VERSION <= "11802"
        net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent.Post event
#elif MC_VERSION <= "12004"
        net.minecraftforge.client.event.ScreenEvent.Init.Pre event
#endif
    ) {
        // As you see, this is a very dumb thing due to we are using Forge.
        // Please read Fabric codes so that you will know how Fabric is awesome.
        #if MC_VERSION >= "11904"
            ziyue.filters.FilterList filterTianjinMetro = ziyue.filters.FilterList.empty();
            filterTianjinMetro.add(ziyue.tjmetro.mod.client.Filters.MISCELLANEOUS);
            filterTianjinMetro.add(ziyue.tjmetro.mod.client.Filters.BUILDING);
            filterTianjinMetro.add(ziyue.tjmetro.mod.client.Filters.SIGNS);
            filterTianjinMetro.add(ziyue.tjmetro.mod.client.Filters.GATES);
            filterTianjinMetro.add(ziyue.tjmetro.mod.client.Filters.DECORATION);
            filterTianjinMetro.add(ziyue.tjmetro.mod.client.Filters.CEILINGS);
            filterTianjinMetro.add(ziyue.tjmetro.mod.client.Filters.RAILWAY_SIGNS);
            ziyue.filters.FilterBuilder.FILTERS.put(net.minecraftforge.common.CreativeModeTabRegistry.getTab(TianjinMetro.CREATIVE_MODE_TAB.identifier), filterTianjinMetro);

            ziyue.tjmetro.mapping.FilterBuilder.setReservedButton(TianjinMetro.CREATIVE_MODE_TAB, org.mtr.mapping.mapper.TextHelper.translatable("button.tjmetro.tianjin_metro_options"), button ->
                    org.mtr.mapping.holder.MinecraftClient.getInstance().openScreen(new Screen(new ConfigClientScreen(org.mtr.mapping.holder.MinecraftClient.getInstance().getCurrentScreenMapped()))));
        #endif
        if (filterInitialized) return;
        Registry.FILTERS_REGISTRY_ITEM.forEach(pair -> pair.getFirst().addItems(pair.getSecond().get().data));
        Registry.FILTERS_REGISTRY_BLOCK.forEach(pair -> pair.getFirst().addItems(pair.getSecond().get().asItem().data));
        filterInitialized = true;
    }
}
