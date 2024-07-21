package ziyue.tjmetro.fabric;

import net.fabricmc.api.ClientModInitializer;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetroClient;

public final class MainFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        TianjinMetroClient.init();
    }
}
