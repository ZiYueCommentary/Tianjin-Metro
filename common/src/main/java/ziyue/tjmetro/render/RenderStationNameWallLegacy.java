package ziyue.tjmetro.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockStationNameBase;
import mtr.client.IDrawing;
import mtr.render.RenderStationNameBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import ziyue.tjmetro.blocks.BlockStationNameWallLegacy;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public class RenderStationNameWallLegacy extends RenderStationNameBase<BlockStationNameWallLegacy.TileEntityStationNameWall>
{
    public RenderStationNameWallLegacy(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String stationName, int color, int light) {
        IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, stationName, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0, 60, color, false, light, null);
    }
}
