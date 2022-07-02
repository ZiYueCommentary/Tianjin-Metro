package ziyue.tjmetro;

import mtr.RegistryObject;
import mtr.item.ItemBlockEnchanted;
import mtr.mappings.BlockEntityMapper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * All register methods are <b>public</b>.
 *
 * @see TianjinMetro
 */

public class MainFabric implements ModInitializer
{
    @Override
    public void onInitialize() {
        TianjinMetro.init(MainFabric::registerItem, MainFabric::registerBlock, MainFabric::registerBlock, MainFabric::registerEnchantedBlock, MainFabric::registerBlockEntityType, MainFabric::registerEntityType);
    }

    public static void registerItem(String path, RegistryObject<Item> item) {
        Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, path), item.get());
    }

    public static void registerBlock(String path, RegistryObject<Block> block) {
        Registry.register(Registry.BLOCK, new ResourceLocation(Reference.MOD_ID, path), block.get());
    }

    public static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTab itemGroup) {
        Registry.register(Registry.BLOCK, new ResourceLocation(Reference.MOD_ID, path), block.get());
        Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, path), new BlockItem(block.get(), new Item.Properties().tab(itemGroup)));
    }

    public static void registerEnchantedBlock(String path, RegistryObject<Block> block, CreativeModeTab itemGroup) {
        registerBlock(path, block);
        Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, path), new ItemBlockEnchanted(block.get(), new Item.Properties().tab(itemGroup)));
    }

    public static void registerEntityType(String path, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(Reference.MOD_ID, path), entityType.get());
    }

    public static <T extends BlockEntityMapper> void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Reference.MOD_ID, path), blockEntityType.get());
    }
}
