package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.Items;
import org.mtr.mod.block.BlockRouteSignBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.block.base.IRailwaySign;
import ziyue.tjmetro.mod.data.IGuiExtension;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since 1.0.0
 */

public class BlockRouteMapBMT extends BlockRailwaySignBase
{
    public BlockRouteMapBMT() {
        this(Blocks.createDefaultBlockSettings(true, state -> 10));
    }

    public BlockRouteMapBMT(BlockSettings blockSettings) {
        super(blockSettings, 6, false);
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction facing = ctx.getPlayerFacing();
        if (!IBlock.isReplaceable(ctx, facing.rotateYClockwise(), getMiddleLength() + 2)) {
            return null;
        }
        for (int i = 0; i < getMiddleLength() + 2; i++) {
            if (!ctx.getWorld().getBlockState(ctx.getBlockPos().down().offset(facing.rotateYClockwise(), i)).canReplace(ctx)) {
                return null;
            }
        }
        return getDefaultState2().with(new Property<>(FACING.data), facing.data);
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingItem(world, player, item -> {
            final BlockPos blockPos = IBlock.getStatePropertySafe(world, pos, HALF) == DoubleBlockHalf.LOWER ? pos.up() : pos;
            final Direction facing = IBlock.getStatePropertySafe(state, FACING);
            final Direction hitSide = hit.getSide();
            if (hitSide == facing || hitSide == facing.getOpposite()) {
                final BlockPos checkPos = findEndWithDirection(world, blockPos, hitSide.getOpposite(), false);
                if (checkPos != null) {
                    if (item.data == Items.BRUSH.get().data) {
                        world.setBlockState(checkPos, world.getBlockState(checkPos).cycle(new Property<>(SIDE.data)));
                    } else {
                        Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(checkPos));
                    }
                }
            }
        }, null, Items.BRUSH.get(), ItemList.WRENCH.get());
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return IRailwaySign.getStateForNeighborUpdate(state, direction, neighborState, BlockList.ROUTE_MAP_BMT_MIDDLE.get());
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient()) return;

        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        world.setBlockState(pos.down(), state.getBlock().getDefaultState().with(new Property<>(FACING.data), facing.data).with(new Property<>(HALF.data), DoubleBlockHalf.LOWER), 3);
        final BlockState middleState = BlockList.ROUTE_MAP_BMT_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data);
        for (int i = 1; i <= getMiddleLength(); i++) {
            final BlockPos blockPos = pos.offset(facing.rotateYClockwise(), i);
            world.setBlockState(blockPos, middleState.with(new Property<>(HALF.data), DoubleBlockHalf.UPPER), 3);
            world.setBlockState(blockPos.down(), middleState.with(new Property<>(HALF.data), DoubleBlockHalf.LOWER), 3);
        }
        final BlockPos blockPos = pos.offset(facing.rotateYClockwise(), getMiddleLength() + 1);
        world.setBlockState(blockPos, state.getBlock().getDefaultState().with(new Property<>(FACING.data), facing.getOpposite().data).with(new Property<>(HALF.data), DoubleBlockHalf.UPPER), 3);
        world.setBlockState(blockPos.down(), state.getBlock().getDefaultState().with(new Property<>(FACING.data), facing.getOpposite().data).with(new Property<>(HALF.data), DoubleBlockHalf.LOWER), 3);
        world.updateNeighbors(pos, org.mtr.mapping.holder.Blocks.getAirMapped());
        state.updateNeighbors(new WorldAccess(world.data), pos, 3);
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final BlockPos blockPos = IBlock.getStatePropertySafe(world, pos, HALF) == DoubleBlockHalf.LOWER ? pos.up() : pos;

        final BlockPos checkPos = findEndWithDirection(world, blockPos, facing, true);
        if (checkPos != null) {
            IBlockExtension.breakBlock(world, checkPos);
            IBlockExtension.breakBlock(world, checkPos.down());
        }

        IBlockExtension.breakBlock(world, pos.down());
        super.onBreak2(world, pos, state, player);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final DoubleBlockHalf half = IBlock.getStatePropertySafe(state, HALF);
        if (this == BlockList.ROUTE_MAP_BMT_MIDDLE.get().data) {
            if (half == DoubleBlockHalf.UPPER) {
                return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
            } else {
                return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 16, 9, facing);
            }
        } else {
            if (half == DoubleBlockHalf.UPPER) {
                final VoxelShape pole = IBlock.getVoxelShapeByDirection(3, 0, 7, 4.25, 16, 9, facing);
                final VoxelShape plate = IBlock.getVoxelShapeByDirection(4.25, 0, 7, 16, 12, 9, facing);
                return VoxelShapes.union(pole, plate);
            } else {
                return IBlock.getVoxelShapeByDirection(3, 0, 7, 16, 16, 9, facing);
            }
        }
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.station_name_plate"));
    }

    @Override
    protected int getMiddleLength() {
        return 1;
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.route_map_bmt";
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(SIDE);
        properties.add(HALF);
    }

    @Override
    protected BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.ROUTE_MAP_BMT_MIDDLE.get());
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == BlockList.ROUTE_MAP_BMT_MIDDLE.get().data || IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.LOWER)
            return null;
        else return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderRouteMapBMT
     * @see ziyue.tjmetro.mod.screen.RailwaySignScreen
     * @since 1.0.0
     */
    public static class BlockEntity extends BlockRouteSignBase.BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.ROUTE_MAP_BMT.get(), pos, state);
        }
    }
}
