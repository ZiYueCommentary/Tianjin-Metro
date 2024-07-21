package ziyue.tjmetro.mod.packet;

import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.block.base.BlockCustomColorBase;

public final class PacketUpdateCustomColor extends PacketHandler
{
    private final BlockPos blockPos;
    private final int color;

    public PacketUpdateCustomColor(PacketBufferReceiver packetBufferReceiver) {
        this(BlockPos.fromLong(packetBufferReceiver.readLong()), packetBufferReceiver.readInt());
    }

    public PacketUpdateCustomColor(BlockPos pos, int color) {
        this.blockPos = pos;
        this.color = color;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(color);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if ((entity != null) && (entity.data instanceof BlockCustomColorBase.BlockEntityBase entity1)) {
            entity1.setData(color);
        }
    }
}
