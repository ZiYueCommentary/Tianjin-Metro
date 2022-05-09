package ziyue.tjmetro;

import mtr.RegistryObject;
import mtr.item.ItemBlockEnchanted;
import mtr.mappings.BlockEntityMapper;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class MainFabric implements ModInitializer
{
	@Override
	public void onInitialize() {
		Main.init(MainFabric::registerItem, MainFabric::registerBlock, MainFabric::registerBlock, MainFabric::registerEnchantedBlock, MainFabric::registerBlockEntityType, MainFabric::registerSoundEvent);
	}

	private static void registerItem(String path, RegistryObject<Item> item) {
		Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, path), item.get());
	}

	private static void registerBlock(String path, RegistryObject<Block> block) {
		Registry.register(Registry.BLOCK, new ResourceLocation(Reference.MOD_ID, path), block.get());
	}

	private static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTab itemGroup) {
		Registry.register(Registry.BLOCK, new ResourceLocation(Reference.MOD_ID, path), block.get());
		Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, path), new BlockItem(block.get(), new Item.Properties().tab(itemGroup)));
	}

	private static void registerEnchantedBlock(String path, RegistryObject<Block> block, CreativeModeTab itemGroup) {
		registerBlock(path, block);
		Registry.register(Registry.ITEM, new ResourceLocation(Reference.MOD_ID, path), new ItemBlockEnchanted(block.get(), new Item.Properties().tab(itemGroup)));
	}

	private static <T extends BlockEntityMapper> void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Reference.MOD_ID, path), blockEntityType.get());
	}

	private static void registerSoundEvent(String path, SoundEvent soundEvent) {
		Registry.register(Registry.SOUND_EVENT, new ResourceLocation(Reference.MOD_ID, path), soundEvent);
	}
}
