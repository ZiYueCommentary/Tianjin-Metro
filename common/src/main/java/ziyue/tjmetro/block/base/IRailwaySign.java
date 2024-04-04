package ziyue.tjmetro.block.base;

import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;

/**
 * Methods that requires specific "Middle Block" of Railway Sign.
 *
 * @author ZiYueCommentary
 * @see BlockRailwaySignBase
 * @since beta-1
 */

public interface IRailwaySign
{
    /**
     * @see mtr.block.BlockRailwaySign#setPlacedBy(Level, BlockPos, BlockState, LivingEntity, ItemStack)
     */
    static void setPlacedBy(Level world, BlockPos pos, BlockState state, Block middle, int middleLength) {
        if (world.isClientSide) return;
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i <= middleLength; i++) {
            world.setBlock(pos.relative(facing.getClockWise(), i), middle.defaultBlockState().setValue(FACING, facing), 3);
        }
        world.setBlock(pos.relative(facing.getClockWise(), middleLength + 1), state.getBlock().defaultBlockState().setValue(FACING, facing.getOpposite()), 3);
        world.updateNeighborsAt(pos, Blocks.AIR);
        state.updateNeighbourShapes(world, pos, 3);
    }

    /**
     * @see mtr.block.BlockRailwaySign#updateShape(BlockState, Direction, BlockState, LevelAccessor, BlockPos, BlockPos)
     */
    static BlockState updateShape(BlockState state, Direction direction, BlockState newState, Block middle) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = (direction == facing.getClockWise()) || state.is(middle) && (direction == facing.getCounterClockWise());
        if (isNext && !(newState.getBlock() instanceof BlockRailwaySignBase)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    /**
     * @see mtr.block.BlockRailwaySign#getShape(BlockState, BlockGetter, BlockPos, CollisionContext)
     */
    static VoxelShape getShape(BlockState state, int xStart, Block middle) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (state.is(middle)) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
        } else {
            final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
            return Shapes.or(main, pole);
        }
    }

    static BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite, Block middle) {
        int i = 0;
        while (true) {
            final BlockPos checkPos = startPos.relative(direction.getCounterClockWise(), i);
            final BlockState checkState = world.getBlockState(checkPos);
            if (checkState.getBlock() instanceof BlockRailwaySignBase) {
                final Direction facing = IBlock.getStatePropertySafe(checkState, FACING);
                if (!checkState.is(middle) && (facing == direction || allowOpposite && facing == direction.getOpposite())) {
                    return checkPos;
                }
            } else {
                return null;
            }
            i++;
        }
    }
}
