package ziyue.tjmetro.packet;

import io.netty.buffer.Unpooled;
import mtr.RegistryClient;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;
import ziyue.tjmetro.blocks.base.CustomContentBlockBase;
import ziyue.tjmetro.screen.ColorPickerScreen;
import ziyue.tjmetro.screen.CustomContentScreen;

import static ziyue.tjmetro.packet.IPacket.PACKET_UPDATE_CUSTOM_COLOR;
import static ziyue.tjmetro.packet.IPacket.PACKET_UPDATE_CUSTOM_CONTENT;

public class PacketGuiClient
{
    public static void openCustomContentScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        minecraftClient.execute(() -> {
            if (minecraftClient.level != null && !(minecraftClient.screen instanceof CustomContentScreen)) {
                final BlockEntity entity = minecraftClient.level.getBlockEntity(pos);
                if (entity instanceof CustomContentBlockBase.CustomContentBlockEntity) {
                    UtilitiesClient.setScreen(minecraftClient, new CustomContentScreen(pos));
                }
            }
        });
    }

    public static void sendCustomContentC2S(BlockPos pos, String content) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos);
        packet.writeUtf(content);
        RegistryClient.sendToServer(PACKET_UPDATE_CUSTOM_CONTENT, packet);
    }


    public static void openCustomColorScreenS2C(Minecraft minecraftClient, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        minecraftClient.execute(() -> {
            if (minecraftClient.level != null && !(minecraftClient.screen instanceof CustomContentScreen)) {
                final BlockEntity entity = minecraftClient.level.getBlockEntity(pos);
                if (entity instanceof BlockCustomColorBase.CustomColorBlockEntity) {
                    UtilitiesClient.setScreen(minecraftClient, new ColorPickerScreen(pos, ((BlockCustomColorBase.CustomColorBlockEntity) entity).color));
                }
            }
        });
    }

    public static void sendCustomColorC2S(BlockPos pos, int color) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(pos);
        packet.writeInt(color);
        RegistryClient.sendToServer(PACKET_UPDATE_CUSTOM_COLOR, packet);
    }
}
