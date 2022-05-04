package ziyue.tjmetro.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.Reference;

@Mixin(RenderSystem.class)
public class FinishInitializationMixin
{
    @Inject(at = @At("TAIL"), method = "finishInitialization")
    private static void init(CallbackInfo callbackInfo) {
        System.out.println("----------------" + Reference.NAME + "----------------");
        System.out.println("Hello from ZiYueCommentary!");
        System.out.println("ModID: " + Reference.MOD_ID);
        System.out.println("Version: " + Reference.VERSION);
    }
}
