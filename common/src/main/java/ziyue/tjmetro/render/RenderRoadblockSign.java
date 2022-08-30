package ziyue.tjmetro.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.blocks.BlockRoadblockSign;

/**
 * Render content for <b>Roadblock with Sign</b>.
 * <br>
 * Support display <i>custom content</i>.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.screen.CustomContentScreen
 * @see ziyue.tjmetro.blocks.BlockRoadblockSign
 * @since 1.0b
 */

public class RenderRoadblockSign<T extends BlockRoadblockSign.TileEntityRoadBlockSign> extends BlockEntityRendererMapper<T> implements IGui, IDrawing
{
    public RenderRoadblockSign(BlockEntityRenderDispatcher dispatcher) {
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
        if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) return;

        matrices.pushPose();
        matrices.translate(0.5, 0.5 + entity.yOffset, 0.5);
        matrices.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
        matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
        final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        for (int i = 0; i < 2; i++) {
            matrices.pushPose();
            matrices.translate(0, 0.023, 0.5 - entity.zOffset - SMALL_OFFSET);
            if (i == 1) matrices.translate(-1, 0, 0);
            drawStationName(entity, matrices, vertexConsumers, immediate, entity.content, light);
            matrices.popPose();
            matrices.mulPose(Vector3f.YP.rotationDegrees(180));
        }
        immediate.endBatch();
        matrices.popPose();
    }

    protected void drawStationName(T entity, PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String content, int light) {
        IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, content, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0.5f, -0.15f, 0.85F * 2 + 0.05f, 1F, 90, ARGB_WHITE, false, light, null);
    }
}