package ziyue.tjmetro.packet;

import io.netty.buffer.Unpooled;
import mtr.Registry;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;
import ziyue.tjmetro.blocks.base.CustomContentBlockBase;

import static ziyue.tjmetro.packet.IPacket.PACKET_OPEN_CUSTOM_COLOR_SCREEN;
import static ziyue.tjmetro.packet.IPacket.PACKET_OPEN_CUSTOM_CONTENT_SCREEN;

public class PacketGuiServer
{
    public static void openCustomContentScreenS2C(ServerPlayer player, BlockPos blockPos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(blockPos);
        Registry.sendToPlayer(player, PACKET_OPEN_CUSTOM_CONTENT_SCREEN, packet);
    }

    public static void receiveCustomContentC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        final String content = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
        minecraftServer.execute(() -> {
            final BlockEntity entity = player.level.getBlockEntity(pos);
            if (entity instanceof CustomContentBlockBase.CustomContentBlockEntity) {
                ((CustomContentBlockBase.CustomContentBlockEntity) entity).setData(content);
            }
        });
    }

    public static void openCustomColorScreenS2C(ServerPlayer player, BlockPos blockPos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(blockPos);
        Registry.sendToPlayer(player, PACKET_OPEN_CUSTOM_COLOR_SCREEN, packet);
    }

    public static void receiveCustomColorC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        final int color = packet.readInt();
        minecraftServer.execute(() -> {
            final BlockEntity entity = player.level.getBlockEntity(pos);
            if (entity instanceof BlockCustomColorBase.CustomColorBlockEntity) {
                ((BlockCustomColorBase.CustomColorBlockEntity) entity).setData(color);
            }
        });
    }
}
