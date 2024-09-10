package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.EntityModelExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ModelPartExtension;
import org.mtr.mod.Init;
import org.mtr.mod.block.*;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;

public class RenderPSDTianjinBMTDoor<T extends BlockPSDAPGDoorBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IGui, IBlock
{
    protected static final ModelSingleCube MODEL_PSD = new ModelSingleCube(36, 18, 0, 0, 0, 16, 16, 2);
    protected static final ModelSingleCube MODEL_PSD_END_LEFT_1 = new ModelSingleCube(20, 18, 0, 0, 0, 8, 16, 2);
    protected static final ModelSingleCube MODEL_PSD_END_RIGHT_1 = new ModelSingleCube(20, 18, 8, 0, 0, 8, 16, 2);
    protected static final ModelSingleCube MODEL_PSD_END_LEFT_2 = new ModelSingleCube(20, 18, 8, 0, 2, 8, 16, 2);
    protected static final ModelSingleCube MODEL_PSD_END_RIGHT_2 = new ModelSingleCube(20, 18, 0, 0, 2, 8, 16, 2);
    protected static final ModelSingleCube MODEL_PSD_LIGHT_LEFT = new ModelSingleCube(16, 16, 0, -3.7F, 5.3F, 1, 1, 1);
    protected static final ModelSingleCube MODEL_PSD_LIGHT_RIGHT = new ModelSingleCube(16, 16, 15, -3.7F, 5.3F, 1, 1, 1);
    protected static final ModelSingleCube MODEL_PSD_DOOR_LOCKED = new ModelSingleCube(6, 6, 5, 6, 1, 6, 6, 0);

    public RenderPSDTianjinBMTDoor(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos blockPos = entity.getPos2();
        final Direction facing = IBlock.getStatePropertySafe(world, blockPos, BlockPSDAPGDoorBase.FACING);
        final boolean side = IBlock.getStatePropertySafe(world, blockPos, BlockPSDAPGDoorBase.SIDE) == EnumSide.RIGHT;
        final boolean half = IBlock.getStatePropertySafe(world, blockPos, BlockPSDAPGDoorBase.HALF) == DoubleBlockHalf.UPPER;
        final boolean end = IBlock.getStatePropertySafe(world, blockPos, BlockPSDAPGDoorBase.END);
        final boolean unlocked = IBlock.getStatePropertySafe(world, blockPos, BlockPSDAPGDoorBase.UNLOCKED);
        final double open = Math.min(entity.getDoorValue(), 1);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(-facing.asRotation());
            graphicsHolderNew.rotateXDegrees(180);
        });
        final StoredMatrixTransformations storedMatrixTransformationsLight = storedMatrixTransformations.copy();

        if (half) {
            MainRenderer.scheduleRender(new Identifier(String.format("mtr:textures/block/light_%s.png", open > 0 ? "on" : "off")), false, open > 0 ? QueuedRenderLayer.LIGHT : QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
                storedMatrixTransformationsLight.transform(graphicsHolderNew, offset);
                (side ? MODEL_PSD_LIGHT_RIGHT : MODEL_PSD_LIGHT_LEFT).render(graphicsHolderNew, light, overlay, 1, 1, 1, 1);
                graphicsHolderNew.pop();
            });
        }
        if (end) {
            MainRenderer.scheduleRender(new Identifier(String.format("mtr:textures/block/psd_door_end_%s_%s_2_%s.png", half ? "top" : "bottom", side ? "right" : "left", "1")), false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
                storedMatrixTransformationsLight.transform(graphicsHolderNew, offset);
                graphicsHolderNew.translate(open / 2 * (side ? -1 : 1), 0, 0);
                (side ? MODEL_PSD_END_RIGHT_2 : MODEL_PSD_END_LEFT_2).render(graphicsHolderNew, light, overlay, 1, 1, 1, 1);
                graphicsHolderNew.pop();
            });
        }

        storedMatrixTransformations.add(matricesNew -> matricesNew.translate(open * (side ? -1 : 1), 0, 0));

        if (end) {
            MainRenderer.scheduleRender(new Identifier(String.format("mtr:textures/block/psd_door_end_%s_%s_1_%s.png", half ? "top" : "bottom", side ? "right" : "left", "1")), false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
                storedMatrixTransformations.transform(graphicsHolderNew, offset);
                (side ? MODEL_PSD_END_RIGHT_1 : MODEL_PSD_END_LEFT_1).render(graphicsHolderNew, light, overlay, 1, 1, 1, 1);
                graphicsHolderNew.pop();
            });
        } else {
            MainRenderer.scheduleRender(new Identifier(String.format("mtr:textures/block/psd_door_%s_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left", "1")), false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
                storedMatrixTransformations.transform(graphicsHolderNew, offset);
                MODEL_PSD.render(graphicsHolderNew, light, overlay, 1, 1, 1, 1);
                graphicsHolderNew.pop();
            });
        }
        if (half && !unlocked) {
            MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/sign/door_not_in_use.png"), false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
                storedMatrixTransformations.transform(graphicsHolderNew, offset);
                if (end) {
                    graphicsHolderNew.translate(side ? 0.25 : -0.25, 0, 0);
                }
                MODEL_PSD_DOOR_LOCKED.render(graphicsHolderNew, light, overlay, 1, 1, 1, 1);
                graphicsHolderNew.pop();
            });
        }
    }

    @Override
    public boolean rendersOutsideBoundingBox2(T blockEntity) {
        return true;
    }

    protected static class ModelSingleCube extends EntityModelExtension<EntityAbstractMapping>
    {
        protected final ModelPartExtension cube;

        protected ModelSingleCube(int textureWidth, int textureHeight, float x, float y, float z, int length, int height, int depth) {
            super(textureWidth, textureHeight);
            cube = createModelPart();
            cube.setTextureUVOffset(0, 0).addCuboid(x - 8, y - 16, z - 8, length, height, depth, 0, false);
            buildModel();
        }

        @Override
        public void render(GraphicsHolder graphicsHolder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            cube.render(graphicsHolder, 0, 0, 0, packedLight, packedOverlay);
        }

        @Override
        public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        }
    }
}
