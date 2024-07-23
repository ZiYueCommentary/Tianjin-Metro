package ziyue.tjmetro.mod.packet;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ScreenExtension;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.block.BlockRailwaySignWallDouble;
import ziyue.tjmetro.mod.block.BlockRoadblockSign;
import ziyue.tjmetro.mod.block.BlockStationNameEntranceTianjin;
import ziyue.tjmetro.mod.block.BlockStationNamePlate;
import ziyue.tjmetro.mod.block.base.BlockCustomColorBase;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.screen.ColorPickerScreen;
import ziyue.tjmetro.mod.screen.RailwaySignDoubleScreen;
import ziyue.tjmetro.mod.screen.RailwaySignScreen;
import ziyue.tjmetro.mod.screen.RoadblockContentScreen;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @since beta-1
 */

public final class ClientPacketHelper
{
    public static void openBlockEntityScreen(BlockPos blockPos) {
        getBlockEntity(blockPos, blockEntity -> {
            if (blockEntity.data instanceof BlockRoadblockSign.BlockEntity entity) {
                openScreen(new RoadblockContentScreen(blockPos, entity.content), screen -> screen instanceof RoadblockContentScreen);
            } else if (blockEntity.data instanceof BlockCustomColorBase.BlockEntityBase entity) {
                openScreen(new ColorPickerScreen(blockPos, entity.color), screen -> screen instanceof ColorPickerScreen);
            } else if (blockEntity.data instanceof BlockRailwaySignWallDouble.BlockEntity entity) {
                openScreen(new RailwaySignDoubleScreen(blockPos), screen -> screen instanceof RailwaySignDoubleScreen);
            } else if (blockEntity.data instanceof BlockRailwaySignBase.BlockEntityBase entity) {
                openScreen(new RailwaySignScreen(blockPos), screen -> screen instanceof RailwaySignScreen);
            } else if (blockEntity.data instanceof BlockStationNameEntranceTianjin.BlockEntity entity) {
                openScreen(new RailwaySignScreen(blockPos), screen -> screen instanceof RailwaySignScreen);
            } else if (blockEntity.data instanceof BlockStationNamePlate.BlockEntity entity) {
                openScreen(new RailwaySignScreen(blockPos), screen -> screen instanceof RailwaySignScreen);
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
