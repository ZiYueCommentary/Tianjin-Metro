package ziyue.tjmetro.mod.mixin;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.PositionAndRotation;
import org.mtr.mod.render.RenderVehicles;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.mod.config.ConfigClient;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-prerelease-1
 */

@Mixin(RenderVehicles.class)
public abstract class RenderVehiclesMixin implements IGui
{
    @Inject(at = @At("HEAD"), method = "renderConnection", cancellable = true, remap = false)
    private static void beforeRenderConnection(boolean shouldRender1, boolean shouldRender2, boolean canHaveLight, RenderVehicles.PreviousConnectionPositions previousConnectionPositions, Identifier innerSideTexture, Identifier innerTopTexture, Identifier innerBottomTexture, Identifier outerSideTexture, Identifier outerTopTexture, Identifier outerBottomTexture, PositionAndRotation positionAndRotation, boolean useOffset, double vehicleLength, double width, double height, double yOffset, double zOffset, double oscillationAmount, boolean isOnRoute, CallbackInfo ci) {
        if (ConfigClient.DISABLE_TRAIN_RENDERING.get()) ci.cancel();
    }
}
