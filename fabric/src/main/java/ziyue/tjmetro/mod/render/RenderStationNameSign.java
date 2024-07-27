package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.RenderStationNameBase;
import org.mtr.mod.render.StoredMatrixTransformations;
import ziyue.tjmetro.mod.block.base.BlockStationNameSignBase;
import ziyue.tjmetro.mod.client.IDrawingExtension;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.block.BlockStationNameSign1
 * @see ziyue.tjmetro.mod.block.BlockStationNameSign2
 * @since 1.0.0-beta-1
 */

public class RenderStationNameSign<T extends BlockStationNameSignBase.BlockEntityBase> extends RenderStationNameBase<T>
{
    public RenderStationNameSign(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void drawStationName(World world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light) {
        MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
            storedMatrixTransformations.transform(graphicsHolder, offset);
            IDrawingExtension.drawStringWithFont(graphicsHolder, stationName, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, -0.135f, 0.85F, 1F, 100, color, false, light, null);
            graphicsHolder.pop();
        });
    }
}
