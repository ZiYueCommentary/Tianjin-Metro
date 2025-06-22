package ziyue.tjmetro.mod;

import org.mtr.mapping.holder.Item;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.registry.ItemRegistryObject;
import ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase;

/**
 * @since 1.0.0-beta-1
 */

public interface ItemList
{
    ItemRegistryObject WRENCH = Registry.registerItem("wrench", itemSettings -> new Item(new ItemExtension(itemSettings.maxCount(1))), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject PSD_DOOR_TIANJIN = Registry.registerItem("psd_door_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_DOOR_TIANJIN_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject PSD_GLASS_TIANJIN = Registry.registerItem("psd_glass_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_GLASS_TIANJIN_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject PSD_GLASS_END_TIANJIN = Registry.registerItem("psd_glass_end_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_GLASS_END_TIANJIN_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_DOOR_TIANJIN = Registry.registerItem("apg_door_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_DOOR_TIANJIN_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_GLASS_TIANJIN = Registry.registerItem("apg_glass_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_TIANJIN_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_GLASS_END_TIANJIN = Registry.registerItem("apg_glass_end_tianjin", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_END_TIANJIN_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_DOOR_TIANJIN_BMT = Registry.registerItem("apg_door_tianjin_bmt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_DOOR_TIANJIN_BMT_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_GLASS_TIANJIN_BMT = Registry.registerItem("apg_glass_tianjin_bmt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_TIANJIN_BMT_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_GLASS_END_TIANJIN_BMT = Registry.registerItem("apg_glass_end_tianjin_bmt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_END_TIANJIN_BMT_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject PSD_DOOR_TIANJIN_BMT = Registry.registerItem("psd_door_tianjin_bmt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_DOOR_TIANJIN_BMT_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject PSD_GLASS_TIANJIN_BMT = Registry.registerItem("psd_glass_tianjin_bmt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_GLASS_TIANJIN_BMT_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject PSD_GLASS_END_TIANJIN_BMT = Registry.registerItem("psd_glass_end_tianjin_bmt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_GLASS_END_TIANJIN_BMT_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_DOOR_TIANJIN_TRT = Registry.registerItem("apg_door_tianjin_trt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_DOOR_TIANJIN_TRT_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_GLASS_TIANJIN_TRT = Registry.registerItem("apg_glass_tianjin_trt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_TIANJIN_TRT_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_GLASS_END_TIANJIN_TRT = Registry.registerItem("apg_glass_end_tianjin_trt", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_END_TIANJIN_TRT_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject PSD_DOOR_TIANJIN_JINJING = Registry.registerItem("psd_door_tianjin_jinjing", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_DOOR_TIANJIN_JINJING_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject PSD_GLASS_TIANJIN_JINJING = Registry.registerItem("psd_glass_tianjin_jinjing", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_GLASS_TIANJIN_JINJING_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject PSD_GLASS_END_TIANJIN_JINJING = Registry.registerItem("psd_glass_end_tianjin_jinjing", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.PSD_GLASS_END_TIANJIN_JINJING_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_DOOR_TIANJIN_JINJING = Registry.registerItem("apg_door_tianjin_jinjing", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_DOOR_TIANJIN_JINJING_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_GLASS_TIANJIN_JINJING = Registry.registerItem("apg_glass_tianjin_jinjing", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_TIANJIN_JINJING_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);
    ItemRegistryObject APG_GLASS_END_TIANJIN_JINJING = Registry.registerItem("apg_glass_end_tianjin_jinjing", itemSettings -> new Item(new ItemPSDAPGTianjinBase(BlockList.APG_GLASS_END_TIANJIN_JINJING_BLOCK, itemSettings)), CreativeModeTabs.TIANJIN_METRO);

    static void registerItems() {
        // Calling this class to initialize constants
        TianjinMetro.LOGGER.info("Registering items");
    }
}
