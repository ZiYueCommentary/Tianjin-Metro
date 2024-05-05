package ziyue.tjmetro.client;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mtr.MTR;
import mtr.client.Config;
import mtr.data.*;
import mtr.mappings.Utilities;
import mtr.client.ClientCache.PlatformRouteDetails;
import mtr.client.ClientCache.ColorNameTuple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Tuple;
import ziyue.tjmetro.Reference;
import ziyue.tjmetro.TianjinMetro;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.AttributedString;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static mtr.client.ClientCache.LINE_HEIGHT_MULTIPLIER;
import static mtr.client.ClientData.*;

/**
 * @author ZiYueCommentary
 * @see mtr.client.ClientCache
 * @since beta-1
 */

public class ClientCache extends DataCache implements IGui
{
    public static final ClientCache DATA_CACHE = new ClientCache(STATIONS, PLATFORMS, SIDINGS, ROUTES, DEPOTS);

    protected Font font;
    protected Font fontCjk;

    public final Map<Long, Map<Integer, ColorNameTuple>> stationIdToRoutes = new HashMap<>();
    protected final Map<TransportMode, Map<BlockPos, List<Platform>>> posToPlatforms = new HashMap<>();
    protected final Map<TransportMode, Map<BlockPos, List<Siding>>> posToSidings = new HashMap<>();
    protected final Map<Long, Map<Long, Platform>> stationIdToPlatforms = new HashMap<>();
    protected final Map<Long, Map<Long, Siding>> depotIdToSidings = new HashMap<>();
    protected final Map<Long, List<PlatformRouteDetails>> platformIdToRoutes = new HashMap<>();

    protected final List<Long> clearStationIdToPlatforms = new ArrayList<>();
    protected final List<Long> clearDepotIdToSidings = new ArrayList<>();
    protected final List<Long> clearPlatformIdToRoutes = new ArrayList<>();

    protected final Object2ObjectLinkedOpenHashMap<String, DynamicResource> dynamicResources = new Object2ObjectLinkedOpenHashMap<>();
    protected final ObjectLinkedOpenHashSet<String> resourcesToRefresh = new ObjectLinkedOpenHashSet<>();
    protected final List<Runnable> resourceRegistryQueue = new ArrayList<>();

    protected static final ResourceLocation DEFAULT_BLACK_RESOURCE = new ResourceLocation(MTR.MOD_ID, "textures/block/black.png");
    protected static final ResourceLocation DEFAULT_WHITE_RESOURCE = new ResourceLocation(MTR.MOD_ID, "textures/block/white.png");
    protected static final ResourceLocation DEFAULT_TRANSPARENT_RESOURCE = new ResourceLocation(MTR.MOD_ID, "textures/block/transparent.png");

    public ClientCache(Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots) {
        super(stations, platforms, sidings, routes, depots, new HashSet<>());
        for (final TransportMode transportMode : TransportMode.values()) {
            posToPlatforms.put(transportMode, new HashMap<>());
            posToSidings.put(transportMode, new HashMap<>());
        }
    }

    @Override
    protected void syncAdditional() {
        for (final TransportMode transportMode : TransportMode.values()) {
            mapPosToSavedRails(posToPlatforms.get(transportMode), platforms, transportMode);
            mapPosToSavedRails(posToSidings.get(transportMode), sidings, transportMode);
        }

        stationIdToRoutes.clear();
        routes.forEach(route -> {
            if (!route.isHidden) {
                route.platformIds.forEach(platformId -> {
                    final Station station = platformIdToStation.get(platformId.platformId);
                    if (station != null) {
                        if (!stationIdToRoutes.containsKey(station.id)) {
                            stationIdToRoutes.put(station.id, new HashMap<>());
                        }
                        stationIdToRoutes.get(station.id).put(route.color, new mtr.client.ClientCache.ColorNameTuple(route.color, route.name.split("\\|\\|")[0]));
                    }
                });
            }
        });

        stationIdToPlatforms.keySet().forEach(id -> {
            if (!clearStationIdToPlatforms.contains(id)) {
                clearStationIdToPlatforms.add(id);
            }
        });
        depotIdToSidings.keySet().forEach(id -> {
            if (!clearDepotIdToSidings.contains(id)) {
                clearDepotIdToSidings.add(id);
            }
        });
        platformIdToRoutes.keySet().forEach(id -> {
            if (!clearPlatformIdToRoutes.contains(id)) {
                clearPlatformIdToRoutes.add(id);
            }
        });
    }

    public void resetFonts() {
        font = null;
        fontCjk = null;
        refreshDynamicResources();
    }

    public void refreshDynamicResources() {
        TianjinMetro.LOGGER.info("Refreshing dynamic resources");
        resourcesToRefresh.addAll(dynamicResources.keySet());
    }

    public DynamicResource getDirectionArrow(long platformId, boolean hasLeft, boolean hasRight, IGui.HorizontalAlignment horizontalAlignment, boolean showToString, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        return getResource(String.format("tjmetro_direction_arrow_%s_%s_%s_%s_%s_%s_%s_%s_%s_%s", platformId, hasLeft, hasRight, horizontalAlignment, showToString, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), () -> RouteMapGenerator.generateDirectionArrow(platformId, hasLeft, hasRight, horizontalAlignment, showToString, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), transparentColor == 0 && backgroundColor == ARGB_WHITE ? ClientCache.DefaultRenderingColor.WHITE : ClientCache.DefaultRenderingColor.TRANSPARENT);
    }

    public DynamicResource getPSDStationName(long platformId, IGui.HorizontalAlignment horizontalAlignment, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        return getResource(String.format("tjmetro_psd_station_name_%s_%s_%s_%s_%s_%s_%s", platformId, horizontalAlignment, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), () -> RouteMapGenerator.generatePSDStationName(platformId, horizontalAlignment, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), transparentColor == 0 && backgroundColor == ARGB_WHITE ? ClientCache.DefaultRenderingColor.WHITE : ClientCache.DefaultRenderingColor.TRANSPARENT);
    }

    public DynamicResource getPSDNextStation(long platformId, int arrowDirection, float paddingScale, float aspectRatio, int backgroundColor, int textColor, int transparentColor) {
        return getResource(String.format("tjmetro_psd_next_station_%s_%s_%s_%s_%s_%s_%s", platformId, arrowDirection, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), () -> RouteMapGenerator.generatePSDNextStation(platformId, arrowDirection, paddingScale, aspectRatio, backgroundColor, textColor, transparentColor), transparentColor == 0 && backgroundColor == ARGB_WHITE ? ClientCache.DefaultRenderingColor.WHITE : ClientCache.DefaultRenderingColor.TRANSPARENT);
    }

    public DynamicResource getRouteMap(long platformId, boolean vertical, boolean flip, float aspectRatio, boolean transparentWhite) {
        return getResource(String.format("tjmetro_route_map_%s_%s_%s_%s_%s", platformId, vertical, flip, aspectRatio, transparentWhite), () -> RouteMapGenerator.generateRouteMap(platformId, vertical, flip, aspectRatio, transparentWhite), transparentWhite ? ClientCache.DefaultRenderingColor.TRANSPARENT : ClientCache.DefaultRenderingColor.WHITE);
    }

    public DynamicResource getStationNameEntrance(long stationId, long selectedId, int style, String stationName, float aspectRatio) {
        return getResource(String.format("tjmetro_station_name_entrance_%s_%s_%s_%s_%s", stationId, selectedId, style, stationName, aspectRatio), () -> RouteMapGenerator.generateStationNameEntrance(stationId, selectedId, style, stationName, aspectRatio), ClientCache.DefaultRenderingColor.TRANSPARENT);
    }

    protected DynamicResource getResource(String key, Supplier<NativeImage> supplier, DefaultRenderingColor defaultRenderingColor) {
        final Minecraft minecraftClient = Minecraft.getInstance();
        if (font == null || fontCjk == null) {
            final ResourceManager resourceManager = minecraftClient.getResourceManager();
            try {
                if (ziyue.tjmetro.Config.USE_TIANJIN_METRO_FONT.get()) {
                    font = fontCjk = Font.createFont(Font.TRUETYPE_FONT, Utilities.getInputStream(resourceManager.getResource(new ResourceLocation(Reference.MOD_ID, "font/dengxian.ttf"))));
                } else {
                    font = Font.createFont(Font.TRUETYPE_FONT, Utilities.getInputStream(resourceManager.getResource(new ResourceLocation(MTR.MOD_ID, "font/noto-sans-semibold.ttf"))));
                    fontCjk = Font.createFont(Font.TRUETYPE_FONT, Utilities.getInputStream(resourceManager.getResource(new ResourceLocation(MTR.MOD_ID, "font/noto-serif-cjk-tc-semibold.ttf"))));
                }
            } catch (Exception e) {
                TianjinMetro.LOGGER.error(e.getMessage());
            }
        }

        if (!resourceRegistryQueue.isEmpty()) {
            final Runnable runnable = resourceRegistryQueue.remove(0);
            if (runnable != null) {
                runnable.run();
            }
        }

        final boolean needsRefresh = resourcesToRefresh.contains(key);
        final DynamicResource dynamicResource = dynamicResources.get(key);

        if (dynamicResource != null && !needsRefresh) {
            return dynamicResource;
        }

        RouteMapGenerator.setConstants();
        CompletableFuture.supplyAsync(supplier).thenAccept(nativeImage -> resourceRegistryQueue.add(() -> {
            final DynamicResource staticTextureProviderOld = dynamicResources.get(key);
            if (staticTextureProviderOld != null) {
                staticTextureProviderOld.remove();
            }

            final DynamicResource dynamicResourceNew;
            if (nativeImage == null) {
                dynamicResourceNew = defaultRenderingColor.dynamicResource;
            } else {
                final DynamicTexture dynamicTexture = new DynamicTexture(nativeImage);
                String newKey = key;
                try {
                    newKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
                } catch (Exception e) {
                    TianjinMetro.LOGGER.error(e);
                }
                final ResourceLocation resourceLocation = new ResourceLocation(Reference.MOD_ID, "dynamic_texture_" + newKey.toLowerCase(Locale.ENGLISH).replaceAll("[^0-9a-z_]", "_"));
                minecraftClient.getTextureManager().register(resourceLocation, dynamicTexture);
                dynamicResourceNew = new DynamicResource(resourceLocation, dynamicTexture);
            }

            dynamicResources.put(key, dynamicResourceNew);
        }));

        if (needsRefresh) resourcesToRefresh.remove(key);

        if (dynamicResource == null) {
            dynamicResources.put(key, defaultRenderingColor.dynamicResource);
            return defaultRenderingColor.dynamicResource;
        } else {
            return dynamicResource;
        }
    }

    public Text getText(String text, int maxWidth, int maxHeight, int fontSizeCjk, int fontSize, int padding, IGui.HorizontalAlignment horizontalAlignment) {
        if (maxWidth <= 0) {
            return new Text(new byte[0], 0, 0, 0);
        }

        final boolean customFont = ziyue.tjmetro.Config.USE_TIANJIN_METRO_FONT.get();
        final boolean oneRow = horizontalAlignment == null;
        final String[] defaultTextSplit = IGui.textOrUntitled(text).split("\\|");
        final String[] textSplit;
        if (Config.languageOptions() == 0) {
            textSplit = defaultTextSplit;
        } else {
            final String[] tempTextSplit = Arrays.stream(IGui.textOrUntitled(text).split("\\|")).filter(textPart -> IGui.isCjk(textPart) == (Config.languageOptions() == 1)).toArray(String[]::new);
            textSplit = tempTextSplit.length == 0 ? defaultTextSplit : tempTextSplit;
        }
        final AttributedString[] attributedStrings = new AttributedString[textSplit.length];
        final int[] textWidths = new int[textSplit.length];
        final int[] fontSizes = new int[textSplit.length];
        final FontRenderContext context = new FontRenderContext(new AffineTransform(), false, false);
        int width = 0;
        int height = 0;

        for (int index = 0; index < textSplit.length; index++) {
            final int newFontSize = IGui.isCjk(textSplit[index]) || font.canDisplayUpTo(textSplit[index]) >= 0 ? fontSizeCjk : fontSize;
            attributedStrings[index] = new AttributedString(textSplit[index]);
            fontSizes[index] = newFontSize;

            final Font fontSized = font.deriveFont(Font.PLAIN, newFontSize);
            final Font fontCjkSized = fontCjk.deriveFont(Font.PLAIN, newFontSize);

            for (int characterIndex = 0; characterIndex < textSplit[index].length(); characterIndex++) {
                final char character = textSplit[index].charAt(characterIndex);
                final Font newFont;
                if (fontSized.canDisplay(character)) {
                    newFont = fontSized;
                } else if (fontCjkSized.canDisplay(character)) {
                    newFont = fontCjkSized;
                } else {
                    Font defaultFont = null;
                    for (final Font testFont : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
                        if (testFont.canDisplay(character)) {
                            defaultFont = testFont;
                            break;
                        }
                    }
                    newFont = (defaultFont == null ? new Font(null) : defaultFont).deriveFont(Font.PLAIN, newFontSize);
                }
                textWidths[index] += newFont.getStringBounds(textSplit[index].substring(characterIndex, characterIndex + 1), context).getBounds().width;
                attributedStrings[index].addAttribute(TextAttribute.FONT, newFont, characterIndex, characterIndex + 1);
            }

            if (oneRow) {
                if (index > 0) {
                    width += padding;
                }
                width += textWidths[index];
                height = Math.max(height, (int) (fontSizes[index] * LINE_HEIGHT_MULTIPLIER));
            } else {
                width = Math.max(width, Math.min(maxWidth, textWidths[index]));
                height += (int) (fontSizes[index] * LINE_HEIGHT_MULTIPLIER);
            }
        }

        int textOffset = 0;
        final int imageHeight = Math.min(height, maxHeight);
        final BufferedImage image = new BufferedImage(width + (oneRow ? 0 : padding * 2), imageHeight + (oneRow ? 0 : padding * 2), BufferedImage.TYPE_BYTE_GRAY);
        final Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int realWidth = 0;
        for (int index = 0; index < textSplit.length; index++) {
            if (oneRow) {
                graphics2D.drawString(attributedStrings[index].getIterator(), textOffset, height / LINE_HEIGHT_MULTIPLIER - (customFont ? height * 0.035F : 0));
                textOffset += textWidths[index] + padding;
            } else {
                final float scaleY = (float) imageHeight / height;
                final float textWidth = Math.min(maxWidth, textWidths[index] * scaleY);
                final float scaleX = textWidth / textWidths[index];
                final AffineTransform stretch = new AffineTransform();
                stretch.concatenate(AffineTransform.getScaleInstance(scaleX, scaleY));
                graphics2D.setTransform(stretch);
                graphics2D.drawString(attributedStrings[index].getIterator(), horizontalAlignment.getOffset(0, textWidth - width) / scaleY + padding / scaleX, textOffset + fontSizes[index] + padding / scaleY - (customFont ? height * 0.035F : 0));
                textOffset += (int) (fontSizes[index] * LINE_HEIGHT_MULTIPLIER);
                realWidth = Math.max(realWidth, (int) textWidth);
            }
        }
        realWidth += padding;

        width = width + (oneRow ? 0 : padding * 2);
        height = imageHeight + (oneRow ? 0 : padding * 2);
        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        graphics2D.dispose();
        image.flush();
        return new Text(pixels, realWidth, height, width);
    }

    protected static <U extends SavedRailBase> void mapPosToSavedRails(Map<BlockPos, List<U>> posToSavedRails, Set<U> savedRails, TransportMode transportMode) {
        posToSavedRails.clear();
        savedRails.forEach(savedRail -> {
            if (savedRail.isTransportMode(transportMode)) {
                final BlockPos pos = savedRail.getMidPos(true);
                if (!posToSavedRails.containsKey(pos)) {
                    posToSavedRails.put(pos, new ArrayList<>());
                }
                posToSavedRails.get(pos).add(savedRail);
            }
        });
    }

    /**
     * A text that contains its pixels, width, and height.
     *
     * @param width       the real width of the text
     * @param renderWidth the width when rendering the text, do not use
     * @author ZiYueCommentary
     * @since 1.0b
     */
    public record Text(byte[] pixels, int width, int height, int renderWidth)
    {

    }

    // ¯\_(ツ)_/¯ private are everywhere

    /**
     * @see mtr.client.ClientCache.DynamicResource
     */
    public static class DynamicResource
    {
        public final int width;
        public final int height;
        public final ResourceLocation resourceLocation;

        protected DynamicResource(ResourceLocation resourceLocation, DynamicTexture dynamicTexture) {
            this.resourceLocation = resourceLocation;
            if (dynamicTexture != null) {
                final NativeImage nativeImage = dynamicTexture.getPixels();
                if (nativeImage != null) {
                    width = nativeImage.getWidth();
                    height = nativeImage.getHeight();
                } else {
                    width = 16;
                    height = 16;
                }
            } else {
                width = 16;
                height = 16;
            }
        }

        protected void remove() {
            if (!resourceLocation.equals(DEFAULT_BLACK_RESOURCE) && !resourceLocation.equals(DEFAULT_WHITE_RESOURCE) && !resourceLocation.equals(DEFAULT_TRANSPARENT_RESOURCE)) {
                final TextureManager textureManager = Minecraft.getInstance().getTextureManager();
                textureManager.release(resourceLocation);
                final AbstractTexture abstractTexture = textureManager.getTexture(resourceLocation);
                if (abstractTexture != null) {
                    abstractTexture.releaseId();
                    abstractTexture.close();
                }
            }
        }
    }

    protected enum DefaultRenderingColor
    {
        BLACK(DEFAULT_BLACK_RESOURCE),
        WHITE(DEFAULT_WHITE_RESOURCE),
        TRANSPARENT(DEFAULT_TRANSPARENT_RESOURCE);

        private final DynamicResource dynamicResource;

        DefaultRenderingColor(ResourceLocation resourceLocation) {
            dynamicResource = new DynamicResource(resourceLocation, null);
        }
    }
}
