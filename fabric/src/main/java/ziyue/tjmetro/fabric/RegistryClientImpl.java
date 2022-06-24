package ziyue.tjmetro.fabric;

import mtr.client.ClientData;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.level.block.Block;

public class RegistryClientImpl
{
    public static void registerCustomColorBlock(Block block) {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            final int defaultColor = 0x7F7F7F;
            int color;
            if (pos != null) {
                try {
                    color = -1;
                    return color != -1 ? color : ClientData.STATIONS.stream().filter(station1 -> station1.inArea(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.color).orElse(defaultColor);
                } catch (Exception ignored) {
                }
            }
            return defaultColor;
        }, block);
    }
}
