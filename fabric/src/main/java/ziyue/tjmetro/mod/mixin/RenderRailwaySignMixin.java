package ziyue.tjmetro.mod.mixin;

import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.data.Station;
import org.mtr.core.data.StationExit;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.QrCodeHelper;
import org.mtr.mod.block.BlockRailwaySign;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.DynamicTextureCache;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.RenderRailwaySign;
import org.mtr.mod.render.StoredMatrixTransformations;
import org.mtr.mod.resource.SignResource;
import org.mtr.mod.screen.EditStationScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mod.block.base.IRailwaySign;
import ziyue.tjmetro.mod.data.IGuiExtension;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.mtr.mod.render.RenderRailwaySign.getSign;

/**
 * @author ZiYueCommentary
 * @see RenderRailwaySign
 * @since 1.0.0-beta-1
 */

@Mixin(RenderRailwaySign.class)
public abstract class RenderRailwaySignMixin<T extends BlockRailwaySign.BlockEntity> extends BlockEntityRenderer<T> implements IBlock, IGui, IDrawing
{
    @Shadow(remap = false)
    private static void renderCustomText(String signText, StoredMatrixTransformations storedMatrixTransformations, Direction facing, float size, float start, boolean flipCustomText, float maxWidth, int backgroundColor) {
    }

    public RenderRailwaySignMixin(Argument argument) {
        super(argument);
    }

    /**
     * @author ZiYueCommentary
     * @reason Render dynamic signs of Tianjin Metro.
     */
    @Overwrite(remap = false)
    public static void drawSign(GraphicsHolder graphicsHolder, @Nullable StoredMatrixTransformations storedMatrixTransformations, BlockPos pos, String signId, float x, float y, float size, float maxWidthLeft, float maxWidthRight, LongAVLTreeSet selectedIds, Direction facing, int backgroundColor, RenderRailwaySign.DrawTexture drawTexture) {
        final SignResource sign = getSign(signId);
        if (sign == null) return;

        final float signSize = (sign.getSmall() ? BlockRailwaySign.SMALL_SIGN_PERCENTAGE : 1) * size;
        final float margin = (size - signSize) / 2;

        final boolean hasCustomText = sign.hasCustomText;
        final boolean flipCustomText = sign.getFlipCustomText();
        final boolean flipTexture = sign.getFlipTexture();
        final boolean isExit = IRailwaySign.signIsExit(signId);
        final boolean isLine = IRailwaySign.signIsLine(signId);
        final boolean isPlatform = IRailwaySign.signIsPlatform(signId);
        final boolean isStation = IRailwaySign.signIsStation(signId);
        final boolean isTransportSystemMap = signId.equals("transport_system_map") || signId.equals("transport_system_map_text") || signId.equals("transport_system_map_text_flipped");

        if (storedMatrixTransformations != null && isExit) {
            final Station station = InitClient.findStation(pos);
            if (station == null) return;

            final ObjectArrayList<StationExit> selectedExitsSorted = new ObjectArrayList<>();
            final ObjectArrayList<StationExit> exits = EditStationScreen.getStationExits(station, true);
            exits.forEach(exit -> {
                if (selectedIds.longStream().anyMatch(selectedId -> EditStationScreen.deserializeExit(selectedId).equals(exit.getName()))) {
                    selectedExitsSorted.add(exit);
                }
            });

            graphicsHolder.push();
            graphicsHolder.translate(x + margin + (flipCustomText ? signSize : 0), y + margin, 0);
            final float maxWidth = ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2;
            final float exitWidth = signSize * selectedExitsSorted.size();
            graphicsHolder.scale(Math.min(1, maxWidth / exitWidth), 1, 1);

            for (int i = 0; i < selectedExitsSorted.size(); i++) {
                final StationExit stationExit = selectedExitsSorted.get(flipCustomText ? selectedExitsSorted.size() - i - 1 : i);
                final float signOffset = (flipCustomText ? -1 : 1) * signSize * i - (flipCustomText ? signSize : 0);

                MainRenderer.scheduleRender(IRailwaySign.getExitSignResource(signId, stationExit.getName().substring(0, 1), stationExit.getName().substring(1), backgroundColor, ARGB_WHITE, true), true, QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolderNew, offset) -> {
                    storedMatrixTransformations.transform(graphicsHolderNew, offset);
                    graphicsHolderNew.translate(x + margin + (flipCustomText ? signSize : 0), y + margin, 0);
                    graphicsHolderNew.scale(Math.min(1, maxWidth / exitWidth), 1, 1);
                    IDrawing.drawTexture(graphicsHolderNew, signOffset, 0, signSize, signSize, facing, GraphicsHolder.getDefaultLight());
                    graphicsHolderNew.pop();
                });

                if (maxWidth > exitWidth && selectedExitsSorted.size() == 1 && !stationExit.getDestinations().isEmpty()) {
                    renderCustomText(stationExit.getDestinations().get(0), storedMatrixTransformations, facing, size, flipCustomText ? x : x + size, flipCustomText, maxWidth - exitWidth - margin * 2, backgroundColor);
                }
            }

            graphicsHolder.pop();
        } else if (storedMatrixTransformations != null && isLine) {
            final Station station = InitClient.findStation(pos);
            if (station == null) return;

            final LongAVLTreeSet platformIds = new LongAVLTreeSet();
            station.savedRails.forEach(platform -> platformIds.add(platform.getId()));
            station.connectedStations.forEach(connectingStation -> connectingStation.savedRails.forEach(platform -> platformIds.add(platform.getId())));

            final ObjectArrayList<IntObjectImmutablePair<String>> selectedRoutesSorted = new ObjectArrayList<>();
            final IntAVLTreeSet addedColors = new IntAVLTreeSet();
            MinecraftClientData.getInstance().simplifiedRoutes.forEach(simplifiedRoute -> {
                final int color = simplifiedRoute.getColor();
                if (!addedColors.contains(color) && selectedIds.contains(color) && simplifiedRoute.getPlatforms().stream().anyMatch(simplifiedRoutePlatform -> platformIds.contains(simplifiedRoutePlatform.getPlatformId()))) {
                    selectedRoutesSorted.add(new IntObjectImmutablePair<>(color, simplifiedRoute.getName().split("\\|\\|")[0]));
                    addedColors.add(color);
                }
            });

            selectedRoutesSorted.sort(Comparator.comparingInt(IntObjectImmutablePair::leftInt));
            final float maxWidth = Math.max(0, ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2);
            final float height = size - margin * 2;
            final List<DynamicTextureCache.DynamicResource> resourceLocationDataList = new ArrayList<>();
            float totalTextWidth = 0;
            for (final IntObjectImmutablePair<String> route : selectedRoutesSorted) {
                final DynamicTextureCache.DynamicResource resourceLocationData = DynamicTextureCache.instance.getRouteSquare(route.leftInt(), route.right(), flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
                resourceLocationDataList.add(resourceLocationData);
                totalTextWidth += height * resourceLocationData.width / resourceLocationData.height + margin / 2F;
            }

            final StoredMatrixTransformations storedMatrixTransformations2 = storedMatrixTransformations.copy();
            storedMatrixTransformations2.add(graphicsHolderNew -> graphicsHolderNew.translate(flipCustomText ? x + size - margin : x + margin, 0, 0));

            if (totalTextWidth > margin / 2F) {
                totalTextWidth -= margin / 2F;
            }
            if (totalTextWidth > maxWidth) {
                final float finalTotalTextWidth = totalTextWidth;
                storedMatrixTransformations2.add(graphicsHolderNew -> graphicsHolderNew.scale(maxWidth / finalTotalTextWidth, 1, 1));
            }

            float xOffset = 0;
            for (final DynamicTextureCache.DynamicResource resourceLocationData : resourceLocationDataList) {
                final float width = height * resourceLocationData.width / resourceLocationData.height;
                final float finalXOffset = xOffset;
                MainRenderer.scheduleRender(resourceLocationData.identifier, true, QueuedRenderLayer.LIGHT, (graphicsHolderNew, offset) -> {
                    storedMatrixTransformations2.transform(graphicsHolderNew, offset);
                    IDrawing.drawTexture(graphicsHolderNew, flipCustomText ? -finalXOffset - width : finalXOffset, margin, width, height, Direction.UP, GraphicsHolder.getDefaultLight());
                    graphicsHolderNew.pop();
                });
                xOffset += width + margin / 2F;
            }
        } else if (storedMatrixTransformations != null && isPlatform) {
            final Station station = InitClient.findStation(pos);
            if (station == null) return;

            final LongArrayList selectedIdsSorted = station.savedRails.stream().sorted().mapToLong(NameColorDataBase::getId).filter(selectedIds::contains).boxed().collect(Collectors.toCollection(LongArrayList::new));
            final int selectedCount = selectedIdsSorted.size();

            final float extraMargin = margin - margin / selectedCount;
            final float height = (size - extraMargin * 2) / selectedCount;
            for (int i = 0; i < selectedIdsSorted.size(); i++) {
                final float topOffset = i * height + extraMargin;
                final float bottomOffset = (i + 1) * height + extraMargin;
                final float left = flipCustomText ? x - maxWidthLeft * size : x + margin;
                final float right = flipCustomText ? x + size - margin : x + (maxWidthRight + 1) * size;
                MainRenderer.scheduleRender(IRailwaySign.getPlatformSignResource(signId, selectedIdsSorted.getLong(i), flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, margin / size, (right - left) / (bottomOffset - topOffset), backgroundColor, ARGB_WHITE, backgroundColor, true), true, QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolderNew, offset) -> {
                    storedMatrixTransformations.transform(graphicsHolderNew, offset);
                    IDrawing.drawTexture(graphicsHolderNew, left, topOffset, 0, right, bottomOffset, 0, 0, 0, 1, 1, facing, -1, GraphicsHolder.getDefaultLight());
                    graphicsHolderNew.pop();
                });
            }
        } else {
            if (storedMatrixTransformations != null && isTransportSystemMap) {
                final StoredMatrixTransformations storedMatrixTransformationsNew = storedMatrixTransformations.copy();
                storedMatrixTransformationsNew.add(graphicsHolderNew -> graphicsHolderNew.translate(x, y, 0));
                QrCodeHelper.INSTANCE.renderQrCode(storedMatrixTransformationsNew, QueuedRenderLayer.LIGHT, signSize);
            } else {
                drawTexture.drawTexture(sign.getTexture(), x + margin, y + margin, signSize, flipTexture);
            }

            if (hasCustomText) {
                final float fixedMargin = size * (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2;
                final boolean isSmall = sign.getSmall();
                final float maxWidth = Math.max(0, (flipCustomText ? maxWidthLeft : maxWidthRight) * size - fixedMargin * (isSmall ? 1 : 2));
                final float start = flipCustomText ? x - (isSmall ? 0 : fixedMargin) : x + size + (isSmall ? 0 : fixedMargin);
                if (storedMatrixTransformations == null) {
                    IDrawing.drawStringWithFont(graphicsHolder, isExit || isLine ? "..." : sign.getCustomText().getString(), flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, start, y + fixedMargin, maxWidth, size - fixedMargin * 2, 0.01F, ARGB_WHITE, false, GraphicsHolder.getDefaultLight(), null);
                } else {
                    final String signText;
                    if (isStation) {
                        signText = IGui.mergeStations(selectedIds.longStream()
                                .filter(MinecraftClientData.getInstance().stationIdMap::containsKey)
                                .sorted()
                                .mapToObj(stationId -> IGuiExtension.insertTranslation("gui.mtr.station_cjk", "gui.mtr.station", 1, MinecraftClientData.getInstance().stationIdMap.get(stationId).getName()))
                                .collect(Collectors.toList())
                        );
                    } else {
                        signText = sign.getCustomText().getString();
                    }
                    renderCustomText(signText, storedMatrixTransformations, facing, size, start, flipCustomText, maxWidth, backgroundColor);
                }
            }
        }
    }
}
