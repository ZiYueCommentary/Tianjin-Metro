package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.Item;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagPSDTianjinJinjing;

import javax.annotation.Nonnull;

/**
 * @author ZiYueCommentary
 * @see BlockPSDGlassTianjin
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @since 1.0.0-beta-4
 */

public class BlockPSDGlassTianjinJinjing extends BlockPSDGlassTianjin implements BlockFlagPSDTianjinJinjing
{
    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.PSD_GLASS_TIANJIN_JINJING.get();
    }
}
