package ziyue.tjmetro.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.Reference;

@Mixin(RenderSystem.class)
public class FinishInitializationMixin
{
    @Shadow @Final private static Logger LOGGER;

    @Inject(at = @At("TAIL"), method = "finishInitialization")
    private static void init(CallbackInfo callbackInfo) {
        LOGGER.info("----------------" + Reference.NAME + "----------------");
        LOGGER.info("Hello from ZiYueCommentary!");
        LOGGER.info("ModID: " + Reference.MOD_ID);
        LOGGER.info("Version: " + Reference.VERSION);
    }
}
