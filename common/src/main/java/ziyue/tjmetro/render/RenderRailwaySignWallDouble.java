package ziyue.tjmetro.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import mtr.block.BlockRailwaySign;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.client.ClientCache;
import mtr.client.ClientData;
import mtr.client.CustomResources;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.render.MoreRenderLayers;
import mtr.render.RenderRailwaySign;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.block.BlockRailwaySignWallDouble;
import ziyue.tjmetro.block.base.BlockRailwaySignBase;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZiYueCommentary
 * @see RenderRailwaySign
 * @since beta-1
 */

public class RenderRailwaySignWallDouble<T extends BlockRailwaySignWallDouble.TileEntityRailwaySignWallDouble> extends BlockEntityRendererMapper<T> implements IBlock, IGui, IDrawing
{
    public static final int HEIGHT_TO_SCALE = 27;

    public RenderRailwaySignWallDouble(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        final BlockGetter world = entity.getLevel();
        if (world == null) return;

        final BlockPos pos = entity.getBlockPos();
        final BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof BlockRailwaySignWallDouble block)) return;
        if (entity.getSignIds()[0].length != block.length) return;
        final Direction facing = IBlock.getStatePropertySafe(state, BlockStationNameBase.FACING);
        final String[][] signIds = entity.getSignIds();
        int[] backgroundColor = new int[2];
        for (int i = 0; i < 2; i++) {
            for (final String signId : signIds[i]) {
                if (signId != null) {
                    final CustomResources.CustomSign sign = RenderRailwaySign.getSign(signId);
                    if (sign != null) {
                        if (sign.backgroundColor != 0) {
                            backgroundColor[i] = sign.backgroundColor;
                            break;
                        }
                    }
                }
            }
        }

        matrices.pushPose();
        matrices.translate(0.5, 0.53125, 0.5);
        matrices.mulPose(Vector3f.YP.rotationDegrees(-facing.toYRot()));
        matrices.mulPose(Vector3f.ZP.rotationDegrees(180));
        matrices.translate(block.getXStart() / 16F - 0.5, -0.468, 0.493);

        final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new ResourceLocation("mtr:textures/block/white.png"), false));
        IDrawing.drawTexture(matrices, vertexConsumer, 0, 0F, SMALL_OFFSET * 2, 0.5F * (signIds[0].length), 0.5F, SMALL_OFFSET * 2, facing, backgroundColor[0] | ARGB_BLACK, MAX_LIGHT_GLOWING);
        IDrawing.drawTexture(matrices, vertexConsumer, 0, 0.5F, SMALL_OFFSET * 2, 0.5F * (signIds[1].length), 1F, SMALL_OFFSET * 2, facing, backgroundColor[1] | ARGB_BLACK, MAX_LIGHT_GLOWING);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < signIds[i].length; j++) {
                if (signIds[i][j] != null) {
                    drawSign(matrices, vertexConsumers, Minecraft.getInstance().font, pos, signIds[i][j], 0.5F * j, 0, 0.5F, RenderRailwaySign.getMaxWidth(signIds[i], j, false), RenderRailwaySign.getMaxWidth(signIds[i], j, true), entity.getSelectedIds().get(i), facing, backgroundColor[i] | ARGB_BLACK, (textureId, x, y, size, flipTexture) -> {
                        final VertexConsumer vertexConsumer1 = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new ResourceLocation(textureId.toString()), true));
                        IDrawing.drawTexture(matrices, vertexConsumer1, x, y, size, size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1, facing, -1, MAX_LIGHT_GLOWING);
                    });
                }
            }
            matrices.translate(0, 0.5, 0);
        }

        matrices.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return true;
    }

    public static void drawSign(PoseStack matrices, MultiBufferSource vertexConsumers, Font textRenderer, BlockPos pos, String signId, float x, float y, float size, float maxWidthLeft, float maxWidthRight, Set<Long> selectedIds, Direction facing, int backgroundColor, RenderRailwaySign.DrawTexture drawTexture) {
        if (RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance, facing)) return;

        final CustomResources.CustomSign sign = RenderRailwaySign.getSign(signId);
        if (sign == null) return;

        final float signSize = (sign.small ? BlockRailwaySignBase.SMALL_SIGN_PERCENTAGE : 1) * size;
        final float margin = (size - signSize) / 2;

        final boolean hasCustomText = sign.hasCustomText();
        final boolean flipCustomText = sign.flipCustomText;
        final boolean flipTexture = sign.flipTexture;
        final boolean isExit = signId.equals(BlockRailwaySign.SignType.EXIT_LETTER.toString()) || signId.equals(BlockRailwaySign.SignType.EXIT_LETTER_FLIPPED.toString());
        final boolean isLine = signId.equals(BlockRailwaySign.SignType.LINE.toString()) || signId.equals(BlockRailwaySign.SignType.LINE_FLIPPED.toString());
        final boolean isPlatform = signId.equals(BlockRailwaySign.SignType.PLATFORM.toString()) || signId.equals(BlockRailwaySign.SignType.PLATFORM_FLIPPED.toString());

        final MultiBufferSource.BufferSource immediate = RenderTrains.shouldNotRender(pos, RenderTrains.maxTrainRenderDistance / 2, null) ? null : MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        if (vertexConsumers != null && isExit) {
            final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
            if (station == null) return;

            final Map<String, List<String>> exits = station.getGeneratedExits();
            final List<String> selectedExitsSorted = selectedIds.stream().map(Station::deserializeExit).filter(exits::containsKey).sorted(String::compareTo).collect(Collectors.toList());

            matrices.pushPose();
            matrices.translate(x + margin + (flipCustomText ? signSize : 0), y + margin, 0);
            final float maxWidth = ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2;
            final float exitWidth = signSize * selectedExitsSorted.size();
            matrices.scale(Math.min(1, maxWidth / exitWidth), 1, 1);

            final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(new ResourceLocation("mtr:textures/block/sign/exit_letter_blank.png"), true));

            for (int i = 0; i < selectedExitsSorted.size(); i++) {
                final String selectedExit = selectedExitsSorted.get(flipCustomText ? selectedExitsSorted.size() - i - 1 : i);
                final float offset = (flipCustomText ? -1 : 1) * signSize * i - (flipCustomText ? signSize : 0);

                IDrawing.drawTexture(matrices, vertexConsumer, offset, 0, SMALL_OFFSET, offset + signSize, signSize, SMALL_OFFSET, facing, -1, MAX_LIGHT_GLOWING);

                final String selectedExitLetter = selectedExit.substring(0, 1);
                final String selectedExitNumber = selectedExit.substring(1);
                final boolean hasNumber = !selectedExitNumber.isEmpty();
                final float space = hasNumber ? margin * 1.5F : 0;
                IDrawing.drawStringWithFont(matrices, textRenderer, immediate, selectedExitLetter, hasNumber ? HorizontalAlignment.LEFT : HorizontalAlignment.CENTER, VerticalAlignment.CENTER, offset + (hasNumber ? margin : signSize / 2), signSize / 2 + margin, signSize - margin * 2 - space, signSize - margin, 1, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
                if (hasNumber) {
                    IDrawing.drawStringWithFont(matrices, textRenderer, immediate, selectedExitNumber, HorizontalAlignment.RIGHT, VerticalAlignment.TOP, offset + signSize - margin, signSize / 2, space, signSize / 2 - margin / 4, 1, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
                }

                if (maxWidth > exitWidth && selectedExitsSorted.size() == 1 && !exits.get(selectedExit).isEmpty()) {
                    IDrawing.drawStringWithFont(matrices, textRenderer, immediate, exits.get(selectedExit).get(0), flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.CENTER, flipCustomText ? offset - margin : offset + signSize + margin, signSize / 2, maxWidth - exitWidth - margin * 2, signSize, HEIGHT_TO_SCALE / signSize, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
                }
            }

            matrices.popPose();
        } else if (vertexConsumers != null && isLine) {
            final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
            if (station == null) return;

            final Map<Integer, ClientCache.ColorNameTuple> routesInStation = ClientData.DATA_CACHE.stationIdToRoutes.get(station.id);
            if (routesInStation != null) {
                final List<ClientCache.ColorNameTuple> selectedIdsSorted = selectedIds.stream().filter(selectedId -> RailwayData.isBetween(selectedId, Integer.MIN_VALUE, Integer.MAX_VALUE)).map(Math::toIntExact).filter(routesInStation::containsKey).map(routesInStation::get).sorted(Comparator.comparingInt(route -> route.color)).collect(Collectors.toList());

                final float maxWidth = Math.max(0, ((flipCustomText ? maxWidthLeft : maxWidthRight) + 1) * size - margin * 2);
                final float height = size - margin * 2;
                final List<ClientCache.DynamicResource> resourceLocationDataList = new ArrayList<>();
                float totalTextWidth = 0;
                for (final ClientCache.ColorNameTuple route : selectedIdsSorted) {
                    final ClientCache.DynamicResource resourceLocationData = ClientData.DATA_CACHE.getRouteSquare(route.color, route.name, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT);
                    resourceLocationDataList.add(resourceLocationData);
                    totalTextWidth += height * resourceLocationData.width / resourceLocationData.height + margin / 2F;
                }

                matrices.pushPose();
                matrices.translate(flipCustomText ? x + size - margin : x + margin, 0, 0);

                if (totalTextWidth > margin / 2F) {
                    totalTextWidth -= margin / 2F;
                }
                if (totalTextWidth > maxWidth) {
                    matrices.scale(maxWidth / totalTextWidth, 1, 1);
                }

                float xOffset = 0;
                for (final ClientCache.DynamicResource resourceLocationData : resourceLocationDataList) {
                    final float width = height * resourceLocationData.width / resourceLocationData.height;
                    IDrawing.drawTexture(matrices, vertexConsumers.getBuffer(MoreRenderLayers.getLight(resourceLocationData.resourceLocation, false)), flipCustomText ? -xOffset - width : xOffset, margin, width, height, Direction.UP, MAX_LIGHT_GLOWING);
                    xOffset += width + margin / 2F;
                }

                matrices.popPose();
            }
        } else if (vertexConsumers != null && isPlatform) {
            final Station station = RailwayData.getStation(ClientData.STATIONS, ClientData.DATA_CACHE, pos);
            if (station == null) {
                return;
            }

            final Map<Long, Platform> platformPositions = ClientData.DATA_CACHE.requestStationIdToPlatforms(station.id);
            if (platformPositions != null) {
                final List<Long> selectedIdsSorted = selectedIds.stream().filter(platformPositions::containsKey).sorted(Comparator.comparing(platformPositions::get)).collect(Collectors.toList());
                final int selectedCount = selectedIdsSorted.size();

                final float extraMargin = margin - margin / selectedCount;
                final float height = (size - extraMargin * 2) / selectedCount;
                for (int i = 0; i < selectedIdsSorted.size(); i++) {
                    final float topOffset = i * height + extraMargin;
                    final float bottomOffset = (i + 1) * height + extraMargin;
                    final float left = flipCustomText ? x - maxWidthLeft * size : x + margin;
                    final float right = flipCustomText ? x + size - margin : x + (maxWidthRight + 1) * size;
                    final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(ClientData.DATA_CACHE.getDirectionArrow(selectedIdsSorted.get(i), false, false, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, false, margin / size, (right - left) / (bottomOffset - topOffset), backgroundColor, ARGB_WHITE, backgroundColor).resourceLocation, true));
                    IDrawing.drawTexture(matrices, vertexConsumer, left, topOffset, 0, right, bottomOffset, 0, 0, 0, 1, 1, facing, -1, MAX_LIGHT_GLOWING);
                }
            }
        } else {
            drawTexture.drawTexture(sign.textureId, x + margin, y + margin, signSize, flipTexture);

            if (hasCustomText) {
                final float fixedMargin = size * (1 - BlockRailwaySignBase.SMALL_SIGN_PERCENTAGE) / 2;
                final boolean isSmall = sign.small;
                final float maxWidth = Math.max(0, (flipCustomText ? maxWidthLeft : maxWidthRight) * size - fixedMargin * (isSmall ? 1 : 2));
                final float start = flipCustomText ? x - (isSmall ? 0 : fixedMargin) : x + size + (isSmall ? 0 : fixedMargin);
                if (vertexConsumers == null) {
                    IDrawing.drawStringWithFont(matrices, textRenderer, immediate, isExit || isLine ? "..." : sign.customText, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, VerticalAlignment.TOP, start, y + fixedMargin, maxWidth, size - fixedMargin * 2, 0.01F, ARGB_WHITE, false, MAX_LIGHT_GLOWING, null);
                } else {
                    final ClientCache.DynamicResource dynamicResource = ClientData.DATA_CACHE.getSignText(sign.customText, flipCustomText ? HorizontalAlignment.RIGHT : HorizontalAlignment.LEFT, fixedMargin / size, backgroundColor, ARGB_WHITE);
                    final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(MoreRenderLayers.getLight(dynamicResource.resourceLocation, true));
                    final float width = Math.min(size * dynamicResource.width / dynamicResource.height, maxWidth);
                    IDrawing.drawTexture(matrices, vertexConsumer, start - (flipCustomText ? width : 0), 0, 0, start + (flipCustomText ? 0 : width), size, 0, 0, 0, 1, 1, facing, -1, MAX_LIGHT_GLOWING);
                }
            }
        }

        if (immediate != null) {
            immediate.endBatch();
        }
    }
}
