package ziyue.tjmetro.packet;

import io.netty.buffer.Unpooled;
import mtr.Registry;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import ziyue.tjmetro.blocks.BlockStationNameSignBase;

import static ziyue.tjmetro.packet.IPacket.PACKET_OPEN_CUSTOM_CONTENT_SCREEN;

public class PacketGuiServer
{
    private static final int PACKET_CHUNK_SIZE = (int)Math.pow(2, 14); // 16384

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
            if (entity instanceof BlockStationNameSignBase.TileEntityStationNameWall) {
                ((BlockStationNameSignBase.TileEntityStationNameWall) entity).setData(content);
            }
        });
    }
}
