package ziyue.tjmetro.mod;

import org.mtr.mapping.holder.ItemConvertible;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.registry.CreativeModeTabHolder;

public interface CreativeModeTabs
{
    CreativeModeTabHolder TIANJIN_METRO = Registry.createCreativeModeTabHolder("tianjin_metro", () -> new ItemStack(new ItemConvertible(BlockList.LOGO.get().data)));

    static void registerCreativeModeTabs() {
        // Calling this class to initialize constants
        TianjinMetro.LOGGER.info("Registering creative mode tabs");
    }
}
