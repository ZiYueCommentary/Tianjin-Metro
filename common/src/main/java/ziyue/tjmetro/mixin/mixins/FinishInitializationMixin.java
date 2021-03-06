package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.Reference;

import static ziyue.tjmetro.TianjinMetro.LOGGER;

/**
 * Print info about <b>Tianjin Metro mod</b>, refresh configs at same time.<br>
 * This mixin is for testing originally.
 *
 * @author ZiYueCommentary
 * @see Reference
 * @see RenderSystem
 * @since 1.0b
 */

@Mixin(RenderSystem.class)
public abstract class FinishInitializationMixin
{
    @Inject(at = @At("TAIL"), method = "finishInitialization")
    private static void init(CallbackInfo callbackInfo) {
        LOGGER.info("----------------" + Reference.NAME + "----------------");
        LOGGER.info("Hello from ZiYueCommentary!");
        LOGGER.info("Mod ID: " + Reference.MOD_ID);
        LOGGER.info("Version: " + Reference.VERSION);
        //Configs.refreshProperties();
    }
}
