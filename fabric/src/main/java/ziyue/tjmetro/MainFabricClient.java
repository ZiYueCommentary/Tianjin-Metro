package ziyue.tjmetro;

import net.fabricmc.api.ClientModInitializer;

/**
 * @see TianjinMetroClient
 * @since beta-1
 */

public class MainFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        TianjinMetroClient.init();
    }
}
