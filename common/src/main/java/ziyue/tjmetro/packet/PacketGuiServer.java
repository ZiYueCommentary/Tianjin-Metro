package ziyue.tjmetro.packet;

import io.netty.buffer.Unpooled;
import mtr.Registry;
import mtr.RegistryClient;
import mtr.block.BlockRouteSignBase;
import mtr.data.RailwayData;
import mtr.data.RailwayDataLoggingModule;
import mtr.data.SerializedDataBase;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;
import ziyue.tjmetro.blocks.base.BlockCustomContentBlockBase;
import ziyue.tjmetro.blocks.base.BlockRailwaySignBase;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static ziyue.tjmetro.packet.IPacket.*;

/**
 * @since beta-1
 */

public class PacketGuiServer
{
    public static void openCustomContentScreenS2C(ServerPlayer player, BlockPos blockPos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(blockPos);
        Registry.sendToPlayer(player, PACKET_OPEN_CUSTOM_CONTENT_SCREEN, packet);
    }

    public static void receiveCustomContentC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        final String content = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
        minecraftServer.execute(() -> {
            final BlockEntity entity = player.level.getBlockEntity(pos);
            if (entity instanceof BlockCustomContentBlockBase.CustomContentBlockEntity) {
                BlockCustomContentBlockBase.CustomContentBlockEntity entity1 = (BlockCustomContentBlockBase.CustomContentBlockEntity) entity;
                entity1.setData(content);
            }
        });
    }

    public static void openCustomColorScreenS2C(ServerPlayer player, BlockPos blockPos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(blockPos);
        Registry.sendToPlayer(player, PACKET_OPEN_CUSTOM_COLOR_SCREEN, packet);
    }

    public static void receiveCustomColorC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
        final BlockPos pos = packet.readBlockPos();
        final int color = packet.readInt();
        minecraftServer.execute(() -> {
            final BlockEntity entity = player.level.getBlockEntity(pos);
            if (entity instanceof BlockCustomColorBase.CustomColorBlockEntity) {
                BlockCustomColorBase.CustomColorBlockEntity entity1 = (BlockCustomColorBase.CustomColorBlockEntity) entity;
                entity1.setData(color);
            }
        });
    }

    public static void openRailwaySignScreenS2C(ServerPlayer player, BlockPos signPos) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(signPos);
        Registry.sendToPlayer(player, PACKET_OPEN_RAILWAY_SIGN_SCREEN, packet);
    }

    public static void sendSignIdsC2S(BlockPos signPos, Set<Long> selectedIds, String[] signIds) {
        final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
        packet.writeBlockPos(signPos);
        packet.writeInt(selectedIds.size());
        selectedIds.forEach(packet::writeLong);
        packet.writeInt(signIds.length);
        for (final String signType : signIds) {
            packet.writeUtf(signType == null ? "" : signType);
        }
        RegistryClient.sendToServer(PACKET_SIGN_TYPES, packet);
    }

    public static void receiveSignIdsC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
        final BlockPos signPos = packet.readBlockPos();
        final int selectedIdsLength = packet.readInt();
        final Set<Long> selectedIds = new HashSet<>();
        for (int i = 0; i < selectedIdsLength; i++) {
            selectedIds.add(packet.readLong());
        }
        final int signLength = packet.readInt();
        final String[] signIds = new String[signLength];
        for (int i = 0; i < signLength; i++) {
            final String signId = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
            signIds[i] = signId.isEmpty() ? null : signId;
        }

        minecraftServer.execute(() -> {
            final BlockEntity entity = player.level.getBlockEntity(signPos);
            if (entity instanceof BlockRailwaySignBase.TileEntityRailwaySign) {
                setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setData(selectedIds, signIds), (BlockRailwaySignBase.TileEntityRailwaySign) entity);
            } else if (entity instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
                final long platformId = selectedIds.isEmpty() ? 0 : (long) selectedIds.toArray()[0];
                final BlockEntity entityAbove = player.level.getBlockEntity(signPos.above());
                if (entityAbove instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
                    setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setPlatformId(platformId), ((BlockRouteSignBase.TileEntityRouteSignBase) entityAbove), (BlockRouteSignBase.TileEntityRouteSignBase) entity);
                } else {
                    setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setPlatformId(platformId), (BlockRouteSignBase.TileEntityRouteSignBase) entity);
                }
            }
        });
    }

    @SafeVarargs
    public static <T extends BlockEntityMapper> void setTileEntityDataAndWriteUpdate(ServerPlayer player, Consumer<T> setData, T... entities) {
        final RailwayData railwayData = RailwayData.getInstance(player.level);

        if (railwayData != null && entities.length > 0) {
            final CompoundTag compoundTagOld = new CompoundTag();
            entities[0].writeCompoundTag(compoundTagOld);

            BlockPos blockPos = null;
            long posLong = 0;
            for (final T entity : entities) {
                setData.accept(entity);
                final BlockPos entityPos = entity.getBlockPos();
                if (blockPos == null || entityPos.asLong() > posLong) {
                    blockPos = entityPos;
                    posLong = entityPos.asLong();
                }
            }

            final CompoundTag compoundTagNew = new CompoundTag();
            entities[0].writeCompoundTag(compoundTagNew);

            railwayData.railwayDataLoggingModule.addEvent(player, entities[0].getClass(), RailwayDataLoggingModule.getData(compoundTagOld), RailwayDataLoggingModule.getData(compoundTagNew), blockPos);
        }
    }
}
