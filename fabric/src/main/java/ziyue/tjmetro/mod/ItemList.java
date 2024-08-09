package ziyue.tjmetro.mod;

import org.mtr.mapping.holder.Item;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.registry.ItemRegistryObject;
import ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase;

import static ziyue.tjmetro.mod.client.Filters.GATES;
import static ziyue.tjmetro.mod.client.Filters.MISCELLANEOUS;

/**
 * @since 1.0.0-beta-1
 */

public interface ItemList
{
    ItemRegistryObject WRENCH = Registry.registerItem("wrench", itemSettings -> new Item(new ItemExtension(itemSettings.maxCount(1))), MISCELLANEOUS);
    ItemRegistryObject PSD_DOOR_TIANJIN = Registry.registerItem("psd_door_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_DOOR_TIANJIN, itemSettings)), GATES);
    ItemRegistryObject PSD_GLASS_TIANJIN = Registry.registerItem("psd_glass_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_GLASS_TIANJIN, itemSettings)), GATES);
    ItemRegistryObject PSD_GLASS_END_TIANJIN = Registry.registerItem("psd_glass_end_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_GLASS_END_TIANJIN, itemSettings)), GATES);
    ItemRegistryObject APG_DOOR_TIANJIN = Registry.registerItem("apg_door_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_DOOR_TIANJIN, itemSettings)), GATES);
    ItemRegistryObject APG_GLASS_TIANJIN = Registry.registerItem("apg_glass_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_TIANJIN, itemSettings)), GATES);
    ItemRegistryObject APG_GLASS_END_TIANJIN = Registry.registerItem("apg_glass_end_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_END_TIANJIN, itemSettings)), GATES);
    ItemRegistryObject APG_DOOR_TIANJIN_BMT = Registry.registerItem("apg_door_tianjin_bmt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_DOOR_TIANJIN_BMT, itemSettings)), GATES);
    ItemRegistryObject APG_GLASS_TIANJIN_BMT = Registry.registerItem("apg_glass_tianjin_bmt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_TIANJIN_BMT, itemSettings)), GATES);
    ItemRegistryObject APG_GLASS_END_TIANJIN_BMT = Registry.registerItem("apg_glass_end_tianjin_bmt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_END_TIANJIN_BMT, itemSettings)), GATES);

    static void registerItems() {
        // Calling this class to initialize constants
        TianjinMetro.LOGGER.info("Registering items");
    }
}
