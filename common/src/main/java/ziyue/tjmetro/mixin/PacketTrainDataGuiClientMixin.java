package ziyue.tjmetro.mixin;

import mtr.data.NameColorDataBase;
import mtr.data.TransportMode;
import mtr.packet.PacketTrainDataBase;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.client.ClientCache;

import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * @author ZiYueCommentary
 * @see PacketTrainDataGuiClient
 * @since beta-1
 */

@Mixin(PacketTrainDataGuiClient.class)
public abstract class PacketTrainDataGuiClientMixin extends PacketTrainDataBase
{
    @Inject(at = @At("HEAD"), method = "receiveUpdateOrDeleteS2C")
    private static <T extends NameColorDataBase> void beforeReceiveUpdateOrDeleteS2C(Minecraft minecraftClient, FriendlyByteBuf packet, Set<T> dataSet, Map<Long, T> cacheMap, BiFunction<Long, TransportMode, T> createDataWithId, boolean isDelete, CallbackInfo ci) {
        ClientCache.DATA_CACHE.sync();
        ClientCache.DATA_CACHE.refreshDynamicResources();
    }

    @Inject(at = @At("TAIL"), method = "sendUpdate")
    private static void afterSendUpdate(ResourceLocation packetId, FriendlyByteBuf packet, CallbackInfo ci) {
        ClientCache.DATA_CACHE.sync();
        ClientCache.DATA_CACHE.refreshDynamicResources();
    }
}
