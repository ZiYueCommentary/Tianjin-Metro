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
import ziyue.tjmetro.mod.block.BlockServiceCorridorSign;
import ziyue.tjmetro.mod.client.IDrawingExtension;
import ziyue.tjmetro.mod.data.IGuiExtension;

import static org.mtr.mapping.mapper.DirectionHelper.FACING;

/**
 * @author ZiYueCommentary
 * @see BlockServiceCorridorSign
 * @since beta-1
 */

public class RenderServiceCorridorSign<T extends BlockServiceCorridorSign.BlockEntity> extends BlockEntityRenderer<T> implements IGui, IDrawing
{
    public RenderServiceCorridorSign(Argument argument) {
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
            graphicsHolderNew.translate(0, -0.04, 0.5 - entity.zOffset - SMALL_OFFSET);
        });

        MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (newGraphicsHolder, offset) -> {
            storedMatrixTransformations.transform(newGraphicsHolder, offset);
            IDrawingExtension.drawStringWithFont(newGraphicsHolder, TextHelper.translatable("gui.tjmetro.service_corridor_sign_cjk").getString(), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, -0.12f, 0.85F, 1F, 150, ARGB_BLACK, false, light, null);
            IDrawingExtension.drawStringWithFont(newGraphicsHolder, IGuiExtension.mergeTranslation("gui.tjmetro.contact_station_for_help_cjk", "gui.tjmetro.contact_station_for_help"), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0.02f, 0.85F, 1F, 270, ARGB_BLACK, false, light, null);
            newGraphicsHolder.pop();
        });
    }
}
