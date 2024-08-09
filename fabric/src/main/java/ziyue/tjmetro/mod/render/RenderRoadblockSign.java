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
import ziyue.tjmetro.mod.block.BlockRoadblockSign;
import ziyue.tjmetro.mod.client.IDrawingExtension;
import ziyue.tjmetro.mod.data.IGuiExtension;

import static org.mtr.mapping.mapper.DirectionHelper.FACING;

/**
 * Render content for Roadblock with Sign.
 * Support displaying custom content.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.screen.RoadblockContentScreen
 * @see BlockRoadblockSign
 * @since 1.0.0-beta-1
 */

public class RenderRoadblockSign<T extends BlockRoadblockSign.BlockEntity> extends BlockEntityRenderer<T> implements IGui, IDrawing
{
    public RenderRoadblockSign(Argument argument) {
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
            graphicsHolderNew.translate(0, 0.023, 0.5 - entity.zOffset - SMALL_OFFSET);
        });

        MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (newGraphicsHolder, offset) -> {
            storedMatrixTransformations.transform(newGraphicsHolder, offset);
            IDrawingExtension.drawStringWithFont(newGraphicsHolder, IGuiExtension.filterLanguage(entity.content), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0.5F, -0.13F, 1.75F, 1F, 80, ARGB_WHITE, false, light, null);
            newGraphicsHolder.pop();
        });
    }
}
