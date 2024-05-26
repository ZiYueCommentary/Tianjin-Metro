package ziyue.tjmetro;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ziyue.tjmetro.client.Config;

public class ModMenuConfig implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return Config::getConfigScreen;
    }
}
