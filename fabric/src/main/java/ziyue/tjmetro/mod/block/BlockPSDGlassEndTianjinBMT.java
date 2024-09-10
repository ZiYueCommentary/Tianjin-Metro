package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.block.BlockPSDAPGGlassEndBase;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagPSDTianjinBMT;

import javax.annotation.Nonnull;

/**
 * @author ZiYueCommentary
 * @see org.mtr.mod.block.BlockPSDGlassEnd
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @since 1.0.0-beta-2
 */

public class BlockPSDGlassEndTianjinBMT extends BlockPSDAPGGlassEndBase implements BlockFlagPSDTianjinBMT
{
    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.PSD_GLASS_END_TIANJIN_BMT.get();
    }
}
