package ziyue.tjmetro.mod.mixin;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.SignResource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.mod.block.base.IRailwaySign;

@Mixin(CustomResourceLoader.class)
public abstract class CustomResourceLoaderMixin
{
    @Shadow(remap = false)
    @Final
    private static Object2ObjectAVLTreeMap<String, SignResource> SIGNS_CACHE;

    @Shadow(remap = false)
    @Final
    private static ObjectArrayList<SignResource> SIGNS;

    @Inject(at = @At("TAIL"), method = "reload", remap = false)
    private static void afterReload(CallbackInfo ci) {
        // In legacy mod, the tjmetro signs will order after vanilla signs. Because vanilla signs was stored in seperated set.
        // In 4.0.0, custom signs and vanilla signs are stored in the same set. So tjmetro signs will order before vanilla signs.
        for (IRailwaySign.SignType value : IRailwaySign.SignType.values()) {
            SIGNS.add(value.sign);
            SIGNS_CACHE.put(value.signId, value.sign);
        }
    }
}
