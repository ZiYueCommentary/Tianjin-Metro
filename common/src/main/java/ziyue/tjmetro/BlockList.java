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
    RegistryObject<Block> LOGO = new RegistryObject<>(BlockLogo::new);
    RegistryObject<Block> ROLLING = new RegistryObject<>(BlockRolling::new);
    RegistryObject<Block> CEILING_NOT_LIT = new RegistryObject<>(BlockCeilingNotLit::new);
    RegistryObject<Block> STATION_COLOR_CEILING = new RegistryObject<>(() -> new BlockStationColorCeilingAuto(BlockBehaviour.Properties.copy(Blocks.CEILING.get())));
    RegistryObject<Block> STATION_COLOR_CEILING_LIGHT = new RegistryObject<>(() -> new BlockStationColorCeiling(BlockBehaviour.Properties.copy(Blocks.CEILING_LIGHT.get())));
    RegistryObject<Block> STATION_COLOR_CEILING_NO_LIGHT = new RegistryObject<>(() -> new BlockStationColorCeiling(BlockBehaviour.Properties.copy(Blocks.CEILING_NO_LIGHT.get())));
    RegistryObject<Block> STATION_COLOR_CEILING_NOT_LIT = new RegistryObject<>(BlockStationColorCeilingNotLit::new);
    RegistryObject<Block> STATION_NAME_SIGN_1 = new RegistryObject<>(BlockStationNameSign1::new);
    RegistryObject<Block> STATION_NAME_SIGN_2 = new RegistryObject<>(BlockStationNameSign2::new);
    RegistryObject<Block> ROADBLOCK = new RegistryObject<>(BlockRoadblock::new);
    RegistryObject<Block> ROADBLOCK_SIGN = new RegistryObject<>(BlockRoadblockSign::new);
    RegistryObject<Block> BENCH = new RegistryObject<>(BlockBench::new);
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
    RegistryObject<Block> STATION_NAME_WALL_LEGACY = new RegistryObject<>(BlockStationNameWallLegacy::new);
    RegistryObject<Block> CUSTOM_COLOR_CONCRETE = new RegistryObject<>(BlockCustomColorConcrete::new);
    RegistryObject<Block> CUSTOM_COLOR_CONCRETE_STAIRS = new RegistryObject<>(BlockCustomColorConcreteStairs::new);
    RegistryObject<Block> CUSTOM_COLOR_CONCRETE_SLAB = new RegistryObject<>(BlockCustomColorConcreteSlab::new);
    RegistryObject<Block> APG_CORNER = new RegistryObject<>(BlockAPGCorner::new);
    RegistryObject<Block> PLAYER_DETECTOR = new RegistryObject<>(BlockPlayerDetector::new);
    RegistryObject<Block> DECORATION_LIGHT = new RegistryObject<>(BlockDecorationLight::new);
    RegistryObject<Block> HIGH_SPEED_REPEATER = new RegistryObject<>(BlockHighSpeedRepeater::new);
    RegistryObject<Block> TIME_DISPLAY = new RegistryObject<>(BlockTimeDisplay::new);
}
