package ziyue.tjmetro.mod.block.base;

import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.StairsBlockExtension;
import org.mtr.mapping.registry.BlockRegistryObject;

/**
 * A class that support construct stair block.
 *
 * @author ZiYueCommentary
 * @see StairsBlockExtension
 * @since beta-1
 */

public class StairBlock extends StairsBlockExtension
{
    public StairBlock(BlockRegistryObject block) {
        super(block.get().getDefaultState(), BlockHelper.createBlockSettings(false));
    }
}
