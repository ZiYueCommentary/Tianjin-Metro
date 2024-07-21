package ziyue.tjmetro.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ziyue.filters.Filter;
import ziyue.filters.FilterBuilder;
import ziyue.filters.FilterList;
import ziyue.filters.FiltersApi;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.TianjinMetroClient;
import ziyue.tjmetro.mod.client.Filters;

import java.util.ArrayList;

@Mod(Reference.MOD_ID)
public final class MainForge
{
    public MainForge() {
        TianjinMetro.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> TianjinMetroClient::init);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::afterRegistry);
    }

    // As you see, this is a very dumb thing due to we are using Forge.
    // Please read Fabric codes so that you will know how Fabric is awesome.
    @SubscribeEvent
    public void afterRegistry(CreativeModeTabEvent.Register event) {
        FilterList filterList = FilterList.empty();
        filterList.add(Filters.MISCELLANEOUS);
        filterList.add(Filters.BUILDING);
        filterList.add(Filters.SIGNS);
        filterList.add(Filters.GATES);
        filterList.add(Filters.DECORATION);
        filterList.add(Filters.CEILINGS);
        filterList.add(Filters.RAILWAY_SIGNS);
        FilterBuilder.FILTERS.put(CreativeModeTabRegistry.getTab(TianjinMetro.CREATIVE_MODE_TAB.identifier), filterList);
        Registry.FILTERS_REGISTRY_ITEM.forEach(pair -> pair.getFirst().addItems(pair.getSecond().get().data));
        Registry.FILTERS_REGISTRY_BLOCK.forEach(pair -> pair.getFirst().addItems(pair.getSecond().get().asItem().data));
    }
}
