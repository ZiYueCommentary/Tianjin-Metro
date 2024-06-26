package ziyue.tjmetro.mixin;

import mtr.client.CustomResources;
import mtr.client.ICustomResources;
import mtr.client.IResourcePackCreatorProperties;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.block.base.IRailwaySign;
import ziyue.tjmetro.client.ClientCache;

import static mtr.client.CustomResources.CUSTOM_SIGNS;

/**
 * @author ZiYueCommentary
 * @since beta-1
 * @see CustomResources
 */

@Mixin(CustomResources.class)
public abstract class CustomResourcesMixin implements IResourcePackCreatorProperties, ICustomResources
{
    @Inject(at = @At("HEAD"), method = "reload", remap = false)
    private static void beforeReload(ResourceManager manager, CallbackInfo ci){
        ClientCache.DATA_CACHE.resetFonts();
    }

    @Inject(at = @At("TAIL"), method = "reload", remap = false)
    private static void afterReload(ResourceManager manager, CallbackInfo ci){
        for (IRailwaySign.SignType value : IRailwaySign.SignType.values()) {
            CUSTOM_SIGNS.put(value.signId, value.sign);
        }
    }
}
