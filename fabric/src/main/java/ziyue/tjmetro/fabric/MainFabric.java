package ziyue.tjmetro.fabric;

import net.fabricmc.api.ModInitializer;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetro;

/**
 * @since 1.0.0-beta-1
 */

public final class MainFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		TianjinMetro.init();
	}
}
