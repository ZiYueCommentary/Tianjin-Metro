package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.blocks.BlockBench;

/**
 * Prevent render shadows for Bench Seats.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.blocks.BlockBench.TileEntityBench
 * @see ziyue.tjmetro.blocks.BlockBench
 * @since beta-1
 */

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin
{
    @Inject(at = @At("HEAD"), method = "renderShadow", cancellable = true)
    private static void beforeRenderShadow(PoseStack poseStack, MultiBufferSource multiBufferSource, Entity entity, float f, float g, LevelReader levelReader, float h, CallbackInfo ci) {
        if (entity.getCustomName() != null) {
            if (entity.getCustomName().getContents().equals(BlockBench.ENTITY_SEAT_NAME)) ci.cancel();
        }
    }
}
