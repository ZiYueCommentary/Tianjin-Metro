package ziyue.tjmetro.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.client.Config;
import ziyue.tjmetro.Reference;
import ziyue.tjmetro.client.RouteMapGenerator;

import static ziyue.tjmetro.TianjinMetro.LOGGER;

/**
 * Print references about <b>Tianjin Metro mod</b>.
 * This mixin is for testing originally.
 *
 * @author ZiYueCommentary
 * @see Reference
 * @see RenderSystem
 * @since beta-1
 */

@Mixin(RenderSystem.class)
public abstract class FinishInitializationMixin
{
    @Inject(at = @At("HEAD"), method = "finishInitialization")
    private static void beforeFinishInitialization(CallbackInfo callbackInfo) {
        LOGGER.info("--------------- " + Reference.NAME + " ---------------");
        LOGGER.info("Hello from ZiYueCommentary!");
        LOGGER.info("Mod ID: " + Reference.MOD_ID);
        LOGGER.info("Version: " + Reference.VERSION);
        LOGGER.info("Modloader: " + (mtr.Registry.isFabric() ? "Fabric" : "Forge"));

        Config.refreshProperties();
        RouteMapGenerator.setConstants();
    }
}
