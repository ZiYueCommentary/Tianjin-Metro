package ziyue.tjmetro.fabric;

import net.fabricmc.api.ModInitializer;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetro;

public final class MainFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		TianjinMetro.init();
	}
}
