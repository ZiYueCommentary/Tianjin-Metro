package ziyue.tjmetro.blocks;

import mtr.block.BlockPSDAPGGlassEndBase;
import net.minecraft.world.item.Item;
import ziyue.tjmetro.ItemList;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.items.ItemPSDTianjinBase
 * @see mtr.block.BlockPSDGlassEnd
 * @since beta-1
 */

public class BlockPSDGlassEndTianjin extends BlockPSDAPGGlassEndBase
{
    @Override
    public Item asItem() {
        return ItemList.PSD_GLASS_END_TIANJIN.get();
    }
}
