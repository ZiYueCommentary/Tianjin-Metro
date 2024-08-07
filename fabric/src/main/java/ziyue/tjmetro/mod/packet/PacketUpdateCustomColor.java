package ziyue.tjmetro.mod.packet;

import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.block.base.BlockCustomColorBase;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.block.BlockCustomColorConcrete.BlockEntity
 * @see ziyue.tjmetro.mod.block.BlockCustomColorConcreteStairs.BlockEntity
 * @see ziyue.tjmetro.mod.block.BlockCustomColorConcreteSlab.BlockEntity
 * @see ziyue.tjmetro.mod.block.BlockMetalPoleBMT.BlockEntity
 * @since 1.0.0-beta-1
 */

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
        if ((entity != null) && (entity.data instanceof BlockCustomColorBase.BlockEntityBase)) {
            ((BlockCustomColorBase.BlockEntityBase) entity.data).setData(color);
        }
    }
}
