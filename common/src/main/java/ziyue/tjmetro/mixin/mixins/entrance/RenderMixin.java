package ziyue.tjmetro.mixin.mixins.entrance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameEntrance;
import mtr.block.BlockStationNameWallBase;
import mtr.block.IBlock;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.render.MoreRenderLayers;
import mtr.render.RenderStationNameBase;
import mtr.render.RenderStationNameTiled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mixin.properties.ShowNameProperty;

/**
 * Core of <i>No "station" of station name entrance</i> feature<br>
 * Inspired by <a href="https://github.com/jonafanho/Minecraft-Transit-Railway/issues/172">Issue #172: 奇怪的“站站”、“Station Station”</a>
 *
 * @author ZiYueCommentary
 * @see mtr.render.RenderStationNameTiled
 * @see EntranceMixin
 * @since 1.0b
 */

@Mixin(RenderStationNameTiled.class)
public abstract class RenderMixin extends RenderStationNameBase<BlockStationNameEntrance.TileEntityStationNameEntrance>
{
    final boolean showLogo;

    @Shadow
    protected abstract int getLength(BlockGetter world, BlockPos pos);

    public RenderMixin(BlockEntityRenderDispatcher dispatcher, boolean showLogo) {
        super(dispatcher);
        this.showLogo = showLogo;
    }

    @Override
    protected void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String stationName, int color, int light) {
        final BlockGetter world = entity.getLevel();
        final BlockPos pos = entity.getBlockPos();

        if (world == null) return;

        String displayContent = IGui.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, stationName);
        displayContent = ((ShowNameProperty) world.getBlockState(pos).getBlock()).getShowNameProperty(world.getBlockState(pos)) ? displayContent : stationName.replaceFirst(" Station", "").replaceFirst("站", "");
        final int length = getLength(world, pos);
        if (showLogo) {
            final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameBase.FACING);
            final int propagateProperty = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntrance.STYLE);
            final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;
            IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, displayContent, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, (length + logoSize) / 2 - 0.5F, 0, length - logoSize, logoSize - 0.125F, 40 / logoSize, propagateProperty < 2 || propagateProperty >= 4 ? ARGB_WHITE : ARGB_BLACK, false, MAX_LIGHT_GLOWING, ((x1, y1, x2, y2) -> {
                final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getInterior(new ResourceLocation("mtr:textures/sign/logo.png")));
                IDrawing.drawTexture(matrices, vertexConsumer, x1 - logoSize, -logoSize / 2, logoSize, logoSize, facing, MAX_LIGHT_GLOWING);
            }));
        } else if (entity instanceof BlockStationNameWallBase.TileEntityStationNameWallBase) {
            IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, displayContent, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, length / 2F - 0.5F, 0, length, 0.875F, 60, ((BlockStationNameWallBase.TileEntityStationNameWallBase) entity).color, false, light, null);
        }
    }
}
