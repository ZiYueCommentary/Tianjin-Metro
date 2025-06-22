package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.block.BlockAPGGlassEnd;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagAPGTianjinJinjing;

import javax.annotation.Nonnull;

/**
 * @author ZiYueCommentary
 * @see BlockAPGGlassEnd
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @since 1.0.0-prerelease-1
 */

public class BlockAPGGlassEndTianjinJinjing extends BlockAPGGlassEnd implements BlockFlagAPGTianjinJinjing
{
    @Override
    protected boolean isAPG() {
        return true;
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.APG_GLASS_END_TIANJIN_JINJING.get();
    }
}
