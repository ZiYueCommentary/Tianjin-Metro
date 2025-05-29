package ziyue.tjmetro.mod.mixin;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.render.DynamicVehicleModel;
import org.mtr.mod.render.StoredMatrixTransformations;
import org.mtr.mod.resource.OptimizedModelWrapper;
import org.mtr.mod.resource.PartCondition;
import org.mtr.mod.resource.VehicleResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.mod.config.ConfigClient;

/**
 * @author ZiYueCommentary
 * @see VehicleResource
 * @since 1.0.0-beta-5
 */

@Mixin(VehicleResource.class)
public abstract class VehicleResourceMixin
{
    @Inject(at = @At("HEAD"), method = "queue(Lorg/mtr/libraries/it/unimi/dsi/fastutil/objects/Object2ObjectOpenHashMap;Lorg/mtr/mod/render/StoredMatrixTransformations;Lorg/mtr/mod/data/VehicleExtension;IZ)V", cancellable = true, remap = false)
    private static void beforeQueue(Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, boolean noOpenDoorways, CallbackInfo ci) {
        if (ConfigClient.DISABLE_TRAIN_RENDERING.get()) ci.cancel();
    }
}
