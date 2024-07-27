package ziyue.tjmetro.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.TianjinMetroClient;

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
    #elif MC_VERSION <= "11905"
    #endif
    }

    // As you see, this is a very dumb thing due to we are using Forge.
    // Please read Fabric codes so that you will know how Fabric is awesome.
    @SubscribeEvent
    public void afterRegistry(ScreenEvent.Init.Post event) {
        //FilterList filterTianjinMetro = FilterList.empty();
        //filterTianjinMetro.add(Filters.MISCELLANEOUS);
        //filterTianjinMetro.add(Filters.BUILDING);
        //filterTianjinMetro.add(Filters.SIGNS);
        //filterTianjinMetro.add(Filters.GATES);
        //filterTianjinMetro.add(Filters.DECORATION);
        //filterTianjinMetro.add(Filters.CEILINGS);
        //filterTianjinMetro.add(Filters.RAILWAY_SIGNS);
        //FilterBuilder.FILTERS.put(CreativeModeTabRegistry.getTab(TianjinMetro.CREATIVE_MODE_TAB.creativeModeTab), filterTianjinMetro);

        if (filterInitialized) return;
        Registry.FILTERS_REGISTRY_ITEM.forEach(pair -> pair.getFirst().addItems(pair.getSecond().get().data));
        Registry.FILTERS_REGISTRY_BLOCK.forEach(pair -> pair.getFirst().addItems(pair.getSecond().get().asItem().data));
        filterInitialized = true;
    }
}
