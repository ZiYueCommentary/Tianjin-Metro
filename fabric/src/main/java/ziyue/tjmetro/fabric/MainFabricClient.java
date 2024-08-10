package ziyue.tjmetro.fabric;

import net.fabricmc.api.ClientModInitializer;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetroClient;
import ziyue.tjmetro.mod.client.Filters;

/**
 * @since 1.0.0-beta-1
 */

public final class MainFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        TianjinMetroClient.init();
        Filters.init();
    }
}
