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
import ziyue.tjmetro.mod.block.BlockRouteMapBMT;
import ziyue.tjmetro.mod.client.DynamicTextureCache;

/**
 * @author ZiYueCommentary
 * @see BlockRouteMapBMT
 * @since 1.0.0
 */

public class RenderRouteMapBMT<T extends BlockRouteMapBMT.BlockEntity> extends BlockEntityRenderer<T> implements IBlock, IGui, IDrawing
{
    public RenderRouteMapBMT(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos pos = entity.getPos2();
        final BlockState state = world.getBlockState(pos);

        final Direction facing = IBlock.getStatePropertySafe(state, DirectionHelper.FACING);
        final EnumSide side = IBlock.getStatePropertySafe(state, SIDE);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), 0.71 + entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(-facing.asRotation());
            graphicsHolderNew.rotateZDegrees(180);
            graphicsHolderNew.translate(-0.25, 0, -0.0625 - SMALL_OFFSET * 2);
        });

        graphicsHolder.push();
        graphicsHolder.translate(0.5, 0.53125, 0.5);
        graphicsHolder.rotateYDegrees(-facing.asRotation());
        graphicsHolder.rotateZDegrees(180);
        graphicsHolder.translate(0.5, 0, -0.0625 - SMALL_OFFSET * 2);

        if (MinecraftClientData.getInstance().platformIdMap.get(entity.getPlatformId()) == null) return;
        MainRenderer.scheduleRender(DynamicTextureCache.instance.getRouteMapBMT(entity.getPlatformId(), side == EnumSide.LEFT, 2.5F / 1.66F, 0xff00379c).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
            storedMatrixTransformations.transform(graphicsHolderNew, offset);
            IDrawing.drawTexture(graphicsHolderNew, 0, 0, 2.5F, 1.66F, 0, 0, 1, 1, facing, -1, GraphicsHolder.getDefaultLight());
            graphicsHolderNew.pop();
        });

        graphicsHolder.pop();
    }
}
