package ziyue.tjmetro;

import mtr.Blocks;
import mtr.RegistryObject;
import mtr.block.BlockPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ziyue.tjmetro.blocks.*;
import ziyue.tjmetro.blocks.base.StairBlock;

/**
 * @since 1.0b
 */

public interface BlockList
{
    RegistryObject<Block> LOGO = new RegistryObject<>(() -> new BlockLogo(BlockBehaviour.Properties.copy(Blocks.LOGO.get()).noCollission().lightLevel((state) -> 0)));
    RegistryObject<Block> ROLLING = new RegistryObject<>(() -> new BlockRolling(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.IRON_BARS)));
    RegistryObject<Block> CEILING_NOT_LIT = new RegistryObject<>(() -> new BlockCeilingNotLit(BlockBehaviour.Properties.copy(Blocks.CEILING.get()).lightLevel((state) -> 0)));
    RegistryObject<Block> STATION_COLOR_CEILING = new RegistryObject<>(() -> new BlockStationColorCeilingAuto(BlockBehaviour.Properties.copy(Blocks.CEILING.get())));
    RegistryObject<Block> STATION_COLOR_CEILING_LIGHT = new RegistryObject<>(() -> new BlockStationColorCeiling(BlockBehaviour.Properties.copy(Blocks.CEILING_LIGHT.get())));
    RegistryObject<Block> STATION_COLOR_CEILING_NO_LIGHT = new RegistryObject<>(() -> new BlockStationColorCeiling(BlockBehaviour.Properties.copy(Blocks.CEILING_NO_LIGHT.get())));
    RegistryObject<Block> STATION_COLOR_CEILING_NOT_LIT = new RegistryObject<>(() -> new BlockStationColorCeilingNotLit(BlockBehaviour.Properties.copy(Blocks.CEILING_NO_LIGHT.get())));
    RegistryObject<Block> STATION_NAME_SIGN_1 = new RegistryObject<>(() -> new BlockStationNameSign1(BlockBehaviour.Properties.copy(Blocks.STATION_NAME_TALL_WALL.get())));
    RegistryObject<Block> STATION_NAME_SIGN_2 = new RegistryObject<>(() -> new BlockStationNameSign2(BlockBehaviour.Properties.copy(Blocks.STATION_NAME_TALL_WALL.get())));
    RegistryObject<Block> ROADBLOCK = new RegistryObject<>(() -> new BlockRoadblock(BlockBehaviour.Properties.copy(Blocks.LOGO.get()).lightLevel((state) -> 0)));
    RegistryObject<Block> ROADBLOCK_SIGN = new RegistryObject<>(() -> new BlockRoadblockSign(BlockBehaviour.Properties.copy(Blocks.LOGO.get()).lightLevel((state) -> 0)));
    RegistryObject<Block> BENCH = new RegistryObject<>(() -> new BlockBench(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_PLANKS)));
    RegistryObject<Block> PLATFORM_TJ_1 = new RegistryObject<>(() -> new BlockPlatform(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2), false));
    RegistryObject<Block> PLATFORM_TJ_1_INDENTED = new RegistryObject<>(() -> new BlockPlatform(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).noOcclusion(), true));
    RegistryObject<Block> MARBLE_GRAY = new RegistryObject<>(() -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(1)));
    RegistryObject<Block> MARBLE_GRAY_SLAB = new RegistryObject<>(() -> new SlabBlock(BlockBehaviour.Properties.copy(BlockList.MARBLE_GRAY.get())));
    RegistryObject<Block> MARBLE_GRAY_STAIRS = new RegistryObject<>(() -> new StairBlock(MARBLE_GRAY.get()));
    RegistryObject<Block> PLATFORM_TJ_2 = new RegistryObject<>(() -> new BlockPlatform(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2), false));
    RegistryObject<Block> PLATFORM_TJ_2_INDENTED = new RegistryObject<>(() -> new BlockPlatform(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).noOcclusion(), true));
    RegistryObject<Block> MARBLE_YELLOW = new RegistryObject<>(() -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_YELLOW).requiresCorrectToolForDrops().strength(1)));
    RegistryObject<Block> MARBLE_YELLOW_SLAB = new RegistryObject<>(() -> new SlabBlock(BlockBehaviour.Properties.copy(MARBLE_YELLOW.get())));
    RegistryObject<Block> MARBLE_YELLOW_STAIRS = new RegistryObject<>(() -> new StairBlock(MARBLE_YELLOW.get()));
    RegistryObject<Block> STATION_NAME_WALL_LEGACY = new RegistryObject<>(() -> new BlockStationNameWallLegacy(BlockBehaviour.Properties.copy(mtr.Blocks.STATION_NAME_WALL_WHITE.get()).noCollission()));
    RegistryObject<Block> CUSTOM_COLOR_CONCRETE = new RegistryObject<>(() -> new BlockCustomColorConcrete(BlockBehaviour.Properties.copy(Blocks.STATION_COLOR_CONCRETE.get())));
    RegistryObject<Block> CUSTOM_COLOR_CONCRETE_STAIRS = new RegistryObject<>(() -> new BlockCustomColorConcreteStairs(Blocks.STATION_COLOR_CONCRETE.get()));
    RegistryObject<Block> CUSTOM_COLOR_CONCRETE_SLAB = new RegistryObject<>(() -> new BlockCustomColorConcreteSlab(BlockBehaviour.Properties.copy(Blocks.STATION_COLOR_CONCRETE.get())));
    RegistryObject<Block> APG_CORNER = new RegistryObject<>(() -> new BlockAPGCorner(BlockBehaviour.Properties.copy(Blocks.APG_GLASS.get())));
    RegistryObject<Block> PLAYER_DETECTOR = new RegistryObject<>(() -> new BlockPlayerDetector(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.STONE_PRESSURE_PLATE)));
    RegistryObject<Block> DECORATION_LIGHT = new RegistryObject<>(() -> new BlockDecorationLight(BlockBehaviour.Properties.copy(Blocks.CEILING_LIGHT.get()).noCollission().lightLevel((state) -> 15)));
    RegistryObject<Block> HIGH_SPEED_REPEATER = new RegistryObject<>(() -> new BlockHighSpeedRepeater(BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.REPEATER)));
    RegistryObject<Block> TIME_DISPLAY = new RegistryObject<>(() -> new BlockTimeDisplay(BlockBehaviour.Properties.copy(Blocks.CLOCK.get()).lightLevel(state -> 5)));
    RegistryObject<Block> EMERGENCY_EXIT_SIGN = new RegistryObject<>(() -> new BlockEmergencyExitSign(BlockBehaviour.Properties.copy(Blocks.STATION_COLOR_POLE.get()).lightLevel(state -> 5)));
    RegistryObject<Block> RAILWAY_SIGN_WALL_2 = new RegistryObject<>(() -> new BlockRailwaySignWall(2));
    RegistryObject<Block> RAILWAY_SIGN_WALL_3 = new RegistryObject<>(() -> new BlockRailwaySignWall(3));
    RegistryObject<Block> RAILWAY_SIGN_WALL_4 = new RegistryObject<>(() -> new BlockRailwaySignWall(4));
    RegistryObject<Block> RAILWAY_SIGN_WALL_5 = new RegistryObject<>(() -> new BlockRailwaySignWall(5));
    RegistryObject<Block> RAILWAY_SIGN_WALL_6 = new RegistryObject<>(() -> new BlockRailwaySignWall(6));
    RegistryObject<Block> RAILWAY_SIGN_WALL_7 = new RegistryObject<>(() -> new BlockRailwaySignWall(7));
    RegistryObject<Block> RAILWAY_SIGN_WALL_MIDDLE = new RegistryObject<>(() -> new BlockRailwaySignWall(0));
    RegistryObject<Block> METAL_DETECTION_DOOR = new RegistryObject<>(() -> new BlockMetalDetectionDoor(BlockBehaviour.Properties.copy(Blocks.LOGO.get()).noCollission()));
}
