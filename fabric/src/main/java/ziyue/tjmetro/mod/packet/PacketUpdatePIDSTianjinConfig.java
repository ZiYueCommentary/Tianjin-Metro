package ziyue.tjmetro.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.block.BlockPIDSTianjin;
import ziyue.tjmetro.mod.block.BlockRoadblockSign;

/**
 * @author ZiYueCommentary
 * @see BlockPIDSTianjin.BlockEntity
 * @since 1.0.0
 */

public final class PacketUpdatePIDSTianjinConfig extends PacketHandler
{
    private final BlockPos blockPos;
    private final LongAVLTreeSet platformIds;
    private final int displayPage;

    public PacketUpdatePIDSTianjinConfig(PacketBufferReceiver packetBufferReceiver) {
        this.blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        final int platformIdCount = packetBufferReceiver.readInt();
        platformIds = new LongAVLTreeSet();
        for (int i = 0; i < platformIdCount; i++) {
            platformIds.add(packetBufferReceiver.readLong());
        }

        displayPage = packetBufferReceiver.readInt();
    }

    public PacketUpdatePIDSTianjinConfig(BlockPos pos, LongAVLTreeSet platformIds, int displayPage) {
        this.blockPos = pos;
        this.platformIds = platformIds;
        this.displayPage = displayPage;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(platformIds.size());
        platformIds.forEach(packetBufferSender::writeLong);
        packetBufferSender.writeInt(displayPage);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if ((entity != null) && (entity.data instanceof BlockPIDSTianjin.BlockEntity)) {
            final BlockPIDSTianjin.BlockEntity entity1 = (BlockPIDSTianjin.BlockEntity) entity.data;
            entity1.setData(platformIds, displayPage);
        }
    }
}
