package ziyue.tjmetro.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
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
import ziyue.tjmetro.IDrawingExtends;
import ziyue.tjmetro.blocks.BlockTimeDisplay;

/**
 * @author ZiYueCommentary
 * @see BlockTimeDisplay
 * @since beta-1
 */

public class RenderTimeDisplay<T extends BlockTimeDisplay.TileEntityTimeDisplay> extends BlockEntityRendererMapper<T> implements IGui, IDrawing
{
    public RenderTimeDisplay(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float f, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (!entity.shouldRender()) return;

        final BlockGetter world = entity.getLevel();
        if (world == null) return;

        final BlockPos pos = entity.getBlockPos();
        final BlockState state = world.getBlockState(pos);
        final Direction facing = IBlock.getStatePropertySafe(state, BlockTimeDisplay.FACING);
        if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, null)) return;

        matrices.pushPose();
        matrices.translate(0.5, 0.5 + entity.yOffset, 0.5);
        matrices.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
        matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
        final MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        for (int i = 0; i < 2; i++) {
            matrices.pushPose();
            matrices.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
            IDrawingExtends.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, getFormattedTime(entity.getLevel().getDayTime()), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 0, 0, -1, -1, 30, -3276781, false, light, true, null);
            matrices.popPose();
            matrices.mulPose(Vector3f.YP.rotationDegrees(180));
        }
        immediate.endBatch();
        matrices.popPose();
    }

    public static String getFormattedTime(long ticks) {
        int hours = (int) ((Math.floor(ticks / 1000.0) + 6) % 24);
        int minutes = (int) Math.floor((ticks % 1000) / 1000.0 * 60);
        return String.format("%02d:%02d", hours, minutes);
    }
}
