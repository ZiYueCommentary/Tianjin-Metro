package ziyue.tjmetro.mod.mixin;

import org.mtr.mod.client.DynamicTextureCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author ZiYueCommentary
 * @see DynamicTextureCache
 * @since 1.0.0-beta-1
 */

@Mixin(DynamicTextureCache.class)
public abstract class DynamicTextureCacheMixin
{
    @Inject(at = @At("TAIL"), method = "reload", remap = false)
    private void afterReload(CallbackInfo ci) {
        ziyue.tjmetro.mod.client.DynamicTextureCache.instance.reload();
        // If a Tianjin Metro sign gets text with MTR font and the MTR fonts are not initialized, the game will throw a NullPointerException.
        // This will happen in case of Tianjin Metro sign is rendered before the MTR signs. To fix this, here we invoke whatever thing to ensure that MTR fonts are initialized.
        DynamicTextureCache.instance.getPixelatedText("", 0, 10, 0, false);
    }

    @Inject(at = @At("TAIL"), method = "tick", remap = false)
    private void afterTick(CallbackInfo ci) {
        ziyue.tjmetro.mod.client.DynamicTextureCache.instance.tick();
    }
}
