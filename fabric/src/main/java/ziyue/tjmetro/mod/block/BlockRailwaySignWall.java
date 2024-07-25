package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.block.base.IRailwaySign;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockRailwaySignWall extends BlockRailwaySignBase implements IRailwaySign
{
    public static final BooleanProperty EOS = BooleanProperty.of("eos"); // end of sign

    public BlockRailwaySignWall(int length) {
        super(BlockHelper.createBlockSettings(true, blockState -> 15).noCollision(), length, false);
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction facing = ctx.getPlayerFacing();
        return IBlock.isReplaceable(ctx, facing.rotateYClockwise(), getMiddleLength() + 1) ? getDefaultState2().with(new Property<>(FACING.data), facing.data).with(new Property<>(EOS.data), false) : null;
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return IRailwaySign.getStateForNeighborUpdate(state, direction, neighborState, BlockList.RAILWAY_SIGN_WALL_MIDDLE.get());
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final BlockPos checkPos = findEndWithDirection(world, pos, facing, false);
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> {
            if (checkPos != null) {
                Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(checkPos));
            }
        });
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient()) return;

        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i < getMiddleLength(); i++) {
            world.setBlockState(pos.offset(facing.rotateYClockwise(), i), BlockList.RAILWAY_SIGN_WALL_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data).with(new Property<>(EOS.data), false), 3);
        }
        world.setBlockState(pos.offset(facing.rotateYClockwise(), getMiddleLength()), BlockList.RAILWAY_SIGN_WALL_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data).with(new Property<>(EOS.data), true), 3);
        world.updateNeighbors(pos, Blocks.getAirMapped());
        state.updateNeighbors(new WorldAccess(world.data), pos, 3);
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.mtr.railway_sign_length", length).formatted(TextFormatting.GRAY));
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.railway_sign_wall";
    }

    @Override
    protected BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_WALL_MIDDLE.get());
    }

    @Override
    public int getXStart() {
        return 0;
    }

    @Override
    protected int getMiddleLength() {
        return length / 2 - 1;
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(EOS);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == BlockList.RAILWAY_SIGN_WALL_MIDDLE.get().data)
            return null;
        else
            return new BlockEntity(length, blockPos, blockState);
    }

    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(int length, BlockPos pos, BlockState state) {
            super(getType(length), length, pos, state);
        }

        public static BlockEntityType<?> getType(int length) {
            switch (length) {
                case 4:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_4.get();
                case 6:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_6.get();
                case 8:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_8.get();
                case 10:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_10.get();
                default:
                    return null;
            }
        }
    }
}
