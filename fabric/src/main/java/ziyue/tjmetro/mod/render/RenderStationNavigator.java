package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import ziyue.tjmetro.mod.block.BlockStationNamePlate;
import ziyue.tjmetro.mod.block.BlockStationNavigator;
import ziyue.tjmetro.mod.client.DynamicTextureCache;

import static ziyue.tjmetro.mod.block.BlockStationNavigator.ARROW_LEFT;

/**
 * @author ZiYueCommentary
 * @see BlockStationNamePlate
 * @since 1.0.0-beta-1
 */

public class RenderStationNavigator<T extends BlockStationNavigator.BlockEntity> extends BlockEntityRenderer<T> implements IBlock, IGui, IDrawing
{
    public RenderStationNavigator(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos pos = entity.getPos2();
        final BlockState state = world.getBlockState(pos);
        if (!(state.getBlock().data instanceof BlockStationNavigator)) return;
        final BlockStationNavigator block = (BlockStationNavigator) state.getBlock().data;

        final Direction facing = IBlock.getStatePropertySafe(state, DirectionHelper.FACING);
        final boolean arrowLeft = IBlock.getStatePropertySafe(state, ARROW_LEFT);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), 0.53125 + entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(-facing.asRotation());
            graphicsHolderNew.rotateZDegrees(180);
            // Totally awful offsets.
            graphicsHolderNew.translate(block.getXStart() / 16F - (arrowLeft ? 0.51 : 0.49), 0, -0.0625 - SMALL_OFFSET * 2);
        });

        graphicsHolder.push();
        graphicsHolder.translate(0.5, 0.53125, 0.5);
        graphicsHolder.rotateYDegrees(-facing.asRotation());
        graphicsHolder.rotateZDegrees(180);
        graphicsHolder.translate(block.getXStart() / 16F - 0.51, 0, -0.0625 - SMALL_OFFSET * 2);

        if (MinecraftClientData.getInstance().simplifiedRoutes.isEmpty()) return;
        MainRenderer.scheduleRender(DynamicTextureCache.instance.getStationNavigator(entity.getSelectedRoutes(), arrowLeft, ARGB_BLACK, entity.length).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
            storedMatrixTransformations.transform(graphicsHolderNew, offset);
            IDrawing.drawTexture(graphicsHolderNew, 0, 0, entity.length / 2F, 0.5F, 0, 0, 1, 1, facing, -1, GraphicsHolder.getDefaultLight());
            graphicsHolderNew.pop();
        });

        graphicsHolder.pop();
    }
}
