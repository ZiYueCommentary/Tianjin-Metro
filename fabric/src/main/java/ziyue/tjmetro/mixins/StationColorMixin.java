package ziyue.tjmetro.mixins;

import mtr.fabric.RegistryClientImpl;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 * @see RegistryClientImpl
 */

@Mixin(RegistryClientImpl.class)
public class StationColorMixin
{
    /**
     * @author ZiYueCommentary
     * @reason Core of <b>custom display color</b>.
     */
    @Overwrite
    public static void registerBlockColors(Block block) {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> 0x55efc4, block);
    }
}
