package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.World;
import org.mtr.mod.block.BlockStationNameBase;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.RenderStationNameBase;
import org.mtr.mod.render.StoredMatrixTransformations;

public class RenderStationNameWallLegacy<T extends BlockStationNameBase.BlockEntityBase> extends RenderStationNameBase<T>
{
    public RenderStationNameWallLegacy(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void drawStationName(World world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, String stationName, int stationColor, int color, int light) {
        // Who will name the station with such a long name? This should be enough.
        MainRenderer.scheduleRender(DynamicTextureCache.instance.getStationName(stationName, 10).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
            storedMatrixTransformations.transform(graphicsHolder, offset);
            IDrawing.drawTexture(graphicsHolder, -5F, -0.5F, 10, 1, 0, 0, 1, 1, facing, color, light);
            graphicsHolder.pop();
        });
    }
}
