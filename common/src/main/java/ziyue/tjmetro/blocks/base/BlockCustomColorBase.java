package ziyue.tjmetro.blocks.base;

import mtr.mappings.EntityBlockMapper;
import net.minecraft.world.level.block.Block;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public abstract class BlockCustomColorBase extends Block implements EntityBlockMapper
{
    public BlockCustomColorBase(Properties properties) {
        super(properties);
    }

    @Override
    public String getDescriptionId() {
        return super.getDescriptionId().replace("block.tjmetro.custom_color_", "block.minecraft.");
    }
}
