package ziyue.tjmetro.fabric;

import net.fabricmc.api.ClientModInitializer;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetroClient;

/**
 * @since 1.0.0-beta-1
 */

public final class MainFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        TianjinMetroClient.init();
        Registry.FILTERS_REGISTRY_ITEM.forEach(pair -> pair.getFirst().addItems(pair.getSecond().get().data));
        Registry.FILTERS_REGISTRY_BLOCK.forEach(pair -> pair.getFirst().addItems(pair.getSecond().get().asItem().data));
    }
}
