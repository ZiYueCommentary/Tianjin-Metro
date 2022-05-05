package ziyue.tjmetro;

import mtr.RegistryClient;
import net.minecraft.client.renderer.RenderType;
import ziyue.tjmetro.render.RenderStationNameSign;
import ziyue.tjmetro.render.RenderStationNameTall;

public class MainClient
{
	public static void init() {
		RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.LOGO.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.STATION_NAME_SIGN_1.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.STATION_NAME_SIGN_2.get());
		RegistryClient.registerBlockRenderType(RenderType.cutout(), BlockList.STATION_NAME_TALL_DOUBLE_SIDED.get());
		RegistryClient.registerBlockRenderType(RenderType.translucent(), BlockList.ROLLING.get());

		RegistryClient.registerBlockColors(BlockList.STATION_COLOR_CEILING.get());
		RegistryClient.registerBlockColors(BlockList.STATION_COLOR_CEILING_LIGHT.get());
		RegistryClient.registerBlockColors(BlockList.STATION_COLOR_CEILING_NO_LIGHT.get());
		RegistryClient.registerBlockColors(BlockList.STATION_NAME_SIGN_1.get());
		RegistryClient.registerBlockColors(BlockList.STATION_NAME_TALL_DOUBLE_SIDED.get());

		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_SIGN_ENTITY_1.get(), RenderStationNameSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_SIGN_ENTITY_2.get(), RenderStationNameSign::new);
		RegistryClient.registerTileEntityRenderer(BlockEntityTypes.STATION_NAME_TALL_BLOCK_TILE_ENTITY.get(), RenderStationNameTall::new);
	}
}
