package ziyue.tjmetro.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.Text;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.blocks.base.BlockStationNameSignBase;

/**
 * Render content for <b>Station Name Sign Block</b>.
 * Support display <b>custom content</b>.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.screen.CustomContentScreen
 * @see BlockStationNameSignBase
 * @since beta-1
 */

public class RenderStationNameSign<T extends BlockStationNameSignBase.TileEntityStationNameBase> extends BlockEntityRendererMapper<T> implements IGui, IDrawing
{
    public RenderStationNameSign(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (!entity.shouldRender()) return;

        final BlockGetter world = entity.getLevel();
        if (world == null) return;

        final BlockPos pos = entity.getBlockPos();
        final BlockState state = world.getBlockState(pos);
        final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
        if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) return;

        final int color;
        switch (IBlock.getStatePropertySafe(state, BlockStationNameBase.COLOR)) {
            case 1:
                color = ARGB_LIGHT_GRAY;
                break;
            case 2:
                color = ARGB_BLACK;
                break;
            default:
                color = ARGB_WHITE;
                break;
        }

        matrices.pushPose();
        matrices.translate(0.5, 0.5 + entity.yOffset, 0.5);
        matrices.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
        matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrices.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
        final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, entity.getBlockPos());
        drawStationName(matrices, immediate, entity.content.equals("") ? station == null ? Text.translatable("gui.mtr.untitled").getString() : station.name : entity.content, color, light);
        immediate.endBatch();
        matrices.popPose();
    }

    protected void drawStationName(PoseStack matrices, MultiBufferSource.BufferSource immediate, String stationName, int color, int light) {
        IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, stationName, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, -0.105f, 0.85F, 1F, 100, color, false, light, null);
    }
}