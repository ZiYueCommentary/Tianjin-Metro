package ziyue.tjmetro;

import mtr.RegistryObject;
import mtr.item.ItemWithCreativeTabBase;
import net.minecraft.world.item.Item;
import ziyue.tjmetro.items.ItemPSDTianjinBase;

/**
 * @since beta-1
 */

public interface ItemList
{
    RegistryObject<Item> WRENCH = new RegistryObject<>(() -> new ItemWithCreativeTabBase(TianjinMetro.CREATIVE_MODE_TAB, properties -> properties.stacksTo(1)));
    RegistryObject<Item> PSD_DOOR_TIANJIN = new RegistryObject<>(() -> new ItemPSDTianjinBase(BlockList.PSD_DOOR_TIANJIN));
    RegistryObject<Item> PSD_GLASS_TIANJIN = new RegistryObject<>(() -> new ItemPSDTianjinBase(BlockList.PSD_GLASS_TIANJIN));
    RegistryObject<Item> PSD_GLASS_END_TIANJIN = new RegistryObject<>(() -> new ItemPSDTianjinBase(BlockList.PSD_GLASS_END_TIANJIN));
}
