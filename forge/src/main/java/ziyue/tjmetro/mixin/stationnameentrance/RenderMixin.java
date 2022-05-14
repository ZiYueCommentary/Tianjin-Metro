package ziyue.tjmetro.mixin.stationnameentrance;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameEntrance;
import mtr.block.IBlock;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.render.MoreRenderLayers;
import mtr.render.RenderStationNameBase;
import mtr.render.RenderStationNameEntrance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import ziyue.tjmetro.ShowNameProperty;

/**
 * Core of <i>No "station" of station name entrance</i> feature<br>
 * Inspired by <a href="https://github.com/jonafanho/Minecraft-Transit-Railway/issues/172">Issue #172: 奇怪的“站站”、“Station Station”</a>
 * @author ZiYueCommentary
 * @since 1.0b
 * @see RenderStationNameEntrance
 */

@Mixin(RenderStationNameEntrance.class)
public abstract class RenderMixin extends RenderStationNameBase<BlockStationNameEntrance.TileEntityStationNameEntrance>
{
    public RenderMixin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void drawStationName(BlockStationNameBase.TileEntityStationNameBase entity, PoseStack matrices, MultiBufferSource vertexConsumers, MultiBufferSource.BufferSource immediate, String stationName, int color, int light) {
        BlockGetter world = entity.getLevel();
        BlockPos pos = entity.getBlockPos();
        if (world != null) {
            Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameBase.FACING);
            int propagateProperty = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntrance.STYLE);
            float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1.0F;
            int length = this.getLength(world, pos);
            String str = IGui.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, stationName);
            boolean showStation = ((ShowNameProperty)world.getBlockState(pos).getBlock()).getShowNameProperty(world.getBlockState(pos)); //need show "station"?
            IDrawing.drawStringWithFont(matrices, Minecraft.getInstance().font, immediate, showStation ? str : str.replaceFirst(" Station", "").replaceFirst("站", ""), HorizontalAlignment.LEFT, VerticalAlignment.CENTER, HorizontalAlignment.CENTER, ((float)length + logoSize) / 2.0F - 0.5F, 0.0F, (float)length - logoSize, logoSize - 0.125F, 40.0F / logoSize, propagateProperty >= 2 && propagateProperty < 4 ? -16777216 : -1, false, 15728880, (x1, y1, x2, y2) -> {
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getInterior(new ResourceLocation("mtr:textures/sign/logo.png")));
                IDrawing.drawTexture(matrices, vertexConsumer, x1 - logoSize, -logoSize / 2.0F, logoSize, logoSize, facing, 15728880);
            });
        }
    }

    private int getLength(BlockGetter world, BlockPos pos) {
        if (world == null) return 1;
        Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntrance.FACING);
        int length = 1;

        while(true) {
            BlockState state = world.getBlockState(pos.relative(facing.getClockWise(), length));
            if (!(state.getBlock() instanceof BlockStationNameEntrance)) return length;
            ++length;
        }
    }
}
