package ziyue.tjmetro.mod.client;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MathHelper;
import org.mtr.mapping.holder.NativeImage;
import org.mtr.mapping.holder.NativeImageFormat;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.data.IGui;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.data.IGuiExtension;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * @author ZiYueCommentary
 * @since 1.0.2
 */

public class DrawingHelper
{
    protected static void drawLine(NativeImage nativeImage, RouteMapGenerator.StationPosition stationPosition1, RouteMapGenerator.StationPosition stationPosition2, float widthScale, float heightScale, float xOffset, float yOffset, int color) {
        final int x1 = Math.round((stationPosition1.x() + xOffset) * RouteMapGenerator.scale * widthScale);
        final int x2 = Math.round((stationPosition2.x() + xOffset) * RouteMapGenerator.scale * widthScale);
        final int y1 = Math.round((stationPosition1.y() + yOffset) * RouteMapGenerator.scale * heightScale);
        final int y2 = Math.round((stationPosition2.y() + yOffset) * RouteMapGenerator.scale * heightScale);
        final int xChange = x2 - x1;
        final int yChange = y2 - y1;
        final int xChangeAbs = Math.abs(xChange);
        final int yChangeAbs = Math.abs(yChange);
        final int changeDifference = Math.abs(yChangeAbs - xChangeAbs);

        if (xChangeAbs > yChangeAbs) {
            final boolean y1OffsetGreater = Math.abs(y1 - yOffset * RouteMapGenerator.scale) > Math.abs(y2 - yOffset * RouteMapGenerator.scale);
            drawLine(nativeImage, x1, y1, x2 - x1, y1OffsetGreater ? 0 : y2 - y1, y1OffsetGreater ? changeDifference : yChangeAbs, color);
            drawLine(nativeImage, x2, y2, x1 - x2, y1OffsetGreater ? y1 - y2 : 0, y1OffsetGreater ? yChangeAbs : changeDifference, color);
        } else {
            final int halfXChangeAbs = xChangeAbs / 2;
            drawLine(nativeImage, x1, y1, x2 - x1, y2 - y1, halfXChangeAbs, color);
            drawLine(nativeImage, x2, y2, x1 - x2, y1 - y2, halfXChangeAbs, color);
            drawLine(nativeImage, (x1 + x2) / 2, y1 + (int) Math.copySign(halfXChangeAbs, y2 - y1), 0, y2 - y1, changeDifference, color);
        }
    }

    public static void drawLine(NativeImage nativeImage, int x, int y, int directionX, int directionY, int length, int color) {
        final int halfLineHeight = RouteMapGenerator.lineSize / 2;
        final int xWidth = directionX == 0 ? halfLineHeight : 0;
        final int yWidth = directionX == 0 ? 0 : directionY == 0 ? halfLineHeight : Math.round(RouteMapGenerator.lineSize * MathHelper.getSquareRootOfTwoMapped() / 2);
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

    public static void drawResource(NativeImage nativeImage, Identifier resource, int x, int y, int width, int height, boolean flipX, float v1, float v2, int color, boolean useActualColor) {
        ResourceManagerHelper.readResource(resource, inputStream -> {
            try {
                final NativeImage nativeImageResource = NativeImage.read(NativeImageFormat.getAbgrMapped(), inputStream);
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
                        final int pixel1 = nativeImageResource.getColor(MathHelper.clamp(floorX, 0, resourceWidth - 1), MathHelper.clamp(floorY, 0, resourceHeight - 1));
                        final int pixel2 = nativeImageResource.getColor(MathHelper.clamp(ceilX, 0, resourceWidth - 1), MathHelper.clamp(floorY, 0, resourceHeight - 1));
                        final int pixel3 = nativeImageResource.getColor(MathHelper.clamp(floorX, 0, resourceWidth - 1), MathHelper.clamp(ceilY, 0, resourceHeight - 1));
                        final int pixel4 = nativeImageResource.getColor(MathHelper.clamp(ceilX, 0, resourceWidth - 1), MathHelper.clamp(ceilY, 0, resourceHeight - 1));
                        final int newColor;
                        if (useActualColor) {
                            newColor = invertColor(pixel1);
                        } else {
                            final float luminance1 = ((pixel1 >> 24) & 0xFF) * percentX1 * percentY1;
                            final float luminance2 = ((pixel2 >> 24) & 0xFF) * percentX2 * percentY1;
                            final float luminance3 = ((pixel3 >> 24) & 0xFF) * percentX1 * percentY2;
                            final float luminance4 = ((pixel4 >> 24) & 0xFF) * percentX2 * percentY2;
                            newColor = (color & IGui.RGB_WHITE) + ((int) (luminance1 + luminance2 + luminance3 + luminance4) << 24);
                        }
                        blendPixel(nativeImage, (flipX ? width - drawX - 1 : drawX) + x, drawY + y, newColor);
                    }
                }
            } catch (Exception e) {
                TianjinMetro.LOGGER.error("", e);
            }
        });
    }

    public static void drawStation(NativeImage nativeImage, int x, int y, float heightScale, int lines, boolean passed) {
        for (int offsetX = -RouteMapGenerator.lineSize; offsetX < RouteMapGenerator.lineSize; offsetX++) {
            for (int offsetY = -RouteMapGenerator.lineSize; offsetY < RouteMapGenerator.lineSize; offsetY++) {
                final int extraOffsetY = offsetY > 0 ? (int) (lines * RouteMapGenerator.lineSpacing * heightScale) : 0;
                final int repeatDraw = offsetY == 0 ? (int) (lines * RouteMapGenerator.lineSpacing * heightScale) : 0;
                final double squareSum = (offsetX + 0.5) * (offsetX + 0.5) + (offsetY + 0.5) * (offsetY + 0.5);

                if (squareSum <= 0.5 * RouteMapGenerator.lineSize * RouteMapGenerator.lineSize) {
                    for (int i = 0; i <= repeatDraw; i++) {
                        drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, IGui.ARGB_WHITE);
                    }
                } else if (squareSum <= RouteMapGenerator.lineSize * RouteMapGenerator.lineSize) {
                    for (int i = 0; i <= repeatDraw; i++) {
                        drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, passed ? IGui.ARGB_LIGHT_GRAY : IGui.ARGB_BLACK);
                    }
                }
            }
        }
    }

    public static void drawStationRouteMapBMT(NativeImage nativeImage, int x, int y, float heightScale, int lines, boolean passed, boolean current) {
        for (int offsetX = -RouteMapGenerator.lineSize; offsetX < RouteMapGenerator.lineSize; offsetX++) {
            for (int offsetY = -RouteMapGenerator.lineSize; offsetY < RouteMapGenerator.lineSize; offsetY++) {
                final int extraOffsetY = offsetY > 0 ? (int) (lines * RouteMapGenerator.lineSpacing * heightScale) : 0;
                final int repeatDraw = offsetY == 0 ? (int) (lines * RouteMapGenerator.lineSpacing * heightScale) : 0;
                final double squareSum = (offsetX + 0.5) * (offsetX + 0.5) + (offsetY + 0.5) * (offsetY + 0.5);

                if (squareSum <= 0.5 * RouteMapGenerator.lineSize * RouteMapGenerator.lineSize) {
                    for (int i = 0; i <= repeatDraw; i++) {
                        drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, passed ? IGui.ARGB_LIGHT_GRAY : (current ? 0xff00acd0 : IGui.ARGB_WHITE));
                    }
                } else if (squareSum <= RouteMapGenerator.lineSize * RouteMapGenerator.lineSize) {
                    for (int i = 0; i <= repeatDraw; i++) {
                        drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, 0xff00379c);
                    }
                }
            }
        }
    }

    public static void drawStationBMT(NativeImage nativeImage, int x, int y, float heightScale, int lines, boolean current) {
        for (int offsetX = -RouteMapGenerator.lineSize; offsetX < RouteMapGenerator.lineSize; offsetX++) {
            for (int offsetY = -RouteMapGenerator.lineSize; offsetY < RouteMapGenerator.lineSize; offsetY++) {
                final int extraOffsetY = offsetY > 0 ? (int) (lines * RouteMapGenerator.lineSpacing * heightScale) : 0;
                final int repeatDraw = offsetY == 0 ? (int) (lines * RouteMapGenerator.lineSpacing * heightScale) : 0;
                final double squareSum = (offsetX + 0.5) * (offsetX + 0.5) + (offsetY + 0.5) * (offsetY + 0.5);

                if (squareSum <= 0.7 * RouteMapGenerator.lineSize * RouteMapGenerator.lineSize) {
                    for (int i = 0; i <= repeatDraw; i++) {
                        drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, current ? 0xffff0000 : IGui.ARGB_BLACK);
                    }
                } else if (squareSum <= RouteMapGenerator.lineSize * RouteMapGenerator.lineSize) {
                    for (int i = 0; i <= repeatDraw; i++) {
                        drawPixelSafe(nativeImage, x + offsetX, y + offsetY + extraOffsetY + i, IGui.ARGB_WHITE);
                    }
                }
            }
        }
    }

    public static void drawVerticalString(NativeImage nativeImage, String text, int x, int y, int maxWidth, int maxHeight, int fontSizeCjk, int fontSize, int padding, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, int backgroundColor, int textColor) {
        final ImmutablePair<String, String> pair = IGuiExtension.splitTranslation(text);
        DynamicTextureCache.Text textCJK;
        if (pair.left.isEmpty()) {
            textCJK = DynamicTextureCache.Text.empty();
            padding = 0;
        } else {
            textCJK = DynamicTextureCache.instance.getText(IGui.formatVerticalChinese(pair.left), maxWidth, Integer.MAX_VALUE, fontSizeCjk, fontSizeCjk, padding, IGui.HorizontalAlignment.LEFT, 1F, false);
            if (textCJK.height() > maxHeight) {
                try {
                    // compressing the height of cjk text...
                    final BufferedImage image = new BufferedImage(textCJK.renderWidth(), textCJK.height(), BufferedImage.TYPE_BYTE_GRAY);
                    final byte[] imageBuffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
                    System.arraycopy(textCJK.pixels(), 0, imageBuffer, 0, textCJK.pixels().length);
                    BufferedImage scaledImage = new BufferedImage(textCJK.renderWidth(), maxHeight, BufferedImage.TYPE_BYTE_GRAY);
                    Graphics2D graphics = scaledImage.createGraphics();
                    graphics.setColor(Color.WHITE);
                    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    graphics.drawImage(image, 0, 0, textCJK.renderWidth(), maxHeight, null);
                    final byte[] pixels = ((DataBufferByte) scaledImage.getRaster().getDataBuffer()).getData();
                    graphics.dispose();
                    scaledImage.flush();
                    image.flush();
                    textCJK = new DynamicTextureCache.Text(pixels, textCJK.width(), maxHeight, textCJK.renderWidth());
                } catch (Exception e) {
                    TianjinMetro.LOGGER.warn("Error when scaling vertical string. Skipping!", e);
                }
            }
        }
        final DynamicTextureCache.Text textNonCJK;
        if (pair.right.isEmpty()) {
            textNonCJK = DynamicTextureCache.Text.empty();
            padding = 0;
        } else {
            textNonCJK = DynamicTextureCache.instance.getText(pair.right, maxHeight, maxWidth, fontSize, fontSize, padding, IGui.HorizontalAlignment.LEFT, 1F, false);
        }
        final int width = textCJK.width() + textNonCJK.height() - padding * 2;
        switch (horizontalAlignment) {
            case LEFT:
                drawString(nativeImage, textCJK, x, y, horizontalAlignment, verticalAlignment, backgroundColor, textColor, false);
                drawString(nativeImage, textNonCJK, x + textCJK.width(), y, horizontalAlignment, verticalAlignment, backgroundColor, textColor, true);
                break;
            case CENTER:
                drawString(nativeImage, textCJK, x - (textNonCJK.height() - textCJK.width()) / 2 + padding, y, IGui.HorizontalAlignment.RIGHT, verticalAlignment, backgroundColor, textColor, false);
                drawString(nativeImage, textNonCJK, x - (textNonCJK.height() - textCJK.width()) / 2, y, IGui.HorizontalAlignment.LEFT, verticalAlignment, backgroundColor, textColor, true);
                break;
            case RIGHT:
                drawString(nativeImage, textCJK, x - width, y, IGui.HorizontalAlignment.LEFT, verticalAlignment, backgroundColor, textColor, false);
                drawString(nativeImage, textNonCJK, x - width + textCJK.width(), y, IGui.HorizontalAlignment.LEFT, verticalAlignment, backgroundColor, textColor, true);
        }
    }

    public static void drawString(NativeImage nativeImage, DynamicTextureCache.Text text, int x, int y, IGui.HorizontalAlignment horizontalAlignment, IGui.VerticalAlignment verticalAlignment, int backgroundColor, int textColor, boolean rotate90) {
        if (text == null || text.pixels() == null) return;

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
            blendPixel(nativeImage, (int) horizontalAlignment.getOffset(x + drawX, (rotate90 ? text.height() : text.renderWidth())), (int) verticalAlignment.getOffset(y + drawY, (rotate90 ? text.renderWidth() : text.height())), ((text.pixels()[i] & 0xFF) << 24) + (textColor & IGui.RGB_WHITE));
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

    public static void blendPixel(NativeImage nativeImage, int x, int y, int color) {
        if (Utilities.isBetween(x, 0, nativeImage.getWidth() - 1) && Utilities.isBetween(y, 0, nativeImage.getHeight() - 1)) {
            final float percent = (float) ((color >> 24) & 0xFF) / 0xFF;
            if (percent > 0) {
                final int existingPixel = nativeImage.getColor(x, y);
                final boolean existingTransparent = ((existingPixel >> 24) & 0xFF) == 0;
                final int r1 = existingTransparent ? 0xFF : (existingPixel & 0xFF);
                final int g1 = existingTransparent ? 0xFF : ((existingPixel >> 8) & 0xFF);
                final int b1 = existingTransparent ? 0xFF : ((existingPixel >> 16) & 0xFF);
                final int r2 = (color >> 16) & 0xFF;
                final int g2 = (color >> 8) & 0xFF;
                final int b2 = color & 0xFF;
                final float inversePercent = 1 - percent;
                final int finalColor = IGui.ARGB_BLACK | (((int) (r1 * inversePercent + r2 * percent) << 16) + ((int) (g1 * inversePercent + g2 * percent) << 8) + (int) (b1 * inversePercent + b2 * percent));
                drawPixelSafe(nativeImage, x, y, finalColor);
            }
        }
    }

    public static void fillRectSafe(NativeImage nativeImage, int x, int y, int width, int height, int color) {
        if (width <= 0 || height <= 0 || x >= nativeImage.getWidth() || y >= nativeImage.getHeight()) return;

        if (x < 0) {
            width += x;
            x = 0;
        }
        if (y < 0) {
            height += y;
            y = 0;
        }
        if (x + width > nativeImage.getWidth()) width = nativeImage.getWidth() - x;
        if (y + height > nativeImage.getHeight()) height = nativeImage.getHeight() - y;
        if (width <= 0 || height <= 0) return;

        nativeImage.fillRect(x, y, width, height, color);
    }

    public static void drawPixelSafe(NativeImage nativeImage, int x, int y, int color) {
        if (Utilities.isBetween(x, 0, nativeImage.getWidth() - 1) && Utilities.isBetween(y, 0, nativeImage.getHeight() - 1)) {
            nativeImage.setPixelColor(x, y, invertColor(color));
        }
    }

    public static int invertColor(int color) {
        return ((color & IGui.ARGB_BLACK) != 0 ? IGui.ARGB_BLACK : 0) + ((color & 0xFF) << 16) + (color & 0xFF00) + ((color & 0xFF0000) >> 16);
    }

    public static void clearColor(NativeImage nativeImage, int color) {
        for (int x = 0; x < nativeImage.getWidth(); x++) {
            for (int y = 0; y < nativeImage.getHeight(); y++) {
                if (nativeImage.getColor(x, y) == color) {
                    nativeImage.setPixelColor(x, y, 0);
                }
            }
        }
    }
}
