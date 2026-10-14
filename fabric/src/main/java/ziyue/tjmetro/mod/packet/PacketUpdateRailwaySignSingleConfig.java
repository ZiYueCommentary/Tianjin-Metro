package ziyue.tjmetro.mod.packet;

import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.block.*;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.block.BlockRailwaySignSingle.BlockEntity
 * @since 1.1.3
 */

public final class PacketUpdateRailwaySignSingleConfig extends PacketHandler
{
    private final BlockPos blockPos;
    private final int index;
    private final String signId;

    public PacketUpdateRailwaySignSingleConfig(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        index = packetBufferReceiver.readInt();
        final String packetSignId = packetBufferReceiver.readString();
        signId = packetSignId.isEmpty() ? null : packetSignId;
    }

    public PacketUpdateRailwaySignSingleConfig(BlockPos blockPos, int index, String signId) {
        this.blockPos = blockPos;
        this.index = index;
        this.signId = signId;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(index);
        packetBufferSender.writeString(signId == null ? "" : signId);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if (entity == null) return;
        if (entity.data instanceof BlockRailwaySignSingle.BlockEntity blockEntity) {
            blockEntity.setData(index, signId);
        }
    }
}
