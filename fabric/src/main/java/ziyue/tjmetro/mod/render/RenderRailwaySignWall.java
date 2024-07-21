package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.Init;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import org.mtr.mod.resource.SignResource;
import ziyue.tjmetro.mod.block.BlockRailwaySignWallBig;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;

import static org.mtr.mod.render.RenderRailwaySign.*;

public class RenderRailwaySignWall<T extends BlockRailwaySignBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IBlock, IGui, IDrawing
{

    public RenderRailwaySignWall(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos pos = entity.getPos2();
        final BlockState state = world.getBlockState(pos);
        if (!(state.getBlock().data instanceof BlockRailwaySignBase block)) return;

        if (entity.getSignIds().length != block.length) return;

        final Direction facing = IBlock.getStatePropertySafe(state, DirectionHelper.FACING);
        final String[] signIds = entity.getSignIds();

        int backgroundColor = 0;
        for (final String signId : signIds) {
            if (signId != null) {
                final SignResource sign = getSign(signId);
                if (sign != null) {
                    if (sign.getBackgroundColor() != 0) {
                        backgroundColor = sign.getBackgroundColor();
                        break;
                    }
                }
            }
        }

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), 0.53125 + entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(-facing.asRotation());
            graphicsHolderNew.rotateZDegrees(180);
            graphicsHolderNew.translate(block.getXStart() / 16F - 0.5, -0.25, 0.493);
            if (entity instanceof BlockRailwaySignWallBig.BlockEntity) {
                graphicsHolderNew.translate(0, -0.218, -0.003);
                graphicsHolderNew.scale(2, 2, 2);
            }
        });

        graphicsHolder.push();
        graphicsHolder.translate(0.5, 0.53125, 0.5);
        graphicsHolder.rotateYDegrees(-facing.asRotation());
        graphicsHolder.rotateZDegrees(180);
        graphicsHolder.translate(block.getXStart() / 16F - 0.5, 0, -0.0625 - SMALL_OFFSET * 2);

        final int newBackgroundColor = backgroundColor | ARGB_BLACK;
        MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/white.png"), false, QueuedRenderLayer.LIGHT, (graphicsHolderNew, offset) -> {
            storedMatrixTransformations.transform(graphicsHolderNew, offset);
            IDrawing.drawTexture(graphicsHolderNew, 0, 0, SMALL_OFFSET, 0.5F * (signIds.length), 0.5F, SMALL_OFFSET, facing, newBackgroundColor, GraphicsHolder.getDefaultLight());
            graphicsHolderNew.pop();
        });
        for (int i = 0; i < signIds.length; i++) {
            if (signIds[i] != null) {
                drawSign(
                        graphicsHolder,
                        storedMatrixTransformations,
                        pos,
                        signIds[i],
                        0.5F * i,
                        0,
                        0.5F,
                        getMaxWidth(signIds, i, false),
                        getMaxWidth(signIds, i, true),
                        entity.getSelectedIds(),
                        facing,
                        backgroundColor | ARGB_BLACK,
                        (textureId, x, y, size, flipTexture) -> MainRenderer.scheduleRender(textureId, true, QueuedRenderLayer.LIGHT_TRANSLUCENT, (graphicsHolderNew, offset) -> {
                            storedMatrixTransformations.transform(graphicsHolderNew, offset);
                            IDrawing.drawTexture(graphicsHolderNew, x, y, size, size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1, facing, -1, GraphicsHolder.getDefaultLight());
                            graphicsHolderNew.pop();
                        })
                );
            }
        }

        graphicsHolder.pop();
    }
}
