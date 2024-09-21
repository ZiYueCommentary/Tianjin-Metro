package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.block.base.IRailwaySign;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author ZiYueCommentary
 * @see BlockRailwaySignWall
 * @see BlockEntity
 * @since 1.0.0-beta-1
 */

public class BlockRailwaySignWallBig extends BlockRailwaySignWall
{
    public BlockRailwaySignWallBig(int length) {
        super(length);
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = ((!IBlock.getStatePropertySafe(state, EOS) && (direction == facing.rotateYClockwise())) || IBlockExtension.isBlock(state, BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get()) && (direction == facing.rotateYCounterclockwise()));
        if (isNext && !(neighborState.getBlock().data instanceof BlockRailwaySignWallBig)) {
            return Blocks.getAirMapped().getDefaultState();
        } else {
            return state;
        }
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient()) return;

        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i < getMiddleLength(); i++) {
            world.setBlockState(pos.offset(facing.rotateYClockwise(), i), BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data).with(new Property<>(EOS.data), false), 3);
        }
        world.setBlockState(pos.offset(facing.rotateYClockwise(), getMiddleLength()), BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data).with(new Property<>(EOS.data), true), 3);
        world.updateNeighbors(pos, Blocks.getAirMapped());
        state.updateNeighbors(new WorldAccess(world.data), pos, 3);
    }

    @Override
    protected BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get());
    }

    @Override
    protected int getMiddleLength() {
        return length - 1;
    }

    @Override
    public @Nonnull String getTranslationKey2() {
        return "block.tjmetro.railway_sign_wall_big";
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get().data)
            return null;
        else
            return new BlockRailwaySignWallBig.BlockEntity(length, blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderRailwaySignWall
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(int length, BlockPos pos, BlockState state) {
            super(getType(length), length, pos, state);
        }

        public static BlockEntityType<?> getType(int length) {
            switch (length) {
                case 2:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_2.get();
                case 3:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_3.get();
                case 4:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_4.get();
                case 5:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_5.get();
                case 6:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_6.get();
                case 7:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_7.get();
                case 8:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_8.get();
                case 9:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_9.get();
                case 10:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_10.get();
                default:
                    return null;
            }
        }
    }
}
