package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.block.BlockPSDAPGGlassBase;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagPSDTianjinBMT;

import javax.annotation.Nonnull;

/**
 * @author ZiYueCommentary
 * @see org.mtr.mod.block.BlockPSDGlass
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @since 1.0.0-beta-2
 */

public class BlockPSDGlassTianjinBMT extends BlockPSDAPGGlassBase implements BlockFlagPSDTianjinBMT
{
    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.PSD_GLASS_TIANJIN_BMT.get();
    }
}
