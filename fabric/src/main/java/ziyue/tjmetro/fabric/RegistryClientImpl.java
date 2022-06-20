package ziyue.tjmetro.fabric;

import mtr.client.ClientData;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.level.block.Block;
import ziyue.tjmetro.TianjinMetro;
import ziyue.tjmetro.blocks.base.CustomColorBlockEntity;

public class RegistryClientImpl
{
    public static void registerCustomColorBlock(Block block) {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            try {
                final int color = ((CustomColorBlockEntity) world.getBlockEntity(pos)).color;
                if (color != -1) return color;
            } catch (NullPointerException ignored) {
            } catch (Exception e) {
                TianjinMetro.LOGGER.error(block + ": Get custom color failed!");
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
        }, block);
    }
}
