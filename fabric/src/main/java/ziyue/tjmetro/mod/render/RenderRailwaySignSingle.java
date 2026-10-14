package ziyue.tjmetro.mod.render;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import ziyue.tjmetro.mod.block.BlockRailwaySignSingle;

import static org.mtr.mod.render.RenderRailwaySign.*;

/**
 * @author ZiYueCommentary
 * @see BlockRailwaySignSingle
 * @since 1.1.3
 */

public class RenderRailwaySignSingle<T extends BlockRailwaySignSingle.BlockEntity> extends BlockEntityRenderer<T> implements IBlock, IGui, IDrawing
{
    public RenderRailwaySignSingle(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos pos = entity.getPos2();
        final BlockState state = world.getBlockState(pos);

        final Direction facing = IBlock.getStatePropertySafe(state, DirectionHelper.FACING);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(
                0.5F + entity.getPos2().getX(), 0.5F + entity.getPos2().getY(), 0.5F + entity.getPos2().getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(-facing.asRotation());
            graphicsHolderNew.rotateZDegrees(180);
            graphicsHolderNew.translate(-0.25, -0.218, -0.1);
        });
        renderSign(graphicsHolder, storedMatrixTransformations, facing, pos, entity.getSignId(0));
        final StoredMatrixTransformations storedMatrixTransformations1 = storedMatrixTransformations.copy();
        storedMatrixTransformations1.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(180);
            graphicsHolderNew.translate(-0.5, 0, -0.2);
        });
        renderSign(graphicsHolder, storedMatrixTransformations1, facing, pos, entity.getSignId(1));

        graphicsHolder.pop();
    }

    protected void renderSign(GraphicsHolder graphicsHolder, StoredMatrixTransformations storedMatrixTransformations, Direction facing, BlockPos pos, String signId) {
        MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (graphicsHolderNew, offset) -> {
            storedMatrixTransformations.transform(graphicsHolderNew, offset);
            IDrawing.drawTexture(graphicsHolderNew, 0, 0, SMALL_OFFSET, 0.5F, 0.5F, SMALL_OFFSET, facing, ARGB_BLACK, GraphicsHolder.getDefaultLight());
            graphicsHolderNew.pop();
        });
        if (signId != null) {
            final DrawTexture drawTexture = (textureId, x, y, size, flipTexture) -> MainRenderer.scheduleRender(textureId, true, QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolderNew, offset) -> {
                storedMatrixTransformations.transform(graphicsHolderNew, offset);
                IDrawing.drawTexture(graphicsHolderNew, x, y, size, size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1, facing, -1, GraphicsHolder.getDefaultLight());
                graphicsHolderNew.pop();
            });
            drawSign(graphicsHolder, storedMatrixTransformations, pos, signId,
                    0, 0, 0.5F, 0.5F, 0.5F,
                    new LongAVLTreeSet(), facing, ARGB_BLACK, drawTexture);
        }
    }
}
