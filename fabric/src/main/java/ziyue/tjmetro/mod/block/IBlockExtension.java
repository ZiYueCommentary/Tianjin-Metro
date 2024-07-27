package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mod.Items;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.ItemList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Some methods similar to methods in <b>IBlock</b>.
 *
 * @see IBlock
 * @since 1.0.0-beta-1
 */

public interface IBlockExtension
{
    /**
     * Replace block with air.
     *
     * @param pos block's position
     * @author ZiYueCommentary
     * @since 1.0.0-beta-1
     */
    static void breakBlock(World world, BlockPos pos) {
        if (world.isClient()) return;

        world.setBlockState(pos, Blocks.getAirMapped().getDefaultState());
    }

    /**
     * Specify a block, if block in pos is specified block, then replace it with air.
     *
     * @param pos   block's position
     * @param block specified block
     * @author ZiYueCommentary
     * @since 1.0.0-beta-1
     */
    static void breakBlock(World world, BlockPos pos, Block block) {
        if (world.isClient()) return;

        if (isBlock(world.getBlockState(pos), block)) {
            world.setBlockState(pos, Blocks.getAirMapped().getDefaultState());
        }
    }

    static boolean isBlock(BlockState state, Block block) {
        return state.getBlock().data == block.data;
    }

    static ActionResult checkHoldingWrench(World world, PlayerEntity player, Runnable callback) {
        return IBlock.checkHoldingItem(world, player, item -> callback.run(), null, ItemList.WRENCH.get());
    }

    static ActionResult checkHoldingBrushOrWrench(World world, PlayerEntity player, Runnable callback) {
        return IBlock.checkHoldingItem(world, player, item -> callback.run(), null, ItemList.WRENCH.get(), Items.BRUSH.get());
    }

    static <T extends Enum<T> & StringIdentifiable> BlockState cycleBlockState(BlockState state, EnumProperty<T> property, Predicate<T> includes) {
        return cycleBlockState(state, property, property.getValues().stream().filter(includes).collect(Collectors.toList()));
    }

    @SafeVarargs
    static <T extends Enum<T> & StringIdentifiable> BlockState cycleBlockState(BlockState state, EnumProperty<T> property, T... includes) {
        return cycleBlockState(state, property, Arrays.asList(includes));
    }

    static <T extends Enum<T> & StringIdentifiable> BlockState cycleBlockState(BlockState state, EnumProperty<T> property, List<T> includes) {
        int index = includes.indexOf(IBlock.getStatePropertySafe(state, property));
        if (index < 0 || (index == includes.size() - 1)) index = -1;
        return state.with(new Property<>(property.data), includes.get(index + 1));
    }
}
