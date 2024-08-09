package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.block.BlockPSDAPGGlassEndBase;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagPSDTianjin;

import javax.annotation.Nonnull;

/**
 * @author ZiYueCommentary
 * @see org.mtr.mod.block.BlockPSDGlassEnd
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @since 1.0.0-beta-1
 */

public class BlockPSDGlassEndTianjin extends BlockPSDAPGGlassEndBase implements BlockFlagPSDTianjin
{
    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.PSD_GLASS_END_TIANJIN.get();
    }
}
