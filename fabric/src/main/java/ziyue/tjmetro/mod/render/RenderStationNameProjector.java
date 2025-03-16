package ziyue.tjmetro.mod.render;

import org.mtr.core.data.Station;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockStationNameBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import ziyue.tjmetro.mod.block.BlockStationNameProjector;
import ziyue.tjmetro.mod.client.DynamicTextureCache;

/**
 * @author ZiYueCommentary
 * @see BlockStationNameProjector
 * @since 1.0.0-beta-4
 */

public class RenderStationNameProjector<T extends BlockStationNameProjector.BlockEntity> extends BlockEntityRenderer<T> implements IGui, IDrawing
{
    public RenderStationNameProjector(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos pos = entity.getPos2();
        final BlockState state = world.getBlockState(pos);
        final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
        final int scale = IBlock.getStatePropertySafe(state, BlockStationNameProjector.SCALE);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + pos.getX(), 0.5 + entity.yOffset + pos.getY(), 0.5 + pos.getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(-facing.asRotation());
            graphicsHolderNew.rotateZDegrees(180);
            graphicsHolderNew.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
            graphicsHolderNew.scale(scale, scale, scale);
        });

        final Station station = InitClient.findStation(pos);
        MainRenderer.scheduleRender(DynamicTextureCache.instance.getStationNameProjector(station == null ? TranslationProvider.GUI_MTR_UNTITLED.getString() : station.getName(), 10).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
            storedMatrixTransformations.transform(graphicsHolderNew, offset);
            IDrawing.drawTexture(graphicsHolderNew, -5F, -0.5F, 10, 1, 0, 0, 1, 1, facing, entity.getColor(state), light);
            graphicsHolderNew.pop();
        });
    }
}
