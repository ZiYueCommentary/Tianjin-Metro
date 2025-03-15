package ziyue.tjmetro.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import ziyue.tjmetro.mod.block.BlockStationNavigator;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.block.BlockRailwaySignTianjinBMT.BlockEntity
 * @see ziyue.tjmetro.mod.block.BlockRailwaySignTianjin.BlockEntity
 * @see ziyue.tjmetro.mod.block.BlockRailwaySignWall.BlockEntity
 * @see ziyue.tjmetro.mod.block.BlockRailwaySignWallBig.BlockEntity
 * @since 1.0.0-beta-1
 */

public final class PacketUpdateStationNavigatorConfig extends PacketHandler
{
    private final BlockPos blockPos;
    private final LongAVLTreeSet selectedRoutes;

    public PacketUpdateStationNavigatorConfig(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        final int selectedIdsLength = packetBufferReceiver.readInt();
        selectedRoutes = new LongAVLTreeSet();
        for (int i = 0; i < selectedIdsLength; i++) {
            selectedRoutes.add(packetBufferReceiver.readLong());
        }
    }

    public PacketUpdateStationNavigatorConfig(BlockPos blockPos, LongAVLTreeSet selectedRoutes) {
        this.blockPos = blockPos;
        this.selectedRoutes = selectedRoutes;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeInt(selectedRoutes.size());
        selectedRoutes.forEach(packetBufferSender::writeLong);
    }

    @Override
    public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
        if (entity != null) {
            if (entity.data instanceof BlockStationNavigator.BlockEntity) {
                final BlockStationNavigator.BlockEntity entity1 = (BlockStationNavigator.BlockEntity) entity.data;
                entity1.setData(selectedRoutes);
            }
        }
    }
}
