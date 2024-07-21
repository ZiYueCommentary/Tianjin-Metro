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

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

public class BlockPSDTopTianjin extends BlockPSDTop
{
    public static final EnumProperty<EnumDoorType> STYLE = EnumProperty.of("style", EnumDoorType.class);

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingItem(world, player, item -> {
            if (item == ItemList.WRENCH.get()) {
                if (BlockList.PSD_DOOR_TIANJIN.get().data == world.getBlockState(pos.down()).getBlock().data) {
                    world.setBlockState(pos, IBlockExtension.cycleBlockState(state, STYLE, value -> value != EnumDoorType.NEXT_STATION));
                    BlockPos pos1 = (IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == EnumSide.LEFT) ? pos.offset(IBlock.getStatePropertySafe(state, FACING).rotateYClockwise()) : pos.offset(IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise());
                    world.setBlockState(pos1, IBlockExtension.cycleBlockState(world.getBlockState(pos1), STYLE, value -> value != EnumDoorType.NEXT_STATION));
                } else {
                    world.setBlockState(pos, IBlockExtension.cycleBlockState(state, STYLE, EnumDoorType.DEFAULT, EnumDoorType.NEXT_STATION));
                    Consumer<Direction> setStyle = direction -> {
                        EnumDoorType style = IBlock.getStatePropertySafe(world, pos, STYLE);
                        BlockPos offsetPos = pos;
                        for (; ; ) {
                            if (BlockList.PSD_DOOR_TIANJIN.get().data == world.getBlockState(offsetPos.down()).getBlock().data) {
                                offsetPos = offsetPos.offset(direction);
                                style = (style == EnumDoorType.DEFAULT) ? EnumDoorType.NEXT_STATION : EnumDoorType.DEFAULT;
                            } else if (this == world.getBlockState(offsetPos).getBlock().data) {
                                world.setBlockState(offsetPos, world.getBlockState(offsetPos).with(new Property<>(STYLE.data), style));
                            } else {
                                break;
                            }
                            offsetPos = offsetPos.offset(direction);
                        }
                    };
                    setStyle.accept(IBlock.getStatePropertySafe(state, FACING).rotateYClockwise());
                    setStyle.accept(IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise());
                }
            } else if (item == org.mtr.mod.Items.BRUSH.get()) {
                world.setBlockState(pos, state.cycle(new Property<>(ARROW_DIRECTION.data)));
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), new Property<>(ARROW_DIRECTION.data), 1);
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), new Property<>(ARROW_DIRECTION.data), 1);
            } else {
                final boolean shouldBePersistent = IBlock.getStatePropertySafe(state, new Property<>(PERSISTENT.data)) == EnumPersistent.NONE;
                setState(world, pos, shouldBePersistent);
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
            }
        }, null, org.mtr.mod.Items.BRUSH.get(), ItemList.WRENCH.get(), org.mtr.mapping.holder.Items.getShearsMapped());
    }

    protected void setState(World world, BlockPos pos, boolean shouldBePersistent) {
        final Block blockBelow = world.getBlockState(pos.down()).getBlock();
        if (blockBelow.data instanceof BlockPSDDoorTianjin || blockBelow.data instanceof BlockPSDGlassTianjin || blockBelow.data instanceof BlockPSDGlassEndTianjin) {
            if (shouldBePersistent) {
                world.setBlockState(pos, world.getBlockState(pos).with(new Property<>(PERSISTENT.data), blockBelow.data instanceof BlockPSDDoorTianjin ? EnumPersistent.ARROW : blockBelow.data instanceof BlockPSDGlassTianjin ? EnumPersistent.ROUTE : EnumPersistent.BLANK));
            } else {
                world.setBlockState(pos, world.getBlockState(pos).with(new Property<>(PERSISTENT.data), EnumPersistent.NONE));
            }
        }
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.DOWN && IBlock.getStatePropertySafe(state, new Property<>(PERSISTENT.data)) == EnumPersistent.NONE && !(neighborState.getBlock().data instanceof BlockPSDAPGBase)) {
            return Blocks.getAirMapped().getDefaultState();
        } else {
            return getActualState(world, pos);
        }
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return super.asItem2();
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(STYLE);
        super.addBlockProperties(properties);
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
        if (blockBelow.data instanceof BlockPSDGlassTianjin || blockBelow.data instanceof BlockPSDDoorTianjin || blockBelow.data instanceof BlockPSDGlassEndTianjin) {
            if (blockBelow.data instanceof BlockPSDDoorTianjin) {
                side = IBlock.getStatePropertySafe(stateBelow, SIDE);
            } else {
                side = IBlock.getStatePropertySafe(stateBelow, SIDE_EXTENDED);
            }

            if (blockBelow.data instanceof BlockPSDGlassEndTianjin) {
                if (IBlock.getStatePropertySafe(stateBelow, new Property<>(BlockPSDGlassEndTianjin.TOUCHING_LEFT.data)) == BlockPSDGlassEndTianjin.EnumPSDAPGGlassEndSide.AIR) {
                    airLeft = true;
                }
                if (IBlock.getStatePropertySafe(stateBelow, new Property<>(BlockPSDGlassEndTianjin.TOUCHING_RIGHT.data)) == BlockPSDGlassEndTianjin.EnumPSDAPGGlassEndSide.AIR) {
                    airRight = true;
                }
            }

            facing = IBlock.getStatePropertySafe(stateBelow, FACING);
        }

        final BlockState oldState = world.getBlockState(pos);
        BlockState neighborState = (oldState.getBlock().data instanceof BlockPSDTop ? oldState : BlockList.PSD_TOP_TIANJIN.get().getDefaultState()).with(new Property<>(AIR_LEFT.data), airLeft).with(new Property<>(AIR_RIGHT.data), airRight);
        if (facing != null) {
            neighborState = neighborState.with(new Property<>(FACING.data), facing.data);
        }
        if (side != null) {
            neighborState = neighborState.with(new Property<>(SIDE_EXTENDED.data), side);
        }
        return neighborState;
    }

    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.PSD_TOP_TIANJIN.get(), pos, state);
        }
    }

    public enum EnumDoorType implements StringIdentifiable
    {
        DEFAULT("default"),
        STATION_NAME("station_name"),
        //NAME_AND_ROUTES_LEFT("name_and_routes_left"),
        //NAME_AND_ROUTES_RIGHT("name_and_routes_right"),
        //NAME_AND_ROUTES_BOTH("name_and_routes_both"),
        NEXT_STATION("next_station");

        final String name;

        EnumDoorType(String name) {
            this.name = name;
        }

        @Nonnull
        @Override
        public String asString2() {
            return this.name;
        }
    }
}
