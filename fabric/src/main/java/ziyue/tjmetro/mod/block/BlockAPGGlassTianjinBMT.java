package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.BlockAPGGlass;
import org.mtr.mod.block.BlockPSDTop;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagAPGTianjinBMT;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class BlockAPGGlassTianjinBMT extends BlockAPGGlass implements BlockFlagAPGTianjinBMT
{
    public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.of("propagate_property", 0, 2);
    public static final EnumProperty<EnumDoorType> STYLE = EnumProperty.of("style", EnumDoorType.class);

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final double y = hit.getPos().getYMapped();
        if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
            return IBlock.checkHoldingItem(world, player, item -> {
                if (item.data == ItemList.WRENCH.get().data) {
                    world.setBlockState(pos, state.cycle(new Property<>(STYLE.data)));
                    BiConsumer<Boolean, Direction> setStyle = (bool, direction) -> {
                        EnumDoorType style = IBlock.getStatePropertySafe(world, pos, STYLE);
                        BlockPos offsetPos = pos;
                        for (; ; ) {
                            if (IBlockExtension.isBlock(world.getBlockState(offsetPos), BlockList.APG_DOOR_TIANJIN_BMT.get())) {
                                offsetPos = offsetPos.offset(direction);
                                if (bool) {
                                    final int id = (style.asId() - 1);
                                    style = EnumDoorType.byId(id < 0 ? 2 : id % 3);
                                } else {
                                    style = EnumDoorType.byId((style.asId() + 1) % 3);
                                }
                            } else if (IBlockExtension.isBlock(world.getBlockState(offsetPos), BlockList.APG_GLASS_TIANJIN_BMT.get())) {
                                world.setBlockState(offsetPos, world.getBlockState(offsetPos).with(new Property<>(STYLE.data), style));
                            } else {
                                break;
                            }
                            offsetPos = offsetPos.offset(direction);
                        }
                    };
                    setStyle.accept(true, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise());
                    setStyle.accept(false, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise());
                } else {
                    world.setBlockState(pos, state.cycle(new Property<>(ARROW_DIRECTION.data)));
                    propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYClockwise(), new Property<>(ARROW_DIRECTION.data), 3);
                    propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).rotateYCounterclockwise(), new Property<>(ARROW_DIRECTION.data), 3);
                }
            }, null, org.mtr.mod.Items.BRUSH.get(), ItemList.WRENCH.get());
        } else {
            return super.onUse2(state, world, pos, player, hand, hit);
        }
    }

    @Override
    protected boolean isAPG() {
        return true;
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.APG_GLASS_TIANJIN_BMT.get();
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(HALF);
        properties.add(SIDE_EXTENDED);
        properties.add(ARROW_DIRECTION);
        properties.add(STYLE);
    }

    public static class BlockEntity extends BlockPSDTop.BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.APG_GLASS_TIANJIN_BMT.get(), pos, state);
        }
    }

    public enum EnumDoorType implements StringIdentifiable
    {
        ROUTE(0, "route"),
        STATION_NAME(1, "station_name"),
        NEXT_STATION(2, "next_station");

        final int id;
        final String name;

        EnumDoorType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int asId() {
            return this.id;
        }

        public static EnumDoorType byId(int id) {
            switch (id) {
                case 0:
                    return ROUTE;
                case 1:
                    return STATION_NAME;
                case 2:
                    return NEXT_STATION;
                default:
                    throw new IllegalStateException();
            }
        }

        @Nonnull
        @Override
        public String asString2() {
            return this.name;
        }
    }
}
