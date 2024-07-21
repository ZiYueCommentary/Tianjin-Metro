package ziyue.tjmetro.mod.mixin;

import org.mtr.mod.client.RouteMapGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RouteMapGenerator.class)
public abstract class RouteMapGeneratorMixin
{
    @Inject(at = @At("TAIL"), method = "setConstants", remap = false)
    private static void afterSetConstants(CallbackInfo ci) {
        ziyue.tjmetro.mod.client.RouteMapGenerator.setConstants();
    }
}
