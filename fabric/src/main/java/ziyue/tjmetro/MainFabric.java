package ziyue.tjmetro;

import mtr.RegistryObject;
import mtr.item.ItemBlockEnchanted;
import mtr.mappings.BlockEntityMapper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ziyue.filters.Filter;

/**
 * Entry of fabric mod.
 *
 * @see TianjinMetro
 * @since beta-1
 */

public class MainFabric implements ModInitializer
{
    @Override
    public void onInitialize() {
        TianjinMetro.init(MainFabric::registerBlock, MainFabric::registerBlock, MainFabric::registerItem, MainFabric::registerEnchantedBlock, MainFabric::registerBlockEntityType);
    }

    public static void registerBlock(String path, RegistryObject<Block> block) {
        Registry.register(Registry.BLOCK, new ResourceLocation(Reference.MOD_ID, path), block.get());
    }

    public static void registerBlock(String path, RegistryObject<Block> block, Filter filter) {
        Registry.register(Registry.BLOCK, new ResourceLocation(Reference.MOD_ID, path), block.get());
        Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, path), new BlockItem(block.get(), new FabricItemSettings()));
        filter.addItems(block.get().asItem());
    }

    public static void registerItem(String path, RegistryObject<Item> item, Filter filter) {
        Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, path), item.get());
        filter.addItems(item.get());
    }

    public static void registerEnchantedBlock(String path, RegistryObject<Block> block, Filter filter) {
        registerBlock(path, block);
        Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, path), new ItemBlockEnchanted(block.get(), new FabricItemSettings()));
        filter.addItems(block.get().asItem());
    }

    public static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Reference.MOD_ID, path), blockEntityType.get());
    }
}
