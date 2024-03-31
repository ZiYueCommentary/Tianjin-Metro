package ziyue.tjmetro.mixin.mixins;

import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.screen.ConfigScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.client.ClientCache;

/**
 * @author ZiYueCommentary
 * @see ConfigScreen
 * @since beta-1
 */

@Mixin(ConfigScreen.class)
public abstract class ConfigScreenMixin extends ScreenMapper implements IGui
{
    protected ConfigScreenMixin(Component title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "onClose")
    private void afterOnClose(CallbackInfo ci) {
        ClientCache.DATA_CACHE.sync();
        ClientCache.DATA_CACHE.refreshDynamicResources();
    }
}
