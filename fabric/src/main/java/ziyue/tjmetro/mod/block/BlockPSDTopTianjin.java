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
import ziyue.tjmetro.mod.block.base.BlockFlagPSDTianjin;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @see BlockPSDTop
 * @since 1.0.0-beta-1
 */

public class BlockPSDTopTianjin extends BlockPSDTop implements BlockFlagPSDTianjin
{
    public final boolean jinjing;
    public static final EnumProperty<EnumPSDType> STYLE = EnumProperty.of("style", EnumPSDType.class);

    public BlockPSDTopTianjin(boolean jinjing) {
        this.jinjing = jinjing;
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingItem(world, player, item -> {
            if (item.data == ItemList.WRENCH.get().data) {
                if ((IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.ARROW) || (world.getBlockState(pos.down()).getBlock().data instanceof BlockPSDDoorTianjin)) {
                    world.setBlockState(pos, IBlockExtension.cycleBlockState(state, STYLE, value -> value != EnumPSDType.NEXT_STATION));
                    BlockPos pos1 = (IBlock.getStatePropertySafe(state, SIDE_EXTENDED) == EnumSide.LEFT) ? pos.offset(IBlock.getStatePropertySafe(state, FACING).rotateYClockwise()) : pos.offset(IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise());
                    world.setBlockState(pos1, IBlockExtension.cycleBlockState(world.getBlockState(pos1), STYLE, value -> value != EnumPSDType.NEXT_STATION));
                } else {
                    world.setBlockState(pos, IBlockExtension.cycleBlockState(state, STYLE, EnumPSDType.DEFAULT, EnumPSDType.NEXT_STATION));
                    Consumer<Direction> setStyle = direction -> {
                        EnumPSDType style = IBlock.getStatePropertySafe(world, pos, STYLE);
                        BlockPos offsetPos = pos;
                        for (; ; ) {
                            if (IBlock.getStatePropertySafe(world.getBlockState(offsetPos), PERSISTENT) == EnumPersistent.ARROW) {
                                offsetPos = offsetPos.offset(direction);
                                style = (style == EnumPSDType.DEFAULT) ? EnumPSDType.NEXT_STATION : EnumPSDType.DEFAULT;
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
            } else if (item.data == org.mtr.mod.Items.BRUSH.get().data) {
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
        if (blockBelow.data instanceof BlockFlagPSDTianjin) {
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
            return getActualState(world, pos, jinjing);
        }
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return jinjing ? ItemList.PSD_GLASS_TIANJIN_JINJING.get() : ItemList.PSD_GLASS_TIANJIN.get();
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(STYLE);
        super.addBlockProperties(properties);
    }

    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState, jinjing);
    }

    public static BlockState getActualState(WorldAccess world, BlockPos pos, boolean jinjing) {
        Direction facing = null;
        EnumSide side = null;
        boolean airLeft = false, airRight = false;

        final BlockState stateBelow = world.getBlockState(pos.down());
        final Block blockBelow = stateBelow.getBlock();
        if (blockBelow.data instanceof BlockFlagPSDTianjin) {
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
        BlockState neighborState = (oldState.getBlock().data instanceof BlockPSDTop ? oldState : (jinjing ? BlockList.PSD_TOP_TIANJIN_JINJING.get() : BlockList.PSD_TOP_TIANJIN.get()).getDefaultState()).with(new Property<>(AIR_LEFT.data), airLeft).with(new Property<>(AIR_RIGHT.data), airRight);
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
        public final boolean jinjing;

        public BlockEntity(BlockPos pos, BlockState state, boolean jinjing) {
            super(jinjing ? BlockEntityTypes.PSD_TOP_TIANJIN_JINJING.get() : BlockEntityTypes.PSD_TOP_TIANJIN.get(), pos, state);
            this.jinjing = jinjing;
        }
    }

    public enum EnumPSDType implements StringIdentifiable
    {
        DEFAULT("default"),
        STATION_NAME("station_name"),
        //NAME_AND_ROUTES_LEFT("name_and_routes_left"),
        //NAME_AND_ROUTES_RIGHT("name_and_routes_right"),
        //NAME_AND_ROUTES_BOTH("name_and_routes_both"),
        NEXT_STATION("next_station");

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
