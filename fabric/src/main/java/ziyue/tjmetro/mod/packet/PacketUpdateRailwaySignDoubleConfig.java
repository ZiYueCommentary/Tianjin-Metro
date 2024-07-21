package ziyue.tjmetro.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.block.BlockRailwaySignWallDouble;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;

import java.util.ArrayList;
import java.util.List;

public final class PacketUpdateRailwaySignDoubleConfig extends PacketHandler
{
    private final BlockPos blockPos;
    private final List<LongAVLTreeSet> selectedIds;
    private final String[][] signIds;

    public PacketUpdateRailwaySignDoubleConfig(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        selectedIds = new ArrayList<>();
        selectedIds.add(new LongAVLTreeSet());
        selectedIds.add(new LongAVLTreeSet());
        for (int i = 0; i < 2; i++) {
            final int selectedIdsLength = packetBufferReceiver.readInt();
            for (int j = 0; j < selectedIdsLength; j++) {
                selectedIds.get(i).add(packetBufferReceiver.readLong());
            }
        }
        final int signLength = packetBufferReceiver.readInt();
        signIds = new String[2][signLength];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < signLength; j++) {
                final String signId = packetBufferReceiver.readString();
                signIds[i][j] = signId.isEmpty() ? null : signId;
            }
        }
    }

    public PacketUpdateRailwaySignDoubleConfig(BlockPos blockPos, List<LongAVLTreeSet> selectedIds, String[][] signIds) {
        this.blockPos = blockPos;
        this.selectedIds = selectedIds;
        this.signIds = signIds;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        for (int i = 0; i < 2; i++) {
            packetBufferSender.writeInt(selectedIds.get(i).size());
            selectedIds.get(i).forEach(packetBufferSender::writeLong);
        }
        packetBufferSender.writeInt(signIds[0].length);
        for (int i = 0; i < 2; i++) {
            for (final String signType : signIds[i]) {
                packetBufferSender.writeString(signType == null ? "" : signType);
            }
        }
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if (entity != null) {
            if (entity.data instanceof BlockRailwaySignWallDouble.BlockEntity entity1) {
                entity1.setData(selectedIds, signIds);
            }
        }
    }
}
