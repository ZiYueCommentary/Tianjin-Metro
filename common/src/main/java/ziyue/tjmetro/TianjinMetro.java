package ziyue.tjmetro;

import mtr.MTR;
import mtr.Registry;
import mtr.RegistryObject;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.function.BiConsumer;

import static ziyue.tjmetro.packet.IPacket.PACKET_UPDATE_CUSTOM_COLOR;
import static ziyue.tjmetro.packet.IPacket.PACKET_UPDATE_CUSTOM_CONTENT;

/**
 * @since 1.0b
 */

public class TianjinMetro
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);

    public static final CreativeModeTab TAB = Registry.getItemGroup(new ResourceLocation(Reference.MOD_ID, "tjmetro_tab"), () -> new ItemStack(BlockList.LOGO.get()));
    public static final GameRules.Key<GameRules.BooleanValue> PREVENT_BLOCK_FALLING = RegistryClient.registerBooleanGameRule("preventBlockFalling", false);

    public static void init(
            BiConsumer<String, RegistryObject<Block>> registerBlock,
            MTR.RegisterBlockItem registerBlockItem,
            MTR.RegisterBlockItem registerEnchantedBlockItem,
            BiConsumer<String, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>>> registerBlockEntityType,
            BiConsumer<String, RegistryObject<? extends EntityType<? extends Entity>>> registerEntityType
    ) {
        registerBlockItem.accept("logo", BlockList.LOGO, TAB);
        registerBlockItem.accept("rolling", BlockList.ROLLING, TAB);
        registerBlockItem.accept("station_name_sign_2", BlockList.STATION_NAME_SIGN_2, TAB);
        registerBlockItem.accept("platform_tj_1", BlockList.PLATFORM_TJ_1, TAB);
        registerBlockItem.accept("platform_tj_1_indented", BlockList.PLATFORM_TJ_1_INDENTED, TAB);
        registerBlockItem.accept("marble_gray", BlockList.MARBLE_GRAY, TAB);
        registerBlockItem.accept("marble_gray_slab", BlockList.MARBLE_GRAY_SLAB, TAB);
        registerBlockItem.accept("marble_gray_stairs", BlockList.MARBLE_GRAY_STAIRS, TAB);
        registerBlockItem.accept("platform_tj_2", BlockList.PLATFORM_TJ_2, TAB);
        registerBlockItem.accept("platform_tj_2_indented", BlockList.PLATFORM_TJ_2_INDENTED, TAB);
        registerBlockItem.accept("marble_yellow", BlockList.MARBLE_YELLOW, TAB);
        registerBlockItem.accept("marble_yellow_slab", BlockList.MARBLE_YELLOW_SLAB, TAB);
        registerBlockItem.accept("marble_yellow_stairs", BlockList.MARBLE_YELLOW_STAIRS, TAB);
        registerBlockItem.accept("station_name_wall_legacy", BlockList.STATION_NAME_WALL_LEGACY, TAB);
        registerBlockItem.accept("bench", BlockList.BENCH, TAB);
        registerBlockItem.accept("ceiling_not_lit", BlockList.CEILING_NOT_LIT, TAB);
        registerBlockItem.accept("apg_corner", BlockList.APG_CORNER, TAB);
        registerBlockItem.accept("player_detector", BlockList.PLAYER_DETECTOR, TAB);
        registerBlockItem.accept("high_speed_repeater", BlockList.HIGH_SPEED_REPEATER, TAB);
        registerBlockItem.accept("time_display", BlockList.TIME_DISPLAY, TAB);
        registerBlockItem.accept("roadblock", BlockList.ROADBLOCK, TAB);
        registerBlockItem.accept("roadblock_sign", BlockList.ROADBLOCK_SIGN, TAB);
        registerBlockItem.accept("emergency_exit_sign", BlockList.EMERGENCY_EXIT_SIGN, TAB);

        registerEnchantedBlockItem.accept("ceiling", BlockList.STATION_COLOR_CEILING, TAB);
        registerEnchantedBlockItem.accept("ceiling_light", BlockList.STATION_COLOR_CEILING_LIGHT, TAB);
        registerEnchantedBlockItem.accept("ceiling_no_light", BlockList.STATION_COLOR_CEILING_NO_LIGHT, TAB);
        registerEnchantedBlockItem.accept("station_name_sign_1", BlockList.STATION_NAME_SIGN_1, TAB);
        registerEnchantedBlockItem.accept("custom_color_concrete", BlockList.CUSTOM_COLOR_CONCRETE, TAB);
        registerEnchantedBlockItem.accept("custom_color_concrete_stairs", BlockList.CUSTOM_COLOR_CONCRETE_STAIRS, TAB);
        registerEnchantedBlockItem.accept("decoration_light", BlockList.DECORATION_LIGHT, TAB);
        registerEnchantedBlockItem.accept("custom_color_concrete_slab", BlockList.CUSTOM_COLOR_CONCRETE_SLAB, TAB);
        registerEnchantedBlockItem.accept("station_color_ceiling_not_lit", BlockList.STATION_COLOR_CEILING_NOT_LIT, TAB);

        registerBlockEntityType.accept("station_name_sign_2", BlockEntityTypes.STATION_NAME_SIGN_ENTITY_2);
        registerBlockEntityType.accept("station_name_sign_1", BlockEntityTypes.STATION_NAME_SIGN_ENTITY_1);
        registerBlockEntityType.accept("roadblock_sign", BlockEntityTypes.ROADBLOCK_SIGN_ENTITY);
        registerBlockEntityType.accept("player_detector", BlockEntityTypes.PLAYER_DETECTOR_TILE_ENTITY);
        registerBlockEntityType.accept("custom_color_concrete", BlockEntityTypes.STATION_COLOR_CONCRETE_TILE_ENTITY);
        registerBlockEntityType.accept("custom_color_concrete_stairs", BlockEntityTypes.STATION_COLOR_CONCRETE_STAIRS_TILE_ENTITY);
        registerBlockEntityType.accept("custom_color_concrete_slab", BlockEntityTypes.STATION_COLOR_CONCRETE_SLAB_TILE_ENTITY);
        registerBlockEntityType.accept("station_name_wall_legacy", BlockEntityTypes.STATION_NAME_WALL_TILE_ENTITY);
        registerBlockEntityType.accept("decoration_light", BlockEntityTypes.DECORATION_LIGHT_TILE_ENTITY);
        registerBlockEntityType.accept("time_display", BlockEntityTypes.TIME_DISPLAY_TILE_ENTITY);

        registerEntityType.accept("bench", EntityTypes.BENCH);

        Registry.registerNetworkReceiver(PACKET_UPDATE_CUSTOM_CONTENT, PacketGuiServer::receiveCustomContentC2S);
        Registry.registerNetworkReceiver(PACKET_UPDATE_CUSTOM_COLOR, PacketGuiServer::receiveCustomColorC2S);
    }
}
