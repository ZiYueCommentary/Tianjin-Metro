package ziyue.tjmetro;

import mtr.MTR;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.function.BiConsumer;

import static ziyue.tjmetro.packet.IPacket.PACKET_UPDATE_CUSTOM_CONTENT;

public class Main
{
	public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);

	static CreativeModeTab TAB = Registry.getItemGroup(new ResourceLocation(Reference.MOD_ID, "tjmetro_tab"), () -> new ItemStack(BlockList.LOGO.get()));

	public static void init(
			BiConsumer<String, RegistryObject<Item>> registerItem,
			BiConsumer<String, RegistryObject<Block>> registerBlock,
			MTR.RegisterBlockItem registerBlockItem,
			MTR.RegisterBlockItem registerEnchantedBlockItem,
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
		registerEnchantedBlockItem.accept("bench", BlockList.BENCH, TAB);

		registerBlockEntityType.accept("station_name_sign_2", BlockEntityTypes.STATION_NAME_SIGN_ENTITY_2);
		registerBlockEntityType.accept("station_name_sign_1", BlockEntityTypes.STATION_NAME_SIGN_ENTITY_1);

		Registry.registerNetworkReceiver(PACKET_UPDATE_CUSTOM_CONTENT, PacketGuiServer::receiveCustomContentC2S);
	}
}
