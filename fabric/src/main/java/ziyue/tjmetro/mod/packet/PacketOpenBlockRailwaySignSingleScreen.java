package ziyue.tjmetro.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.screen.RailwaySignSingleScreen;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.screen.RailwaySignSingleScreen
 * @since 1.1.3
 */

public final class PacketOpenBlockRailwaySignSingleScreen extends PacketHandler
{
    private final BlockPos blockPos;
    private final int index;

    public PacketOpenBlockRailwaySignSingleScreen(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        index = packetBufferReceiver.readInt();
    }

    public PacketOpenBlockRailwaySignSingleScreen(BlockPos blockPos, int index) {
        this.blockPos = blockPos;
        this.index = index;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(index);
    }

    @Override
    public void runClient() {
        ClientPacketHelper.openScreen(new RailwaySignSingleScreen(blockPos, index), screen -> true);
    }
}
