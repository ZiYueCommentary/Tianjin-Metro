package ziyue.tjmetro.mod.client;

import org.mtr.core.data.*;
import org.mtr.libraries.it.unimi.dsi.fastutil.Pair;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.NativeImage;
import org.mtr.mapping.holder.NativeImageFormat;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockRailwaySign;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.IGui;
import org.mtr.mod.screen.DashboardListItem;
import org.mtr.mod.screen.EditStationScreen;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.block.BlockStationNameEntranceTianjin;
import ziyue.tjmetro.mod.config.ConfigClient;
import ziyue.tjmetro.mod.data.IGuiExtension;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static org.mtr.mod.client.DynamicTextureCache.LINE_HEIGHT_MULTIPLIER;

/**
 * @author ZiYueCommentary
 * @see org.mtr.mod.client.RouteMapGenerator
 * @since 1.0.0-beta-1
 */

public class RouteMapGenerator implements IGui
{
    protected static int scale;
    protected static int lineSize;
    protected static int lineSpacing;
    protected static int fontSizeBig;
    protected static int fontSizeSmall;

    public static final int MIN_VERTICAL_SIZE = 5;
    public static final Identifier SUBWAY_LOGO_RESOURCE = new Identifier(Reference.MOD_ID, "textures/sign/to_subway.png");
    public static final Identifier TRAIN_LOGO_RESOURCE = new Identifier(Reference.MOD_ID, "textures/sign/train.png");
    public static final Identifier TRAIN_BORDERLESS_LOGO_RESOURCE = new Identifier(Reference.MOD_ID, "textures/sign/train_borderless.png");
    public static final Identifier TRAIN_BMT_LOGO_RESOURCE = new Identifier(Reference.MOD_ID, "textures/sign/to_subway_bmt.png");
    public static final Identifier TRAIN_JINJING_LOGO_RESOURCE = new Identifier(Reference.MOD_ID, "textures/sign/to_subway_jinjing.png");
    public static final Identifier EXIT_RESOURCE = new Identifier(Init.MOD_ID, "textures/block/sign/exit_letter_blank.png");
    public static final Identifier ARROW_RESOURCE = new Identifier(Init.MOD_ID, "textures/block/sign/arrow.png");
    public static final Identifier CIRCLE_RESOURCE = new Identifier(Init.MOD_ID, "textures/block/sign/circle.png");
    public static final String TEMP_CIRCULAR_MARKER_CLOCKWISE = String.format("temp_circular_marker_%s_clockwise", Integer.toHexString((new Random()).nextInt()));
    public static final String TEMP_CIRCULAR_MARKER_ANTICLOCKWISE = String.format("temp_circular_marker_%s_anticlockwise", Integer.toHexString((new Random()).nextInt()));

    public static void setConstants() {
        scale = (int) Math.pow(2, Config.getClient().getDynamicTextureResolution() + 5);
        lineSize = scale / 8;
        lineSpacing = lineSize * 3 / 2;
        fontSizeBig = lineSize * 2;
        fontSizeSmall = fontSizeBig / 2;
    }

    public static NativeImage generateDirectionArrow(long platformId, boolean hasLeft, boolean hasRight, HorizontalAlignment horizontalAlignment, boolean showToString, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        if (aspectRatio <= 0.0F) return null;
        try {
            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            IntArrayList colors = getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                    case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                    case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                    default -> "";
                };

                if (!simplifiedRoute.getName().isEmpty()) {
                    destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
                }
            });
            final boolean isTerminating = destinations.isEmpty();
            final boolean leftToRight = horizontalAlignment == HorizontalAlignment.CENTER ? hasLeft || !hasRight : horizontalAlignment != HorizontalAlignment.RIGHT;
            final int height = scale;
            final int width = Math.round((float) height * aspectRatio);
            final int padding = Math.round((float) height * paddingScale);
            final int tileSize = height - padding * 2;

            if (width <= 0 || height <= 0) return null;

            DynamicTextureCache clientCache = DynamicTextureCache.instance;
            NativeImage nativeImage = new NativeImage(NativeImageFormat.getAbgrMapped(), width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(backgroundColor));
            final int circleX;
            if (isTerminating) {
                circleX = (int) horizontalAlignment.getOffset(0.0F, (float) (tileSize - width));
            } else {
                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else if (showToString) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.to_cjk", "gui.mtr.to", 1, destinationString);
                    }
                }

                final int tilePadding = tileSize / 4;
                final int leftSize = ((hasLeft ? 1 : 0) + (leftToRight ? 1 : 0)) * (tileSize + tilePadding);
                final int rightSize = ((hasRight ? 1 : 0) + (leftToRight ? 0 : 1)) * (tileSize + tilePadding);
                final DynamicTextureCache.Text text = clientCache.getText(destinationString, width - leftSize - rightSize - padding * (showToString ? 2 : 1), (int) ((float) tileSize * 1.25F), tileSize * 3 / 5, tileSize * 3 / 10, tilePadding, leftToRight ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
                final int leftPadding = (int) horizontalAlignment.getOffset(0.0F, (float) (leftSize + rightSize + text.renderWidth() - tilePadding * 2 - width));
                DrawingHelper.drawString(nativeImage, text, leftPadding + leftSize - tilePadding, height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, backgroundColor, textColor, false);
                if (hasLeft) {
                    DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, leftPadding, padding, tileSize, tileSize, false, 0.0F, 1.0F, textColor, false);
                }

                if (hasRight) {
                    DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, leftPadding + leftSize + text.renderWidth() - tilePadding * 2 + rightSize - tileSize, padding, tileSize, tileSize, true, 0.0F, 1.0F, textColor, false);
                }

                circleX = leftPadding + leftSize + (leftToRight ? -tileSize - tilePadding : text.renderWidth() - tilePadding);
            }

            for (int i = 0; i < colors.size(); ++i) {
                DrawingHelper.drawResource(nativeImage, CIRCLE_RESOURCE, circleX, padding, tileSize, tileSize, false, (float) i / (float) colors.size(), ((float) i + 1.0F) / (float) colors.size(), colors.getInt(i), false);
            }

            Platform platform = MinecraftClientData.getInstance().platformIdMap.get(platformId);
            if (platform != null) {
                final DynamicTextureCache.Text text = clientCache.getText(platform.getName(), tileSize, (int) ((float) tileSize * 1.25F * 3.0F / 4.0F), tileSize * 3 / 4, tileSize * 3 / 4, 0, HorizontalAlignment.CENTER);
                DrawingHelper.drawString(nativeImage, text, circleX + tileSize / 2, padding + tileSize / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, -1, false);
            }

            if (transparentColor != 0)
                DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(transparentColor));

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    public static NativeImage generateStationName(long platformId, boolean isAPG, HorizontalAlignment horizontalAlignment, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        if (aspectRatio <= 0) return null;

        try {
            final boolean leftToRight = horizontalAlignment != HorizontalAlignment.RIGHT;
            final int height = scale;
            final int width = Math.round(height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;

            if (width <= 0 || height <= 0) return null;

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(backgroundColor));

            final int tilePadding = tileSize / 4;
            final int leftSize = ((leftToRight ? 1 : 0)) * (tileSize + tilePadding);
            final int rightSize = ((leftToRight ? 0 : 1)) * (tileSize + tilePadding);

            final DynamicTextureCache.Text stationName = DynamicTextureCache.instance.getText(getStationName(platformId), isAPG ? width - padding : width - leftSize - rightSize - padding, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize * 3, tileSize * 3 / 2, tilePadding, HorizontalAlignment.CENTER);
            DrawingHelper.drawString(nativeImage, stationName, width / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, backgroundColor, textColor, false);

            if (transparentColor != 0) {
                DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(transparentColor));
            }

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateSingleRowStationName(long platformId, float aspectRatio) {
        if (aspectRatio <= 0) return null;

        try {
            final DynamicTextureCache.Text text = DynamicTextureCache.instance.getText(getStationName(platformId).replace("|", " | "), fontSizeBig, fontSizeSmall);
            final int padding = text.height() / 2;
            final int height = text.height() + padding;
            final int width = Math.max(Math.round(height * aspectRatio), text.renderWidth() + padding);

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.getAbgrMapped(), width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, ARGB_WHITE);
            DrawingHelper.drawString(nativeImage, text, width / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
            return nativeImage;
        } catch (Exception e) {
            Init.LOGGER.error("", e);
        }

        return null;
    }

    public static NativeImage generateStationNameBMT(long platformId, boolean flip, HorizontalAlignment horizontalAlignment, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        if (aspectRatio <= 0) return null;

        try {
            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            ObjectArrayList<String> nextStations = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                    case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                    case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                    default -> "";
                };

                if (!simplifiedRoute.getName().isEmpty()) {
                    destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
                    nextStations.add(simplifiedRoute.getPlatforms().get(currentStationIndex + 1).getStationName());
                }
            });
            final boolean isTerminating = destinations.isEmpty();

            final boolean leftToRight = horizontalAlignment != HorizontalAlignment.RIGHT;
            final int height = scale;
            final int width = Math.round(height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;

            if (width <= 0 || height <= 0) return null;

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(backgroundColor));

            final int tilePadding = tileSize / 8;
            final int leftSize = ((leftToRight ? 1 : 0)) * (tileSize + tilePadding);
            final int rightSize = ((leftToRight ? 0 : 1)) * (tileSize + tilePadding);

            final DynamicTextureCache.Text textStationName = DynamicTextureCache.instance.getText(getStationName(platformId), width - leftSize - rightSize - padding, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize / 2, tileSize / 4, tilePadding, HorizontalAlignment.CENTER);
            DrawingHelper.drawString(nativeImage, textStationName, width / 2, (int) (tileSize / 3.8F), HorizontalAlignment.CENTER, VerticalAlignment.TOP, backgroundColor, textColor, false);

            if (isTerminating) {
                final DynamicTextureCache.Text terminus = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus_bmt"), width / 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig / 2, fontSizeSmall / 2, tilePadding, HorizontalAlignment.CENTER);
                DrawingHelper.drawString(nativeImage, terminus, tileSize, height, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, backgroundColor, textColor, false);
                DrawingHelper.drawString(nativeImage, terminus, width - tileSize, height, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, backgroundColor, textColor, false);
            } else {
                String nextStationString = IGui.mergeStations(nextStations);
                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                nextStationString = IGuiExtension.insertTranslation("gui.tjmetro.next_station_bmt_cjk", "gui.tjmetro.next_station_bmt", 1, nextStationString);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else {
                        destinationString = IGuiExtension.insertTranslation("gui.tjmetro.bound_for_bmt_cjk", "gui.tjmetro.bound_for", 1, destinationString);
                    }
                }
                final DynamicTextureCache.Text textBoundFor = DynamicTextureCache.instance.getText(destinationString, width / 2 - tilePadding * 4, height, fontSizeBig / 2, fontSizeSmall / 2, tilePadding, HorizontalAlignment.CENTER);
                final DynamicTextureCache.Text textNextStation = DynamicTextureCache.instance.getText(nextStationString, width / 2 - tilePadding * 4, height, fontSizeBig / 2, fontSizeSmall / 2, tilePadding, HorizontalAlignment.CENTER);
                DrawingHelper.drawString(nativeImage, flip ? textBoundFor : textNextStation, tilePadding, height, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, textColor, false);
                DrawingHelper.drawString(nativeImage, flip ? textNextStation : textBoundFor, width - tilePadding, height, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, 0, textColor, false);
            }

            if (transparentColor != 0) {
                DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(transparentColor));
            }

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateNextStation(long platformId, int arrowDirection, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        if (aspectRatio <= 0) return null;

        try {
            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            ObjectArrayList<String> nextStations = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                    case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                    case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                    default -> "";
                };

                if (!simplifiedRoute.getName().isEmpty()) {
                    destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
                    nextStations.add(simplifiedRoute.getPlatforms().get(currentStationIndex + 1).getStationName());
                }
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
                nativeImage = new NativeImage(NativeImageFormat.RGBA, width, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(backgroundColor));
                final DynamicTextureCache.Text destination = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus"), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize * 3, tileSize * 3 / 2, tilePadding, HorizontalAlignment.CENTER);
                DrawingHelper.drawString(nativeImage, destination, width / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, backgroundColor, textColor, false);
            } else {
                final int arrowSizeAndPadding = switch (arrowDirection) {
                    case 0, 3 -> -tilePadding;
                    default -> tileSize;
                };
                String nextStationString = IGui.mergeStations(nextStations);
                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else {
                        destinationString = IGuiExtension.insertTranslation("gui.tjmetro.bound_for_bmt_cjk", "gui.tjmetro.bound_for", 1, destinationString);
                    }
                }

                final DynamicTextureCache.Text textDestination = DynamicTextureCache.instance.getText(destinationString, width - leftSize - padding * 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize, tileSize / 2, tilePadding, (arrowDirection == 2) ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
                final DynamicTextureCache.Text textNextStations = DynamicTextureCache.instance.getText(nextStationString, width - leftSize - padding * 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, tilePadding, (arrowDirection == 2) ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
                final DynamicTextureCache.Text textNextStationSign = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.next_station_cjk", "gui.tjmetro.next_station"), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, tilePadding, HorizontalAlignment.CENTER);
                int renderWidth = Math.max(width, leftSize + arrowSizeAndPadding + textDestination.width() + textNextStationSign.width() + tilePadding + textNextStations.width() + leftSize);
                final boolean renderNextStation = (width - leftSize - arrowSizeAndPadding - textDestination.width() - leftSize) > textNextStations.width();
                if (!renderNextStation)
                    renderWidth = Math.max(width, renderWidth - textNextStations.width() - tilePadding - textNextStationSign.width());
                nativeImage = new NativeImage(NativeImageFormat.RGBA, renderWidth, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, renderWidth, height, DrawingHelper.invertColor(backgroundColor));
                if (arrowDirection == 2) {
                    DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, renderWidth - leftSize - tileSize, padding, tileSize, tileSize, true, 0, 1, textColor, false);
                    DrawingHelper.drawString(nativeImage, textDestination, renderWidth - leftSize - tileSize, height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, backgroundColor, textColor, false);
                    if (renderNextStation) {
                        DrawingHelper.drawString(nativeImage, textNextStationSign, leftSize - tilePadding, height, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, textColor, false);
                        DrawingHelper.drawString(nativeImage, textNextStations, leftSize - tilePadding + textNextStationSign.width(), height, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, textColor, false);
                    }
                } else {
                    if (arrowSizeAndPadding > 0)
                        DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, leftSize, padding, tileSize, tileSize, false, 0, 1, textColor, false);
                    DrawingHelper.drawString(nativeImage, textDestination, leftSize + arrowSizeAndPadding, height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, backgroundColor, textColor, false);
                    if (renderNextStation) {
                        DrawingHelper.drawString(nativeImage, textNextStations, renderWidth - leftSize + tilePadding, height, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, 0, textColor, false);
                        DrawingHelper.drawString(nativeImage, textNextStationSign, renderWidth - leftSize - textNextStations.width() + tilePadding, height, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, 0, textColor, false);
                    }
                }
            }

            if (transparentColor != 0) {
                DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(transparentColor));
            }

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateNextStationJinjing(long platformId, int arrowDirection, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        if (aspectRatio <= 0) return null;

        try {
            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            ObjectArrayList<String> nextStations = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                    case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                    case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                    default -> "";
                };

                if (!simplifiedRoute.getName().isEmpty()) {
                    destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
                    nextStations.add(simplifiedRoute.getPlatforms().get(currentStationIndex + 1).getStationName());
                }
            });
            final boolean isTerminating = destinations.isEmpty();

            final int height = scale;
            final int width = Math.round(height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;

            if (width <= 0 || height <= 0) return null;

            final int tilePadding = tileSize / 4;
            final int leftSize = tileSize + tilePadding;
            final NativeImage nativeImage;
            Platform platform = MinecraftClientData.getInstance().platformIdMap.get(platformId);
            final DynamicTextureCache.Text platformName = DynamicTextureCache.instance.getText(platform.getName(), width, tileSize, tileSize * 2, tileSize * 2, tilePadding, arrowDirection == 2 ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
            final DynamicTextureCache.Text platformSign = DynamicTextureCache.instance.getText(TextHelper.translatable("gui.tjmetro.platform").getString(), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize * 3, tileSize * 3 / 2, tilePadding, arrowDirection == 2 ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
            final int nonNextStationWidth = leftSize + platformName.width() + platformSign.width() + leftSize * 2;

            if (isTerminating) {
                final DynamicTextureCache.Text destination = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus"), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize * 3, tileSize * 3 / 2, tilePadding, HorizontalAlignment.CENTER);
                final int imageWidth = Math.max(nonNextStationWidth + destination.width() + leftSize, width);
                nativeImage = new NativeImage(NativeImageFormat.RGBA, imageWidth, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, imageWidth, height, DrawingHelper.invertColor(backgroundColor));
                if (arrowDirection == 2) {
                    DrawingHelper.drawString(nativeImage, platformName, imageWidth - leftSize - platformName.width(), height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.drawString(nativeImage, platformSign, imageWidth - leftSize - platformName.width(), height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.fillRectSafe(nativeImage, imageWidth - leftSize - platformName.width() - platformSign.width() - leftSize, 0, padding / 6, height, ARGB_BLACK);
                    DrawingHelper.drawString(nativeImage, destination, imageWidth - nonNextStationWidth - (imageWidth - nonNextStationWidth - leftSize) / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, textColor, false);
                } else {
                    DrawingHelper.drawString(nativeImage, platformName, leftSize + platformName.width(), height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.drawString(nativeImage, platformSign, leftSize + platformName.width(), height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.fillRectSafe(nativeImage, leftSize + platformName.width() + platformSign.width() + leftSize, 0, padding / 6, height, ARGB_BLACK);
                    DrawingHelper.drawString(nativeImage, destination, nonNextStationWidth + (imageWidth - nonNextStationWidth - leftSize) / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, textColor, false);
                }
            } else {
                final int arrowSizeAndPadding = switch (arrowDirection) {
                    case 0, 3 -> -tilePadding;
                    default -> tileSize;
                };
                String nextStationString = IGui.mergeStations(nextStations);
                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else {
                        destinationString = IGuiExtension.insertTranslation("gui.tjmetro.bound_for_bmt_cjk", "gui.tjmetro.bound_for", 1, destinationString);
                    }
                }

                final DynamicTextureCache.Text textDestination = DynamicTextureCache.instance.getText(destinationString, width - leftSize - padding * 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize, tileSize / 2, tilePadding, (arrowDirection == 2) ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
                final DynamicTextureCache.Text textNextStations = DynamicTextureCache.instance.getText(nextStationString, width - leftSize - padding * 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, tilePadding, (arrowDirection == 2) ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
                final DynamicTextureCache.Text textNextStationSign = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.next_station_cjk", "gui.tjmetro.next_station"), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, tilePadding, HorizontalAlignment.CENTER);
                int renderWidth = Math.max(width, nonNextStationWidth + arrowSizeAndPadding + textDestination.width() + textNextStationSign.width() + tilePadding + textNextStations.width() + leftSize);
                final boolean renderNextStation = (width - leftSize - arrowSizeAndPadding - textDestination.width() - leftSize) > textNextStations.width();
                if (!renderNextStation)
                    renderWidth = Math.max(width, renderWidth - textNextStations.width() - tilePadding - textNextStationSign.width());
                nativeImage = new NativeImage(NativeImageFormat.RGBA, renderWidth, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, renderWidth, height, DrawingHelper.invertColor(backgroundColor));
                if (arrowDirection == 2) {
                    DrawingHelper.drawString(nativeImage, platformName, renderWidth - leftSize - platformName.width(), height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.drawString(nativeImage, platformSign, renderWidth - leftSize - platformName.width(), height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.fillRectSafe(nativeImage, renderWidth - leftSize - platformName.width() - platformSign.width() - leftSize, 0, padding / 6, height, ARGB_BLACK);
                    DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, renderWidth - nonNextStationWidth - tileSize, padding, tileSize, tileSize, true, 0, 1, textColor, false);
                    DrawingHelper.drawString(nativeImage, textDestination, renderWidth - nonNextStationWidth - arrowSizeAndPadding, height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, backgroundColor, textColor, false);
                    if (renderNextStation) {
                        DrawingHelper.drawString(nativeImage, textNextStationSign, leftSize - tilePadding, height, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, textColor, false);
                        DrawingHelper.drawString(nativeImage, textNextStations, leftSize - tilePadding + textNextStationSign.width(), height, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, textColor, false);
                    }
                } else {
                    if (arrowSizeAndPadding > 0)
                        DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, nonNextStationWidth, padding, tileSize, tileSize, false, 0, 1, textColor, false);
                    DrawingHelper.drawString(nativeImage, platformName, leftSize + platformName.width(), height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.drawString(nativeImage, platformSign, leftSize + platformName.width(), height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.fillRectSafe(nativeImage, leftSize + platformName.width() + platformSign.width() + leftSize, 0, padding / 6, height, ARGB_BLACK);
                    DrawingHelper.drawString(nativeImage, textDestination, nonNextStationWidth + arrowSizeAndPadding, height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, textColor, false);
                    if (renderNextStation) {
                        DrawingHelper.drawString(nativeImage, textNextStations, renderWidth - leftSize + tilePadding, height, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, 0, textColor, false);
                        DrawingHelper.drawString(nativeImage, textNextStationSign, renderWidth - leftSize - textNextStations.width() + tilePadding, height, HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM, 0, textColor, false);
                    }
                }
            }

            if (transparentColor != 0) {
                DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(transparentColor));
            }

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateRouteMap(long platformId, boolean vertical, boolean flip, float aspectRatio, boolean transparentWhite) {
        if (aspectRatio <= 0) return null;

        try {
            final ObjectArrayList<ObjectIntImmutablePair<SimplifiedRoute>> routeDetails = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                if (!simplifiedRoute.getName().isEmpty()) {
                    routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));
                }
            });
            if (routeDetails.isEmpty()) {
                MinecraftClientData.getInstance().simplifiedRoutes.stream().filter(simplifiedRoute -> simplifiedRoute.getPlatformIndex(platformId) >= 0).sorted().forEach(simplifiedRoute -> {
                    final int currentStationIndex = simplifiedRoute.getPlatformIndex(platformId);
                    if (!simplifiedRoute.getName().isEmpty()) {
                        routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));
                    }
                });
            }
            final int routeCount = routeDetails.size();

            if (routeCount > 0) {
                final DynamicTextureCache clientCache = DynamicTextureCache.instance;
                final ObjectArrayList<LongArrayList> stationsIdsBefore = new ObjectArrayList<>();
                final ObjectArrayList<LongArrayList> stationsIdsAfter = new ObjectArrayList<>();
                final ObjectArrayList<Int2ObjectAVLTreeMap<StationPosition>> stationPositions = new ObjectArrayList<>();
                final IntAVLTreeSet colors = new IntAVLTreeSet();
                final int[] colorIndices = new int[routeCount];
                int colorIndex = -1;
                int previousColor = -1;
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationsIdsBefore.add(new LongArrayList());
                    stationsIdsAfter.add(new LongArrayList());
                    stationPositions.add(new Int2ObjectAVLTreeMap<>());

                    final ObjectIntImmutablePair<SimplifiedRoute> routeDetail = routeDetails.get(routeIndex);
                    final ObjectArrayList<SimplifiedRoutePlatform> simplifiedRoutePlatforms = routeDetail.left().getPlatforms();
                    final int currentIndex = routeDetail.rightInt();
                    for (int stationIndex = 0; stationIndex < simplifiedRoutePlatforms.size(); stationIndex++) {
                        if (stationIndex != currentIndex) {
                            final long stationId = simplifiedRoutePlatforms.get(stationIndex).getStationId();
                            if (stationIndex < currentIndex) {
                                stationsIdsBefore.get(stationsIdsBefore.size() - 1).add(0, stationId);
                            } else {
                                stationsIdsAfter.get(stationsIdsAfter.size() - 1).add(stationId);
                            }
                        }
                    }

                    final int color = routeDetail.left().getColor();
                    colors.add(color);
                    if (color != previousColor) {
                        colorIndex++;
                        previousColor = color;
                    }
                    colorIndices[routeIndex] = colorIndex;
                }

                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationPositions.get(routeIndex).put(0, new StationPosition(0, getLineOffset(routeIndex, colorIndices), true));
                }

                final boolean rotateStationName = ConfigClient.ROTATED_STATION_NAME.get();
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
                    yOffset = rawHeightPart + (rotateStationName ? 0.4F : 0);
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

                if (width <= 0 || height <= 0) return null;

                final NativeImage nativeImage = new NativeImage(NativeImageFormat.getAbgrMapped(), width, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, ARGB_WHITE);

                final Object2ObjectOpenHashMap<String, ObjectOpenHashSet<StationPositionGrouped>> stationPositionsGrouped = new Object2ObjectOpenHashMap<>();
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    final SimplifiedRoute simplifiedRoute = routeDetails.get(routeIndex).left();
                    final int currentIndex = routeDetails.get(routeIndex).rightInt();
                    final Int2ObjectAVLTreeMap<StationPosition> routeStationPositions = stationPositions.get(routeIndex);

                    for (int stationIndex = 0; stationIndex < simplifiedRoute.getPlatforms().size(); stationIndex++) {
                        final StationPosition stationPosition = routeStationPositions.get(stationIndex - currentIndex);
                        if (stationIndex < simplifiedRoute.getPlatforms().size() - 1) {
                            DrawingHelper.drawLine(nativeImage, stationPosition, routeStationPositions.get(stationIndex + 1 - currentIndex), widthScale, heightScale, xOffset, yOffset, stationIndex < currentIndex ? ARGB_LIGHT_GRAY : ARGB_BLACK | simplifiedRoute.getColor());
                        }

                        final SimplifiedRoutePlatform simplifiedRoutePlatform = simplifiedRoute.getPlatforms().get(stationIndex);
                        final String key = String.format("%s||%s", simplifiedRoutePlatform.getStationName(), simplifiedRoutePlatform.getStationId());

                        if (!stationPosition.isCommon || stationPositionsGrouped.getOrDefault(key, new ObjectOpenHashSet<>()).stream().noneMatch(stationPosition2 -> stationPosition2.stationPosition.x == stationPosition.x)) {
                            final IntArrayList interchangeColors = new IntArrayList();
                            final ObjectArrayList<String> interchangeNames = new ObjectArrayList<>();
                            simplifiedRoutePlatform.forEach((color, interchangeRouteNamesForColor) -> {
                                if (!colors.contains(color)) {
                                    interchangeColors.add(color);
                                    interchangeRouteNamesForColor.forEach(interchangeNames::add);
                                }
                            });
                            Data.put(stationPositionsGrouped, key, new StationPositionGrouped(stationPosition, stationIndex - currentIndex, interchangeColors, interchangeNames), ObjectOpenHashSet::new);
                        }
                    }
                }

                final int maxStringWidth = (int) (scale * 0.9 * ((vertical ? heightScale : widthScale) / 2 + extraPadding / routeCount));
                stationPositionsGrouped.forEach((key, stationPositionGroupedSet) -> stationPositionGroupedSet.forEach(stationPositionGrouped -> {
                    final int x = Math.round((stationPositionGrouped.stationPosition.x + xOffset) * scale * widthScale);
                    final int y = Math.round((stationPositionGrouped.stationPosition.y + yOffset) * scale * heightScale);
                    final int lines = stationPositionGrouped.stationPosition.isCommon ? colorIndices[colorIndices.length - 1] : 0;
                    final boolean currentStation = stationPositionGrouped.stationOffset == 0;
                    final boolean passed = stationPositionGrouped.stationOffset < 0;
                    final boolean textBelow = vertical || (stationPositionGrouped.stationPosition.isCommon ? Math.abs(stationPositionGrouped.stationOffset) % 2 == 0 : y >= yOffset * scale);

                    final IntArrayList interchangeColors = stationPositionGrouped.interchangeColors;
                    if (!interchangeColors.isEmpty()) {
                        final int lineHeight = lineSize * 2;
                        final int lineWidth = (int) Math.ceil((float) lineSize / interchangeColors.size());
                        for (int i = 0; i < interchangeColors.size(); i++) {
                            for (int drawX = 0; drawX < lineWidth; drawX++) {
                                for (int drawY = 0; drawY < lineHeight; drawY++) {
                                    if (rotateStationName) {
                                        DrawingHelper.drawPixelSafe(nativeImage, x + drawX + lineWidth * i - lineWidth * interchangeColors.size() / 2, y + lines * lineSpacing + drawY, ARGB_BLACK | interchangeColors.getInt(i));
                                    } else {
                                        DrawingHelper.drawPixelSafe(nativeImage, x + drawX + lineWidth * i - lineWidth * interchangeColors.size() / 2, y + (textBelow ? -1 : lines * lineSpacing) + (textBelow ? -drawY : drawY), passed ? ARGB_LIGHT_GRAY : ARGB_BLACK | interchangeColors.getInt(i));
                                    }
                                }
                            }
                        }

                        final DynamicTextureCache.Text text = clientCache.getText(IGui.mergeStations(stationPositionGrouped.interchangeNames), maxStringWidth - (vertical ? lineHeight : 0), (int) ((fontSizeBig + fontSizeSmall) * LINE_HEIGHT_MULTIPLIER / 2), fontSizeBig / 2, fontSizeSmall / 2, 0, vertical ? HorizontalAlignment.LEFT : HorizontalAlignment.CENTER);
                        if (rotateStationName) {
                            DrawingHelper.drawString(nativeImage, text, x, y + lines * lineSpacing + lineHeight, HorizontalAlignment.CENTER, VerticalAlignment.TOP, 0, ARGB_BLACK, vertical);
                        } else {
                            DrawingHelper.drawString(nativeImage, text, x, y + (textBelow ? -1 - lineHeight : lines * lineSpacing + lineHeight), HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.BOTTOM : VerticalAlignment.TOP, 0, passed ? ARGB_LIGHT_GRAY : ARGB_BLACK, vertical);
                        }
                    }

                    DrawingHelper.drawStation(nativeImage, x, y, heightScale, lines, passed);

                    final DynamicTextureCache.Text text = clientCache.getText(key.split("\\|\\|")[0], maxStringWidth, y - lineSize, (int) ((currentStation ? 1.2 : 1) * fontSizeBig), (int) ((currentStation ? 1.2 : 1) * fontSizeSmall), fontSizeSmall / 4, vertical ? HorizontalAlignment.RIGHT : (rotateStationName ? HorizontalAlignment.LEFT : HorizontalAlignment.CENTER), LINE_HEIGHT_MULTIPLIER, false, rotateStationName);
                    if (rotateStationName) {
                        DrawingHelper.drawString(nativeImage, text, x - lineSize * 3 / 2, y - lineSize, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, passed ? ARGB_LIGHT_GRAY : ARGB_BLACK, vertical);
                    } else {
                        DrawingHelper.drawString(nativeImage, text, x, y + (textBelow ? lines * lineSpacing : -1) + (textBelow ? 1 : -1) * lineSize * 5 / 4, HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.TOP : VerticalAlignment.BOTTOM, currentStation ? ARGB_BLACK : 0, passed ? ARGB_LIGHT_GRAY : currentStation ? ARGB_WHITE : ARGB_BLACK, vertical);
                    }
                }));

                if (transparentWhite) DrawingHelper.clearColor(nativeImage, ARGB_WHITE);

                return nativeImage;
            }
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateRouteMapTRT(long platformId, boolean vertical, boolean flip, float aspectRatio, boolean transparentWhite) {
        if (aspectRatio <= 0) return null;

        try {
            final ObjectArrayList<ObjectIntImmutablePair<SimplifiedRoute>> routeDetails = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                if (!simplifiedRoute.getName().isEmpty()) {
                    routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));
                }
            });
            if (routeDetails.isEmpty()) {
                MinecraftClientData.getInstance().simplifiedRoutes.stream().filter(simplifiedRoute -> simplifiedRoute.getPlatformIndex(platformId) >= 0).sorted().forEach(simplifiedRoute -> {
                    final int currentStationIndex = simplifiedRoute.getPlatformIndex(platformId);
                    if (!simplifiedRoute.getName().isEmpty()) {
                        routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));
                    }
                });
            }
            final int routeCount = routeDetails.size();

            if (routeCount > 0) {
                final DynamicTextureCache clientCache = DynamicTextureCache.instance;
                final ObjectArrayList<LongArrayList> stationsIdsBefore = new ObjectArrayList<>();
                final ObjectArrayList<LongArrayList> stationsIdsAfter = new ObjectArrayList<>();
                final ObjectArrayList<Int2ObjectAVLTreeMap<StationPosition>> stationPositions = new ObjectArrayList<>();
                final IntAVLTreeSet colors = new IntAVLTreeSet();
                final int[] colorIndices = new int[routeCount];
                int colorIndex = -1;
                int previousColor = -1;
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationsIdsBefore.add(new LongArrayList());
                    stationsIdsAfter.add(new LongArrayList());
                    stationPositions.add(new Int2ObjectAVLTreeMap<>());

                    final ObjectIntImmutablePair<SimplifiedRoute> routeDetail = routeDetails.get(routeIndex);
                    final ObjectArrayList<SimplifiedRoutePlatform> simplifiedRoutePlatforms = routeDetail.left().getPlatforms();
                    final int currentIndex = routeDetail.rightInt();
                    for (int stationIndex = 0; stationIndex < simplifiedRoutePlatforms.size(); stationIndex++) {
                        if (stationIndex != currentIndex) {
                            final long stationId = simplifiedRoutePlatforms.get(stationIndex).getStationId();
                            if (stationIndex < currentIndex) {
                                stationsIdsBefore.get(stationsIdsBefore.size() - 1).add(0, stationId);
                            } else {
                                stationsIdsAfter.get(stationsIdsAfter.size() - 1).add(stationId);
                            }
                        }
                    }

                    final int color = routeDetail.left().getColor();
                    colors.add(color);
                    if (color != previousColor) {
                        colorIndex++;
                        previousColor = color;
                    }
                    colorIndices[routeIndex] = colorIndex;
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

                if (width <= 0 || height <= 0) return null;

                final NativeImage nativeImage = new NativeImage(NativeImageFormat.getAbgrMapped(), width, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, 0);

                final Object2ObjectOpenHashMap<String, ObjectOpenHashSet<StationPositionGrouped>> stationPositionsGrouped = new Object2ObjectOpenHashMap<>();
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    final SimplifiedRoute simplifiedRoute = routeDetails.get(routeIndex).left();
                    final int currentIndex = routeDetails.get(routeIndex).rightInt();
                    final Int2ObjectAVLTreeMap<StationPosition> routeStationPositions = stationPositions.get(routeIndex);

                    for (int stationIndex = 0; stationIndex < simplifiedRoute.getPlatforms().size(); stationIndex++) {
                        final StationPosition stationPosition = routeStationPositions.get(stationIndex - currentIndex);
                        if (stationIndex < simplifiedRoute.getPlatforms().size() - 1) {
                            DrawingHelper.drawLine(nativeImage, stationPosition, routeStationPositions.get(stationIndex + 1 - currentIndex), widthScale, heightScale, xOffset, yOffset, stationIndex < currentIndex ? ARGB_LIGHT_GRAY : ARGB_BLACK | simplifiedRoute.getColor());
                        }

                        final SimplifiedRoutePlatform simplifiedRoutePlatform = simplifiedRoute.getPlatforms().get(stationIndex);
                        final String key = String.format("%s||%s", simplifiedRoutePlatform.getStationName(), simplifiedRoutePlatform.getStationId());

                        if (!stationPosition.isCommon || stationPositionsGrouped.getOrDefault(key, new ObjectOpenHashSet<>()).stream().noneMatch(stationPosition2 -> stationPosition2.stationPosition.x == stationPosition.x)) {
                            final IntArrayList interchangeColors = new IntArrayList();
                            final ObjectArrayList<String> interchangeNames = new ObjectArrayList<>();
                            simplifiedRoutePlatform.forEach((color, interchangeRouteNamesForColor) -> {
                                if (!colors.contains(color)) {
                                    interchangeRouteNamesForColor.forEach(name -> {
                                        interchangeColors.add(color);
                                        interchangeNames.add(name);
                                    });
                                }
                            });
                            Data.put(stationPositionsGrouped, key, new StationPositionGrouped(stationPosition, stationIndex - currentIndex, interchangeColors, interchangeNames), ObjectOpenHashSet::new);
                        }
                    }
                }

                final int maxStringWidth = (int) (scale * 0.9 * ((vertical ? heightScale : widthScale) / 2 + extraPadding / routeCount));
                stationPositionsGrouped.forEach((key, stationPositionGroupedSet) -> stationPositionGroupedSet.forEach(stationPositionGrouped -> {
                    final int x = Math.round((stationPositionGrouped.stationPosition.x + xOffset) * scale * widthScale);
                    final int y = Math.round((stationPositionGrouped.stationPosition.y + yOffset) * scale * heightScale);
                    final int lines = stationPositionGrouped.stationPosition.isCommon ? colorIndices[colorIndices.length - 1] : 0;
                    final boolean currentStation = stationPositionGrouped.stationOffset == 0;
                    final boolean passed = stationPositionGrouped.stationOffset < 0;
                    final boolean textBelow = vertical || (stationPositionGrouped.stationPosition.isCommon ? Math.abs(stationPositionGrouped.stationOffset) % 2 == 0 : y >= yOffset * scale);

                    final IntArrayList interchangeColors = stationPositionGrouped.interchangeColors;
                    if (!interchangeColors.isEmpty()) {
                        final int lineHeight = lineSize * 2;
                        final int lineWidth = (int) Math.ceil((float) lineSize / interchangeColors.size());
                        for (int i = 0; i < interchangeColors.size(); i++) {
                            for (int drawX = 0; drawX < lineWidth; drawX++) {
                                for (int drawY = 0; drawY < lineHeight; drawY++) {
                                    DrawingHelper.drawPixelSafe(nativeImage, x + drawX + lineWidth * i - lineWidth * interchangeColors.size() / 2, y + (textBelow ? -1 : lines * lineSpacing) + (textBelow ? -drawY : drawY), ARGB_BLACK | interchangeColors.getInt(i));
                                }
                            }
                        }

                        int interchangesWidth = 0;
                        int interchangesHeight = 0;
                        final ObjectArrayList<DynamicTextureCache.Text> interchanges = new ObjectArrayList<>();
                        for (int i = 0; i < stationPositionGrouped.interchangeNames.size(); i++) {
                            final DynamicTextureCache.Text text = clientCache.getText(stationPositionGrouped.interchangeNames.get(i), maxStringWidth, (int) ((fontSizeBig + fontSizeSmall) * LINE_HEIGHT_MULTIPLIER / 2), fontSizeBig / 2, fontSizeSmall / 2, 0, HorizontalAlignment.CENTER);
                            interchanges.add(text);
                            interchangesWidth += text.renderWidth();
                            interchangesHeight = Math.max(interchangesHeight, text.height());
                        }
                        if (interchangesWidth > 0) {
                            int renderX = x - interchangesWidth / 2;
                            for (int i = 0; i < interchanges.size(); i++) {
                                DrawingHelper.fillRectSafe(nativeImage, renderX, y + (textBelow ? -lineHeight - interchangesHeight : lines * lineSpacing + lineHeight), interchanges.get(i).renderWidth(), interchangesHeight, DrawingHelper.invertColor(ARGB_BLACK | interchangeColors.getInt(i)));
                                DrawingHelper.drawString(nativeImage, interchanges.get(i), renderX, y + (textBelow ? -lineHeight : lines * lineSpacing + lineHeight + interchangesHeight / 2), HorizontalAlignment.LEFT, textBelow ? VerticalAlignment.BOTTOM : VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
                                renderX += interchanges.get(i).renderWidth();
                            }
                        }
                    }

                    DrawingHelper.drawStation(nativeImage, x, y, heightScale, lines, passed);

                    final DynamicTextureCache.Text text = clientCache.getText(key.split("\\|\\|")[0], maxStringWidth, y - lineSize, (int) ((currentStation ? 1.2 : 1) * fontSizeBig), (int) ((currentStation ? 1.2 : 1) * fontSizeSmall), fontSizeSmall / 4, vertical ? HorizontalAlignment.RIGHT : HorizontalAlignment.CENTER, LINE_HEIGHT_MULTIPLIER, false, false);
                    DrawingHelper.drawString(nativeImage, text, x, y + (textBelow ? lines * lineSpacing : -1) + (textBelow ? 1 : -1) * lineSize * 5 / 4, HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.TOP : VerticalAlignment.BOTTOM, currentStation ? ARGB_BLACK | routeDetails.get(lines).left().getColor() : 0, passed ? ARGB_LIGHT_GRAY : ARGB_WHITE, vertical);
                }));

                return nativeImage;
            }
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateRouteMapPSDBMT(long platformId, boolean flip, float aspectRatio, boolean transparentWhite) {
        if (aspectRatio <= 0) return null;

        try {
            final ObjectArrayList<ObjectIntImmutablePair<SimplifiedRoute>> routeDetails = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                if (!simplifiedRoute.getName().isEmpty()) {
                    routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));
                }
            });
            if (routeDetails.isEmpty()) {
                MinecraftClientData.getInstance().simplifiedRoutes.stream().filter(simplifiedRoute -> simplifiedRoute.getPlatformIndex(platformId) >= 0).sorted().forEach(simplifiedRoute -> {
                    final int currentStationIndex = simplifiedRoute.getPlatformIndex(platformId);
                    if (!simplifiedRoute.getName().isEmpty()) {
                        routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));
                    }
                });
            }
            final int routeCount = routeDetails.size();

            if (routeCount > 0) {
                final DynamicTextureCache clientCache = DynamicTextureCache.instance;
                final ObjectArrayList<LongArrayList> stationsIdsBefore = new ObjectArrayList<>();
                final ObjectArrayList<LongArrayList> stationsIdsAfter = new ObjectArrayList<>();
                final ObjectArrayList<Int2ObjectAVLTreeMap<StationPosition>> stationPositions = new ObjectArrayList<>();
                final IntAVLTreeSet colors = new IntAVLTreeSet();
                final int[] colorIndices = new int[routeCount];
                int colorIndex = -1;
                int previousColor = -1;
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationsIdsBefore.add(new LongArrayList());
                    stationsIdsAfter.add(new LongArrayList());
                    stationPositions.add(new Int2ObjectAVLTreeMap<>());

                    final ObjectIntImmutablePair<SimplifiedRoute> routeDetail = routeDetails.get(routeIndex);
                    final ObjectArrayList<SimplifiedRoutePlatform> simplifiedRoutePlatforms = routeDetail.left().getPlatforms();
                    final int currentIndex = routeDetail.rightInt();
                    for (int stationIndex = 0; stationIndex < simplifiedRoutePlatforms.size(); stationIndex++) {
                        if (stationIndex != currentIndex) {
                            final long stationId = simplifiedRoutePlatforms.get(stationIndex).getStationId();
                            if (stationIndex < currentIndex) {
                                stationsIdsBefore.get(stationsIdsBefore.size() - 1).add(0, stationId);
                            } else {
                                stationsIdsAfter.get(stationsIdsAfter.size() - 1).add(stationId);
                            }
                        }
                    }

                    final int color = routeDetail.left().getColor();
                    colors.add(color);
                    if (color != previousColor) {
                        colorIndex++;
                        previousColor = color;
                    }
                    colorIndices[routeIndex] = colorIndex;
                }

                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationPositions.get(routeIndex).put(0, new StationPosition(0, getLineOffset(routeIndex, colorIndices), true));
                }

                final float[] bounds = new float[3];
                setup(stationPositions, flip ? stationsIdsBefore : stationsIdsAfter, colorIndices, bounds, flip, true);
                final float xOffset = bounds[0] + 0.5F;
                setup(stationPositions, flip ? stationsIdsAfter : stationsIdsBefore, colorIndices, bounds, !flip, false);
                final float rawHeightPart = Math.abs(bounds[1]) + 1.78F;
                final float rawWidth = xOffset + bounds[0] + 0.5F;
                final float rawHeight = rawHeightPart + bounds[2] + 1F;
                final float extraPadding = 0;

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

                if (width <= 0 || height <= 0) return null;

                final NativeImage nativeImage = new NativeImage(NativeImageFormat.getAbgrMapped(), width, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, ARGB_WHITE);

                final Object2ObjectOpenHashMap<String, ObjectOpenHashSet<StationPositionGrouped>> stationPositionsGrouped = new Object2ObjectOpenHashMap<>();
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    final SimplifiedRoute simplifiedRoute = routeDetails.get(routeIndex).left();
                    final int currentIndex = routeDetails.get(routeIndex).rightInt();
                    final Int2ObjectAVLTreeMap<StationPosition> routeStationPositions = stationPositions.get(routeIndex);

                    for (int stationIndex = 0; stationIndex < simplifiedRoute.getPlatforms().size(); stationIndex++) {
                        final StationPosition stationPosition = routeStationPositions.get(stationIndex - currentIndex);
                        if (stationIndex < simplifiedRoute.getPlatforms().size() - 1) {
                            DrawingHelper.drawLine(nativeImage, stationPosition, routeStationPositions.get(stationIndex + 1 - currentIndex), widthScale, heightScale, xOffset, rawHeightPart + 0.4F, stationIndex < currentIndex ? ARGB_LIGHT_GRAY : ARGB_BLACK | simplifiedRoute.getColor());
                        }

                        final SimplifiedRoutePlatform simplifiedRoutePlatform = simplifiedRoute.getPlatforms().get(stationIndex);
                        final String key = String.format("%s||%s", simplifiedRoutePlatform.getStationName(), simplifiedRoutePlatform.getStationId());

                        if (!stationPosition.isCommon || stationPositionsGrouped.getOrDefault(key, new ObjectOpenHashSet<>()).stream().noneMatch(stationPosition2 -> stationPosition2.stationPosition.x == stationPosition.x)) {
                            final IntArrayList interchangeColors = new IntArrayList();
                            final ObjectArrayList<String> interchangeNames = new ObjectArrayList<>();
                            simplifiedRoutePlatform.forEach((color, interchangeRouteNamesForColor) -> {
                                if (!colors.contains(color)) {
                                    interchangeRouteNamesForColor.forEach(name -> {
                                        interchangeColors.add(color);
                                        interchangeNames.add(name);
                                    });
                                }
                            });
                            Data.put(stationPositionsGrouped, key, new StationPositionGrouped(stationPosition, stationIndex - currentIndex, interchangeColors, interchangeNames), ObjectOpenHashSet::new);
                        }
                    }
                }

                final int maxStringWidth = (int) (scale * 0.9 * (widthScale / 2 + extraPadding / routeCount));
                stationPositionsGrouped.forEach((key, stationPositionGroupedSet) -> stationPositionGroupedSet.forEach(stationPositionGrouped -> {
                    final int x = Math.round((stationPositionGrouped.stationPosition.x + xOffset) * scale * widthScale);
                    final int y = Math.round((stationPositionGrouped.stationPosition.y + rawHeightPart + 0.4F) * scale * heightScale);
                    final int lines = stationPositionGrouped.stationPosition.isCommon ? colorIndices[colorIndices.length - 1] : 0;
                    final boolean currentStation = stationPositionGrouped.stationOffset == 0;

                    final IntArrayList interchangeColors = stationPositionGrouped.interchangeColors;
                    if (!interchangeColors.isEmpty()) {
                        final int lineHeight = lineSize * 2;
                        final int lineWidth = (int) Math.ceil((float) lineSize / 4);
                        for (int drawX = 0; drawX < lineWidth; drawX++) {
                            for (int drawY = 0; drawY < lineSize * 1.8; drawY++) {
                                DrawingHelper.drawPixelSafe(nativeImage, x + drawX - lineWidth / 2, y + lines * lineSpacing + drawY, ARGB_BLACK);
                            }
                        }

                        final int padding = scale / 32;
                        int interchangesWidth = 0;
                        int interchangesHeight = 0;
                        final ObjectArrayList<DynamicTextureCache.Text> interchanges = new ObjectArrayList<>();
                        for (int i = 0; i < stationPositionGrouped.interchangeNames.size(); i++) {
                            final DynamicTextureCache.Text text = clientCache.getText(stationPositionGrouped.interchangeNames.get(i), maxStringWidth, (int) ((fontSizeBig + fontSizeSmall) * LINE_HEIGHT_MULTIPLIER / 2), fontSizeBig / 2, fontSizeSmall / 2, 0, HorizontalAlignment.CENTER);
                            interchanges.add(text);
                            interchangesWidth += text.renderWidth() + padding;
                            interchangesHeight = Math.max(interchangesHeight, text.height());
                        }
                        interchangesWidth -= padding;
                        if (interchangesWidth > 0) {
                            int renderX = x - interchangesWidth / 2;
                            for (int i = 0; i < interchanges.size(); i++) {
                                DrawingHelper.fillRectSafe(nativeImage, renderX, y + lines * lineSpacing + lineHeight, interchanges.get(i).renderWidth(), interchangesHeight, DrawingHelper.invertColor(ARGB_BLACK | interchangeColors.getInt(i)));
                                DrawingHelper.drawString(nativeImage, interchanges.get(i), renderX, y + lines * lineSpacing + lineHeight + interchangesHeight / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
                                renderX += interchanges.get(i).renderWidth() + padding;
                            }
                        }
                    }

                    DrawingHelper.drawStationBMT(nativeImage, x, y, heightScale, lines, currentStation);
                    DrawingHelper.drawVerticalString(nativeImage, key.split("\\|\\|")[0], x, y - lineSize * 5 / 4, Integer.MAX_VALUE, y - lineSize * 4, fontSizeBig, fontSizeSmall, fontSizeSmall / 4, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, currentStation ? 0xffff0000 : ARGB_BLACK);
                }));

                if (transparentWhite) DrawingHelper.clearColor(nativeImage, ARGB_WHITE);

                return nativeImage;
            }
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateRouteMapJinjing(long platformId, int arrowDirection, float aspectRatio, boolean transparentWhite) {
        if (aspectRatio <= 0) return null;

        try {
            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            ObjectArrayList<String> nextStations = new ObjectArrayList<>();
            final ObjectArrayList<ObjectIntImmutablePair<SimplifiedRoute>> routeDetails = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                if (!simplifiedRoute.getName().isEmpty()) {
                    routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));

                    final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                        case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                        case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                        default -> "";
                    };

                    if (!simplifiedRoute.getName().isEmpty()) {
                        destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
                        nextStations.add(simplifiedRoute.getPlatforms().get(currentStationIndex + 1).getStationName());
                    }
                }
            });
            if (routeDetails.isEmpty()) {
                MinecraftClientData.getInstance().simplifiedRoutes.stream().filter(simplifiedRoute -> simplifiedRoute.getPlatformIndex(platformId) >= 0).sorted().forEach(simplifiedRoute -> {
                    final int currentStationIndex = simplifiedRoute.getPlatformIndex(platformId);
                    if (!simplifiedRoute.getName().isEmpty()) {
                        routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));
                    }
                });
            }
            final boolean isTerminating = destinations.isEmpty();
            final int routeCount = routeDetails.size();

            if (routeCount > 0) {
                final DynamicTextureCache clientCache = DynamicTextureCache.instance;
                final ObjectArrayList<LongArrayList> stationsIdsBefore = new ObjectArrayList<>();
                final ObjectArrayList<LongArrayList> stationsIdsAfter = new ObjectArrayList<>();
                final ObjectArrayList<Int2ObjectAVLTreeMap<StationPosition>> stationPositions = new ObjectArrayList<>();
                final IntAVLTreeSet colors = new IntAVLTreeSet();
                final int[] colorIndices = new int[routeCount];
                int colorIndex = -1;
                int previousColor = -1;
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationsIdsBefore.add(new LongArrayList());
                    stationsIdsAfter.add(new LongArrayList());
                    stationPositions.add(new Int2ObjectAVLTreeMap<>());

                    final ObjectIntImmutablePair<SimplifiedRoute> routeDetail = routeDetails.get(routeIndex);
                    final ObjectArrayList<SimplifiedRoutePlatform> simplifiedRoutePlatforms = routeDetail.left().getPlatforms();
                    final int currentIndex = routeDetail.rightInt();
                    for (int stationIndex = 0; stationIndex < simplifiedRoutePlatforms.size(); stationIndex++) {
                        if (stationIndex != currentIndex) {
                            final long stationId = simplifiedRoutePlatforms.get(stationIndex).getStationId();
                            if (stationIndex < currentIndex) {
                                stationsIdsBefore.get(stationsIdsBefore.size() - 1).add(0, stationId);
                            } else {
                                stationsIdsAfter.get(stationsIdsAfter.size() - 1).add(stationId);
                            }
                        }
                    }

                    final int color = routeDetail.left().getColor();
                    colors.add(color);
                    if (color != previousColor) {
                        colorIndex++;
                        previousColor = color;
                    }
                    colorIndices[routeIndex] = colorIndex;
                }

                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationPositions.get(routeIndex).put(0, new StationPosition(0, getLineOffset(routeIndex, colorIndices), true));
                }

                final boolean rotateStationName = ConfigClient.ROTATED_STATION_NAME.get();
                final float[] bounds = new float[3];
                setup(stationPositions, arrowDirection == 2 ? stationsIdsBefore : stationsIdsAfter, colorIndices, bounds, arrowDirection == 2, true);
                final float xOffset = bounds[0] + 0.5F;
                setup(stationPositions, arrowDirection == 2 ? stationsIdsAfter : stationsIdsBefore, colorIndices, bounds, arrowDirection != 2, false);
                final float rawHeightPart = Math.abs(bounds[1]) + 1;
                final float rawWidth = xOffset + bounds[0] + 0.5F;
                final float rawHeightTotal = rawHeightPart + bounds[2] + 3;
                final float yOffset = rawHeightPart + (rotateStationName ? 0.6F : 0) + 1.7F;
                final float extraPadding = 0;

                final int height;
                final int width;
                final float widthScale;
                final float heightScale;
                if (rawWidth / rawHeightTotal > aspectRatio) {
                    width = Math.round(rawWidth * scale);
                    height = Math.round(width / aspectRatio);
                    widthScale = 1;
                    heightScale = height / rawHeightTotal / scale;
                } else {
                    height = Math.round(rawHeightTotal * scale);
                    width = Math.round(height * aspectRatio);
                    heightScale = 1;
                    widthScale = width / rawWidth / scale;
                }

                if (width <= 0 || height <= 0) return null;

                final int padding = Math.round(height * 0.25F);

                final NativeImage nativeImage = new NativeImage(NativeImageFormat.getAbgrMapped(), width, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, ARGB_WHITE);

                String nextStationString = IGui.mergeStations(nextStations);
                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else {
                        destinationString = IGuiExtension.insertTranslation("gui.tjmetro.train_to_cjk", "gui.tjmetro.train_to", 1, destinationString);
                    }
                }

                /* WARNING - HORRIBLE! */
                // This is too complex to fully understand. Do not modify them if they are working properly.
                final DynamicTextureCache.Text textCurrentStationSign = clientCache.getText(IGuiExtension.mergeTranslation("gui.tjmetro.current_station_cjk", "gui.tjmetro.current_station"), width, height, lineSize * 4, lineSize * 2, 0, HorizontalAlignment.CENTER);
                final DynamicTextureCache.Text textCurrentStation = clientCache.getText(MinecraftClientData.getInstance().platformIdMap.get(platformId).area.getName(), width, height, lineSize * 5, lineSize * 3, 0, HorizontalAlignment.CENTER);

                final int textY = (int) ((height - (rawHeightPart + 1.5F) * scale * heightScale) / 2);
                final int arrowSize = arrowDirection % 3 == 0 ? 0 : padding;
                final int arrowPadding = arrowDirection % 3 == 0 ? 0 : padding / 4;
                final boolean shouldRenderCurrentStationText = isTerminating || (((double) (textCurrentStation.width() + textCurrentStationSign.width() + padding / 4) / width) <= 0.4);
                final int CurrentStationSignStartX = (width - textCurrentStationSign.width() - padding - textCurrentStation.width()) / 2;
                if (isTerminating) {
                    final DynamicTextureCache.Text textTerminus = clientCache.getText(IGuiExtension.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus"), arrowDirection == 2 ? width - CurrentStationSignStartX - textCurrentStationSign.width() - padding / 4 - textCurrentStation.width() : CurrentStationSignStartX - padding, height, lineSize * 4, lineSize * 2, 0, HorizontalAlignment.CENTER);
                    if (arrowDirection == 2) {
                        DrawingHelper.drawString(nativeImage, textTerminus, width - padding, textY, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                    } else {
                        DrawingHelper.drawString(nativeImage, textTerminus, padding, textY, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                    }
                } else {
                    final DynamicTextureCache.Text textNextStationSign = clientCache.getText(IGuiExtension.mergeTranslation("gui.tjmetro.next_station_cjk", "gui.tjmetro.next_station"), width, height, lineSize * 4, lineSize * 2, 0, HorizontalAlignment.CENTER);
                    final DynamicTextureCache.Text textNextStations, textDestinations;
                    final boolean renderDestinationOnly;
                    if (arrowDirection == 2) {
                        final int leftWidthForText = shouldRenderCurrentStationText ? CurrentStationSignStartX - padding - textNextStationSign.width() - padding / 4 : width;
                        textNextStations = clientCache.getText(nextStationString, leftWidthForText, height, lineSize * 4, lineSize * 2, 0, HorizontalAlignment.CENTER);
                        final int rightWidthForText = (shouldRenderCurrentStationText ? width / 2 - textCurrentStation.width() / 2 - padding / 2 : width - padding - textNextStationSign.width() - padding / 4 - textNextStations.width()) - padding - arrowSize - arrowPadding;
                        renderDestinationOnly = ((double) rightWidthForText / width) < 0.3 && !shouldRenderCurrentStationText;
                        textDestinations = clientCache.getText(destinationString, renderDestinationOnly ? width - padding * 2 - arrowSize - arrowPadding : rightWidthForText, height, lineSize * 4, lineSize * 2, 0, HorizontalAlignment.RIGHT);
                    } else {
                        final int rightWidthForText = shouldRenderCurrentStationText ? width - CurrentStationSignStartX - textCurrentStationSign.width() - padding / 4 - textCurrentStation.width() - textNextStationSign.width() - padding / 4 - padding : width;
                        textNextStations = clientCache.getText(nextStationString, rightWidthForText, height, lineSize * 4, lineSize * 2, 0, HorizontalAlignment.CENTER);
                        final int leftWidthForText = shouldRenderCurrentStationText ? CurrentStationSignStartX - padding - arrowSize - arrowPadding : width - padding * 2 - textNextStations.width() - padding / 4 - textNextStationSign.width();
                        renderDestinationOnly = ((double) leftWidthForText / width) < 0.3 && !shouldRenderCurrentStationText;
                        textDestinations = clientCache.getText(destinationString, renderDestinationOnly ? width - padding * 2 - arrowSize - arrowPadding : leftWidthForText, height, lineSize * 4, lineSize * 2, 0, HorizontalAlignment.LEFT);
                    }

                    if (arrowDirection == 2) {
                        if (!renderDestinationOnly) {
                            DrawingHelper.drawString(nativeImage, textNextStationSign, padding, textY, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                            DrawingHelper.drawString(nativeImage, textNextStations, padding + textNextStationSign.width() + padding / 4, textY, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                        }
                        DrawingHelper.drawString(nativeImage, textDestinations, width - padding - arrowSize - arrowPadding, textY, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                        DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, width - padding - arrowSize, textY - arrowSize / 2, arrowSize, arrowSize, true, 0, 1, ARGB_BLACK, false);
                    } else {
                        DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, padding, textY - arrowSize / 2, arrowSize, arrowSize, false, 0, 1, ARGB_BLACK, false);
                        DrawingHelper.drawString(nativeImage, textDestinations, padding + arrowSize + arrowPadding, textY, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                        if (!renderDestinationOnly) {
                            DrawingHelper.drawString(nativeImage, textNextStationSign, width - padding - textNextStations.width() - padding / 4, textY, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                            DrawingHelper.drawString(nativeImage, textNextStations, width - padding, textY, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                        }
                    }
                }
                if (shouldRenderCurrentStationText) {
                    DrawingHelper.drawString(nativeImage, textCurrentStationSign, CurrentStationSignStartX, textY, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                    DrawingHelper.drawString(nativeImage, textCurrentStation, (width - textCurrentStation.width() + padding) / 2, textY, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
                }
                /* END OF HORRIBLE */

                final Object2ObjectOpenHashMap<String, ObjectOpenHashSet<StationPositionGrouped>> stationPositionsGrouped = new Object2ObjectOpenHashMap<>();
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    final SimplifiedRoute simplifiedRoute = routeDetails.get(routeIndex).left();
                    final int currentIndex = routeDetails.get(routeIndex).rightInt();
                    final Int2ObjectAVLTreeMap<StationPosition> routeStationPositions = stationPositions.get(routeIndex);

                    for (int stationIndex = 0; stationIndex < simplifiedRoute.getPlatforms().size(); stationIndex++) {
                        final StationPosition stationPosition = routeStationPositions.get(stationIndex - currentIndex);
                        if (stationIndex < simplifiedRoute.getPlatforms().size() - 1) {
                            DrawingHelper.drawLine(nativeImage, stationPosition, routeStationPositions.get(stationIndex + 1 - currentIndex), widthScale, heightScale, xOffset, yOffset, stationIndex < currentIndex ? ARGB_LIGHT_GRAY : ARGB_BLACK | simplifiedRoute.getColor());
                        }

                        final SimplifiedRoutePlatform simplifiedRoutePlatform = simplifiedRoute.getPlatforms().get(stationIndex);
                        final String key = String.format("%s||%s", simplifiedRoutePlatform.getStationName(), simplifiedRoutePlatform.getStationId());

                        if (!stationPosition.isCommon || stationPositionsGrouped.getOrDefault(key, new ObjectOpenHashSet<>()).stream().noneMatch(stationPosition2 -> stationPosition2.stationPosition.x == stationPosition.x)) {
                            final IntArrayList interchangeColors = new IntArrayList();
                            final ObjectArrayList<String> interchangeNames = new ObjectArrayList<>();
                            simplifiedRoutePlatform.forEach((color, interchangeRouteNamesForColor) -> {
                                if (!colors.contains(color)) {
                                    interchangeColors.add(color);
                                    interchangeRouteNamesForColor.forEach(interchangeNames::add);
                                }
                            });
                            Data.put(stationPositionsGrouped, key, new StationPositionGrouped(stationPosition, stationIndex - currentIndex, interchangeColors, interchangeNames), ObjectOpenHashSet::new);
                        }
                    }
                }

                final int maxStringWidth = (int) (scale * 0.9 * (widthScale / 2 + extraPadding / routeCount));
                stationPositionsGrouped.forEach((key, stationPositionGroupedSet) -> stationPositionGroupedSet.forEach(stationPositionGrouped -> {
                    final int x = Math.round((stationPositionGrouped.stationPosition.x + xOffset) * scale * widthScale);
                    final int y = Math.round((stationPositionGrouped.stationPosition.y + yOffset) * scale * heightScale);
                    final int lines = stationPositionGrouped.stationPosition.isCommon ? colorIndices[colorIndices.length - 1] : 0;
                    final boolean currentStation = stationPositionGrouped.stationOffset == 0;
                    final boolean passed = stationPositionGrouped.stationOffset < 0;
                    final boolean textBelow = stationPositionGrouped.stationPosition.isCommon ? Math.abs(stationPositionGrouped.stationOffset) % 2 == 0 : y >= yOffset * scale;

                    final IntArrayList interchangeColors = stationPositionGrouped.interchangeColors;
                    if (!interchangeColors.isEmpty()) {
                        final int lineHeight = lineSize * 2;
                        final int lineWidth = (int) Math.ceil((float) lineSize / interchangeColors.size());
                        for (int i = 0; i < interchangeColors.size(); i++) {
                            for (int drawX = 0; drawX < lineWidth; drawX++) {
                                for (int drawY = 0; drawY < lineHeight; drawY++) {
                                    if (rotateStationName) {
                                        DrawingHelper.drawPixelSafe(nativeImage, x + drawX + lineWidth * i - lineWidth * interchangeColors.size() / 2, y + lines * lineSpacing + drawY, ARGB_BLACK | interchangeColors.getInt(i));
                                    } else {
                                        DrawingHelper.drawPixelSafe(nativeImage, x + drawX + lineWidth * i - lineWidth * interchangeColors.size() / 2, y + (textBelow ? -1 : lines * lineSpacing) + (textBelow ? -drawY : drawY), passed ? ARGB_LIGHT_GRAY : ARGB_BLACK | interchangeColors.getInt(i));
                                    }
                                }
                            }
                        }

                        final DynamicTextureCache.Text text = clientCache.getText(IGui.mergeStations(stationPositionGrouped.interchangeNames), maxStringWidth, (int) ((fontSizeBig + fontSizeSmall) * LINE_HEIGHT_MULTIPLIER / 2), fontSizeBig / 2, fontSizeSmall / 2, 0, HorizontalAlignment.CENTER);
                        if (rotateStationName) {
                            DrawingHelper.drawString(nativeImage, text, x, y + lines * lineSpacing + lineHeight, HorizontalAlignment.CENTER, VerticalAlignment.TOP, 0, ARGB_BLACK, false);
                        } else {
                            DrawingHelper.drawString(nativeImage, text, x, y + (textBelow ? -1 - lineHeight : lines * lineSpacing + lineHeight), HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.BOTTOM : VerticalAlignment.TOP, 0, passed ? ARGB_LIGHT_GRAY : ARGB_BLACK, false);
                        }
                    }

                    DrawingHelper.drawStation(nativeImage, x, y, heightScale, lines, passed);

                    final DynamicTextureCache.Text text = clientCache.getText(key.split("\\|\\|")[0], maxStringWidth, y - textY - textCurrentStation.height() / 2, (int) ((currentStation ? 1.2 : 1) * fontSizeBig), (int) ((currentStation ? 1.2 : 1) * fontSizeSmall), fontSizeSmall / 4, rotateStationName ? HorizontalAlignment.LEFT : HorizontalAlignment.CENTER, LINE_HEIGHT_MULTIPLIER, false, rotateStationName);
                    if (rotateStationName) {
                        DrawingHelper.drawString(nativeImage, text, x - lineSize * 3 / 2, y - lineSize, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, passed ? ARGB_LIGHT_GRAY : ARGB_BLACK, false);
                    } else {
                        DrawingHelper.drawString(nativeImage, text, x, y + (textBelow ? lines * lineSpacing : -1) + (textBelow ? 1 : -1) * lineSize * 5 / 4, HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.TOP : VerticalAlignment.BOTTOM, currentStation ? ARGB_BLACK : 0, passed ? ARGB_LIGHT_GRAY : currentStation ? ARGB_WHITE : ARGB_BLACK, false);
                    }
                }));

                if (transparentWhite) DrawingHelper.clearColor(nativeImage, ARGB_WHITE);

                return nativeImage;
            }
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateRouteMapBMT(long platformId, boolean arrowLeft, float aspectRatio, int backgroundColor) {
        if (aspectRatio <= 0) return null;

        try {
            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            final ObjectArrayList<ObjectIntImmutablePair<SimplifiedRoute>> routeDetails = new ObjectArrayList<>();
            final IntArrayList routeColors = getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                if (!simplifiedRoute.getName().isEmpty()) {
                    routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));

                    final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                        case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                        case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                        default -> "";
                    };

                    if (!simplifiedRoute.getName().isEmpty()) {
                        destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
                    }
                }
            });
            if (routeDetails.isEmpty()) {
                MinecraftClientData.getInstance().simplifiedRoutes.stream().filter(simplifiedRoute -> simplifiedRoute.getPlatformIndex(platformId) >= 0).sorted().forEach(simplifiedRoute -> {
                    final int currentStationIndex = simplifiedRoute.getPlatformIndex(platformId);
                    routeDetails.add(new ObjectIntImmutablePair<>(simplifiedRoute, currentStationIndex));
                });
                destinations.add(getStationName(platformId));
            }
            final int routeCount = routeDetails.size();

            if (routeCount > 0) {
                final DynamicTextureCache clientCache = DynamicTextureCache.instance;
                final ObjectArrayList<LongArrayList> stationsIdsBefore = new ObjectArrayList<>();
                final ObjectArrayList<LongArrayList> stationsIdsAfter = new ObjectArrayList<>();
                final ObjectArrayList<Int2ObjectAVLTreeMap<StationPosition>> stationPositions = new ObjectArrayList<>();
                final IntAVLTreeSet colors = new IntAVLTreeSet();
                final int[] colorIndices = new int[routeCount];
                int colorIndex = -1;
                int previousColor = -1;
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationsIdsBefore.add(new LongArrayList());
                    stationsIdsAfter.add(new LongArrayList());
                    stationPositions.add(new Int2ObjectAVLTreeMap<>());

                    final ObjectIntImmutablePair<SimplifiedRoute> routeDetail = routeDetails.get(routeIndex);
                    final ObjectArrayList<SimplifiedRoutePlatform> simplifiedRoutePlatforms = routeDetail.left().getPlatforms();
                    final int currentIndex = routeDetail.rightInt();
                    for (int stationIndex = 0; stationIndex < simplifiedRoutePlatforms.size(); stationIndex++) {
                        if (stationIndex != currentIndex) {
                            final long stationId = simplifiedRoutePlatforms.get(stationIndex).getStationId();
                            if (stationIndex < currentIndex) {
                                stationsIdsBefore.get(stationsIdsBefore.size() - 1).add(0, stationId);
                            } else {
                                stationsIdsAfter.get(stationsIdsAfter.size() - 1).add(stationId);
                            }
                        }
                    }

                    final int color = routeDetail.left().getColor();
                    colors.add(color);
                    if (color != previousColor) {
                        colorIndex++;
                        previousColor = color;
                    }
                    colorIndices[routeIndex] = colorIndex;
                }

                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    stationPositions.get(routeIndex).put(0, new StationPosition(0, getLineOffset(routeIndex, colorIndices), true));
                }

                final boolean rotateStationName = ConfigClient.ROTATED_STATION_NAME.get();
                final float[] bounds = new float[3];
                setup(stationPositions, arrowLeft ? stationsIdsAfter : stationsIdsBefore, colorIndices, bounds, !arrowLeft, true);
                final float xOffset = bounds[0] + 0.5F;
                setup(stationPositions, arrowLeft ? stationsIdsBefore : stationsIdsAfter, colorIndices, bounds, arrowLeft, false);
                final float rawHeightPart = Math.abs(bounds[1]) + 1;
                final float rawWidth = xOffset + bounds[0] + 0.5F;
                final float rawHeightTotal = rawHeightPart + bounds[2] + 3;
                final float yOffset = rawHeightPart + (rotateStationName ? 0.6F : 0) + 1.5F;
                final float extraPadding = 0;

                final int height;
                final int width;
                final float widthScale;
                final float heightScale;
                if (rawWidth / rawHeightTotal > aspectRatio) {
                    width = Math.round(rawWidth * scale);
                    height = Math.round(width / aspectRatio);
                    widthScale = 1;
                    heightScale = height / rawHeightTotal / scale;
                } else {
                    height = Math.round(rawHeightTotal * scale);
                    width = Math.round(height * aspectRatio);
                    heightScale = 1;
                    widthScale = width / rawWidth / scale;
                }

                if (width <= 0 || height <= 0) return null;

                final NativeImage nativeImage = new NativeImage(NativeImageFormat.getAbgrMapped(), width, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(backgroundColor));

                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else {
                        destinationString = IGuiExtension.insertTranslation("gui.tjmetro.train_to_cjk", "gui.tjmetro.train_to", 1, destinationString);
                    }
                }

                final List<String> routeNames = routeDetails.stream()
                        .map(pair -> pair.left().getName())
                        .collect(Collectors.toList());
                // The next line is using those functions "unproperly", but everything works properly.
                final String information = IGuiExtension.insertTranslation("gui.tjmetro.route_map_bmt_routes", "gui.tjmetro.route_map_bmt_routes", 2, IGui.mergeStations(routeNames), destinationString);

                final int padding = (int) (height / aspectRatio * 0.1); // fixed size
                final DynamicTextureCache.Text textInformation = clientCache.getText(information, width - padding * 2, height, (int) (height / aspectRatio * 0.12), (int) (height / aspectRatio * 0.06), 0, arrowLeft ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
                if (arrowLeft) {
                    DrawingHelper.drawString(nativeImage, textInformation, padding, (int) (height / aspectRatio * 0.2), HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
                } else {
                    DrawingHelper.drawString(nativeImage, textInformation, width - padding, (int) (height / aspectRatio * 0.2), HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
                }

                final int colorStripY = (int) (height / aspectRatio * 0.4);
                final int heightPerColor = padding / 2 / routeColors.size();
                for (int i = 0; i < routeColors.size(); i++) {
                    DrawingHelper.fillRectSafe(nativeImage, 0, colorStripY + heightPerColor * i, width, heightPerColor, DrawingHelper.invertColor(ARGB_BLACK | routeColors.getInt(i)));
                }

                final Object2ObjectOpenHashMap<String, ObjectOpenHashSet<StationPositionGrouped>> stationPositionsGrouped = new Object2ObjectOpenHashMap<>();
                for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                    final SimplifiedRoute simplifiedRoute = routeDetails.get(routeIndex).left();
                    final int currentIndex = routeDetails.get(routeIndex).rightInt();
                    final Int2ObjectAVLTreeMap<StationPosition> routeStationPositions = stationPositions.get(routeIndex);

                    for (int stationIndex = 0; stationIndex < simplifiedRoute.getPlatforms().size(); stationIndex++) {
                        final StationPosition stationPosition = routeStationPositions.get(stationIndex - currentIndex);
                        if (stationIndex < simplifiedRoute.getPlatforms().size() - 1) {
                            DrawingHelper.drawLine(nativeImage, stationPosition, routeStationPositions.get(stationIndex + 1 - currentIndex), widthScale, heightScale, xOffset, yOffset, stationIndex < currentIndex ? ARGB_LIGHT_GRAY : ARGB_BLACK | simplifiedRoute.getColor());
                        }

                        final SimplifiedRoutePlatform simplifiedRoutePlatform = simplifiedRoute.getPlatforms().get(stationIndex);
                        final String key = String.format("%s||%s", simplifiedRoutePlatform.getStationName(), simplifiedRoutePlatform.getStationId());

                        if (!stationPosition.isCommon || stationPositionsGrouped.getOrDefault(key, new ObjectOpenHashSet<>()).stream().noneMatch(stationPosition2 -> stationPosition2.stationPosition.x == stationPosition.x)) {
                            final IntArrayList interchangeColors = new IntArrayList();
                            final ObjectArrayList<String> interchangeNames = new ObjectArrayList<>();
                            simplifiedRoutePlatform.forEach((color, interchangeRouteNamesForColor) -> {
                                if (!colors.contains(color)) {
                                    interchangeRouteNamesForColor.forEach(name -> {
                                        interchangeColors.add(color);
                                        interchangeNames.add(name);
                                    });
                                }
                            });
                            Data.put(stationPositionsGrouped, key, new StationPositionGrouped(stationPosition, stationIndex - currentIndex, interchangeColors, interchangeNames), ObjectOpenHashSet::new);
                        }
                    }
                }

                final int maxStringWidth = (int) (scale * 0.9 * (widthScale / 2 + extraPadding / routeCount));
                stationPositionsGrouped.forEach((key, stationPositionGroupedSet) -> stationPositionGroupedSet.forEach(stationPositionGrouped -> {
                    final int x = Math.round((stationPositionGrouped.stationPosition.x + xOffset) * scale * widthScale);
                    final int y = Math.round((stationPositionGrouped.stationPosition.y + yOffset) * scale * heightScale);
                    final int lines = stationPositionGrouped.stationPosition.isCommon ? colorIndices[colorIndices.length - 1] : 0;
                    final boolean currentStation = stationPositionGrouped.stationOffset == 0;
                    final boolean passed = stationPositionGrouped.stationOffset < 0;
                    final boolean textBelow = stationPositionGrouped.stationPosition.isCommon ? Math.abs(stationPositionGrouped.stationOffset) % 2 == 0 : y >= yOffset * scale;

                    final IntArrayList interchangeColors = stationPositionGrouped.interchangeColors;
                    if (!interchangeColors.isEmpty()) {
                        final int lineHeight = lineSize * 2;
                        final int lineWidth = (int) Math.ceil((float) lineSize / 4);
                        for (int drawX = 0; drawX < lineWidth; drawX++) {
                            for (int drawY = 0; drawY < lineSize * 1.8; drawY++) {
                                DrawingHelper.drawPixelSafe(nativeImage, x + drawX - lineWidth / 2, y + lines * lineSpacing + drawY, ARGB_WHITE);
                            }
                        }

                        final int interchangePadding = scale / 32;
                        int interchangesWidth = 0;
                        int interchangesHeight = 0;
                        final ObjectArrayList<DynamicTextureCache.Text> interchanges = new ObjectArrayList<>();
                        for (int i = 0; i < stationPositionGrouped.interchangeNames.size(); i++) {
                            final DynamicTextureCache.Text text = clientCache.getText(stationPositionGrouped.interchangeNames.get(i), maxStringWidth, (int) ((fontSizeBig + fontSizeSmall) * LINE_HEIGHT_MULTIPLIER / 2), fontSizeBig / 2, fontSizeSmall / 2, 0, HorizontalAlignment.CENTER);
                            interchanges.add(text);
                            interchangesWidth += text.renderWidth() + interchangePadding;
                            interchangesHeight = Math.max(interchangesHeight, text.height());
                        }
                        interchangesWidth -= interchangePadding;
                        if (interchangesWidth > 0) {
                            int renderX = x - interchangesWidth / 2;
                            for (int i = 0; i < interchanges.size(); i++) {
                                DrawingHelper.fillRectSafe(nativeImage, renderX, y + lines * lineSpacing + lineHeight, interchanges.get(i).renderWidth(), interchangesHeight, DrawingHelper.invertColor(ARGB_BLACK | interchangeColors.getInt(i)));
                                DrawingHelper.drawString(nativeImage, interchanges.get(i), renderX, y + lines * lineSpacing + lineHeight + interchangesHeight / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
                                renderX += interchanges.get(i).renderWidth() + interchangePadding;
                                if (renderX >= width) break;
                            }
                        }
                    }

                    DrawingHelper.drawStationRouteMapBMT(nativeImage, x, y, heightScale, lines, passed, currentStation);

                    final DynamicTextureCache.Text text = clientCache.getText(key.split("\\|\\|")[0], maxStringWidth, y - colorStripY - padding, fontSizeBig, fontSizeSmall, fontSizeSmall / 4, rotateStationName ? HorizontalAlignment.LEFT : HorizontalAlignment.CENTER, LINE_HEIGHT_MULTIPLIER, false, rotateStationName);
                    if (rotateStationName) {
                        DrawingHelper.drawString(nativeImage, text, x - lineSize * 3 / 2, y - lineSize, HorizontalAlignment.LEFT, VerticalAlignment.BOTTOM, 0, passed ? ARGB_LIGHT_GRAY : (currentStation ? 0xff00acd0 : ARGB_WHITE), false);
                    } else {
                        DrawingHelper.drawString(nativeImage, text, x, y + (textBelow ? lines * lineSpacing : -1) + (textBelow ? 1 : -1) * lineSize * 5 / 4, HorizontalAlignment.CENTER, textBelow ? VerticalAlignment.TOP : VerticalAlignment.BOTTOM, currentStation ? 0xff00acd0 : 0, passed ? ARGB_LIGHT_GRAY : currentStation ? ARGB_WHITE : ARGB_BLACK, false);
                    }
                }));

                return nativeImage;
            }
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateRouteSquare(int color, String routeName, HorizontalAlignment horizontalAlignment) {
        try {
            final int padding = scale / 32;
            final DynamicTextureCache.Text text = DynamicTextureCache.instance.getText(routeName, Integer.MAX_VALUE, (int) ((fontSizeBig + fontSizeSmall) * LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, padding, horizontalAlignment);

            final int width = text.renderWidth() + padding * 2;
            final int height = text.height() + padding * 2;
            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(ARGB_BLACK | color));
            DrawingHelper.drawString(nativeImage, text, width / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateExitSignLetter(String exitLetter, String exitNumber, int backgroundColor) {
        try {
            final int size = scale / 2;
            final boolean noNumber = exitNumber.isEmpty();
            final int textSize = size * 7 / 8;
            final DynamicTextureCache.Text letter = DynamicTextureCache.instance.getText(exitLetter, noNumber ? textSize : textSize * 2 / 3, textSize, textSize, size, size, HorizontalAlignment.CENTER);
            final DynamicTextureCache.Text number = noNumber ? null : DynamicTextureCache.instance.getText(exitNumber, textSize / 3, textSize, textSize / 2, textSize / 2, size, HorizontalAlignment.CENTER);

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, size, size, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, size, size, backgroundColor);
            DrawingHelper.drawResource(nativeImage, EXIT_RESOURCE, 0, 0, size, size, false, 0, 1, 0, true);
            DrawingHelper.drawString(nativeImage, letter, size / 2 - (noNumber ? 0 : textSize / 6 - size / 32), size / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
            if (!noNumber) {
                DrawingHelper.drawString(nativeImage, number, size / 2 + textSize / 3 - size / 32, size / 2 + textSize / 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
            }
            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateExitSignLetterTianjin(String exitLetter, String exitNumber, int backgroundColor, int textColor, boolean forceMTRFont) {
        try {
            final int size = scale / 2;
            final boolean noNumber = exitNumber.isEmpty();

            final DynamicTextureCache.Text letter = DynamicTextureCache.instance.getText(exitLetter, noNumber ? size : size * 2 / 3, size, size, size, size, HorizontalAlignment.CENTER, forceMTRFont);
            final DynamicTextureCache.Text number = noNumber ? null : DynamicTextureCache.instance.getText(exitNumber, size / 3, size, size / 2, size / 2, size, HorizontalAlignment.CENTER, forceMTRFont);

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, size, size, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, size, size, 0);
            DrawingHelper.drawString(nativeImage, letter, size / 2 - (noNumber ? 0 : size / 6 - size / 32), size / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, textColor, false);
            if (!noNumber) {
                DrawingHelper.drawString(nativeImage, number, size / 2 + size / 3 - size / 32, size / 2 + size / 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, textColor, false);
            }
            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateBoundFor(long platformId, HorizontalAlignment horizontalAlignment, float aspectRatio, float paddingScale, int backgroundColor, int textColor, boolean forceMTRFont) {
        try {
            final int height = scale;
            final int width = (int) (height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;

            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            //final List<Pair<DynamicTextureCache.Text, Integer>> routes = new ArrayList<>();
            final IntArrayList allRoutes = getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                    case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                    case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                    default -> "";
                };

                destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
            });
            //allRoutes.forEach(
            //        route -> {
            //            final DynamicTextureCache.Text name = DynamicTextureCache.instance.getText(route.name, Integer.MAX_VALUE, (int) ((fontSizeBig + fontSizeSmall) * mtr.client.DynamicTextureCache.LINE_HEIGHT_MULTIPLIER * 1.25F), (int) (fontSizeBig * 1.25F), (int) (fontSizeSmall * 1.25F), padding, horizontalAlignment, forceMTRFont);
            //            routes.add(new Pair<>(name, route.color));
            //            width.addAndGet(routeSquarePadding * 5 + name.width());
            //        }
            //);
            final boolean isTerminating = destinations.isEmpty();
            final DynamicTextureCache.Text boundFor;
            if (isTerminating) {
                boundFor = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus"), width - padding * 2, height, tileSize * 3 / 5, tileSize * 3 / 10, padding, horizontalAlignment, forceMTRFont);
            } else {
                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else {
                        destinationString = IGuiExtension.insertTranslation("gui.tjmetro.bound_for_cjk", "gui.tjmetro.bound_for", 1, destinationString);
                    }
                }
                boundFor = DynamicTextureCache.instance.getText(destinationString, width - padding * 2, height, tileSize * 3 / 5, tileSize * 3 / 10, padding, horizontalAlignment, forceMTRFont);
            }

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(backgroundColor));

            //routes.forEach(route -> {
            //    DrawingHelper.fillRectSafe(nativeImage, currentX.get(), 0, route.left().width(), route.left().height(), invertColor(ARGB_BLACK | route.right()));
            //    drawString(nativeImage, route.left(), currentX.get(), height / 2, horizontalAlignment, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
            //    currentX.addAndGet(routeSquarePadding * 3 + route.left().width());
            //});
            DrawingHelper.drawString(nativeImage, boundFor, horizontalAlignment == HorizontalAlignment.LEFT ? 0 : width, height / 2, horizontalAlignment, VerticalAlignment.CENTER, backgroundColor, textColor, false);
            DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(backgroundColor));

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateTrainTo(long platformId, HorizontalAlignment horizontalAlignment, float aspectRatio, float paddingScale, int backgroundColor, int textColor, boolean forceMTRFont) {
        try {
            final int height = scale;
            final int width = (int) (height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;
            final boolean leftToRight = horizontalAlignment == HorizontalAlignment.LEFT;

            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                    case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                    case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                    default -> "";
                };

                destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
            });
            final boolean isTerminating = destinations.isEmpty();
            final DynamicTextureCache.Text trainTo;
            if (isTerminating) {
                trainTo = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus"), width - padding * 2, height, tileSize * 3 / 5, tileSize * 3 / 10, padding, horizontalAlignment, forceMTRFont);
            } else {
                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else {
                        destinationString = IGuiExtension.insertTranslation("gui.tjmetro.train_to_cjk", "gui.tjmetro.train_to", 1, destinationString);
                    }
                }
                trainTo = DynamicTextureCache.instance.getText(destinationString, width - padding * 2 - tileSize, height, tileSize * 3 / 5, tileSize * 3 / 10, padding, horizontalAlignment, forceMTRFont);
            }

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(backgroundColor));

            DrawingHelper.drawResource(nativeImage, SUBWAY_LOGO_RESOURCE, leftToRight ? 0 : width - tileSize, padding, tileSize, tileSize, false, 0.0F, 1.0F, textColor, false);
            DrawingHelper.drawString(nativeImage, trainTo, leftToRight ? tileSize : width - tileSize, height / 2, horizontalAlignment, VerticalAlignment.CENTER, backgroundColor, textColor, false);
            DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(backgroundColor));

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateCrossLineTrainTo(long platformId, HorizontalAlignment horizontalAlignment, float aspectRatio, float paddingScale, int backgroundColor, int textColor, boolean forceMTRFont) {
        try {
            final int height = scale;
            final int width = (int) (height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;
            final boolean leftToRight = horizontalAlignment == HorizontalAlignment.LEFT;

            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                    case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                    case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                    default -> "";
                };

                destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
            });
            final boolean isTerminating = destinations.isEmpty();
            final DynamicTextureCache.Text trainTo;
            if (isTerminating) {
                trainTo = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus"), width - padding * 2, height, tileSize * 3 / 5, tileSize * 3 / 10, padding, horizontalAlignment, forceMTRFont);
            } else {
                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else {
                        destinationString = IGuiExtension.insertTranslation("gui.tjmetro.cross_line_train_to_cjk", "gui.tjmetro.cross_line_train_to", 1, destinationString);
                    }
                }
                trainTo = DynamicTextureCache.instance.getText(destinationString, width - padding * 2 - tileSize, height, tileSize * 3 / 5, tileSize * 3 / 10, padding, horizontalAlignment, forceMTRFont);
            }

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(backgroundColor));

            DrawingHelper.drawResource(nativeImage, SUBWAY_LOGO_RESOURCE, leftToRight ? 0 : width - tileSize, padding, tileSize, tileSize, false, 0.0F, 1.0F, textColor, false);
            DrawingHelper.drawString(nativeImage, trainTo, leftToRight ? tileSize : width - tileSize, height / 2, horizontalAlignment, VerticalAlignment.CENTER, backgroundColor, textColor, false);
            DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(backgroundColor));

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateSignText(String string, HorizontalAlignment horizontalAlignment, float paddingScale, int backgroundColor, int textColor) {
        try {
            final int height = scale;
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;
            final int tilePadding = tileSize / 4;

            final DynamicTextureCache.Text text = DynamicTextureCache.instance.getText(string, Integer.MAX_VALUE, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize * 3 / 5, tileSize * 3 / 10, tilePadding, horizontalAlignment);
            final int width = text.renderWidth() - tilePadding * 2;

            if (width <= 0 || height <= 0) return null;

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, 0);
            DrawingHelper.drawString(nativeImage, text, width / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, backgroundColor, textColor, false);
            DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(backgroundColor));

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateStationNameEntrance(long stationId, long selectedId, int style, String stationName, BlockStationNameEntranceTianjin.Type type, float aspectRatio) {
        if (aspectRatio <= 0) return null;

        try {
            final int size = scale * 2;
            int width = Math.round(size * aspectRatio);
            final int padding = scale / 16;
            final DynamicTextureCache.Text text = DynamicTextureCache.instance.getText(stationName, width - size - padding, size - padding * 2, fontSizeBig * 3, fontSizeSmall * 3, padding, HorizontalAlignment.CENTER);
            final DynamicTextureCache.Text exit = DynamicTextureCache.instance.getText(EditStationScreen.deserializeExit(selectedId), width - size - padding, size - padding * 2, fontSizeBig * 3, fontSizeBig * 7, padding, HorizontalAlignment.CENTER);
            final int iconOffset = (int) (size * (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2);
            final int iconSize = (int) (size * BlockRailwaySign.SMALL_SIGN_PERCENTAGE);
            final int backgroundColor = 0;

            final AtomicInteger totalWidth = new AtomicInteger(iconOffset + iconSize + text.width());
            final List<Pair<DynamicTextureCache.Text, Integer>> routes = new ArrayList<>();
            switch (style) {
                case 0:
                case 1:
                case 4:
                case 5: {
                    final Station station = MinecraftClientData.getInstance().stationIdMap.get(stationId);
                    final ObjectArraySet<Station> connectingStationsIncludingThisOne = new ObjectArraySet<>(station.connectedStations);
                    connectingStationsIncludingThisOne.add(station);

                    final LongAVLTreeSet platformIds = new LongAVLTreeSet();
                    connectingStationsIncludingThisOne.forEach(connectingStation -> connectingStation.savedRails.forEach(platform -> platformIds.add(platform.getId())));

                    final Set<Integer> addedColors = new HashSet<>();
                    MinecraftClientData.getInstance().simplifiedRoutes.forEach(simplifiedRoute -> {
                        final int color = simplifiedRoute.getColor();
                        if (!addedColors.contains(color) && simplifiedRoute.getPlatforms().stream().anyMatch(simplifiedRoutePlatform -> platformIds.contains(simplifiedRoutePlatform.getPlatformId()))) {
                            DashboardListItem route = new DashboardListItem(color, simplifiedRoute.getName().split("\\|\\|")[0], color);
                            if (!route.getName(false).isEmpty()) {
                                final DynamicTextureCache.Text routeName = DynamicTextureCache.instance.getText(route.getName(false), Integer.MAX_VALUE, iconSize, (int) (fontSizeBig * 2.5F), (int) (fontSizeSmall * 2.5F), padding, HorizontalAlignment.LEFT);
                                routes.add(new ObjectIntImmutablePair<>(routeName, route.getColor(false)));
                                totalWidth.addAndGet(padding * 5 + routeName.width());
                                addedColors.add(color);
                            }
                        }
                    });
                    totalWidth.addAndGet(iconOffset);
                    totalWidth.addAndGet(padding * (routes.isEmpty() ? -5 : -2));
                }
            }
            if (selectedId != -1) {
                totalWidth.addAndGet(iconOffset + exit.width());
            }

            width = Math.max(width, totalWidth.get());
            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, size, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, size, backgroundColor);

            final AtomicInteger currentX = new AtomicInteger(iconOffset + iconSize);
            if (!routes.isEmpty()) currentX.addAndGet(iconOffset);
            routes.forEach(route -> {
                DrawingHelper.fillRectSafe(nativeImage, currentX.get(), iconOffset, padding * 3 + route.left().width(), iconSize, DrawingHelper.invertColor(ARGB_BLACK | route.right()));
                DrawingHelper.drawString(nativeImage, route.left(), currentX.get() + padding, size / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, backgroundColor, ARGB_WHITE, false);
                currentX.addAndGet(padding * 5 + route.left().width());
            });
            if (!routes.isEmpty()) currentX.addAndGet(padding * -3);
            if (selectedId != -1) currentX.addAndGet(-iconOffset - exit.width());
            final Identifier resource = switch (type) {
                case TRT -> TRAIN_LOGO_RESOURCE;
                case BMT -> TRAIN_BMT_LOGO_RESOURCE;
                case JINJING -> TRAIN_JINJING_LOGO_RESOURCE;
            };
            DrawingHelper.drawResource(nativeImage, resource, iconOffset, iconOffset, iconSize, iconSize, false, 0, 1, 0, true);
            DrawingHelper.drawString(nativeImage, text, (width + currentX.get()) / 2, size / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, backgroundColor, ARGB_WHITE, false);
            if (selectedId != -1)
                DrawingHelper.drawString(nativeImage, exit, width - iconOffset, size / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, backgroundColor, ARGB_WHITE, false);
            DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(backgroundColor));

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateStationNamePlate(long platformId, int arrowDirection, int backgroundColor, float paddingScale, float aspectRatio, int textColor, int transparentColor) {
        if (aspectRatio <= 0) return null;

        try {
            if (platformId == 0) return null;
            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            ObjectArrayList<String> nextStations = new ObjectArrayList<>();
            getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                    case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                    case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                    default -> "";
                };

                if (!simplifiedRoute.getName().isEmpty()) {
                    destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
                    nextStations.add(simplifiedRoute.getPlatforms().get(currentStationIndex + 1).getStationName());
                }
            });
            final boolean isTerminating = destinations.isEmpty();

            final int height = scale;
            final int width = Math.round(height * aspectRatio);
            final int padding = Math.round(height * paddingScale);
            final int tileSize = height - padding * 2;

            if (width <= 0 || height <= 0) return null;

            final NativeImage nativeImage;

            final int tilePadding = tileSize / 4;

            if (isTerminating) {
                final DynamicTextureCache.Text destination = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus_bmt"), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize, tileSize / 2, tilePadding, HorizontalAlignment.CENTER);
                final DynamicTextureCache.Text stationName = DynamicTextureCache.instance.getText(getStationName(platformId), width, height - tilePadding * 2, tileSize * 4 / 3, tileSize * 2 / 3, tilePadding, HorizontalAlignment.CENTER);
                final int imageWidth = Math.max(width, destination.width() + destination.renderWidth() + stationName.width());
                nativeImage = new NativeImage(NativeImageFormat.RGBA, imageWidth, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, imageWidth, height, DrawingHelper.invertColor(backgroundColor));
                DrawingHelper.drawString(nativeImage, destination, tilePadding, height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, textColor, false);
                DrawingHelper.drawString(nativeImage, stationName, imageWidth / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, textColor, false);
                DrawingHelper.drawString(nativeImage, destination, imageWidth - tilePadding, height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, textColor, false);
            } else {
                final int arrowSizeAndPadding = switch (arrowDirection) {
                    case 0, 3 -> -tilePadding;
                    default -> tileSize;
                };
                String nextStationString = IGui.mergeStations(nextStations);
                String destinationString = IGui.mergeStations(destinations);
                final boolean isClockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_CLOCKWISE);
                final boolean isAnticlockwise = destinationString.startsWith(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE);
                destinationString = destinationString.replace(TEMP_CIRCULAR_MARKER_CLOCKWISE, "").replace(TEMP_CIRCULAR_MARKER_ANTICLOCKWISE, "");
                if (!destinationString.isEmpty()) {
                    if (isClockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.clockwise_via_cjk", "gui.mtr.clockwise_via", 1, destinationString);
                    } else if (isAnticlockwise) {
                        destinationString = IGuiExtension.insertTranslation("gui.mtr.anticlockwise_via_cjk", "gui.mtr.anticlockwise_via", 1, destinationString);
                    } else {
                        destinationString = IGuiExtension.insertTranslation("gui.tjmetro.bound_for_bmt_cjk", "gui.tjmetro.bound_for", 1, destinationString);
                    }
                }

                final DynamicTextureCache.Text textDestination = DynamicTextureCache.instance.getText(destinationString, width - tilePadding - padding * 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize, tileSize / 2, tilePadding, (arrowDirection == 2) ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
                final DynamicTextureCache.Text textNextStations = DynamicTextureCache.instance.getText(IGuiExtension.insertTranslation("gui.tjmetro.next_station_bmt_cjk", "gui.tjmetro.next_station_bmt", 1, nextStationString), width - tilePadding - padding * 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig, fontSizeSmall, tilePadding, HorizontalAlignment.CENTER);
                final DynamicTextureCache.Text textStationName = DynamicTextureCache.instance.getText(getStationName(platformId), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), tileSize, tileSize / 2, tilePadding, HorizontalAlignment.CENTER);
                int imageWidth = Math.max(width, tilePadding + arrowSizeAndPadding + textDestination.width() + textStationName.width() + textNextStations.width() + tilePadding);
                final boolean renderNextStation = (width - tilePadding - arrowSizeAndPadding - textDestination.width() - tilePadding - textStationName.width()) > textNextStations.width() * 2 / 3;
                if (!renderNextStation) {
                    imageWidth = Math.max(width, imageWidth - textNextStations.width());
                }
                nativeImage = new NativeImage(NativeImageFormat.RGBA, imageWidth, height, false);
                DrawingHelper.fillRectSafe(nativeImage, 0, 0, imageWidth, height, DrawingHelper.invertColor(backgroundColor));
                if (arrowDirection == 2) {
                    DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, imageWidth - tilePadding - tileSize, padding, tileSize, tileSize, true, 0, 1, textColor, false);
                    DrawingHelper.drawString(nativeImage, textDestination, imageWidth - tilePadding - tileSize, height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.drawString(nativeImage, textStationName, (textStationName.width() - textStationName.renderWidth()) / 2 + tilePadding, height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, textColor, false);
                    if (renderNextStation) {
                        DrawingHelper.drawString(nativeImage, textNextStations, (imageWidth + textStationName.width() - textDestination.width() - arrowSizeAndPadding) / 2, height, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, 0xffdcdcdc, false);
                    }
                } else {
                    if (arrowSizeAndPadding > 0) {
                        DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, tilePadding, padding, tileSize, tileSize, false, 0, 1, textColor, false);
                    }
                    DrawingHelper.drawString(nativeImage, textDestination, tilePadding + arrowSizeAndPadding, height / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, 0, textColor, false);
                    DrawingHelper.drawString(nativeImage, textStationName, imageWidth - (textStationName.width() - textStationName.renderWidth()) / 2 - tilePadding, height / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, 0, textColor, false);
                    if (renderNextStation) {
                        DrawingHelper.drawString(nativeImage, textNextStations, (imageWidth + arrowSizeAndPadding + textDestination.width() - textStationName.width()) / 2, height, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, 0xffdcdcdc, false);
                    }
                }
            }

            if (transparentColor != 0) {
                DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(transparentColor));
            }

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateStationNameSignTianjin(long platformId, float aspectRatio, int backgroundColor) {
        if (aspectRatio <= 0) return null;

        try {
            if (platformId == 0) return null;
            ObjectArrayList<String> destinations = new ObjectArrayList<>();
            ObjectArrayList<String> nextStations = new ObjectArrayList<>();
            IntArrayList routeColors = getRouteStream(platformId, (simplifiedRoute, currentStationIndex) -> {
                final String tempMarker = switch (simplifiedRoute.getCircularState()) {
                    case CLOCKWISE -> TEMP_CIRCULAR_MARKER_CLOCKWISE;
                    case ANTICLOCKWISE -> TEMP_CIRCULAR_MARKER_ANTICLOCKWISE;
                    default -> "";
                };

                if (!simplifiedRoute.getName().isEmpty()) {
                    destinations.add(tempMarker + simplifiedRoute.getPlatforms().get(currentStationIndex).getDestination());
                    nextStations.add(simplifiedRoute.getPlatforms().get(currentStationIndex + 1).getStationName());
                }
            });
            final boolean isTerminating = destinations.isEmpty();

            final int height = scale;
            final int width = Math.round(height * aspectRatio);
            final int padding = Math.round(height * 0.05F);
            final int tileSize = height - padding * 2;

            if (width <= 0 || height <= 0) return null;

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, DrawingHelper.invertColor(backgroundColor));

            final int tilePadding = tileSize / 4;

            final DynamicTextureCache.Text stationName = DynamicTextureCache.instance.getText(getStationName(platformId), width - padding * 2, height - tilePadding * 2, fontSizeBig, fontSizeSmall, 0, HorizontalAlignment.CENTER);
            final DynamicTextureCache.Text destinationOrNextStation;
            if (isTerminating) {
                destinationOrNextStation = DynamicTextureCache.instance.getText(IGuiExtension.mergeTranslation("gui.tjmetro.terminus_cjk", "gui.tjmetro.terminus"), width, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig / 2, fontSizeSmall / 2, 0, HorizontalAlignment.CENTER);
            } else {
                String nextStationString = IGui.mergeStations(nextStations);
                destinationOrNextStation = DynamicTextureCache.instance.getText(IGuiExtension.insertTranslation("gui.tjmetro.next_station_format_cjk", "gui.tjmetro.next_station_format", 1, nextStationString), width - padding * 2, (int) (tileSize * LINE_HEIGHT_MULTIPLIER), fontSizeBig / 2, fontSizeSmall / 2, 0, HorizontalAlignment.CENTER);
            }

            DrawingHelper.drawString(nativeImage, stationName, width / 2, (height - destinationOrNextStation.height() - padding * 3) / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_BLACK, false);
            if (!routeColors.isEmpty()) {
                final int colorStripY = height - destinationOrNextStation.height() - padding * 3;
                final int heightPerColor = padding / routeColors.size();
                for (int i = 0; i < routeColors.size(); i++) {
                    DrawingHelper.fillRectSafe(nativeImage, 0, colorStripY + heightPerColor * i, width, heightPerColor, DrawingHelper.invertColor(ARGB_BLACK | routeColors.getInt(i)));
                }
            }
            DrawingHelper.drawString(nativeImage, destinationOrNextStation, width / 2, height - padding, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, 0, ARGB_BLACK, false);

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateStationNavigator(LongAVLTreeSet selectedRoutes, boolean arrowLeft, int backgroundColor, float aspectRatio) {
        if (aspectRatio <= 0) return null;

        try {
            final int size = scale * 2;
            int width = Math.round(size * aspectRatio);
            final int padding = scale / 16;
            final int iconOffset = (int) (size * (1 - BlockRailwaySign.SMALL_SIGN_PERCENTAGE) / 2);
            final int iconSize = (int) (size * BlockRailwaySign.SMALL_SIGN_PERCENTAGE);

            final AtomicInteger totalWidth = new AtomicInteger((iconOffset + iconSize) * 2);
            final List<Pair<DynamicTextureCache.Text, Integer>> routes = new ArrayList<>();
            totalWidth.addAndGet(iconOffset);
            selectedRoutes.forEach(routeId -> {
                final SimplifiedRoute route = MinecraftClientData.getInstance().simplifiedRoutes.stream().filter(route1 -> route1.getId() == routeId).findFirst().get();
                final DynamicTextureCache.Text routeName = DynamicTextureCache.instance.getText(route.getName().split("\\|\\|")[0], Integer.MAX_VALUE, iconSize, (int) (fontSizeBig * 2.5F), (int) (fontSizeSmall * 2.5F), padding, arrowLeft ? HorizontalAlignment.LEFT : HorizontalAlignment.RIGHT);
                routes.add(new ObjectIntImmutablePair<>(routeName, route.getColor()));
                totalWidth.addAndGet(padding * 5 + routeName.width());
            });

            width = Math.max(width, totalWidth.get());
            final NativeImage nativeImage = new NativeImage(NativeImageFormat.RGBA, width, size, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, size, backgroundColor);

            if (arrowLeft) {
                final AtomicInteger currentX = new AtomicInteger((iconOffset + iconSize) * 2);
                DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, iconOffset, iconOffset, iconSize, iconSize, false, 0, 1, 0, true);
                DrawingHelper.drawResource(nativeImage, TRAIN_BORDERLESS_LOGO_RESOURCE, iconOffset + iconSize + iconOffset, iconOffset, iconSize, iconSize, false, 0, 1, 0, true);
                currentX.addAndGet(iconOffset);
                routes.forEach(route -> {
                    DrawingHelper.fillRectSafe(nativeImage, currentX.get(), iconOffset, padding * 3 + route.left().width(), iconSize, DrawingHelper.invertColor(ARGB_BLACK | route.right()));
                    DrawingHelper.drawString(nativeImage, route.left(), currentX.get() + padding, size / 2, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, route.right(), ARGB_WHITE, false);
                    currentX.addAndGet(padding * 5 + route.left().width());
                });
                if (!routes.isEmpty()) currentX.addAndGet(padding * -3);
            } else {
                final AtomicInteger currentX = new AtomicInteger(width - (iconOffset + iconSize) * 2);
                DrawingHelper.drawResource(nativeImage, ARROW_RESOURCE, width - iconOffset - iconSize, iconOffset, iconSize, iconSize, true, 0, 1, 0, true);
                DrawingHelper.drawResource(nativeImage, TRAIN_BORDERLESS_LOGO_RESOURCE, width - (iconOffset + iconSize) * 2, iconOffset, iconSize, iconSize, false, 0, 1, 0, true);
                currentX.addAndGet(-iconOffset);
                routes.forEach(route -> {
                    DrawingHelper.fillRectSafe(nativeImage, currentX.get() - padding * 3 - route.left().width(), iconOffset, padding * 3 + route.left().width(), iconSize, DrawingHelper.invertColor(ARGB_BLACK | route.right()));
                    DrawingHelper.drawString(nativeImage, route.left(), currentX.get() - padding * 3 / 2, size / 2, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, route.right(), ARGB_WHITE, false);
                    currentX.addAndGet(-(padding * 5 + route.left().width()));
                });
                if (!routes.isEmpty()) currentX.addAndGet(padding * 3);
            }
            DrawingHelper.clearColor(nativeImage, DrawingHelper.invertColor(backgroundColor));

            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generateStationNameProjector(String stationName, float aspectRatio) {
        if (aspectRatio <= 0) return null;

        try {
            final int height = scale * 2;
            final int width = Math.round(height * aspectRatio);
            final int padding = scale / 16;
            final DynamicTextureCache.Text text = DynamicTextureCache.instance.getText(stationName, width - padding * 2, height - padding * 2, fontSizeBig * 2, fontSizeSmall * 2, padding, HorizontalAlignment.CENTER);

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.getAbgrMapped(), width, height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, width, height, 0);
            DrawingHelper.drawString(nativeImage, text, width / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, 0, ARGB_WHITE, false);
            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    public static NativeImage generatePlainText(String text, int backgroundColor, int textColor) {
        try {
            final int height = scale;
            final int padding = scale / 16;
            final DynamicTextureCache.Text instanceText = DynamicTextureCache.instance.getText(text, Integer.MAX_VALUE, height, fontSizeBig, fontSizeBig, padding, null);

            final NativeImage nativeImage = new NativeImage(NativeImageFormat.getAbgrMapped(), instanceText.renderWidth(), height, false);
            DrawingHelper.fillRectSafe(nativeImage, 0, 0, instanceText.renderWidth(), height, 0);
            DrawingHelper.drawString(nativeImage, instanceText, instanceText.renderWidth() / 2, height / 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER, backgroundColor, textColor, false);
            return nativeImage;
        } catch (Exception e) {
            TianjinMetro.LOGGER.error(e.getMessage(), e);
        }

        return null;
    }

    protected static void setup(ObjectArrayList<Int2ObjectAVLTreeMap<StationPosition>> stationPositions, ObjectArrayList<LongArrayList> stationsIdLists, int[] colorIndices, float[] bounds, boolean passed, boolean reverse) {
        final int passedMultiplier = passed ? -1 : 1;
        final int reverseMultiplier = reverse ? -1 : 1;
        bounds[0] = 0;

        final LongArrayList commonStationIds = new LongArrayList();
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
            final long commonStationId = lastStation ? -1 : commonStationIds.getLong(commonStationIndex);

            int intermediateSegmentsMaxCount = 0;
            final int[] intermediateSegmentsCounts = new int[routeCount];
            for (int routeIndex = 0; routeIndex < routeCount; routeIndex++) {
                intermediateSegmentsCounts[routeIndex] = (lastStation ? stationsIdLists.get(routeIndex).size() : stationsIdLists.get(routeIndex).indexOf(commonStationId) + 1) - traverseIndex[routeIndex];
                intermediateSegmentsMaxCount = Math.max(intermediateSegmentsMaxCount, intermediateSegmentsCounts[routeIndex]);
            }

            final IntArrayList routesIndicesInSection = new IntArrayList();
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

    protected static String getStationName(long platformId) {
        final Platform platform = MinecraftClientData.getInstance().platformIdMap.get(platformId);
        final Station station = platform == null ? null : platform.area;
        return station == null ? "" : station.getName();
    }

    protected static IntArrayList getRouteStream(long platformId, BiConsumer<SimplifiedRoute, Integer> nonTerminatingCallback) {
        final IntArrayList colors = new IntArrayList();
        final IntArrayList terminatingColors = new IntArrayList();
        MinecraftClientData.getInstance().simplifiedRoutes.stream().filter(simplifiedRoute -> simplifiedRoute.getPlatformIndex(platformId) >= 0).sorted().forEach(simplifiedRoute -> {
            final int currentStationIndex = simplifiedRoute.getPlatformIndex(platformId);
            if (currentStationIndex < simplifiedRoute.getPlatforms().size() - 1) {
                nonTerminatingCallback.accept(simplifiedRoute, currentStationIndex);
                if (!colors.contains(simplifiedRoute.getColor())) {
                    colors.add(simplifiedRoute.getColor());
                }
            } else {
                if (!terminatingColors.contains(simplifiedRoute.getColor())) {
                    terminatingColors.add(simplifiedRoute.getColor());
                }
            }
        });
        if (colors.isEmpty()) {
            colors.addAll(terminatingColors);
        }
        return colors;
    }

    protected record StationPosition(float x, float y, boolean isCommon)
    {
    }

    protected record StationPositionGrouped(StationPosition stationPosition, int stationOffset,
                                            IntArrayList interchangeColors, ObjectArrayList<String> interchangeNames)
    {
    }
}
