package ziyue.tjmetro.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.IDrawing;
import mtr.data.IGui;
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
import ziyue.tjmetro.IDrawingExtends;
import ziyue.tjmetro.blocks.BlockServiceCorridorSign;

/**
 * Render contents for <b>Service Corridor Sign</b>.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.blocks.BlockServiceCorridorSign
 * @since beta-1
 */

public class RenderServiceCorridorSign<T extends BlockServiceCorridorSign.TileEntityServiceCorridorSign> extends BlockEntityRendererMapper<T> implements IGui, IDrawing
{
    public RenderServiceCorridorSign(BlockEntityRenderDispatcher dispatcher) {
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

        matrices.pushPose();
        matrices.translate(0.5, 0.5 + entity.yOffset, 0.5);
        matrices.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
        matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrices.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
        final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        IDrawingExtends.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, Text.translatable("gui.tjmetro.service_corridor_sign").getString(), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, -0.12f, 0.85F, 1F, 150, ARGB_BLACK, false, light, null);
        IDrawingExtends.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, Text.translatable("gui.tjmetro.contact_station_for_help").getString(), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0.02f, 0.85F, 1F, 270, ARGB_BLACK, false, light, null);
        immediate.endBatch();
        matrices.popPose();
    }
}