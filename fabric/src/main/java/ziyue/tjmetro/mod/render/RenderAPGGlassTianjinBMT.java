package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockAPGGlass;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import ziyue.tjmetro.mod.block.BlockAPGGlassTianjinBMT;
import ziyue.tjmetro.mod.client.DynamicTextureCache;

import static org.mtr.mod.render.RenderRouteBase.getShadingColor;
import static ziyue.tjmetro.mod.block.BlockAPGGlassTianjinBMT.STYLE;

public class RenderAPGGlassTianjinBMT extends RenderRouteBase<BlockAPGGlassTianjinBMT.BlockEntity>
{
    public static final float COLOR_STRIP_START = 15F / 16;
    public static final float COLOR_STRIP_END = 15.5F / 16F;

    public RenderAPGGlassTianjinBMT(Argument dispatcher) {
        super(dispatcher, 4, 8F, 1F, 8, false, 2, BlockAPGGlass.ARROW_DIRECTION);
    }

    @Override
    protected RenderType getRenderType(World world, BlockPos pos, BlockState state) {
        if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER) {
            return RenderType.NONE;
        } else {
            return RenderType.ARROW;
        }
    }

    @Override
    public void render(BlockAPGGlassTianjinBMT.BlockEntity entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos blockPos = entity.getPos2();
        final BlockState state = world.getBlockState(blockPos);
        final Direction facing = IBlock.getStatePropertySafe(state, DirectionHelper.FACING);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> graphicsHolderNew.rotateYDegrees(-facing.asRotation()));

        renderAdditionalUnmodified(storedMatrixTransformations.copy(), state, facing, light);

        InitClient.findClosePlatform(blockPos.down(platformSearchYOffset), 5, platform -> {
            final long platformId = platform.getId();

            storedMatrixTransformations.add(graphicsHolderNew -> {
                graphicsHolderNew.translate(0, 1, 0);
                graphicsHolderNew.rotateZDegrees(180);
                graphicsHolderNew.translate(-0.5, -getAdditionalOffset(state), z);
            });

            final int leftBlocks = getTextureNumber(world, blockPos, facing, true);
            final int rightBlocks = getTextureNumber(world, blockPos, facing, false);
            final int color = getShadingColor(facing, ARGB_WHITE);
            final RenderType renderType = getRenderType(world, blockPos.offset(facing.rotateYCounterclockwise(), leftBlocks), state);
            final BlockAPGGlassTianjinBMT.EnumDoorType style = IBlock.getStatePropertySafe(state, STYLE);

            if ((renderType != RenderType.NONE) && IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE) {
                final float width = leftBlocks + rightBlocks + 1 - sidePadding * 2;
                final float height = 1 - topPadding - bottomPadding;
                final int arrowDirection = IBlock.getStatePropertySafe(state, arrowDirectionProperty);

                final Identifier identifier;
                switch (style) {
                    case ROUTE:
                        identifier = DynamicTextureCache.instance.getRouteMap(platformId, false, arrowDirection == 2, width / height, transparentWhite).identifier;
                        break;
                    case STATION_NAME:
                        identifier = DynamicTextureCache.instance.getStationName(platformId, true, HorizontalAlignment.CENTER, 0.25F, width / height, ARGB_WHITE, ARGB_BLACK, transparentWhite ? ARGB_WHITE : 0).identifier;
                        break;
                    case NEXT_STATION:
                        identifier = DynamicTextureCache.instance.getNextStation(platformId, arrowDirection, 0.25F, width / height, ARGB_WHITE, ARGB_BLACK, transparentWhite ? ARGB_WHITE : 0).identifier;
                        break;
                    default:
                        identifier = null;
                }

                MainRenderer.scheduleRender(identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
                    storedMatrixTransformations.transform(graphicsHolderNew, offset);
                    IDrawing.drawTexture(graphicsHolderNew, leftBlocks == 0 ? sidePadding : 0, topPadding, 0, 1 - (rightBlocks == 0 ? sidePadding : 0), 1 - bottomPadding, 0, (leftBlocks - (leftBlocks == 0 ? 0 : sidePadding)) / width, 0, (width - rightBlocks + (rightBlocks == 0 ? 0 : sidePadding)) / width, 1, facing.getOpposite(), color, light);
                    graphicsHolderNew.pop();
                });
            }

            renderAdditional(storedMatrixTransformations, platformId, state, leftBlocks, rightBlocks, facing.getOpposite(), color, light);
        });
    }

    @Override
    protected void renderAdditional(StoredMatrixTransformations storedMatrixTransformations, long platformId, BlockState state, int leftBlocks, int rightBlocks, Direction facing, int color, int light) {
        if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER && IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE) {
            final boolean isLeft = isLeft(state);
            final boolean isRight = isRight(state);
            MainRenderer.scheduleRender(org.mtr.mod.client.DynamicTextureCache.instance.getColorStrip(platformId).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
                storedMatrixTransformations.transform(graphicsHolder, offset);
                IDrawing.drawTexture(graphicsHolder, isLeft ? sidePadding : 0, COLOR_STRIP_START, 0, isRight ? 1 - sidePadding : 1, COLOR_STRIP_END, 0, facing, color, light);
                IDrawing.drawTexture(graphicsHolder, isRight ? 1 - sidePadding : 1, COLOR_STRIP_START, 0.125F, isLeft ? sidePadding : 0, COLOR_STRIP_END, 0.125F, facing, color, light);
                graphicsHolder.pop();
            });

            final float width = leftBlocks + rightBlocks + 1 - sidePadding * 2;
            final float height = 1 - topPadding - bottomPadding;
            MainRenderer.scheduleRender(org.mtr.mod.client.DynamicTextureCache.instance.getSingleRowStationName(platformId, width / height).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
                storedMatrixTransformations.transform(graphicsHolder, offset);
                IDrawing.drawTexture(graphicsHolder, 1 - (rightBlocks == 0 ? sidePadding : 0), topPadding, 0.125F, leftBlocks == 0 ? sidePadding : 0, 1 - bottomPadding, 0.125F, (rightBlocks - (rightBlocks == 0 ? 0 : sidePadding)) / width, 0, (width - leftBlocks + (leftBlocks == 0 ? 0 : sidePadding)) / width, 1, facing, color, light);
                graphicsHolder.pop();
            });
        }
    }
}
