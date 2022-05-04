package ziyue.tjmetro;

import mtr.Blocks;
import mtr.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import ziyue.tjmetro.blocks.*;

public interface BlockList
{
    RegistryObject<Block> LOGO = new RegistryObject<>(BlockLogo::new);
    RegistryObject<Block> ROLLING = new RegistryObject<>(BlockRolling::new);
    RegistryObject<Block> STATION_COLOR_CEILING = new RegistryObject<>(() -> new BlockStationColorCeilingAuto(BlockBehaviour.Properties.copy(Blocks.CEILING.get())));
    RegistryObject<Block> STATION_COLOR_CEILING_LIGHT = new RegistryObject<>(() -> new BlockStationColorCeiling(BlockBehaviour.Properties.copy(Blocks.CEILING_LIGHT.get())));
    RegistryObject<Block> STATION_COLOR_CEILING_NO_LIGHT = new RegistryObject<>(() -> new BlockStationColorCeiling(BlockBehaviour.Properties.copy(Blocks.CEILING_NO_LIGHT.get())));
    RegistryObject<Block> STATION_NAME_SIGN_1 = new RegistryObject<>(BlockStationNameSign1::new);
    RegistryObject<Block> STATION_NAME_SIGN_2 = new RegistryObject<>(BlockStationNameSign2::new);
}
