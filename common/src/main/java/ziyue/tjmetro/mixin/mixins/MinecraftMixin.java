package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.platform.WindowEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.SnooperPopulator;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.Config;
import ziyue.tjmetro.mixin.properties.ConfigScreenProperties;

/**
 * Redirect the old MTR config screen to the experimental config screen.
 *
 * @author ZiYueCommentary
 * @see Config
 * @see ConfigScreenMixin
 * @see ConfigScreenProperties
 * @since beta-1
 */
@Mixin(Minecraft.class)
public abstract class MinecraftMixin extends ReentrantBlockableEventLoop<Runnable> implements SnooperPopulator, WindowEventHandler
{
    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    public MinecraftMixin(String string) {
        super(string);
    }

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    private void beforeSetScreen(Screen screen, CallbackInfo ci) {
        if (Config.EXPERIMENTAL_MTR_CONFIG_SCREEN.get()) {
            if (screen instanceof ConfigScreenProperties properties) {
                this.setScreen(Config.getConfigScreen(properties.hasTimeAndWindControls(), properties.useTimeAndWindSync(), false));
                ci.cancel();
            }
        }
    }
}
