package ziyue.tjmetro.blocks.base;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A class that support construct stair block.
 *
 * @author ZiYueCommentary
 * @see net.minecraft.world.level.block.StairBlock
 * @since beta-1
 */

public class StairBlock extends net.minecraft.world.level.block.StairBlock
{
    public StairBlock(Block block) {
        this(block.defaultBlockState(), BlockBehaviour.Properties.copy(block));
    }

    public StairBlock(BlockState state, Properties properties) {
        super(state, properties);
    }
}
