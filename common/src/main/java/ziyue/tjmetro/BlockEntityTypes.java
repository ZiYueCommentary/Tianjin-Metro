package ziyue.tjmetro;

import mtr.RegistryObject;
import mtr.mappings.RegistryUtilities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import ziyue.tjmetro.blocks.*;

import java.util.Collections;

/**
 * @since 1.0b
 */

public interface BlockEntityTypes
{
    RegistryObject<BlockEntityType<BlockStationNameSign1.TileEntityStationNameWall>> STATION_NAME_SIGN_ENTITY_1 = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(BlockStationNameSign1.TileEntityStationNameWall::new, BlockList.STATION_NAME_SIGN_1.get()));
    RegistryObject<BlockEntityType<BlockStationNameSign2.TileEntityStationNameWall>> STATION_NAME_SIGN_ENTITY_2 = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(BlockStationNameSign2.TileEntityStationNameWall::new, BlockList.STATION_NAME_SIGN_2.get()));
    RegistryObject<BlockEntityType<BlockRoadblockSign.TileEntityRoadBlockSign>> ROADBLOCK_SIGN_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(BlockRoadblockSign.TileEntityRoadBlockSign::new, BlockList.ROADBLOCK_SIGN.get()));
    RegistryObject<BlockEntityType<BlockStationNameWallLegacy.TileEntityStationNameWall>> STATION_NAME_WALL_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(BlockStationNameWallLegacy.TileEntityStationNameWall::new, BlockList.STATION_NAME_WALL_LEGACY.get()));
    RegistryObject<BlockEntityType<BlockCustomColorConcrete.CustomColorConcreteEntity>> STATION_COLOR_CONCRETE_TILE_ENTITY = new RegistryObject<>(() -> RegistryUtilities.getBlockEntityType(BlockCustomColorConcrete.CustomColorConcreteEntity::new, BlockList.CUSTOM_COLOR_CONCRETE.get()));
    RegistryObject<BlockEntityType<BlockPlayerDetector.TileEntityPlayerDetector>> PLAYER_DETECTOR_TILE_ENTITY = new RegistryObject<>(() -> new BlockEntityType<>(BlockPlayerDetector.TileEntityPlayerDetector::new, Collections.singleton(BlockList.PLAYER_DETECTOR.get()), null));
}