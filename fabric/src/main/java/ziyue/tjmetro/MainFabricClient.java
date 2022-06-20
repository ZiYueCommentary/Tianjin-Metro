package ziyue.tjmetro;

import net.fabricmc.api.ClientModInitializer;

public class MainFabricClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        TianjinMetroClient.init();
    }
}
