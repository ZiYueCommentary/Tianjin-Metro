package ziyue.tjmetro.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ziyue.tjmetro.mapping.BooleanGameRule;
import ziyue.tjmetro.mapping.GameRuleRegistry;
import ziyue.tjmetro.mod.packet.*;

/**
 * @since 1.0.0-beta-1
 */

public final class TianjinMetro
{
    public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);

    public static final BooleanGameRule NO_FALLING_BLOCK = GameRuleRegistry.registerBoolean("preventBlockFalling", false);

    public static void init() {
        LOGGER.info("--------------- " + Reference.NAME + " ---------------");
        LOGGER.info("Hello from ZiYueCommentary!");
        LOGGER.info("Mod ID: " + Reference.MOD_ID);
        LOGGER.info("Version: " + Reference.VERSION);

        CreativeModeTabs.registerCreativeModeTabs();
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
