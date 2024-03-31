package ziyue.tjmetro;

import mtr.Items;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;
import static ziyue.tjmetro.TianjinMetro.LOGGER;

/**
 * Some methods similar to methods in <b>IBlock</b>.
 *
 * @see mtr.block.IBlock
 * @since beta-1
 */

public interface IBlockExtends
{
    /**
     * Fence's collision height.
     *
     * @since beta-1
     */
    byte FENCE_HEIGHT = 24;

    /**
     * Replace block with air.
     *
     * @param pos block's position
     * @author ZiYueCommentary
     * @since beta-1
     */
    static void breakBlock(Level world, BlockPos pos) {
        try {
            world.setBlock(pos, world.getBlockState(pos).getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
        } catch (Exception exception) {
            LOGGER.warn("IBlockExtends.breakBlock: Property \"waterlogged\" at {} not found", pos.toShortString());
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
        }
    }

    /**
     * Specify a block, if block in pos is specified block, then replace it with air.
     *
     * @param pos   block's position
     * @param block specified block
     * @author ZiYueCommentary
     * @since beta-1
     */
    static void breakBlock(Level world, BlockPos pos, Block block) {
        try {
            if (world.getBlockState(pos).getBlock() == block) {
                world.setBlock(pos, world.getBlockState(pos).getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
            }
        } catch (Exception exception) {
            LOGGER.warn("IBlockExtends.breakBlock: Property \"waterlogged\" at {} not found", pos.toShortString());
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
        }
    }

    /**
     * Check the direction whether is horizontal direction.
     *
     * @author ZiYueCommentary
     * @since beta-1
     */
    static boolean isHorizontalDirection(Direction direction) {
        return (direction == Direction.EAST) || (direction == Direction.WEST) || (direction == Direction.NORTH) || (direction == Direction.SOUTH);
    }

    static <T extends Comparable<T>> BlockState cycleBlockState(BlockState state, Property<T> property, Predicate<T> includes) {
        return cycleBlockState(state, property, property.getPossibleValues().stream().filter(includes).toList());
    }

    @SafeVarargs
    static <T extends Comparable<T>> BlockState cycleBlockState(BlockState state, Property<T> property, T... includes) {
        return cycleBlockState(state, property, Arrays.asList(includes));
    }

    static <T extends Comparable<T>> BlockState cycleBlockState(BlockState state, Property<T> property, List<T> includes) {
        int index = includes.indexOf(state.getValue(property));
        if (index < 0 || (index == includes.size() - 1)) index = -1;
        return state.setValue(property, includes.get(index + 1));
    }

    static InteractionResult checkHoldingWrench(Level world, Player player, Runnable callback) {
        return IBlock.checkHoldingItem(world, player, item -> callback.run(), null, ItemList.WRENCH.get());
    }

    static InteractionResult checkHoldingBrushOrWrench(Level world, Player player, Runnable callback) {
        return IBlock.checkHoldingItem(world, player, item -> callback.run(), null, ItemList.WRENCH.get(), Items.BRUSH.get());
    }
}
