package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockStationNameEntrance;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.render.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mixin.properties.BlockStationNameProperties;

/**
 * @author ZiYueCommentary
 * @see mtr.render.RenderStationNameTiled
 * @see BlockStationNameEntranceMixin
 * @since beta-1
 */

@Mixin(RenderStationNameTiled.class)
public abstract class RenderStationNameTiledMixin extends RenderStationNameBase<BlockStationNameEntrance.TileEntityStationNameEntrance>
{
    @Shadow
    protected abstract int getLength(BlockGetter world, BlockPos pos, boolean lookRight);

    final boolean showLogo;

    public RenderStationNameTiledMixin(BlockEntityRenderDispatcher dispatcher, boolean showLogo) {
        super(dispatcher);
        this.showLogo = showLogo;
    }

    @Override
    protected void drawStationName(BlockGetter world, BlockPos pos, BlockState state, Direction facing, StoredMatrixTransformations storedMatrixTransformations, MultiBufferSource vertexConsumers, String stationName, int stationColor, int color, int light) {
        final int lengthLeft = getLength(world, pos, false);
        final int lengthRight = getLength(world, pos, true);
        final PoseStack poseStack = new PoseStack();
        storedMatrixTransformations.transform(poseStack);

        if (world == null) return;

        String displayContent = IGui.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, stationName);
        displayContent = ((BlockStationNameProperties) world.getBlockState(pos).getBlock()).getShowNameProperty(world.getBlockState(pos)) ? displayContent : stationName.replaceFirst(" Station", "").replaceFirst("站", "");
        final int totalLength = lengthLeft + lengthRight - 1;
        if (showLogo) {
            final int propagateProperty = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntrance.STYLE);
            final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;
            RenderTrains.scheduleRender(ClientData.DATA_CACHE.getStationNameEntrance(propagateProperty < 2 || propagateProperty >= 4 ? ARGB_WHITE : ARGB_BLACK, displayContent, totalLength / logoSize).resourceLocation, false, RenderTrains.QueuedRenderLayer.INTERIOR, (matrices, vertexConsumer) -> {
                storedMatrixTransformations.transform(matrices);
                IDrawing.drawTexture(matrices, vertexConsumer, -0.5F, -logoSize / 2, 1, logoSize, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, color, light);
                matrices.popPose();
            });
        } else {
            IDrawing.drawTexture(poseStack, vertexConsumers.getBuffer(MoreRenderLayers.getExterior(ClientData.DATA_CACHE.getStationName(stationName, totalLength).resourceLocation)), -0.5F, -0.5F, 1, 1, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, color, light);
        }
    }
}
