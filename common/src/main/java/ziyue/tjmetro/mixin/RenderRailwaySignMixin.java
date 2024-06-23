package ziyue.tjmetro.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import mtr.block.BlockRailwaySign;
import mtr.block.IBlock;
import mtr.client.ClientData;
import mtr.client.CustomResources;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.render.RenderRailwaySign;
import mtr.render.RenderTrains;
import mtr.render.StoredMatrixTransformations;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.block.base.IRailwaySign;
import ziyue.tjmetro.client.ClientCache;

import java.util.*;
import java.util.stream.Collectors;

import static mtr.render.RenderRailwaySign.getSign;

@Mixin(RenderRailwaySign.class)
public abstract class RenderRailwaySignMixin<T extends BlockRailwaySign.TileEntityRailwaySign> extends BlockEntityRendererMapper<T> implements IBlock, IGui, IDrawing
{
    @Shadow(remap = false)
    private static void renderCustomText(String signText, StoredMatrixTransformations storedMatrixTransformations, Direction facing, float size, float start, boolean flipCustomText, float maxWidth, int backgroundColor) {
    }

    public RenderRailwaySignMixin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static void drawSign(PoseStack matrices, MultiBufferSource vertexConsumers, StoredMatrixTransformations storedMatrixTransformations, Font textRenderer, BlockPos pos, String signId, float x, float y, float size, float maxWidthLeft, float maxWidthRight, Set<Long> selectedIds, Direction facing, int backgroundColor, RenderRailwaySign.DrawTexture drawTexture) {
        if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) return;

        final CustomResources.CustomSign sign = getSign(signId);
        if (sign == null) return;

        final float signSize = (sign.small ? BlockRailwaySign.SMALL_SIGN_PERCENTAGE : 1) * size;
        final float margin = (size - signSize) / 2;

        final boolean hasCustomText = sign.hasCustomText();
        final boolean flipCustomText = sign.flipCustomText;
        final boolean flipTexture = sign.flipTexture;
        final boolean isExit = IRailwaySign.signIsExit(signId);
        final boolean isLine = IRailwaySign.signIsLine(signId);
        final boolean isPlatform = IRailwaySign.signIsPlatform(signId);
        final boolean isStation = IRailwaySign.signIsStation(signId);

        final MultiBufferSource.BufferSource immediate = RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance / 2, null) ? null : MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        if (vertexConsumers != null && isExit) {
            final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
            if (station == null) return;

            final Map<String, List<String>> exits = station.getGeneratedExits();
            final List<String> selectedExitsSorted = selectedIds.stream().map(Station::deserializeExit).filter(exits::containsKey).sorted(String::compareTo).toList();

            matrices.pushPose();
            matrices.translate(x + margin + (flipCustomText ? signSize : 0), y + margin, 0);
            final float maxWidth = ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2;
            final float exitWidth = signSize * selectedExitsSorted.size();
            matrices.scale(Math.min(1, maxWidth / exitWidth), 1, 1);

            for (int i = 0; i < selectedExitsSorted.size(); i++) {
                final String selectedExit = selectedExitsSorted.get(flipCustomText ? selectedExitsSorted.size() - i - 1 : i);
                final float offset = (flipCustomText ? -1 : 1) * signSize * i - (flipCustomText ? signSize : 0);

                RenderTrains.scheduleRender(IRailwaySign.getExitSignResource(signId, selectedExit.substring(0, 1), selectedExit.substring(1), backgroundColor, true), true, RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, (matricesNew, vertexConsumer) -> {
                    storedMatrixTransformations.transform(matricesNew);
                    matricesNew.translate(x + margin + (flipCustomText ? signSize : 0), y + margin, 0);
                    matricesNew.scale(Math.min(1, maxWidth / exitWidth), 1, 1);
                    IDrawing.drawTexture(matricesNew, vertexConsumer, offset, 0, signSize, signSize, facing, MAX_LIGHT_GLOWING);
                    matricesNew.popPose();
                });

                if (maxWidth > exitWidth && selectedExitsSorted.size() == 1 && !exits.get(selectedExit).isEmpty()) {
                    renderCustomText(exits.get(selectedExit).get(0), storedMatrixTransformations, facing, size, flipCustomText ? x : x + size, flipCustomText, maxWidth - exitWidth - margin * 2, backgroundColor);
                }
            }

            matrices.popPose();
        } else if (vertexConsumers != null && isLine) {
            final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
            if (station == null) return;

            final Map<Integer, mtr.client.ClientCache.ColorNameTuple> routesInStation = ClientData.DATA_CACHE.getAllRoutesIncludingConnectingStations(station);
            final List<mtr.client.ClientCache.ColorNameTuple> selectedIdsSorted = selectedIds.stream().filter(selectedId -> RailwayData.isBetween(selectedId, Integer.MIN_VALUE, Integer.MAX_VALUE)).map(Math::toIntExact).filter(routesInStation::containsKey).map(routesInStation::get).sorted(Comparator.comparingInt(route -> route.color)).toList();

            final float maxWidth = Math.max(0, ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2);
            final float height = size - margin * 2;
            final List<mtr.client.ClientCache.DynamicResource> resourceLocationDataList = new ArrayList<>();
            float totalTextWidth = 0;
            for (final mtr.client.ClientCache.ColorNameTuple route : selectedIdsSorted) {
                final mtr.client.ClientCache.DynamicResource resourceLocationData = ClientData.DATA_CACHE.getRouteSquare(route.color, route.name, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
                resourceLocationDataList.add(resourceLocationData);
                totalTextWidth += height * resourceLocationData.width / resourceLocationData.height + margin / 2F;
            }

            final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
            storedMatrixTransformations2.add(matricesNew -> matricesNew.translate(flipCustomText ? x + size - margin : x + margin, 0, 0));

            if (totalTextWidth > margin / 2F) {
                totalTextWidth -= margin / 2F;
            }
            if (totalTextWidth > maxWidth) {
                final float finalTotalTextWidth = totalTextWidth;
                storedMatrixTransformations2.add(matricesNew -> matricesNew.scale(maxWidth / finalTotalTextWidth, 1, 1));
            }

            float xOffset = 0;
            for (final mtr.client.ClientCache.DynamicResource resourceLocationData : resourceLocationDataList) {
                final float width = height * resourceLocationData.width / resourceLocationData.height;
                final float finalXOffset = xOffset;
                RenderTrains.scheduleRender(resourceLocationData.resourceLocation, true, RenderTrains.QueuedRenderLayer.LIGHT, (matricesNew, vertexConsumer) -> {
                    storedMatrixTransformations2.transform(matricesNew);
                    IDrawing.drawTexture(matricesNew, vertexConsumer, flipCustomText ? -finalXOffset - width : finalXOffset, margin, width, height, Direction.UP, MAX_LIGHT_GLOWING);
                    matricesNew.popPose();
                });
                xOffset += width + margin / 2F;
            }
        } else if (vertexConsumers != null && isPlatform) {
            final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
            if (station == null) return;

            final Map<Long, Platform> platformPositions = ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id);
            if (platformPositions != null) {
                final List<Long> selectedIdsSorted = selectedIds.stream().filter(platformPositions::containsKey).sorted(Comparator.comparing(platformPositions::get)).toList();
                final int selectedCount = selectedIdsSorted.size();

                final float extraMargin = margin - margin / selectedCount;
                final float height = (size - extraMargin * 2) / selectedCount;
                for (int i = 0; i < selectedIdsSorted.size(); i++) {
                    final float topOffset = i * height + extraMargin;
                    final float bottomOffset = (i + 1) * height + extraMargin;
                    final float left = flipCustomText ? x - maxWidthLeft * size : x + margin;
                    final float right = flipCustomText ? x + size - margin : x + (maxWidthRight + 1) * size;
                    RenderTrains.scheduleRender(IRailwaySign.getPlatformSignResource(signId, selectedIdsSorted.get(i), flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, margin / size, (right - left) / (bottomOffset - topOffset), backgroundColor, ARGB_WHITE, backgroundColor, true), true, RenderTrains.QueuedRenderLayer.LIGHT_TRANSLUCENT, (matricesNew, vertexConsumer) -> {
                        storedMatrixTransformations.transform(matricesNew);
                        IDrawing.drawTexture(matricesNew, vertexConsumer, left, topOffset, 0, right, bottomOffset, 0, 0, 0, 1, 1, facing, -1, MAX_LIGHT_GLOWING);
                        matricesNew.popPose();
                    });
                }
            }
        } else {
            drawTexture.drawTexture(sign.textureId, x + margin, y + margin, signSize, flipTexture);

            if (hasCustomText) {
                final float fixedMargin = size * (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2;
                final boolean isSmall = sign.small;
                final float maxWidth = Math.max(0, (flipCustomText ? maxWidthLeft : maxWidthRight) * size - fixedMargin * (isSmall ? 1 : 2));
                final float start = flipCustomText ? x - (isSmall ? 0 : fixedMargin) : x + size + (isSmall ? 0 : fixedMargin);
                if (vertexConsumers == null) {
                    IDrawing.drawStringWithFont(matrices, textRenderer, immediate, isExit || isLine ? "..." : sign.customText, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, start, y + fixedMargin, maxWidth, size - fixedMargin * 2, 0.01F, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
                } else {
                    final String signText;
                    if (isStation) {
                        signText = IGui.mergeStations(selectedIds.stream().filter(ClientData.DATA_CACHE.stationIdMap::containsKey).sorted(Long::compareTo).map(stationId -> IGui.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, ClientData.DATA_CACHE.stationIdMap.get(stationId).name)).collect(Collectors.toList()));
                    } else {
                        signText = sign.customText;
                    }
                    renderCustomText(signText, storedMatrixTransformations, facing, size, start, flipCustomText, maxWidth, backgroundColor);
                }
            }
        }

        if (immediate != null) immediate.endBatch();
    }
}