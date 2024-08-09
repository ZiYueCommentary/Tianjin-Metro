package ziyue.tjmetro.mod.packet;

import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.block.BlockRoadblockSign;

/**
 * @author ZiYueCommentary
 * @see BlockRoadblockSign.BlockEntity
 * @since 1.0.0-beta-1
 */

public final class PacketUpdateRoadblockContent extends PacketHandler
{
    private final BlockPos blockPos;
    private final String content;

    public PacketUpdateRoadblockContent(PacketBufferReceiver packetBufferReceiver) {
        this(BlockPos.fromLong(packetBufferReceiver.readLong()), packetBufferReceiver.readString());
    }

    public PacketUpdateRoadblockContent(BlockPos pos, String content) {
        this.blockPos = pos;
        this.content = content;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeString(content);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if ((entity != null) && (entity.data instanceof BlockRoadblockSign.BlockEntity)) {
            final BlockRoadblockSign.BlockEntity entity1 = (BlockRoadblockSign.BlockEntity) entity.data;
            entity1.setData(content);
        }
    }
}
