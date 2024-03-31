package ziyue.tjmetro;

import mtr.RegistryClient;
import mtr.render.RenderPSDAPGDoor;
import mtr.render.RenderPSDTop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import ziyue.tjmetro.client.ClientCache;
import ziyue.tjmetro.client.RouteMapGenerator;
import ziyue.tjmetro.packet.IPacket;
import ziyue.tjmetro.packet.PacketGuiClient;
import ziyue.tjmetro.render.*;

/**
 * Client Entry of <b>Tianjin Metro Mod</b>.
 *
 * @see TianjinMetro
 * @since beta-1
 */

public class TianjinMetroClient
{
    public static void init() {
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.LOGO.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.STATION_NAME_SIGN_1.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.STATION_NAME_SIGN_2.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.PLATFORM_TJ_1.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.PLATFORM_TJ_1_INDENTED.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.PLATFORM_TJ_2.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.PLATFORM_TJ_2_INDENTED.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.APG_CORNER.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.HIGH_SPEED_REPEATER.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.EMERGENCY_EXIT_SIGN.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.SERVICE_CORRIDOR_SIGN.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.PSD_DOOR_TIANJIN.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.PSD_GLASS_TIANJIN.get());
        RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.PSD_GLASS_END_TIANJIN.get());
        RegistryClient.registerBlockRenderType(RenderType.translucent(), BlockList.ROLLING.get());

        RegistryClient.registerBlockColors(BlockList.STATION_COLOR_CEILING.get());
        RegistryClient.registerBlockColors(BlockList.STATION_COLOR_CEILING_LIGHT.get());
        RegistryClient.registerBlockColors(BlockList.STATION_COLOR_CEILING_NO_LIGHT.get());
        RegistryClient.registerBlockColors(BlockList.STATION_NAME_SIGN_1.get());
        RegistryClient.registerBlockColors(BlockList.STATION_COLOR_CEILING_NOT_LIT.get());

        Registry.registerCustomColorBlock(BlockList.CUSTOM_COLOR_CONCRETE.get());
        Registry.registerCustomColorBlock(BlockList.CUSTOM_COLOR_CONCRETE_STAIRS.get());
        Registry.registerCustomColorBlock(BlockList.CUSTOM_COLOR_CONCRETE_SLAB.get());

        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_SIGN_ENTITY_1.get(), RenderStationNameSign::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_SIGN_ENTITY_2.get(), RenderStationNameSign::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.ROADBLOCK_SIGN_ENTITY.get(), RenderRoadblockSign::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_WALL_LEGACY_TILE_ENTITY.get(), RenderStationNameWallLegacy::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.TIME_DISPLAY_TILE_ENTITY.get(), RenderTimeDisplay::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_4_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_6_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_8_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_10_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_4_TILE_ENTITY.get(), RenderRailwaySignWallDouble::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_6_TILE_ENTITY.get(), RenderRailwaySignWallDouble::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_8_TILE_ENTITY.get(), RenderRailwaySignWallDouble::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_10_TILE_ENTITY.get(), RenderRailwaySignWallDouble::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_2_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_3_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_4_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_5_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_6_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_7_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_8_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_9_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_10_TILE_ENTITY.get(), RenderRailwaySignWall::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_2_EVEN_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_EVEN_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_EVEN_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_EVEN_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_EVEN_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_EVEN_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_2_ODD_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_ODD_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_ODD_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_ODD_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_ODD_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_ODD_TILE_ENTITY.get(), RenderRailwaySignTianjin::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.SERVICE_CORRIDOR_SIGN_TILE_ENTITY.get(), RenderServiceCorridorSign::new);
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_DOOR_TIANJIN_TILE_ENTITY.get(), dispatcher -> new RenderPSDAPGDoor<>(dispatcher, 0));
        RegistryClient.registerTileEntityRenderer(BlockEntityTypes.PSD_TOP_TIANJIN_TILE_ENTITY.get(), RenderPSDTopTianjin::new);

        RegistryClient.registerNetworkReceiver(IPacket.PACKET_OPEN_CUSTOM_CONTENT_SCREEN, packet -> PacketGuiClient.openCustomContentScreenS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(IPacket.PACKET_OPEN_CUSTOM_COLOR_SCREEN, packet -> PacketGuiClient.openCustomColorScreenS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(IPacket.PACKET_OPEN_RAILWAY_SIGN_SCREEN, packet -> PacketGuiClient.openRailwaySignScreenS2C(Minecraft.getInstance(), packet));
        RegistryClient.registerNetworkReceiver(IPacket.PACKET_OPEN_RAILWAY_SIGN_WALL_DOUBLE_SCREEN, packet -> PacketGuiClient.openRailwaySignWallDoubleScreenS2C(Minecraft.getInstance(), packet));
    }
}
