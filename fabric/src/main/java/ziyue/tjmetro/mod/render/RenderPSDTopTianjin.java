package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import org.mtr.mod.block.BlockPSDAPGGlassEndBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import ziyue.tjmetro.mod.block.BlockPSDTopTianjin;
import ziyue.tjmetro.mod.client.DynamicTextureCache;

import static org.mtr.mod.render.RenderRouteBase.getShadingColor;

/**
 * @author ZiYueCommentary
 * @see BlockPSDTopTianjin
 * @see RenderRouteBase
 * @since 1.0.0-beta-1
 */

public class RenderPSDTopTianjin extends RenderRouteBase<BlockPSDTopTianjin.BlockEntity>
{
    protected static final float END_FRONT_OFFSET = 1 / (MathHelper.getSquareRootOfTwoMapped() * 16);
    protected static final float BOTTOM_DIAGONAL_OFFSET = ((float) Math.sqrt(3) - 1) / 32;
    protected static final float ROOT_TWO_SCALED = MathHelper.getSquareRootOfTwoMapped() / 16;
    protected static final float BOTTOM_END_DIAGONAL_OFFSET = END_FRONT_OFFSET - BOTTOM_DIAGONAL_OFFSET / MathHelper.getSquareRootOfTwoMapped();
    protected static final float COLOR_STRIP_START = 14.5F / 16;
    protected static final float COLOR_STRIP_END = 15 / 16F;

    public RenderPSDTopTianjin(Argument dispatcher) {
        super(dispatcher, 2 - SMALL_OFFSET_16, 7.5F, 1.5F, 0.125F, true, 3, BlockPSDTopTianjin.ARROW_DIRECTION);
    }

    @Override
    public void render(BlockPSDTopTianjin.BlockEntity entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
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

            if ((renderType == RenderType.ARROW || renderType == RenderType.ROUTE) && IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE) {
                final float width = leftBlocks + rightBlocks + 1 - sidePadding * 2;
                final float height = 1 - topPadding - bottomPadding;
                final int arrowDirection = IBlock.getStatePropertySafe(state, arrowDirectionProperty);

                final Identifier identifier;
                final BlockPSDTopTianjin.EnumDoorType doorType = IBlock.getStatePropertySafe(entity.getCachedState2(), BlockPSDTopTianjin.STYLE);
                if (renderType == RenderType.ARROW) {
                    if (doorType == BlockPSDTopTianjin.EnumDoorType.STATION_NAME) {
                        identifier = DynamicTextureCache.instance.getStationName(platformId, false, HorizontalAlignment.CENTER, 0.25F, width / height, ARGB_WHITE, ARGB_BLACK, transparentWhite ? ARGB_WHITE : 0).identifier;
                    } else {
                        identifier = DynamicTextureCache.instance.getDirectionArrow(platformId, (arrowDirection & 0b01) > 0, (arrowDirection & 0b10) > 0, HorizontalAlignment.CENTER, true, 0.25F, width / height, ARGB_WHITE, ARGB_BLACK, transparentWhite ? ARGB_WHITE : 0).identifier;
                    }
                } else {
                    if (doorType == BlockPSDTopTianjin.EnumDoorType.NEXT_STATION) {
                        identifier = DynamicTextureCache.instance.getNextStation(platformId, arrowDirection, 0.25F, width / height, ARGB_WHITE, ARGB_BLACK, transparentWhite ? ARGB_WHITE : 0).identifier;
                    } else {
                        identifier = DynamicTextureCache.instance.getRouteMap(platformId, false, arrowDirection == 2, width / height, transparentWhite).identifier;
                    }
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
    protected RenderType getRenderType(World world, BlockPos pos, BlockState state) {
        final BlockPSDTopTianjin.EnumPersistent persistent = IBlock.getStatePropertySafe(state, BlockPSDTopTianjin.PERSISTENT);
        if (persistent == BlockPSDTopTianjin.EnumPersistent.NONE) {
            final Block blockBelow = world.getBlockState(pos.down()).getBlock();
            if (blockBelow.data instanceof BlockPSDAPGDoorBase) {
                return RenderType.ARROW;
            } else if (!(blockBelow.data instanceof BlockPSDAPGGlassEndBase)) {
                return RenderType.ROUTE;
            } else {
                return RenderType.NONE;
            }
        } else {
            return persistent == BlockPSDTopTianjin.EnumPersistent.ARROW ? RenderType.ARROW : persistent == BlockPSDTopTianjin.EnumPersistent.ROUTE ? RenderType.ROUTE : RenderType.NONE;
        }
    }

    @Override
    protected void renderAdditionalUnmodified(StoredMatrixTransformations storedMatrixTransformations, BlockState state, Direction facing, int light) {
        final boolean airLeft = IBlock.getStatePropertySafe(state, BlockPSDTopTianjin.AIR_LEFT);
        final boolean airRight = IBlock.getStatePropertySafe(state, BlockPSDTopTianjin.AIR_RIGHT);
        final boolean persistent = IBlock.getStatePropertySafe(state, BlockPSDTopTianjin.PERSISTENT) != BlockPSDTopTianjin.EnumPersistent.NONE;
        if (!airLeft && !airRight || persistent) return;
        MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/psd_top.png"), false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
            storedMatrixTransformations.transform(graphicsHolder, offset);
            if (airLeft) {
                // back
                IDrawing.drawTexture(graphicsHolder, -0.125F, 0, 0.5F, 0.5F, 0, -0.125F, 0.5F, 1, -0.125F, -0.125F, 1, 0.5F, 0, 0, 1, 1, facing, -1, light);
                // front
                IDrawing.drawTexture(graphicsHolder, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, -0.25F - END_FRONT_OFFSET, 1, 0.25F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0, 0, 1, 0.9375F, facing.getOpposite(), -1, light);
                // top curve
                IDrawing.drawTexture(graphicsHolder, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, -0.25F - END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0, 0.9375F, 1, 0.96875F, facing.getOpposite(), -1, light);
                // bottom curve
                IDrawing.drawTexture(graphicsHolder, 0.5F, 0, -0.5F, -0.25F, 0, 0.25F, -0.25F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0, 0.96875F, 1, 1, facing.getOpposite(), -1, light);
                // bottom
                IDrawing.drawTexture(graphicsHolder, 0.5F, SMALL_OFFSET, -0.125F, -0.125F, SMALL_OFFSET, 0.5F, -0.125F, SMALL_OFFSET, 0.125F, 0.5F, SMALL_OFFSET, -0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, facing, -1, light);
                // top
                IDrawing.drawTexture(graphicsHolder, 0.5F, 1 - SMALL_OFFSET, -0.5F, -0.125F, 1 - SMALL_OFFSET, 0.125F, -0.125F, 1 - SMALL_OFFSET, 0.5F, 0.5F, 1 - SMALL_OFFSET, -0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
                // top front
                IDrawing.drawTexture(graphicsHolder, 0.5F - END_FRONT_OFFSET, 1 - SMALL_OFFSET, -0.5F - END_FRONT_OFFSET, -0.125F - ROOT_TWO_SCALED, 1 - SMALL_OFFSET, 0.125F, -0.125F, 1 - SMALL_OFFSET, 0.125F, 0.5F, 1 - SMALL_OFFSET, -0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
                // left side diagonal
                IDrawing.drawTexture(graphicsHolder, 0.5F, 0.0625F, -0.5F, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.5F - END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0.5F, 1, -0.5F, 0.9375F, 0, 1, 0.9375F, facing, -1, light);
                // left side diagonal square
                IDrawing.drawTexture(graphicsHolder, 0.5F, 0, -0.5F, 0.5F - BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0.5F - END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.5F, 0.0625F, -0.5F, 0.9375F, 0.9375F, 1, 1, facing, -1, light);
            }
            if (airRight) {
                // back
                IDrawing.drawTexture(graphicsHolder, -0.5F, 0, -0.125F, 0.125F, 0, 0.5F, 0.125F, 1, 0.5F, -0.5F, 1, -0.125F, 0, 0, 1, 1, facing, -1, light);
                // front
                IDrawing.drawTexture(graphicsHolder, 0.25F + END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.5F + END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0.25F + END_FRONT_OFFSET, 1, 0.25F - END_FRONT_OFFSET, 0, 0, 1, 0.9375F, facing.getOpposite(), -1, light);
                // top curve
                IDrawing.drawTexture(graphicsHolder, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0.25F + END_FRONT_OFFSET, 0.0625F, 0.25F - END_FRONT_OFFSET, 0, 0.9375F, 1, 0.96875F, facing.getOpposite(), -1, light);
                // bottom curve
                IDrawing.drawTexture(graphicsHolder, 0.25F, 0, 0.25F, -0.5F, 0, -0.5F, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, 0.25F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, 0.25F - BOTTOM_END_DIAGONAL_OFFSET, 0, 0.96875F, 1, 1, facing.getOpposite(), -1, light);
                // bottom
                IDrawing.drawTexture(graphicsHolder, 0.125F, SMALL_OFFSET, 0.5F, -0.5F, SMALL_OFFSET, -0.125F, -0.5F, SMALL_OFFSET, -0.5F, 0.125F, SMALL_OFFSET, 0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, facing, -1, light);
                // top
                IDrawing.drawTexture(graphicsHolder, 0.125F, 1 - SMALL_OFFSET, 0.125F, -0.5F, 1 - SMALL_OFFSET, -0.5F, -0.5F, 1 - SMALL_OFFSET, -0.125F, 0.125F, 1 - SMALL_OFFSET, 0.5F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
                // top front
                IDrawing.drawTexture(graphicsHolder, 0.125F + ROOT_TWO_SCALED, 1 - SMALL_OFFSET, 0.125F, -0.5F + END_FRONT_OFFSET, 1 - SMALL_OFFSET, -0.5F - END_FRONT_OFFSET, -0.5F, 1 - SMALL_OFFSET, -0.5F, 0.125F, 1 - SMALL_OFFSET, 0.125F, 0.125F, 0.125F, 0.1875F, 0.1875F, Direction.UP, -1, light);
                // left side diagonal
                IDrawing.drawTexture(graphicsHolder, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, -0.5F, 0.0625F, -0.5F, -0.5F, 1, -0.5F, -0.5F + END_FRONT_OFFSET, 1, -0.5F - END_FRONT_OFFSET, 0, 0, 0.0625F, 0.9375F, facing, -1, light);
                // left side diagonal square
                IDrawing.drawTexture(graphicsHolder, -0.5F + BOTTOM_END_DIAGONAL_OFFSET, BOTTOM_DIAGONAL_OFFSET, -0.5F - BOTTOM_END_DIAGONAL_OFFSET, -0.5F, 0, -0.5F, -0.5F, 0.0625F, -0.5F, -0.5F + END_FRONT_OFFSET, 0.0625F, -0.5F - END_FRONT_OFFSET, 0, 0.9375F, 0.0625F, 1, facing, -1, light);
            }
            graphicsHolder.pop();
        });
    }

    @Override
    protected void renderAdditional(StoredMatrixTransformations storedMatrixTransformations, long platformId, BlockState state, int leftBlocks, int rightBlocks, Direction facing, int color, int light) {
        final boolean isNotPersistent = IBlock.getStatePropertySafe(state, BlockPSDTopTianjin.PERSISTENT) == BlockPSDTopTianjin.EnumPersistent.NONE;
        final boolean airLeft = isNotPersistent && IBlock.getStatePropertySafe(state, BlockPSDTopTianjin.AIR_LEFT);
        final boolean airRight = isNotPersistent && IBlock.getStatePropertySafe(state, BlockPSDTopTianjin.AIR_RIGHT);
        MainRenderer.scheduleRender(org.mtr.mod.client.DynamicTextureCache.instance.getColorStrip(platformId).identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolder, offset) -> {
            storedMatrixTransformations.transform(graphicsHolder, offset);
            IDrawing.drawTexture(graphicsHolder, airLeft ? 0.625F : 0, COLOR_STRIP_START, 0, airRight ? 0.375F : 1, COLOR_STRIP_END, 0, facing, color, light);
            if (airLeft) {
                IDrawing.drawTexture(graphicsHolder, END_FRONT_OFFSET, COLOR_STRIP_START, -0.625F - END_FRONT_OFFSET, 0.75F + END_FRONT_OFFSET, COLOR_STRIP_END, 0.125F - END_FRONT_OFFSET, facing, -1, light);
            }
            if (airRight) {
                IDrawing.drawTexture(graphicsHolder, 0.25F - END_FRONT_OFFSET, COLOR_STRIP_START, 0.125F - END_FRONT_OFFSET, 1 - END_FRONT_OFFSET, COLOR_STRIP_END, -0.625F - END_FRONT_OFFSET, facing, -1, light);
            }
            graphicsHolder.pop();
        });
    }

    @Override
    protected float getAdditionalOffset(BlockState state) {
        return IBlock.getStatePropertySafe(state, BlockPSDTopTianjin.PERSISTENT) == BlockPSDTopTianjin.EnumPersistent.NONE ? 0 : BlockPSDTopTianjin.PERSISTENT_OFFSET_SMALL;
    }
}
