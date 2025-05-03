package ziyue.tjmetro.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import mtr.render.RenderStationNameBase;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.data.IGuiExtends;
import ziyue.tjmetro.block.BlockStationNameWallLegacy;

/**
 * @author ZiYueCommentary
 * @see RenderStationNameBase
 * @since beta-1
 */

public class RenderStationNameWallLegacy<T extends BlockStationNameWallLegacy.TileEntityStationNameLegacy> extends BlockEntityRendererMapper<T> implements IGui, IDrawing
{
    public RenderStationNameWallLegacy(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float f, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int j) {
        final BlockGetter world = entity.getLevel();
        if (world == null) return;

        final BlockPos pos = entity.getBlockPos();
        final BlockState state = world.getBlockState(pos);
        final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
        if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) return;

        matrices.pushPose();
        matrices.translate(0.5, 0.5 + entity.yOffset, 0.5);
        matrices.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        matrices.mulPose(Axis.ZP.rotationDegrees(180));
        final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
        matrices.translate(0, 0.023, 0.5 - entity.zOffset - SMALL_OFFSET);
        drawStationName(matrices, vertexConsumers, immediate, station == null ? Text.translatable("gui.mtr.untitled").getString() : IGuiExtends.filterLanguage(station.name), entity.getColor(state), light);
        immediate.endBatch();
        matrices.popPose();
    }

    protected void drawStationName(PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String stationName, int color, int light) {
        IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, stationName, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0, -1, -1, 60, color, false, light, null);
    }
}
