package ziyue.tjmetro;

import mtr.Registry;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.BiConsumer;

public class Main
{
	static CreativeModeTab TAB = Registry.getItemGroup(new ResourceLocation(Reference.MOD_ID, "tjmetro_tab"), () -> new ItemStack(BlockList.LOGO.get()));

	public static void init(
			BiConsumer<String, RegistryObject<Item>> registerItem,
			BiConsumer<String, RegistryObject<Block>> registerBlock,
			RegisterBlockItem registerBlockItem,
			RegisterBlockItem registerEnchantedBlockItem,
			BiConsumer<String,RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType,
			BiConsumer<String, SoundEvent> registerSoundEvent
	) {
		registerBlockItem.accept("logo", BlockList.LOGO, TAB);
		registerBlockItem.accept("rolling", BlockList.ROLLING, TAB);
		registerBlockItem.accept("station_name_sign_2", BlockList.STATION_NAME_SIGN_2, TAB);
		registerBlockItem.accept("roadblock", BlockList.ROADBLOCK, TAB);

		registerEnchantedBlockItem.accept("ceiling", BlockList.STATION_COLOR_CEILING, TAB);
		registerEnchantedBlockItem.accept("ceiling_light", BlockList.STATION_COLOR_CEILING_LIGHT, TAB);
		registerEnchantedBlockItem.accept("ceiling_no_light", BlockList.STATION_COLOR_CEILING_NO_LIGHT, TAB);
		registerEnchantedBlockItem.accept("station_name_sign_1", BlockList.STATION_NAME_SIGN_1, TAB);
		registerEnchantedBlockItem.accept("station_name_tall_block_double_sided", BlockList.STATION_NAME_TALL_DOUBLE_SIDED, TAB);
	}

	@FunctionalInterface
	public interface RegisterBlockItem {
		void accept(String string, RegistryObject<Block> block, CreativeModeTab tab);
	}
}
