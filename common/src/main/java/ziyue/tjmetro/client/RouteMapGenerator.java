package ziyue.tjmetro.client;

import com.mojang.blaze3d.platform.NativeImage;
import mtr.MTR;
import mtr.client.ClientData;
import mtr.client.Config;
import mtr.data.*;
import mtr.mappings.Text;
import mtr.mappings.Utilities;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import ziyue.tjmetro.IDrawingExtends;
import ziyue.tjmetro.TianjinMetro;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static mtr.client.ClientCache.LINE_HEIGHT_MULTIPLIER;

/**
 * @author ZiYueCommentary
 * @see mtr.client.RouteMapGenerator
 * @since beta-1
 */

public class RouteMapGenerator implements IGui
{
    protected static int scale;
    protected static int lineSize;
    protected static int lineSpacing;
    protected static int fontSizeBig;
    protected static int fontSizeSmall;

    protected static final int MIN_VERTICAL_SIZE = 5;
    protected static final String TEMP_CIRCULAR_MARKER = "temp_circular_marker";
    protected static final String ARROW_RESOURCE = "textures/block/sign/arrow.png";
    protected static final String CIRCLE_RESOURCE = "textures/block/sign/circle.png";

    public static void setConstants() {
        scale = (int) Math.pow(2, Config.dynamicTextureResolution() + 5);
        lineSize = scale / 8;
        lineSpacing = lineSize * 3 / 2;
        fontSizeBig = lineSize * 2;
        fontSizeSmall = fontSizeBig / 2;
    }

    public static int getScale() {
        return scale;
    }

    public static NativeImage generateDirectionArrow(long platformId, boolean hasLeft, boolean hasRight, HorizontalAlignment horizontalAlignment, boolean showToString, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        if (aspectRatio <= 0) return null;

        try {
            final List<String> destinations = new ArrayList<>();
            final List<Route> routes = getRouteStream(platformId, (route, currentStationIndex) -> destinations.add(ClientData.DATA_CACHE.getFormattedRouteDestination(route, currentStationIndex, TEMP_CIRCULAR_MARKER)));
            final boolean isTerminating = destinations.isEmpty();

            final boolean leftToRight = horizontalAlignment == HorizontalAlignment.CENTER ? hasLeft || !hasRight : horizontalAlignment != HorizontalAlignment.RIGHT;
            final int height = scale;
            final int width = Math.round(height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;

            if (width <= 0 || height <= 0) return null;

            final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
            nativeImage.fillRect(0, 0, width, height, invertColor(backgroundColor));

            final boolean customFont = ziyue.tjmetro.Config.USE_TIANJIN_METRO_FONT.get();

            final int circleX;
            if (isTerminating) {
                circleX = (int) horizontalAlignment.getOffset(0, tileSize - width);
            } else {
                String destinationString = IGui.mergeStations(destinations);
                final boolean noToString = destinationString.startsWith(TEMP_CIRCULAR_MARKER);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER, "");
                if (!destinationString.isEmpty() && showToString && !noToString) {
                    destinationString = IGui.insertTranslation("gui.mtr.to_cjk", "gui.mtr.to", 1, destinationString);
                }

                final int tilePadding = tileSize / 4;
                final int leftSize = ((hasLeft ? 1 : 0) + (leftToRight ? 1 : 0)) * (tileSize + tilePadding);
                final int rightSize = ((hasRight ? 1 : 0) + (leftToRight ? 0 : 1)) * (tileSize + tilePadding);

                final ClientCache.Text destination = ClientCache.DATA_CACHE.getText(destinationString, width - leftSize - rightSize - padding * (showToString ? 2 : 1), (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize * 3 / 5, tileSize * 3 / 10, tilePadding, leftToRight ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
                final int leftPadding = (int) horizontalAlignment.getOffset(0, leftSize + rightSize + destination.renderWidth() - tilePadding * 2 - width);
                drawString(nativeImage, destination, leftPadding + leftSize - tilePadding, height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, backgroundColor, textColor, false);

                if (hasLeft) {
                    drawResource(nativeImage, ARROW_RESOURCE, leftPadding, padding, tileSize, tileSize, false, 0, 1, textColor, false);
                }
                if (hasRight) {
                    drawResource(nativeImage, ARROW_RESOURCE, leftPadding + leftSize + destination.renderWidth() - tilePadding * 2 + rightSize - tileSize, padding, tileSize, tileSize, true, 0, 1, textColor, false);
                }

                circleX = leftPadding + leftSize + (leftToRight ? -tileSize - tilePadding : destination.renderWidth() - tilePadding);
            }

            for (int i = 0; i < routes.size(); i++) {
                drawResource(nativeImage, CIRCLE_RESOURCE, circleX, padding, tileSize, tileSize, false, (float) i / routes.size(), (i + 1F) / routes.size(), routes.get(i).color, false);
            }

            final Platform platform = ClientCache.DATA_CACHE.platformIdMap.get(platformId);
            if (platform != null) {
                final ClientCache.Text platformNumber = ClientCache.DATA_CACHE.getText(platform.name, tileSize, (int) (tileSize * LINE_HEIGHT_MULTIPLIER * 3 / 4), tileSize * 3 / (customFont ? 1 : 4), tileSize * 3 / (customFont ? 1 : 4), 0, HorizontalAlignment.CENTER);
                drawString(nativeImage, platformNumber, circleX + tileSize / 2, padding + tileSize / 2 - (customFont ? 8 : 0), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
            }

            if (transparentColor != 0) {
                clearColor(nativeImage, invertColor(transparentColor));
            }

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage());
        }

        return null;
    }

    public static NativeImage generatePSDStationName(long platformId, HorizontalAlignment horizontalAlignment, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        if (aspectRatio <= 0) return null;

        try {
            final boolean leftToRight = horizontalAlignment != HorizontalAlignment.RIGHT;
            final int height = scale;
            final int width = Math.round(height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;

            if (width <= 0 || height <= 0) return null;


            final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
            nativeImage.fillRect(0, 0, width, height, invertColor(backgroundColor));

            final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, ClientData.DATA_CACHE.platformIdMap.get(platformId).getMidPos());

            final int tilePadding = tileSize / 4;
            final int leftSize = ((leftToRight ? 1 : 0)) * (tileSize + tilePadding);
            final int rightSize = ((leftToRight ? 0 : 1)) * (tileSize + tilePadding);

            final ClientCache.Text destination = ClientCache.DATA_CACHE.getText(station.name, width - leftSize - rightSize - padding, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize * 3, tileSize * 3 / 2, tilePadding, HorizontalAlignment.CENTER);
            drawString(nativeImage, destination, width / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, backgroundColor, textColor, false);

            if (transparentColor != 0) {
                clearColor(nativeImage, invertColor(transparentColor));
            }

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage());
        }

        return null;
    }

    public static NativeImage generatePSDNextStation(long platformId, int arrowDirection, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        if (aspectRatio <= 0) return null;

        try {
            final List<String> destinations = new ArrayList<>();
            final List<String> nextStations = new ArrayList<>();
            getRouteStream(platformId, (route, currentStationIndex) -> {
                destinations.add(ClientData.DATA_CACHE.getFormattedRouteDestination(route, currentStationIndex, TEMP_CIRCULAR_MARKER));
                nextStations.add(ClientData.DATA_CACHE.platformIdToStation.get(route.platformIds.get(currentStationIndex + 1).platformId).name);
            });
            final boolean isTerminating = destinations.isEmpty();

            final int height = scale;
            final int width = Math.round(height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;

            if (width <= 0 || height <= 0) return null;

            final NativeImage nativeImage;

            final int tilePadding = tileSize / 4;
            final int leftSize = tileSize + tilePadding;

            if (isTerminating) {
                final boolean customFont = ziyue.tjmetro.Config.USE_TIANJIN_METRO_FONT.get();
                nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
                nativeImage.fillRect(0, 0, width, height, invertColor(backgroundColor));
                final ClientCache.Text destination = ClientCache.DATA_CACHE.getText(IDrawingExtends.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus"), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize * 3 / (customFont ? 1 : 5), tileSize * 3 / 2 / (customFont ? 1 : 5), tilePadding, HorizontalAlignment.CENTER);
                drawString(nativeImage, destination, width / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, backgroundColor, textColor, false);
            } else {
                final int arrowSizeAndPadding = switch (arrowDirection) {
                    case 0, 3 -> -tilePadding;
                    default -> tileSize;
                };
                String destinationString = IGui.mergeStations(destinations);
                String nextStationString = IGui.mergeStations(nextStations);
                final boolean noToString = destinationString.startsWith(TEMP_CIRCULAR_MARKER);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER, "");
                if (!noToString)
                    destinationString = IGui.insertTranslation("gui.mtr.to_cjk", "gui.mtr.to", 1, destinationString);
                final ClientCache.Text textDestination = ClientCache.DATA_CACHE.getText(destinationString, width - leftSize - padding * 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize, tileSize / 2, tilePadding, (arrowDirection == 2) ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
                final ClientCache.Text textNextStations = ClientCache.DATA_CACHE.getText(nextStationString, width - leftSize - padding * 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, tilePadding, (arrowDirection == 2) ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
                final ClientCache.Text textNextStationSign = ClientCache.DATA_CACHE.getText(IDrawingExtends.mergeTranslation("gui.tjmetro.next_station_cjk", "gui.tjmetro.next_station"), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, tilePadding, HorizontalAlignment.CENTER);
                int renderWidth = Math.max(width, leftSize + arrowSizeAndPadding + textDestination.width() + textNextStationSign.width() + tilePadding + textNextStations.width() + leftSize);
                final boolean renderNextStation = (width - leftSize - arrowSizeAndPadding - textDestination.width() - leftSize) > textNextStations.width();
                if (!renderNextStation)
                    renderWidth = Math.max(width, renderWidth - textNextStations.width() - tilePadding - textNextStationSign.width());
                nativeImage = new NativeImage(NativeImage.Format.RGBA, renderWidth, height, false);
                nativeImage.fillRect(0, 0, renderWidth, height, invertColor(backgroundColor));
                if (arrowDirection == 2) {
                    drawResource(nativeImage, ARROW_RESOURCE, renderWidth - leftSize - tileSize, padding, tileSize, tileSize, true, 0, 1, textColor, false);
                    drawString(nativeImage, textDestination, renderWidth - leftSize - tileSize, height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, backgroundColor, textColor, false);
                    if (renderNextStation) {
                        drawString(nativeImage, textNextStationSign, leftSize - tilePadding, height, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, textColor, false);
                        drawString(nativeImage, textNextStations, leftSize - tilePadding + textNextStationSign.width(), height, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, textColor, false);
                    }
                } else {
                    if (arrowSizeAndPadding > 0)
                        drawResource(nativeImage, ARROW_RESOURCE, leftSize, padding, tileSize, tileSize, false, 0, 1, textColor, false);
                    drawString(nativeImage, textDestination, leftSize + arrowSizeAndPadding, height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, backgroundColor, textColor, false);
                    if (renderNextStation) {
                        drawString(nativeImage, textNextStations, renderWidth - leftSize + tilePadding, height, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, 0, textColor, false);
                        drawString(nativeImage, textNextStationSign, renderWidth - leftSize - textNextStations.width() + tilePadding, height, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, 0, textColor, false);
                    }
                }
            }

            if (transparentColor != 0) {
                clearColor(nativeImage, invertColor(transparentColor));
            }

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage());
        }

        return null;
    }

    public static NativeImage generateRouteMap(long platformId, boolean vertical, boolean flip, float aspectRatio, boolean transparentWhite) {
        if (aspectRatio <= 0) return null;

        try {
            final List<Tuple<Route, Integer>> routeDetails = new ArrayList<>();
            getRouteStream(platformId, (route, currentStationIndex) -> routeDetails.add(new Tuple<>(route, currentStationIndex)));
            final int routeCount = routeDetails.size();

            if (routeCount > 0) {
                final List<List<Long>> stationsIdsBefore = new ArrayList<>();
                final List<List<Long>> stationsIdsAfter = new ArrayList<>();
                final List<Map<Integer, StationPosition>> stationPositions = new ArrayList<>();
                final int[] colorIndices = new int[routeCount];
                final Set<Integer> currentRouteColors = new HashSet<>();
                final Set<String> currentRouteNames = new HashSet<>();
                int colorIndex = -1;
                int previousColor = -1;
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationsIdsBefore.add(new ArrayList<>());
                    stationsIdsAfter.add(new ArrayList<>());
                    stationPositions.add(new HashMap<>());

                    final Tuple<Route, Integer> routeDetail = routeDetails.get(routeIndex);
                    final List<Route.RoutePlatform> platformIds = routeDetail.getA().platformIds;
                    final int currentIndex = routeDetail.getB();
                    for (int stationIndex = 0; stationIndex < platformIds.size(); stationIndex++) {
                        if (stationIndex != currentIndex) {
                            final long stationId = getStationId(platformIds.get(stationIndex).platformId);
                            if (stationIndex < currentIndex) {
                                stationsIdsBefore.get(stationsIdsBefore.size() - 1).add(0, stationId);
                            } else {
                                stationsIdsAfter.get(stationsIdsAfter.size() - 1).add(stationId);
                            }
                        }
                    }

                    final int color = routeDetail.getA().color;
                    if (color != previousColor) {
                        colorIndex++;
                        previousColor = color;
                    }
                    colorIndices[routeIndex] = colorIndex;
                    currentRouteColors.add(color);
                    currentRouteNames.add(routeDetail.getA().name.split("\\|\\|")[0]);
                }

                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationPositions.get(routeIndex).put(0, new StationPosition(0, getLineOffset(routeIndex, colorIndices), true));
                }

                final float[] bounds = new float[3];
                setup(stationPositions, flip ? stationsIdsBefore : stationsIdsAfter, colorIndices, bounds, flip, true);
                final float xOffset = bounds[0] + 0.5F;
                setup(stationPositions, flip ? stationsIdsAfter : stationsIdsBefore, colorIndices, bounds, !flip, false);
                final float rawHeightPart = Math.abs(bounds[1]) + (vertical ? 0.6F : 1);
                final float rawWidth = xOffset + bounds[0] + 0.5F;
                final float rawHeightTotal = rawHeightPart + bounds[2] + (vertical ? 0.6F : 1);
                final float rawHeight;
                final float yOffset;
                final float extraPadding;
                if (vertical && rawHeightTotal < MIN_VERTICAL_SIZE) {
                    rawHeight = MIN_VERTICAL_SIZE;
                    extraPadding = (MIN_VERTICAL_SIZE - rawHeightTotal) / 2;
                    yOffset = rawHeightPart + extraPadding;
                } else {
                    rawHeight = rawHeightTotal;
                    extraPadding = 0;
                    yOffset = rawHeightPart;
                }

                final int height;
                final int width;
                final float widthScale;
                final float heightScale;
                if (rawWidth / rawHeight > aspectRatio) {
                    width = Math.round(rawWidth * scale);
                    height = Math.round(width / aspectRatio);
                    widthScale = 1;
                    heightScale = height / rawHeight / scale;
                } else {
                    height = Math.round(rawHeight * scale);
                    width = Math.round(height * aspectRatio);
                    heightScale = 1;
                    widthScale = width / rawWidth / scale;
                }

                if (width <= 0 || height <= 0) {
                    return null;
                }

                final NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, width, height, false);
                nativeImage.fillRect(0, 0, width, height, ARGB_WHITE);

                final Map<Long, Set<StationPositionGrouped>> stationPositionsGrouped = new HashMap<>();
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    final Route route = routeDetails.get(routeIndex).getA();
                    final int currentIndex = routeDetails.get(routeIndex).getB();
                    final Map<Integer, StationPosition> routeStationPositions = stationPositions.get(routeIndex);

                    for (int stationIndex = 0; stationIndex < route.platformIds.size(); stationIndex++) {
                        final StationPosition stationPosition = routeStationPositions.get(stationIndex - currentIndex);
                        if (stationIndex < route.platformIds.size() - 1) {
                            drawLine(nativeImage, stationPosition, routeStationPositions.get(stationIndex + 1 - currentIndex), widthScale, heightScale, xOffset, yOffset, stationIndex < currentIndex ? ARGB_LIGHT_GRAY : ARGB_BLACK | route.color);
                        }

                        final long stationId = getStationId(route.platformIds.get(stationIndex).platformId);
                        if (!stationPositionsGrouped.containsKey(stationId)) {
                            stationPositionsGrouped.put(stationId, new HashSet<>());
                        }
                        if (!stationPosition.isCommon || stationPositionsGrouped.get(stationId).stream().noneMatch(stationPosition2 -> stationPosition2.stationPosition.x == stationPosition.x)) {
                            final Map<Integer, mtr.client.ClientCache.ColorNameTuple> interchangeRoutes = getInterchangeRoutes(stationId);
                            final List<Integer> allColors = new ArrayList<>(interchangeRoutes.keySet());
                            allColors.sort(Integer::compareTo);

                            final List<Integer> interchangeColors = new ArrayList<>();
                            final List<String> interchangeNames = new ArrayList<>();
                            allColors.forEach(color -> {
                                final String name = interchangeRoutes.get(color).name;
                                if (!currentRouteColors.contains(color) && !currentRouteNames.contains(name)) {
                                    if (!interchangeColors.contains(color)) {
                                        interchangeColors.add(color);
                                    }
                                    if (!interchangeNames.contains(name)) {
                                        interchangeNames.add(name);
                                    }
                                }
                            });
                            stationPositionsGrouped.get(stationId).add(new StationPositionGrouped(stationPosition, stationIndex - currentIndex, interchangeColors, interchangeNames));
                        }
                    }
                }

                final int maxStringWidth = (int) (scale * 0.9 * ((vertical ? heightScale : widthScale) / 2 + extraPadding / routeCount));
                stationPositionsGrouped.forEach((stationId, stationPositionGroupedSet) -> stationPositionGroupedSet.forEach(stationPositionGrouped -> {
                    final int x = Math.round((stationPositionGrouped.stationPosition.x + xOffset) * scale * widthScale);
                    final int y = Math.round((stationPositionGrouped.stationPosition.y + yOffset) * scale * heightScale);
                    final int lines = stationPositionGrouped.stationPosition.isCommon ? colorIndices[colorIndices.length - 1] : 0;
                    final boolean textBelow = vertical || (stationPositionGrouped.stationPosition.isCommon ? Math.abs(stationPositionGrouped.stationOffset) % 2 == 0 : y >= yOffset * scale);
                    final boolean currentStation = stationPositionGrouped.stationOffset == 0;
                    final boolean passed = stationPositionGrouped.stationOffset < 0;

                    final List<Integer> interchangeColors = stationPositionGrouped.interchangeColors;
                    if (!interchangeColors.isEmpty() && !currentStation) {
                        final int lineHeight = lineSize * 2;
                        final int lineWidth = (int) Math.ceil((float) lineSize / interchangeColors.size());
                        for (int i = 0; i < interchangeColors.size(); i++) {
                            for (int drawX = 0; drawX < lineWidth; drawX++) {
                                for (int drawY = 0; drawY < lineHeight; drawY++) {
                                    drawPixelSafe(nativeImage, x + drawX + lineWidth * i - lineWidth * interchangeColors.size() / 2, y + (textBelow ? -1 : lines * lineSpacing) + (textBelow ? -drawY : drawY), passed ? ARGB_LIGHT_GRAY : ARGB_BLACK | interchangeColors.get(i));
                                }
                            }
                        }

                        final ClientCache.Text text = ClientCache.DATA_CACHE.getText(IGui.mergeStations(stationPositionGrouped.interchangeNames), maxStringWidth - (vertical ? lineHeight : 0), (int) ((fontSizeBig + fontSizeSmall) * mtr.client.ClientCache.LINE_HEIGHT_MULTIPLIER / 2), fontSizeBig / 2, fontSizeSmall / 2, 0, vertical ? HorizontalAlignment.LEFT : HorizontalAlignment.CENTER);
                        drawString(nativeImage, text, x, y + (textBelow ? -1 - lineHeight : lines * lineSpacing + lineHeight), HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.BOTTOM : VerticalAlignment.TOP, 0, passed ? ARGB_LIGHT_GRAY : ARGB_BLACK, vertical);
                    }

                    drawStation(nativeImage, x, y, heightScale, lines, passed);

                    final Station station = ClientCache.DATA_CACHE.stationIdMap.get(stationId);
                    final ClientCache.Text text = ClientCache.DATA_CACHE.getText(station == null ? "" : station.name, maxStringWidth, (int) ((fontSizeBig + fontSizeSmall) * mtr.client.ClientCache.LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, fontSizeSmall / 4, vertical ? HorizontalAlignment.RIGHT : HorizontalAlignment.CENTER);
                    // temporary
                    drawString(nativeImage, text, x, y + (textBelow ? lines * lineSpacing : -1) + (textBelow ? 1 : -1) * lineSize * 5 / 4, HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.TOP : VerticalAlignment.BOTTOM, currentStation ? ARGB_BLACK : 0, passed ? ARGB_LIGHT_GRAY : currentStation ? ARGB_WHITE : ARGB_BLACK, vertical);
                }));

                if (transparentWhite) {
                    clearColor(nativeImage, ARGB_WHITE);
                }

                return nativeImage;
            }
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage());
        }

        return null;
    }

    protected static void setup(List<Map<Integer, StationPosition>> stationPositions, List<List<Long>> stationsIdLists, int[] colorIndices, float[] bounds, boolean passed, boolean reverse) {
        final int passedMultiplier = passed ? -1 : 1;
        final int reverseMultiplier = reverse ? -1 : 1;
        bounds[0] = 0;

        final List<Long> commonStationIds = new ArrayList<>();
        stationsIdLists.get(0).forEach(stationId -> {
            if (stationId != 0 && !commonStationIds.contains(stationId) && stationsIdLists.stream().allMatch(stationsIds -> stationsIds.contains(stationId))) {
                commonStationIds.add(stationId);
            }
        });

        int positionXOffset = 0;
        final int routeCount = stationsIdLists.size();
        final int[] traverseIndex = new int[routeCount];
        for (int commonStationIndex = 0; commonStationIndex <= commonStationIds.size(); commonStationIndex++) {
            final boolean lastStation = commonStationIndex == commonStationIds.size();
            final long commonStationId = lastStation ? -1 : commonStationIds.get(commonStationIndex);

            int intermediateSegmentsMaxCount = 0;
            final int[] intermediateSegmentsCounts = new int[routeCount];
            for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                intermediateSegmentsCounts[routeIndex] = (lastStation ? stationsIdLists.get(routeIndex).size() : stationsIdLists.get(routeIndex).indexOf(commonStationId) + 1) - traverseIndex[routeIndex];
                intermediateSegmentsMaxCount = Math.max(intermediateSegmentsMaxCount, intermediateSegmentsCounts[routeIndex]);
            }

            final List<Integer> routesIndicesInSection = new ArrayList<>();
            for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                if (!lastStation || intermediateSegmentsCounts[routeIndex] > 0) {
                    routesIndicesInSection.add(routeIndex);
                }
            }

            for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                if (intermediateSegmentsCounts[routeIndex] > 0) {
                    final float increment = (float) intermediateSegmentsMaxCount / intermediateSegmentsCounts[routeIndex];
                    for (int j = 0; j < intermediateSegmentsCounts[routeIndex] - (lastStation ? 0 : 1); j++) {
                        final float stationX = positionXOffset + increment * (j + 1);
                        bounds[0] = Math.max(bounds[0], stationX / 2);
                        final float stationY = routesIndicesInSection.indexOf(routeIndex) - (routesIndicesInSection.size() - 1) / 2F + getLineOffset(routeIndex, colorIndices);
                        bounds[1] = Math.min(bounds[1], stationY);
                        bounds[2] = Math.max(bounds[2], stationY);
                        stationPositions.get(routeIndex).put(passedMultiplier * (j + traverseIndex[routeIndex] + 1), new StationPosition(reverseMultiplier * stationX / 2, stationY, false));
                    }
                    traverseIndex[routeIndex] += intermediateSegmentsCounts[routeIndex];
                }
            }

            if (!lastStation) {
                positionXOffset += intermediateSegmentsMaxCount;
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    final float stationY = getLineOffset(routeIndex, colorIndices);
                    bounds[1] = Math.min(bounds[1], stationY);
                    bounds[2] = Math.max(bounds[2], stationY);
                    stationPositions.get(routeIndex).put(passedMultiplier * traverseIndex[routeIndex], new StationPosition(reverseMultiplier * positionXOffset / 2F, stationY, true));
                }
                bounds[0] = positionXOffset / 2F;
            }
        }
    }

    protected static float getLineOffset(int routeIndex, int[] colorIndices) {
        return (float) lineSpacing / scale * (colorIndices[routeIndex] - colorIndices[colorIndices.length - 1] / 2F);
    }

    protected static long getStationId(long platformId) {
        final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platformId);
        return station == null ? -1 : station.id;
    }

    protected static String getStationName(long platformId) {
        final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platformId);
        return station == null ? "" : station.name;
    }

    protected static Map<Integer, mtr.client.ClientCache.ColorNameTuple> getInterchangeRoutes(long stationId) {
        return ClientData.DATA_CACHE.stationIdToRoutes.get(stationId);
    }

    protected static void drawLine(NativeImage nativeImage, StationPosition stationPosition1, StationPosition stationPosition2, float widthScale, float heightScale, float xOffset, float yOffset, int color) {
        final int x1 = Math.round((stationPosition1.x + xOffset) * scale * widthScale);
        final int x2 = Math.round((stationPosition2.x + xOffset) * scale * widthScale);
        final int y1 = Math.round((stationPosition1.y + yOffset) * scale * heightScale);
        final int y2 = Math.round((stationPosition2.y + yOffset) * scale * heightScale);
        final int xChange = x2 - x1;
        final int yChange = y2 - y1;
        final int xChangeAbs = Math.abs(xChange);
        final int yChangeAbs = Math.abs(yChange);
        final int changeDifference = Math.abs(yChangeAbs - xChangeAbs);

        if (xChangeAbs > yChangeAbs) {
            final boolean y1OffsetGreater = Math.abs(y1 - yOffset * scale) > Math.abs(y2 - yOffset * scale);
            drawLine(nativeImage, x1, y1, x2 - x1, y1OffsetGreater ? 0 : y2 - y1, y1OffsetGreater ? changeDifference : yChangeAbs, color);
            drawLine(nativeImage, x2, y2, x1 - x2, y1OffsetGreater ? y1 - y2 : 0, y1OffsetGreater ? yChangeAbs : changeDifference, color);
        } else {
            final int halfXChangeAbs = xChangeAbs / 2;
            drawLine(nativeImage, x1, y1, x2 - x1, y2 - y1, halfXChangeAbs, color);
            drawLine(nativeImage, x2, y2, x1 - x2, y1 - y2, halfXChangeAbs, color);
            drawLine(nativeImage, (x1 + x2) / 2, y1 + (int) Math.copySign(halfXChangeAbs, y2 - y1), 0, y2 - y1, changeDifference, color);
        }
    }

    protected static void drawLine(NativeImage nativeImage, int x, int y, int directionX, int directionY, int length, int color) {
        final int halfLineHeight = lineSize / 2;
        final int xWidth = directionX == 0 ? halfLineHeight : 0;
        final int yWidth = directionX == 0 ? 0 : directionY == 0 ? halfLineHeight : Math.round(lineSize * Mth.SQRT_OF_TWO / 2);
        final int yMin = y - halfLineHeight - (directionY < 0 ? length : 0) + 1;
        final int yMax = y + halfLineHeight + (directionY > 0 ? length : 0) - 1;
        final int drawOffset = directionX != 0 && directionY != 0 ? halfLineHeight : 0;

        for (int i = -drawOffset; i < Math.abs(length) + drawOffset; i++) {
            final int drawX = x + (directionX == 0 ? 0 : (int) Math.copySign(i, directionX)) + (directionX < 0 ? -1 : 0);
            final int drawY = y + (directionY == 0 ? 0 : (int) Math.copySign(i, directionY)) + (directionY < 0 ? -1 : 0);

            for (int xOffset = 0; xOffset < xWidth; xOffset++) {
                drawPixelSafe(nativeImage, drawX - xOffset - 1, drawY, color);
                drawPixelSafe(nativeImage, drawX + xOffset, drawY, color);
            }

            for (int yOffset = 0; yOffset < yWidth; yOffset++) {
                drawPixelSafe(nativeImage, drawX, Math.max(drawY - yOffset, yMin) - 1, color);
                drawPixelSafe(nativeImage, drawX, Math.min(drawY + yOffset, yMax), color);
            }
        }
    }

    public static List<Route> getRouteStream(long platformId, BiConsumer<Route, Integer> nonTerminatingCallback) {
        final Set<Route> routes = new HashSet<>();
        final Set<Route> terminating = new HashSet<>();
        ClientData.ROUTES.stream().filter(route -> route.containsPlatformId(platformId) && !route.isHidden).sorted((a, b) -> a.color == b.color ? a.compareTo(b) : a.color - b.color).forEach(route -> {
            final int currentStationIndex = route.getPlatformIdIndex(platformId);
            if (currentStationIndex < route.platformIds.size() - 1) {
                nonTerminatingCallback.accept(route, currentStationIndex);
                routes.add(route);
            } else {
                terminating.add(route);
            }
        });
        if (routes.isEmpty()) {
            routes.addAll(terminating);
        }
        return routes.stream().toList();
    }

    protected static void drawResource(NativeImage nativeImage, String resource, int x, int y, int width, int height, boolean flipX, float v1, float v2, int color, boolean useActualColor) throws IOException {
        final NativeImage nativeImageResource = NativeImage.read(NativeImage.Format.RGBA, Utilities.getInputStream(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(MTR.MOD_ID, resource))));
        final int resourceWidth = nativeImageResource.getWidth();
        final int resourceHeight = nativeImageResource.getHeight();
        for (int drawX = 0; drawX < width; drawX++) {
            for (int drawY = Math.round(v1 * height); drawY < Math.round(v2 * height); drawY++) {
                final float pixelX = (float) drawX / width * resourceWidth;
                final float pixelY = (float) drawY / height * resourceHeight;
                final int floorX = (int) pixelX;
                final int floorY = (int) pixelY;
                final int ceilX = floorX + 1;
                final int ceilY = floorY + 1;
                final float percentX1 = ceilX - pixelX;
                final float percentY1 = ceilY - pixelY;
                final float percentX2 = pixelX - floorX;
                final float percentY2 = pixelY - floorY;
                final int pixel1 = nativeImageResource.getPixelRGBA(Mth.clamp(floorX, 0, resourceWidth - 1), Mth.clamp(floorY, 0, resourceHeight - 1));
                final int pixel2 = nativeImageResource.getPixelRGBA(Mth.clamp(ceilX, 0, resourceWidth - 1), Mth.clamp(floorY, 0, resourceHeight - 1));
                final int pixel3 = nativeImageResource.getPixelRGBA(Mth.clamp(floorX, 0, resourceWidth - 1), Mth.clamp(ceilY, 0, resourceHeight - 1));
                final int pixel4 = nativeImageResource.getPixelRGBA(Mth.clamp(ceilX, 0, resourceWidth - 1), Mth.clamp(ceilY, 0, resourceHeight - 1));
                final int newColor;
                if (useActualColor) {
                    newColor = invertColor(pixel1);
                } else {
                    final float luminance1 = ((pixel1 >> 24) & 0xFF) * percentX1 * percentY1;
                    final float luminance2 = ((pixel2 >> 24) & 0xFF) * percentX2 * percentY1;
                    final float luminance3 = ((pixel3 >> 24) & 0xFF) * percentX1 * percentY2;
                    final float luminance4 = ((pixel4 >> 24) & 0xFF) * percentX2 * percentY2;
                    newColor = (color & RGB_WHITE) + ((int) (luminance1 + luminance2 + luminance3 + luminance4) << 24);
                }
                blendPixel(nativeImage, (flipX ? width - drawX - 1 : drawX) + x, drawY + y, newColor);
            }
        }
    }

    protected static void drawStation(NativeImage nativeImage, int x, int y, float heightScale, int lines, boolean passed) {
        for (int offsetX = -lineSize; offsetX < lineSize; offsetX++) {
            for (int offsetY = -lineSize; offsetY < lineSize; offsetY++) {
                final int extraOffsetY = offsetY > 0 ? (int) (lines * lineSpacing * heightScale) : 0;
                final int repeatDraw = offsetY == 0 ? (int) (lines * lineSpacing * heightScale) : 0;
                final double squareSum = (offsetX + 0.5) * (offsetX + 0.5) + (offsetY + 0.5) * (offsetY + 0.5);

                if (squareSum <= 0.5 * lineSize * lineSize) {
                    for (int i = 0; i <= repeatDraw; i++) {
                        drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, ARGB_WHITE);
                    }
                } else if (squareSum <= lineSize * lineSize) {
                    for (int i = 0; i <= repeatDraw; i++) {
                        drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, passed ? ARGB_LIGHT_GRAY : ARGB_BLACK);
                    }
                }
            }
        }
    }

    protected static void drawString(NativeImage nativeImage, ClientCache.Text text, int x, int y, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, int backgroundColor, int textColor, boolean rotate90) {
        if (((backgroundColor >> 24) & 0xFF) > 0) {
            for (int drawX = 0; drawX < (rotate90 ? text.height() : text.renderWidth()); drawX++) {
                for (int drawY = 0; drawY < (rotate90 ? text.renderWidth() : text.height()); drawY++) {
                    drawPixelSafe(nativeImage, (int) horizontalAlignment.getOffset(drawX + x, (rotate90 ? text.height() : text.renderWidth())), (int) verticalAlignment.getOffset(drawY + y, (rotate90 ? text.renderWidth() : text.height())), backgroundColor);
                }
            }
        }
        int drawX = 0;
        int drawY = rotate90 ? text.renderWidth() - 1 : 0;
        for (int i = 0; i < text.renderWidth() * text.height(); i++) {
            blendPixel(nativeImage, (int) horizontalAlignment.getOffset(x + drawX, (rotate90 ? text.height() : text.renderWidth())), (int) verticalAlignment.getOffset(y + drawY, (rotate90 ? text.renderWidth() : text.height())), ((text.pixels()[i] & 0xFF) << 24) + (textColor & RGB_WHITE));
            if (rotate90) {
                drawY--;
                if (drawY < 0) {
                    drawY = text.renderWidth() - 1;
                    drawX++;
                }
            } else {
                drawX++;
                if (drawX == text.renderWidth()) {
                    drawX = 0;
                    drawY++;
                }
            }
        }
    }

    protected static void blendPixel(NativeImage nativeImage, int x, int y, int color) {
        if (RailwayData.isBetween(x, 0, nativeImage.getWidth() - 1) && RailwayData.isBetween(y, 0, nativeImage.getHeight() - 1)) {
            final float percent = (float) ((color >> 24) & 0xFF) / 0xFF;
            if (percent > 0) {
                final int existingPixel = nativeImage.getPixelRGBA(x, y);
                final boolean existingTransparent = ((existingPixel >> 24) & 0xFF) == 0;
                final int r1 = existingTransparent ? 0xFF : (existingPixel & 0xFF);
                final int g1 = existingTransparent ? 0xFF : ((existingPixel >> 8) & 0xFF);
                final int b1 = existingTransparent ? 0xFF : ((existingPixel >> 16) & 0xFF);
                final int r2 = (color >> 16) & 0xFF;
                final int g2 = (color >> 8) & 0xFF;
                final int b2 = color & 0xFF;
                final float inversePercent = 1 - percent;
                final int finalColor = ARGB_BLACK | (((int) (r1 * inversePercent + r2 * percent) << 16) + ((int) (g1 * inversePercent + g2 * percent) << 8) + (int) (b1 * inversePercent + b2 * percent));
                drawPixelSafe(nativeImage, x, y, finalColor);
            }
        }
    }

    protected static void drawPixelSafe(NativeImage nativeImage, int x, int y, int color) {
        if (RailwayData.isBetween(x, 0, nativeImage.getWidth() - 1) && RailwayData.isBetween(y, 0, nativeImage.getHeight() - 1)) {
            nativeImage.setPixelRGBA(x, y, invertColor(color));
        }
    }

    protected static int invertColor(int color) {
        return ((color & ARGB_BLACK) != 0 ? ARGB_BLACK : 0) + ((color & 0xFF) << 16) + (color & 0xFF00) + ((color & 0xFF0000) >> 16);
    }

    protected static void clearColor(NativeImage nativeImage, int color) {
        for (int x = 0; x < nativeImage.getWidth(); x++) {
            for (int y = 0; y < nativeImage.getHeight(); y++) {
                if (nativeImage.getPixelRGBA(x, y) == color) {
                    nativeImage.setPixelRGBA(x, y, 0);
                }
            }
        }
    }

    protected record StationPosition(float x, float y, boolean isCommon)
    {
    }

    protected record StationPositionGrouped(StationPosition stationPosition, int stationOffset,
                                            List<Integer> interchangeColors, List<String> interchangeNames)
    {
    }
}
