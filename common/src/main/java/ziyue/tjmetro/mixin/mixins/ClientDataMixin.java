package ziyue.tjmetro.mixin.mixins;

import mtr.client.ClientData;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.client.ClientCache;

/**
 * @author ZiYueCommentary
 * @see ClientData
 * @since beta-1
 */

@Mixin(ClientData.class)
public abstract class ClientDataMixin
{
    @Inject(at = @At("TAIL"), method = "receivePacket")
    private static void afterReceivePacket(FriendlyByteBuf packet, CallbackInfo ci) {
        ClientCache.DATA_CACHE.sync();
        ClientCache.DATA_CACHE.refreshDynamicResources();
    }
}
