package ziyue.tjmetro.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockStationNameBase;
import mtr.client.IDrawing;
import mtr.render.RenderStationNameBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.blocks.BlockStationNameWallLegacy;

/**
 * @author ZiYueCommentary
 * @see RenderStationNameBase
 * @since 1.0b
 */

public class RenderStationNameWallLegacy extends RenderStationNameBase<BlockStationNameWallLegacy.TileEntityStationNameWall>
{
    public RenderStationNameWallLegacy(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void drawStationName(BlockGetter world, BlockPos pos, BlockState state, Direction facing, PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String stationName, int stationColor, int color, int light) {
        IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, stationName, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, 0, 60, color, false, light, null);
    }
}
