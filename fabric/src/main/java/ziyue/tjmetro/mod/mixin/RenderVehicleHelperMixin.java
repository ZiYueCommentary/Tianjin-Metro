package ziyue.tjmetro.mod.mixin;

import org.mtr.mod.render.RenderVehicleHelper;
import org.mtr.mod.render.RenderVehicleTransformationHelper;
import org.mtr.mod.render.StoredMatrixTransformations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.mod.config.ConfigClient;

import java.util.function.Consumer;

@Mixin(RenderVehicleHelper.class)
public abstract class RenderVehicleHelperMixin
{
    @Inject(at = @At("HEAD"), method = "renderModel", cancellable = true, remap = false)
    private static void beforeRender(RenderVehicleTransformationHelper renderVehicleTransformationHelper, double oscillationAmount, Consumer<StoredMatrixTransformations> render, CallbackInfo ci) {
        if (ConfigClient.DISABLE_TRAIN_RENDERING.get()) ci.cancel();
    }
}
