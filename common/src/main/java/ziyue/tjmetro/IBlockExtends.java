package ziyue.tjmetro;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;
import static ziyue.tjmetro.TianjinMetro.LOGGER;

/**
 * Some methods similar to methods in IBlock.
 *
 * @author ZiYueCommentary
 * @see mtr.block.IBlock
 * @since 1.0b
 */

public interface IBlockExtends
{
    static void onBreak(Level world, BlockPos pos) {
        try {
            world.setBlock(pos, world.getBlockState(pos).getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
        } catch (Exception exception) {
            LOGGER.warn("Get " + WATERLOGGED.getName() + " status failed: replace with air");
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
        }
    }

    static void onBreak(Level world, BlockPos pos, Block block) {
        try {
            if (world.getBlockState(pos).getBlock() == block)
                world.setBlock(pos, world.getBlockState(pos).getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
        } catch (Exception exception) {
            LOGGER.warn("Get " + WATERLOGGED.getName() + " status failed: replace with air");
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 35);
        }
    }
}
