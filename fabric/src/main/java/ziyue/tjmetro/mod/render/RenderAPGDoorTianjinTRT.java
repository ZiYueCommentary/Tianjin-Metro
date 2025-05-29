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
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.block.BlockAPGDoorTianjinTRT;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.block.BlockAPGDoorTianjinTRT
 * @see RenderRouteBase
 * @since 1.0.0-beta-2
 */

public class RenderAPGDoorTianjinTRT<T extends BlockPSDAPGDoorBase.BlockEntityBase> extends BlockEntityRenderer<T> implements IGui, IBlock
{
    protected static final ModelSingleCube MODEL_APG_TOP = new ModelSingleCube(34, 9, 0, 8, 1, 16, 8, 1);
    protected static final ModelAPGDoorBottom MODEL_APG_BOTTOM = new ModelAPGDoorBottom();
    protected static final ModelSingleCube MODEL_APG_DOOR_LOCKED = new ModelSingleCube(6, 6, 5, 10, 1, 6, 6, 0);

    public RenderAPGDoorTianjinTRT(Argument dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        entity.tick(tickDelta);

        final BlockPos blockPos = entity.getPos2();
        final Direction facing = IBlock.getStatePropertySafe(world, blockPos, BlockAPGDoorTianjinTRT.FACING);
        final boolean side = IBlock.getStatePropertySafe(world, blockPos, BlockAPGDoorTianjinTRT.SIDE) == EnumSide.RIGHT;
        final boolean half = IBlock.getStatePropertySafe(world, blockPos, BlockAPGDoorTianjinTRT.HALF) == DoubleBlockHalf.UPPER;
        final boolean unlocked = IBlock.getStatePropertySafe(world, blockPos, BlockAPGDoorTianjinTRT.UNLOCKED);
        final double open = Math.min(entity.getDoorValue(), 1);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(0.5 + entity.getPos2().getX(), entity.getPos2().getY(), 0.5 + entity.getPos2().getZ());
        storedMatrixTransformations.add(graphicsHolderNew -> {
            graphicsHolderNew.rotateYDegrees(-facing.asRotation());
            graphicsHolderNew.rotateXDegrees(180);
        });

        // Traditional lights.
        if (half) {
            final Block sideBlock = world.getBlockState(blockPos.offset(side ? facing.rotateYClockwise() : facing.rotateYCounterclockwise())).getBlock();
            if (sideBlock.data instanceof BlockAPGGlass || sideBlock.data instanceof BlockAPGGlassEnd) {
                if (open > 0) {
                    tryUpdateLightState(world, blockPos, BlockAPGDoorTianjinTRT.LightProperty.LIGHT_ON);
                } else {
                    tryUpdateLightState(world, blockPos, BlockAPGDoorTianjinTRT.LightProperty.LIGHT_OFF);
                }
            } else {
                tryUpdateLightState(world, blockPos, BlockAPGDoorTianjinTRT.LightProperty.NO_LIGHT);
            }
        }

        storedMatrixTransformations.add(matricesNew -> matricesNew.translate(open * (side ? -1 : 1), 0, 0));

        MainRenderer.scheduleRender(new Identifier(Reference.MOD_ID, String.format("textures/block/apg_door_tianjin_trt_%s_%s.png", half ? "top" : "bottom", side ? "right" : "left")), false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
            storedMatrixTransformations.transform(graphicsHolderNew, offset);
            (half ? MODEL_APG_TOP : MODEL_APG_BOTTOM).render(graphicsHolderNew, light, overlay, 1, 1, 1, 1);
            graphicsHolderNew.pop();
        });
        if (half && !unlocked) {
            MainRenderer.scheduleRender(new Identifier(Init.MOD_ID, "textures/block/sign/door_not_in_use.png"), false, QueuedRenderLayer.EXTERIOR, (graphicsHolderNew, offset) -> {
                storedMatrixTransformations.transform(graphicsHolderNew, offset);
                MODEL_APG_DOOR_LOCKED.render(graphicsHolderNew, light, overlay, 1, 1, 1, 1);
                graphicsHolderNew.pop();
            });
        }
    }

    @Override
    public boolean rendersOutsideBoundingBox2(T blockEntity) {
        return true;
    }

    protected void tryUpdateLightState(World world, BlockPos blockPos, BlockAPGDoorTianjinTRT.LightProperty lightProperty) {
        if (IBlock.getStatePropertySafe(world, blockPos, BlockAPGDoorTianjinTRT.LIGHT) != lightProperty) {
            world.setBlockState(blockPos, world.getBlockState(blockPos).with(new Property<>(BlockAPGDoorTianjinTRT.LIGHT.data), lightProperty));
        }
    }

    protected static class ModelSingleCube extends EntityModelExtension<EntityAbstractMapping>
    {
        protected final ModelPartExtension cube;

        protected ModelSingleCube(int textureWidth, int textureHeight, int x, int y, int z, int length, int height, int depth) {
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

    protected static class ModelAPGDoorBottom extends EntityModelExtension<EntityAbstractMapping>
    {
        protected final ModelPartExtension bone;

        protected ModelAPGDoorBottom() {
            super(34, 27);

            bone = createModelPart();
            bone.setTextureUVOffset(0, 0).addCuboid(-8, -16, -7, 16, 16, 1, 0, false);
            bone.setTextureUVOffset(0, 17).addCuboid(-8, -6, -8, 16, 6, 1, 0, false);

            final ModelPartExtension cube = bone.addChild();
            cube.setPivot(0, -6, -8);
            cube.setRotation(-0.7854F, 0, 0);
            cube.setTextureUVOffset(0, 24).addCuboid(-8, -2, 0, 16, 2, 1, 0, false);

            buildModel();
        }

        @Override
        public void render(GraphicsHolder graphicsHolder, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            bone.render(graphicsHolder, 0, 0, 0, packedLight, packedOverlay);
        }

        @Override
        public void setAngles2(EntityAbstractMapping entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        }
    }
}
