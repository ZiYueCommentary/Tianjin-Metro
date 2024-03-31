package ziyue.tjmetro.block;

import mtr.block.BlockPSDAPGGlassBase;
import net.minecraft.world.item.Item;
import ziyue.tjmetro.ItemList;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.item.ItemPSDTianjinBase
 * @see BlockPSDAPGGlassBase
 * @since beta-1
 */

public class BlockPSDGlassTianjin extends BlockPSDAPGGlassBase
{
    @Override
    public Item asItem() {
        return ItemList.PSD_GLASS_TIANJIN.get();
    }
}
