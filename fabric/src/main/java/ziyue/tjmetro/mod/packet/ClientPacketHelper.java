package ziyue.tjmetro.mod.packet;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.screen.DashboardListItem;
import org.mtr.mod.screen.DashboardListSelectorScreen;
import ziyue.tjmetro.mod.RegistryClient;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.block.*;
import ziyue.tjmetro.mod.block.base.BlockCustomColorBase;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.screen.*;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @since 1.0.0-beta-1
 */

public final class ClientPacketHelper
{
    public static void openBlockEntityScreen(BlockPos blockPos) {
        getBlockEntity(blockPos, blockEntity -> {
            if (blockEntity.data instanceof BlockRoadblockSign.BlockEntity entity) {
                openScreen(new RoadblockContentScreen(blockPos, entity.content), screen -> screen instanceof RoadblockContentScreen);
            } else if (blockEntity.data instanceof BlockCustomColorBase.BlockEntityBase entity) {
                openScreen(new ColorPickerScreen(blockPos, entity), screen -> screen instanceof ColorPickerScreen);
            } else if (blockEntity.data instanceof BlockRailwaySignWallDouble.BlockEntity) {
                openScreen(new RailwaySignDoubleScreen(blockPos), screen -> screen instanceof RailwaySignDoubleScreen);
            } else if (blockEntity.data instanceof BlockRailwaySignBase.BlockEntityBase ||
                    blockEntity.data instanceof BlockStationNameEntranceTianjin.BlockEntity ||
                    blockEntity.data instanceof BlockStationNamePlate.BlockEntity ||
                    blockEntity.data instanceof BlockRouteMapBMT.BlockEntity ||
                    blockEntity.data instanceof BlockStationNameSignTianjin.BlockEntity
            ) {
                openScreen(new RailwaySignScreen(blockPos), screen -> screen instanceof RailwaySignScreen);
            } else if (blockEntity.data instanceof BlockPIDSTianjin.BlockEntity) {
                openScreen(new PIDSTianjinConfigScreen(blockPos), screen -> screen instanceof PIDSTianjinConfigScreen);
            } else if (blockEntity.data instanceof BlockStationNavigator.BlockEntity) {
                final ObjectArraySet<DashboardListItem> routes = new ObjectArraySet<>();
                MinecraftClientData.getInstance().simplifiedRoutes.forEach(route ->
                        routes.add(new DashboardListItem(route.getId(), route.getName().split("\\|\\|")[0], route.getColor())));

                final LongAVLTreeSet selectedRoutes = new LongAVLTreeSet(((BlockStationNavigator.BlockEntity) blockEntity.data).getSelectedRoutes());
                openScreen(new DashboardListSelectorScreen(
                        () -> RegistryClient.sendPacketToServer(new PacketUpdateStationNavigatorConfig(blockPos, selectedRoutes)),
                        new ObjectImmutableList<>(routes),
                        selectedRoutes,
                        false,
                        false,
                        null), screen -> screen instanceof DashboardListSelectorScreen);
            } else {
                TianjinMetro.LOGGER.warn("Unknown block entity data at {}: {}", blockPos.toShortString(), blockEntity.data);
            }
        });
    }

    public static void openScreen(ScreenExtension screenExtension, Predicate<ScreenExtension> isInstance) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Screen screen = minecraftClient.getCurrentScreenMapped();
        if (screen == null || screen.data instanceof ScreenExtension && !isInstance.test((ScreenExtension) screen.data)) {
            minecraftClient.openScreen(new Screen(screenExtension));
        }
    }

    public static void getBlockEntity(BlockPos blockPos, Consumer<BlockEntity> consumer) {
        final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
        if (clientWorld != null) {
            final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
            if (blockEntity != null) {
                consumer.accept(blockEntity);
            }
        }
    }
}
