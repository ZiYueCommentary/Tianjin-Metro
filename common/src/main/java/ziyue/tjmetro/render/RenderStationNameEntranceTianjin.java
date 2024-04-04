package ziyue.tjmetro.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import mtr.render.StoredMatrixTransformations;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.block.BlockStationNameEntranceTianjin;
import ziyue.tjmetro.client.ClientCache;

/**
 * @author ZiYueCommentary
 * @see mtr.render.RenderStationNameTiled
 * @see BlockStationNameEntranceTianjin
 * @since beta-1
 */

public class RenderStationNameEntranceTianjin<T extends BlockStationNameEntranceTianjin.TileEntityStationNameEntranceTianjin> extends BlockEntityRendererMapper<T> implements IGui, IDrawing
{
    public RenderStationNameEntranceTianjin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        final BlockGetter world = entity.getLevel();
        if (world == null) return;


        final BlockPos pos = entity.getBlockPos();
        final BlockState state = world.getBlockState(pos);
        final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations();
        storedMatrixTransformations.add(matricesNew -> {
            matricesNew.translate(0.5 + entity.getBlockPos().getX(), 0.5 + entity.yOffset + entity.getBlockPos().getY(), 0.5 + entity.getBlockPos().getZ());
            UtilitiesClient.rotateYDegrees(matricesNew, -facing.toYRot());
            UtilitiesClient.rotateZDegrees(matricesNew, 180);
        });

        final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
        for (int i = 0; i < 1; i++) {
            final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
            storedMatrixTransformations2.add(matricesNew -> matricesNew.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET));
            final int lengthLeft = getLength(world, pos, false);
            final int lengthRight = getLength(world, pos, true);

            final int totalLength = lengthLeft + lengthRight - 1;
            final int propagateProperty = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntranceTianjin.STYLE);
            final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;

            final ClientCache.DynamicResource resource;
            if (station == null) {
                resource = ClientCache.DATA_CACHE.getStationNameEntrance(-1, -1, switch (propagateProperty) {
                    case 0, 1, 4, 5 -> propagateProperty + 2;
                    default -> propagateProperty;
                }, IGui.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, Text.translatable("gui.mtr.untitled").getString()), totalLength / logoSize);
            } else {
                resource = ClientCache.DATA_CACHE.getStationNameEntrance(station.id, entity.getSelectedId(), propagateProperty, IGui.insertTranslation("gui.mtr.station_cjk", ((BlockStationNameEntranceTianjin) state.getBlock()).pinyin ? "gui.tjmetro.station_pinyin" : "gui.mtr.station", 1, station.name), totalLength / logoSize);
            }
            RenderTrains.scheduleRender(resource.resourceLocation, false, RenderTrains.QueuedRenderLayer.INTERIOR, (poseStack, vertexConsumer) -> {
                storedMatrixTransformations2.transform(poseStack);
                IDrawing.drawTexture(poseStack, vertexConsumer, -0.5F, -logoSize / 2, 1, logoSize, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, ARGB_WHITE, light);
                poseStack.popPose();
            });
        }
    }

    protected int getLength(BlockGetter world, BlockPos pos, boolean lookRight) {
        if (world == null) {
            return 1;
        }
        final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameBase.FACING);
        final Block thisBlock = world.getBlockState(pos).getBlock();

        int length = 1;
        while (true) {
            final Block checkBlock = world.getBlockState(pos.relative(lookRight ? facing.getClockWise() : facing.getCounterClockWise(), length)).getBlock();
            if (checkBlock instanceof BlockStationNameBase && checkBlock == thisBlock) {
                length++;
            } else {
                break;
            }
        }

        return length;
    }
}
