package ziyue.tjmetro.forge;

import mtr.client.ClientData;
import mtr.mappings.RegistryUtilitiesClient;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.TianjinMetro;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;

public class RegistryClientImpl
{
    public static void registerCustomColorBlock(Block block) {
        RegistryUtilitiesClient.registerBlockColors(new StationColor(), block);
    }

    private static class StationColor implements BlockColor
    {
        @Override
        public int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos pos, int i) {
            try {
                final int color = ((BlockCustomColorBase.CustomColorBlockEntity) blockAndTintGetter.getBlockEntity(pos)).color;
                if (color != -1) return color;
            } catch (NullPointerException ignored) {
            } catch (Exception e) {
                TianjinMetro.LOGGER.error(blockAndTintGetter + ": Get custom color failed!");
            }
            final int defaultColor = 0x7F7F7F;
            if (pos != null) {
                try {
                    return ClientData.STATIONS.stream().filter(station1 -> station1.inArea(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.color).orElse(defaultColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return defaultColor;
        }
    }
}
