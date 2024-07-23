package ziyue.tjmetro.mod.block;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockRouteSignBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.block.base.IRailwaySign;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockStationNamePlate extends BlockRailwaySignBase
{
    public static final IntegerProperty ARROW_DIRECTION = IntegerProperty.of("arrow_direction", 0, 2);

    public BlockStationNamePlate() {
        this(BlockHelper.createBlockSettings(true));
    }

    public BlockStationNamePlate(BlockSettings settings) {
        super(settings, 8, false);
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingItem(world, player, item -> {
            final Direction facing = IBlock.getStatePropertySafe(state, FACING);
            final Direction hitSide = hit.getSide();
            if (hitSide == facing || hitSide == facing.getOpposite()) {
                final BlockPos checkPos = findEndWithDirection(world, pos, hitSide.getOpposite(), false);
                if (checkPos != null) {
                    if (item == org.mtr.mod.Items.BRUSH.get()) {
                        world.setBlockState(checkPos, world.getBlockState(checkPos).cycle(new Property<>(ARROW_DIRECTION.data)));
                    } else {
                        Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(checkPos));
                    }
                }
            }
        }, null, Items.BRUSH.get(), ItemList.WRENCH.get());
    }

    @NotNull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return IRailwaySign.getStateForNeighborUpdate(state, direction, neighborState, BlockList.STATION_NAME_PLATE_MIDDLE.get());
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient()) return;

        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i <= getMiddleLength(); i++) {
            world.setBlockState(pos.offset(facing.rotateYClockwise(), i), BlockList.STATION_NAME_PLATE_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data), 3);
        }
        world.setBlockState(pos.offset(facing.rotateYClockwise(), getMiddleLength() + 1), state.getBlock().getDefaultState().with(new Property<>(FACING.data), facing.getOpposite().data).with(new Property<>(ARROW_DIRECTION.data), 0), 3);
        world.updateNeighbors(pos, Blocks.getAirMapped());
        state.updateNeighbors(new WorldAccess(world.data), pos, 3);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (this == BlockList.STATION_NAME_PLATE_MIDDLE.get().data) {
            return IBlock.getVoxelShapeByDirection(0, 3, 7, 16, 12, 9, facing);
        } else {
            final VoxelShape main = IBlock.getVoxelShapeByDirection(0, 3, 7, 16, 12, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(-1, 3, 7, 0.25, 16, 9, facing);
            return VoxelShapes.union(main, pole);
        }
    }

    @Override
    public void addTooltips(ItemStack stack, @org.jetbrains.annotations.Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
    }

    @Override
    protected int getMiddleLength() {
        return 2;
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.station_name_plate";
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(ARROW_DIRECTION);
    }

    @Override
    protected BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.STATION_NAME_PLATE_MIDDLE.get());
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == BlockList.STATION_NAME_PLATE_MIDDLE.get().data) return null;
        else return new BlockEntity(blockPos, blockState);
    }

    public static class BlockEntity extends BlockRouteSignBase.BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_PLATE.get(), pos, state);
        }
    }
}
