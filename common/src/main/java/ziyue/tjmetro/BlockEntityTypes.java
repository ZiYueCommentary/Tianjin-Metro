package ziyue.tjmetro;

import mtr.RegistryObject;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ziyue.tjmetro.blocks.BlockStationNameSign1;
import ziyue.tjmetro.blocks.BlockStationNameSign2;
import ziyue.tjmetro.blocks.BlockStationNameTallDoubleSided;

public interface BlockEntityTypes
{
    RegistryObject<BlockEntityType<BlockStationNameSign1.TileEntityStationNameWall>> STATION_NAME_SIGN_ENTITY_1 = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(BlockStationNameSign1.TileEntityStationNameWall::new, BlockList.STATION_NAME_SIGN_1.get()));
    RegistryObject<BlockEntityType<BlockStationNameSign2.TileEntityStationNameWall>> STATION_NAME_SIGN_ENTITY_2 = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(BlockStationNameSign2.TileEntityStationNameWall::new, BlockList.STATION_NAME_SIGN_2.get()));
    RegistryObject<BlockEntityType<BlockStationNameTallDoubleSided.TileEntityStationNameTallBlock>> STATION_NAME_TALL_BLOCK_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(BlockStationNameTallDoubleSided.TileEntityStationNameTallBlock::new, BlockList.STATION_NAME_TALL_DOUBLE_SIDED.get()));
}
