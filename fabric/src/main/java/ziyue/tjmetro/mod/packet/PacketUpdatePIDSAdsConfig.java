package ziyue.tjmetro.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.block.BlockPIDSTianjin;

/**
 * @author ZiYueCommentary
 * @see BlockPIDSTianjin.BlockEntity
 * @since 1.0.0
 */

public final class PacketUpdatePIDSAdsConfig extends PacketHandler
{
    private final BlockPos blockPos;
    private final LongArrayList categories;

    public PacketUpdatePIDSAdsConfig(PacketBufferReceiver packetBufferReceiver) {
        this.blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        final int categoryCount = packetBufferReceiver.readInt();
        categories = new LongArrayList();
        for (int i = 0; i < categoryCount; i++) {
            categories.add(packetBufferReceiver.readLong());
        }
    }

    public PacketUpdatePIDSAdsConfig(BlockPos pos, LongArrayList categories) {
        this.blockPos = pos;
        this.categories = categories;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(categories.size());
        categories.forEach(packetBufferSender::writeLong);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if ((entity != null) && (entity.data instanceof BlockPIDSTianjin.BlockEntity entity1)) {
            entity1.setData(categories);
        }
    }
}
