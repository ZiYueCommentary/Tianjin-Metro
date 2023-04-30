package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevent render bench-seat-minecart.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.blocks.BlockBench
 * @see ziyue.tjmetro.blocks.BlockBench.TileEntityBench
 * @since beta-1
 */

@Mixin(MinecartRenderer.class)
public abstract class MinecartRendererMixin<T extends AbstractMinecart> extends EntityRenderer<T>
{
    protected MinecartRendererMixin(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/world/entity/vehicle/AbstractMinecart;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", cancellable = true)
    private void beforeRender(T abstractMinecart, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (abstractMinecart.getCustomName() != null) {
            if (abstractMinecart.getCustomName().getContents().equals("BenchSeat")) ci.cancel();
        }
    }
}
