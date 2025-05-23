package ziyue.tjmetro.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.mtr.mapping.holder.Screen;
import ziyue.tjmetro.mod.config.ConfigClient;

/**
 * @since 1.0.0-beta-1
 */

public class ModMenuConfig implements ModMenuApi
{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ConfigClient.getConfigScreen(new Screen(parent)).data;
    }
}
