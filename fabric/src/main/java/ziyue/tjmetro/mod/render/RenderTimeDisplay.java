package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import ziyue.tjmetro.mod.block.BlockTimeDisplay;
import ziyue.tjmetro.mod.client.IDrawingExtension;

import static org.mtr.mapping.mapper.DirectionHelper.FACING;

/**
 * @author ZiYueCommentary
 * @see BlockTimeDisplay
 * @since 1.0.0-beta-1
 */

public class RenderTimeDisplay<T extends BlockTimeDisplay.BlockEntity> extends BlockEntityRenderer<T> implements IGui, IDrawing
{
    public RenderTimeDisplay(Argument argument) {
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
        });

        for (int i = 0; i < 2; i++) {
            final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
            final boolean shouldFlip = i == 1;
            storedMatrixTransformations2.add(graphicsHolderNew -> {
                if (shouldFlip) {
                    graphicsHolderNew.rotateYDegrees(180);
                }
                graphicsHolderNew.translate(0, -0.04, 0.5 - entity.zOffset - SMALL_OFFSET);
            });
            MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (newGraphicsHolder, offset) -> {
                storedMatrixTransformations2.transform(newGraphicsHolder, offset);
                IDrawingExtension.drawStringWithFont(newGraphicsHolder, getFormattedTime(entity.getWorld2().getLunarTime()), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, 0, 0.03F, -1, -1, 30, -3276781, -3276781, 2, false, light, true, null);
                newGraphicsHolder.pop();
            });
        }
    }

    public static String getFormattedTime(long ticks) {
        int hours = (int) ((Math.floor(ticks / 1000.0) + 6) % 24);
        int minutes = (int) Math.floor((ticks % 1000) / 1000.0 * 60);
        return String.format("%02d:%02d", hours, minutes);
    }
}
