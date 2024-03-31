package ziyue.tjmetro.blocks;

import mtr.Items;
import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDTop;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.IBlockExtends;
import ziyue.tjmetro.ItemList;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author ZiYueCommentary
 * @see BlockPSDTop
 * @see ziyue.tjmetro.items.ItemPSDTianjinBase
 * @since beta-1
 */

public class BlockPSDTopTianjin extends BlockPSDTop
{
    public static final EnumProperty<EnumDoorType> STYLE = EnumProperty.create("style", EnumDoorType.class);

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingItem(world, player, item -> {
            if (item == ItemList.WRENCH.get()) {
                if (BlockList.PSD_DOOR_TIANJIN.get() == world.getBlockState(pos.below()).getBlock()) {
                    world.setBlockAndUpdate(pos, IBlockExtends.cycleBlockState(state, STYLE, value -> value != EnumDoorType.NEXT_STATION));
                    BlockPos pos1 = (state.getValue(SIDE_EXTENDED) == EnumSide.LEFT) ? pos.relative(state.getValue(FACING).getClockWise()) : pos.relative(state.getValue(FACING).getCounterClockWise());
                    world.setBlockAndUpdate(pos1, IBlockExtends.cycleBlockState(world.getBlockState(pos1), STYLE, value -> value != EnumDoorType.NEXT_STATION));
                } else {
                    world.setBlockAndUpdate(pos, IBlockExtends.cycleBlockState(state, STYLE, EnumDoorType.DEFAULT, EnumDoorType.NEXT_STATION));
                    Consumer<Direction> setStyle = direction -> {
                        EnumDoorType style = IBlock.getStatePropertySafe(world, pos, STYLE);
                        BlockPos offsetPos = pos;
                        for (; ; ) {
                            if (BlockList.PSD_DOOR_TIANJIN.get() == world.getBlockState(offsetPos.below()).getBlock()) {
                                offsetPos = offsetPos.relative(direction);
                                style = (style == EnumDoorType.DEFAULT) ? EnumDoorType.NEXT_STATION : EnumDoorType.DEFAULT;
                            } else if (this == world.getBlockState(offsetPos).getBlock()) {
                                world.setBlockAndUpdate(offsetPos, world.getBlockState(offsetPos).setValue(STYLE, style));
                            } else {
                                break;
                            }
                            offsetPos = offsetPos.relative(direction);
                        }
                    };
                    setStyle.accept(IBlock.getStatePropertySafe(state, FACING).getClockWise());
                    setStyle.accept(IBlock.getStatePropertySafe(state, FACING).getCounterClockWise());
                }
            } else if (item == Items.BRUSH.get()) {
                world.setBlockAndUpdate(pos, state.cycle(ARROW_DIRECTION));
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), ARROW_DIRECTION, 1);
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), ARROW_DIRECTION, 1);
            } else {
                final boolean shouldBePersistent = IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE;
                setState(world, pos, shouldBePersistent);
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
                propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), offsetPos -> setState(world, offsetPos, shouldBePersistent), 1);
            }
        }, null, ItemList.WRENCH.get(), Items.BRUSH.get(), net.minecraft.world.item.Items.SHEARS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(STYLE);
    }

    @Override
    public Item asItem() {
        return ItemList.PSD_GLASS_TIANJIN.get();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        if (direction == Direction.DOWN && IBlock.getStatePropertySafe(state, PERSISTENT) == EnumPersistent.NONE && !(newState.getBlock() instanceof BlockPSDAPGBase)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return getActualState(world, pos);
        }
    }

    protected void setState(Level world, BlockPos pos, boolean shouldBePersistent) {
        final Block blockBelow = world.getBlockState(pos.below()).getBlock();
        if (blockBelow instanceof BlockPSDDoorTianjin || blockBelow instanceof BlockPSDGlassTianjin || blockBelow instanceof BlockPSDGlassEndTianjin) {
            if (shouldBePersistent) {
                world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(PERSISTENT, blockBelow instanceof BlockPSDDoorTianjin ? EnumPersistent.ARROW : blockBelow instanceof BlockPSDGlassTianjin ? EnumPersistent.ROUTE : EnumPersistent.BLANK));
            } else {
                world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(PERSISTENT, EnumPersistent.NONE));
            }
        }
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockPSDTopTianjin.TileEntityPSDTopTianjin(pos, state);
    }

    public static BlockState getActualState(BlockGetter world, BlockPos pos) {
        Direction facing = null;
        EnumSide side = null;
        boolean airLeft = false, airRight = false;

        final BlockState stateBelow = world.getBlockState(pos.below());
        final Block blockBelow = stateBelow.getBlock();
        if (blockBelow instanceof BlockPSDGlassTianjin || blockBelow instanceof BlockPSDDoorTianjin || blockBelow instanceof BlockPSDGlassEndTianjin) {
            if (blockBelow instanceof BlockPSDDoorTianjin) {
                side = IBlock.getStatePropertySafe(stateBelow, SIDE);
            } else {
                side = IBlock.getStatePropertySafe(stateBelow, SIDE_EXTENDED);
            }

            if (blockBelow instanceof BlockPSDGlassEndTianjin) {
                if (IBlock.getStatePropertySafe(stateBelow, BlockPSDGlassEndTianjin.TOUCHING_LEFT) == BlockPSDGlassEndTianjin.EnumPSDAPGGlassEndSide.AIR) {
                    airLeft = true;
                }
                if (IBlock.getStatePropertySafe(stateBelow, BlockPSDGlassEndTianjin.TOUCHING_RIGHT) == BlockPSDGlassEndTianjin.EnumPSDAPGGlassEndSide.AIR) {
                    airRight = true;
                }
            }

            facing = IBlock.getStatePropertySafe(stateBelow, FACING);
        }

        final BlockState oldState = world.getBlockState(pos);
        BlockState newState = (oldState.getBlock() instanceof BlockPSDTopTianjin ? oldState : BlockList.PSD_TOP_TIANJIN.get().defaultBlockState()).setValue(AIR_LEFT, airLeft).setValue(AIR_RIGHT, airRight);
        if (facing != null) {
            newState = newState.setValue(FACING, facing);
        }
        if (side != null) {
            newState = newState.setValue(SIDE_EXTENDED, side);
        }
        return newState;
    }

    public static class TileEntityPSDTopTianjin extends TileEntityRouteBase
    {
        public TileEntityPSDTopTianjin(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.PSD_TOP_TIANJIN_TILE_ENTITY.get(), pos, state);
        }
    }

    public enum EnumDoorType implements StringRepresentable
    {
        DEFAULT("default"),
        STATION_NAME("station_name"),
        //        NAME_AND_ROUTES_LEFT("name_and_routes_left"),
//        NAME_AND_ROUTES_RIGHT("name_and_routes_right"),
//        NAME_AND_ROUTES_BOTH("name_and_routes_both"),
        NEXT_STATION("next_station");


        final String name;

        EnumDoorType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
