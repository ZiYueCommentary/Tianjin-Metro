package ziyue.tjmetro.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mtr.mapping.holder.ItemConvertible;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.registry.CreativeModeTabHolder;
import ziyue.tjmetro.mapping.BooleanGameRule;
import ziyue.tjmetro.mapping.GameRuleRegistry;
import ziyue.tjmetro.mod.packet.*;

public final class TianjinMetro
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);

    public static final BooleanGameRule NO_FALLING_BLOCK = GameRuleRegistry.registerBoolean("preventBlockFalling", false);
    public static final CreativeModeTabHolder CREATIVE_MODE_TAB = Registry.createCreativeModeTabHolder("tjmetro_tab", () -> new ItemStack(new ItemConvertible(BlockList.LOGO.get().data)));

    public static void init() {
        LOGGER.info("--------------- " + Reference.NAME + " ---------------");
        LOGGER.info("Hello from ZiYueCommentary!");
        LOGGER.info("Mod ID: " + Reference.MOD_ID);
        LOGGER.info("Version: " + Reference.VERSION);

        Registry.REGISTRY_TABS.init();

        BlockList.registerBlocks();
        ItemList.registerItems();
        BlockEntityTypes.registerBlockEntities();
        EntityTypes.registerEntities();

        Registry.setupPackets("packet");
        Registry.registerPacket(PacketOpenBlockEntityScreen.class, PacketOpenBlockEntityScreen::new);
        Registry.registerPacket(PacketUpdateRoadblockContent.class, PacketUpdateRoadblockContent::new);
        Registry.registerPacket(PacketUpdateCustomColor.class, PacketUpdateCustomColor::new);
        Registry.registerPacket(PacketUpdateRailwaySignConfig.class, PacketUpdateRailwaySignConfig::new);
        Registry.registerPacket(PacketUpdateRailwaySignDoubleConfig.class, PacketUpdateRailwaySignDoubleConfig::new);

        Registry.init();
    }
}
