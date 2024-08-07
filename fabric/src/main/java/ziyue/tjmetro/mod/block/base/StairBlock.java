package ziyue.tjmetro.mod.block.base;

import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.StairsBlockExtension;
import org.mtr.mapping.registry.BlockRegistryObject;

/**
 * A class that support construct stair block.
 *
 * @author ZiYueCommentary
 * @see StairsBlockExtension
 * @since 1.0.0-beta-1
 */

public class StairBlock extends StairsBlockExtension
{
    public StairBlock(Block block) {
        super(block.getDefaultState(), BlockHelper.createBlockSettings(false));
    }
}
