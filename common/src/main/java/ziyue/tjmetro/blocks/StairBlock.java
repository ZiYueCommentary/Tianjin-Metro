package ziyue.tjmetro.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * A class that support construct stair block.
 *
 * @author ZiYueCommentary
 * @see net.minecraft.world.level.block.StairBlock
 * @since 1.0b
 */

public class StairBlock extends net.minecraft.world.level.block.StairBlock
{
    public StairBlock(Block block) {
        super(block.defaultBlockState(), BlockBehaviour.Properties.copy(block));
    }
}
