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
import ziyue.tjmetro.mod.block.BlockAPGGlassTianjinTRT;
import ziyue.tjmetro.mod.client.DynamicTextureCache;

import static org.mtr.mod.render.RenderRouteBase.getShadingColor;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.block.BlockAPGGlassTianjinTRT
 * @see RenderRouteBase
 * @since 1.0.0-beta-5
 */

public class RenderAPGGlassTianjinTRT extends RenderRouteBase<BlockAPGGlassTianjinTRT.BlockEntity>
{
    public RenderAPGGlassTianjinTRT(Argument dispatcher) {
        super(dispatcher, 4, 9, 3, 8, true, 2, BlockAPGGlass.ARROW_DIRECTION);
    }

    @Override
    public void render(BlockAPGGlassTianjinTRT.BlockEntity entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos blockPos = entity.getPos2();
        final BlockState state = world.getBlockState(blockPos);
        final Direction facing = IBlock.getStatePropertySafe(state, DirectionHelper.FACING);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> graphicsHolderNew.rotateYDegrees(-facing.asRotation()));

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

            if ((renderType != RenderType.NONE) && (IBlock.getStatePropertySafe(state, SIDE_EXTENDED) != EnumSide.SINGLE)) {
                final float width = leftBlocks + rightBlocks + 1 - sidePadding * 2;
                final float height = 1 - topPadding - bottomPadding;
                final int arrowDirection = IBlock.getStatePropertySafe(state, arrowDirectionProperty);

                final Identifier identifier = DynamicTextureCache.instance.getRouteMapTRT(platformId, false, arrowDirection == 2, width / height, transparentWhite).identifier;

                MainRenderer.scheduleRender(identifier, false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
                    storedMatrixTransformations.transform(graphicsHolderNew, offset);
                    IDrawing.drawTexture(graphicsHolderNew, leftBlocks == 0 ? sidePadding : 0, topPadding, 0, 1 - (rightBlocks == 0 ? sidePadding : 0), 1 - bottomPadding, 0, (leftBlocks - (leftBlocks == 0 ? 0 : sidePadding)) / width, 0, (width - rightBlocks + (rightBlocks == 0 ? 0 : sidePadding)) / width, 1, facing.getOpposite(), color, light);
                    graphicsHolderNew.pop();
                });
            }
        });
    }

    @Override
    protected RenderType getRenderType(World world, BlockPos pos, BlockState state) {
        if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER) {
            return RenderType.NONE;
        } else {
            return RenderType.ROUTE;
        }
    }

    @Override
    protected void renderAdditional(StoredMatrixTransformations storedMatrixTransformations, long platformId, BlockState state, int leftBlocks, int rightBlocks, Direction facing, int color, int light) {
    }
}
