package ziyue.tjmetro.mod.render;

import org.mtr.core.data.Station;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockStationNameBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import ziyue.tjmetro.mod.block.BlockStationNameEntranceTianjin;
import ziyue.tjmetro.mod.client.DynamicTextureCache;
import ziyue.tjmetro.mod.data.IGuiExtension;

import javax.annotation.Nullable;

/**
 * @author ZiYueCommentary
 * @see BlockStationNameEntranceTianjin
 * @since 1.0.0-beta-1
 */

public class RenderStationNameEntranceTianjin<T extends BlockStationNameEntranceTianjin.BlockEntity> extends BlockEntityRenderer<T> implements IGui, IDrawing
{
    public RenderStationNameEntranceTianjin(Argument argument) {
        super(argument);
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos pos = entity.getPos2();
        final BlockState state = world.getBlockState(pos);
        if (!(state.getBlock().data instanceof BlockStationNameEntranceTianjin block)) return;
        final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + pos.getX(), 0.5 + entity.yOffset + pos.getY(), 0.5 + pos.getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(-facing.asRotation());
            graphicsHolderNew.rotateZDegrees(180);
            graphicsHolderNew.translate(0, 0, 0.5 - entity.zOffset - SMALL_OFFSET);
        });

        final Station station = InitClient.findStation(pos);

        final int lengthLeft = getLength(world, pos, false);
        final int lengthRight = getLength(world, pos, true);

        final int totalLength = lengthLeft + lengthRight - 1;
        final int propagateProperty = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntranceTianjin.STYLE);
        final float logoSize = propagateProperty % 2 == 0 ? 0.5F : 1;

        final DynamicTextureCache.DynamicResource resource;
        if (station == null) {
            final int style = switch (propagateProperty) {
                case 0, 1, 4, 5 -> propagateProperty + 2;
                default -> propagateProperty;
            };
            resource = DynamicTextureCache.instance.getStationNameEntrance(-1, -1, style, IGuiExtension.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, TextHelper.translatable("gui.mtr.untitled").getString()), block.type, totalLength / logoSize);
        } else {
            resource = DynamicTextureCache.instance.getStationNameEntrance(station.getId(), entity.getSelectedId(), propagateProperty, IGuiExtension.insertTranslation("gui.mtr.station_cjk", block.pinyin ? "gui.tjmetro.station_pinyin" : "gui.mtr.station", 1, station.getName()), block.type, totalLength / logoSize);
        }
        MainRenderer.scheduleRender(resource.identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
            storedMatrixTransformations.transform(graphicsHolderNew, offset);
            IDrawing.drawTexture(graphicsHolderNew, -0.5F, -logoSize / 2, 1, logoSize, (float) (lengthLeft - 1) / totalLength, 0, (float) lengthLeft / totalLength, 1, facing, ARGB_WHITE, light);
            graphicsHolderNew.pop();
        });
    }

    protected int getLength(@Nullable World world, BlockPos pos, boolean lookRight) {
        if (world == null) return 1;
        final Direction facing = IBlock.getStatePropertySafe(world, pos, BlockStationNameEntranceTianjin.FACING);
        final Block thisBlock = world.getBlockState(pos).getBlock();

        int length = 1;
        while (true) {
            final Block checkBlock = world.getBlockState(pos.offset(lookRight ? facing.rotateYClockwise() : facing.rotateYCounterclockwise(), length)).getBlock();
            if (checkBlock.data instanceof BlockStationNameEntranceTianjin && checkBlock.data == thisBlock.data) {
                length++;
            } else {
                break;
            }
        }

        return length;
    }
}
