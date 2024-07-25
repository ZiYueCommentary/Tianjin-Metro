package ziyue.tjmetro.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.block.BlockStationNameEntranceTianjin;
import ziyue.tjmetro.mod.block.BlockStationNamePlate;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.render.RenderStationNameEntranceTianjin;

public final class PacketUpdateRailwaySignConfig extends PacketHandler
{
    private final BlockPos blockPos;
    private final LongAVLTreeSet selectedIds;
    private final String[] signIds;

    public PacketUpdateRailwaySignConfig(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        final int selectedIdsLength = packetBufferReceiver.readInt();
        selectedIds = new LongAVLTreeSet();
        for (int i = 0; i < selectedIdsLength; i++) {
            selectedIds.add(packetBufferReceiver.readLong());
        }
        final int signLength = packetBufferReceiver.readInt();
        signIds = new String[signLength];
        for (int i = 0; i < signLength; i++) {
            final String signId = packetBufferReceiver.readString();
            signIds[i] = signId.isEmpty() ? null : signId;
        }
    }

    public PacketUpdateRailwaySignConfig(BlockPos blockPos, LongAVLTreeSet selectedIds, String[] signIds) {
        this.blockPos = blockPos;
        this.selectedIds = selectedIds;
        this.signIds = signIds;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(selectedIds.size());
        selectedIds.forEach(packetBufferSender::writeLong);
        packetBufferSender.writeInt(signIds.length);
        for (final String signType : signIds) {
            packetBufferSender.writeString(signType == null ? "" : signType);
        }
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if (entity != null) {
            if (entity.data instanceof BlockRailwaySignBase.BlockEntityBase) {
                final BlockRailwaySignBase.BlockEntityBase entity1 = (BlockRailwaySignBase.BlockEntityBase) entity.data;
                entity1.setData(selectedIds, signIds);
            } else if (entity.data instanceof BlockStationNameEntranceTianjin.BlockEntity) {
                final BlockStationNameEntranceTianjin.BlockEntity entity1 = (BlockStationNameEntranceTianjin.BlockEntity) entity.data;
                final long platformId = selectedIds.isEmpty() ? -1 : (long) selectedIds.toArray()[0];
                entity1.setData(platformId);
            } else if (entity.data instanceof BlockStationNamePlate.BlockEntity) {
                final BlockStationNamePlate.BlockEntity entity1 = (BlockStationNamePlate.BlockEntity) entity.data;
                final long platformId = selectedIds.isEmpty() ? 0 : (long) selectedIds.toArray()[0];
                entity1.setPlatformId(platformId);
            }
        }
    }
}
