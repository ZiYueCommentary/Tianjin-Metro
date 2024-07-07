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
import ziyue.tjmetro.block.BlockStationNamePlate;
import ziyue.tjmetro.client.ClientCache;

import static ziyue.tjmetro.block.BlockStationNamePlate.POS;

/**
 * @author ZiYueCommentary
 * @see mtr.render.RenderStationNameTiled
 * @see BlockStationNameEntranceTianjin
 * @since beta-1
 */

public class RenderStationNamePlate<T extends BlockStationNamePlate.TileEntityStationNamePlate> extends BlockEntityRendererMapper<T> implements IGui, IDrawing
{
    public RenderStationNamePlate(BlockEntityRenderDispatcher dispatcher) {
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
        final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
        storedMatrixTransformations2.add(matricesNew -> matricesNew.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET));

        final int lengthLeft = entity.getBlockState().getValue(POS);
        final int propagateProperty = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntranceTianjin.STYLE);
        final float logoSize = 0.5F;

        final ClientCache.DynamicResource resource = ClientCache.DATA_CACHE.getStationNameEntrance(station.id, entity.getSelectedId(), propagateProperty, IGui.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, station.name), false, 4);

        RenderTrains.scheduleRender(resource.resourceLocation, false, RenderTrains.QueuedRenderLayer.INTERIOR, (poseStack, vertexConsumer) -> {
            storedMatrixTransformations2.transform(poseStack);
            IDrawing.drawTexture(poseStack, vertexConsumer, -0.5F, -logoSize / 2, 1, logoSize, (float) (lengthLeft - 1) / 4, 0, (float) lengthLeft / 4, 1, facing, ARGB_WHITE, light);
            poseStack.popPose();
        });
    }
}
