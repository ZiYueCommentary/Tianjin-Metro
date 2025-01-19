package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import ziyue.tjmetro.mod.block.BlockSecurityCheckSign;
import ziyue.tjmetro.mod.client.IDrawingExtension;

import static org.mtr.mapping.mapper.DirectionHelper.FACING;

/**
 * @author ZiYueCommentary
 * @see BlockSecurityCheckSign
 * @since 1.0.0-beta-3
 */

public class RenderSecurityCheckSign<T extends BlockSecurityCheckSign.BlockEntity> extends BlockEntityRenderer<T> implements IGui, IDrawing
{
    public RenderSecurityCheckSign(Argument argument) {
        super(argument);
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos pos = entity.getPos2();
        final BlockState state = world.getBlockState(pos);
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + pos.getX(), 0.5 + entity.yOffset + pos.getY(), 0.5 + pos.getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(-facing.asRotation());
            graphicsHolderNew.rotateZDegrees(180);
            graphicsHolderNew.translate(0, 0.18, 0.5 - entity.zOffset - SMALL_OFFSET);
        });

        MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (newGraphicsHolder, offset) -> {
            storedMatrixTransformations.transform(newGraphicsHolder, offset);
            IDrawingExtension.drawStringWithFont(newGraphicsHolder, TextHelper.translatable("sign.tjmetro.security_check").getString(), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, -0.105f, 0.85f, 1f, 140, ARGB_WHITE, false, light, null);
            IDrawingExtension.drawStringWithFont(newGraphicsHolder, TextHelper.translatable("gui.tjmetro.accept_security_check_cjk").getString(), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0.043f, 0.85f, 1f, 270, ARGB_WHITE, false, light, null);
            newGraphicsHolder.pop();
        });
    }
}
