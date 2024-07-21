package ziyue.tjmetro.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.mtr.mapping.holder.Screen;
import ziyue.tjmetro.mod.config.ConfigClient;

public class ModMenuConfig implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> ConfigClient.getConfigScreen(new Screen(screen)).data;
    }
}
