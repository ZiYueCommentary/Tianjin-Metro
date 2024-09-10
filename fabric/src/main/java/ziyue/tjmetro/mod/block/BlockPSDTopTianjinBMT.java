package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.BlockPSDAPGBase;
import org.mtr.mod.block.BlockPSDTop;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagPSDTianjinBMT;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @see BlockPSDTop
 * @since 1.0.0-beta-2
 */

public class BlockPSDTopTianjinBMT extends BlockPSDTop implements BlockFlagPSDTianjinBMT
{
    public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.of("arrow_direction", 0, 1);
    public static final EnumProperty<EnumPSDType> STYLE = EnumProperty.of("style", EnumPSDType.class);

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingItem(world, player, item -> {
            if (item.data == ItemList.WRENCH.get().data) {
                world.setBlockState(pos, state.cycle(new Property<>(STYLE.data)));
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), new Property<>(STYLE.data), 1);
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), new Property<>(STYLE.data), 1);
            } else {
                world.setBlockState(pos, state.cycle(new Property<>(ARROW_DIRECTION.data)));
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), new Property<>(ARROW_DIRECTION.data), 1);
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), new Property<>(ARROW_DIRECTION.data), 1);
            }
        }, null, org.mtr.mod.Items.BRUSH.get());
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && !(neighborState.getBlock().data instanceof BlockPSDAPGBase)) {
            return Blocks.getAirMapped().getDefaultState();
        } else {
            return getActualState(world, pos);
        }
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.PSD_GLASS_TIANJIN_BMT.get();
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(STYLE);
        properties.add(FACING);
        properties.add(SIDE_EXTENDED);
        properties.add(AIR_LEFT);
        properties.add(AIR_RIGHT);
        properties.add(ARROW_DIRECTION);
    }

    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    public static BlockState getActualState(WorldAccess world, BlockPos pos) {
        Direction facing = null;
        EnumSide side = null;
        boolean airLeft = false, airRight = false;

        final BlockState stateBelow = world.getBlockState(pos.down());
        final Block blockBelow = stateBelow.getBlock();
        if (blockBelow.data instanceof BlockPSDGlassTianjinBMT || blockBelow.data instanceof BlockPSDDoorTianjinBMT || blockBelow.data instanceof BlockPSDGlassEndTianjinBMT) {
            if (blockBelow.data instanceof BlockPSDDoorTianjinBMT) {
                side = IBlock.getStatePropertySafe(stateBelow, SIDE);
            } else {
                side = IBlock.getStatePropertySafe(stateBelow, SIDE_EXTENDED);
            }

            if (blockBelow.data instanceof BlockPSDGlassEndTianjinBMT) {
                if (IBlock.getStatePropertySafe(stateBelow, new Property<>(BlockPSDGlassEndTianjinBMT.TOUCHING_LEFT.data)) == BlockPSDGlassEndTianjinBMT.EnumPSDAPGGlassEndSide.AIR) {
                    airLeft = true;
                }
                if (IBlock.getStatePropertySafe(stateBelow, new Property<>(BlockPSDGlassEndTianjinBMT.TOUCHING_RIGHT.data)) == BlockPSDGlassEndTianjinBMT.EnumPSDAPGGlassEndSide.AIR) {
                    airRight = true;
                }
            }

            facing = IBlock.getStatePropertySafe(stateBelow, FACING);
        }

        final BlockState oldState = world.getBlockState(pos);
        BlockState neighborState = (oldState.getBlock().data instanceof BlockPSDTop ? oldState : BlockList.PSD_TOP_TIANJIN_BMT.get().getDefaultState()).with(new Property<>(AIR_LEFT.data), airLeft).with(new Property<>(AIR_RIGHT.data), airRight);
        if (facing != null) {
            neighborState = neighborState.with(new Property<>(FACING.data), facing.data);
        }
        if (side != null) {
            neighborState = neighborState.with(new Property<>(SIDE_EXTENDED.data), side);
        }
        return neighborState;
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderPSDTopTianjin
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.PSD_TOP_TIANJIN_BMT.get(), pos, state);
        }
    }

    public enum EnumPSDType implements StringIdentifiable
    {
        BMT("bmt"),
        TRT("trt");

        final String name;

        EnumPSDType(String name) {
            this.name = name;
        }

        @Nonnull
        @Override
        public String asString2() {
            return this.name;
        }
    }
}
