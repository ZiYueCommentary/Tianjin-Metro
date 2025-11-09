package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.block.BlockAPGGlassEnd;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagAPGTianjinTRT;

import javax.annotation.Nonnull;

/**
 * @author ZiYueCommentary
 * @see BlockAPGGlassEnd
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @since 1.0.0-beta-2
 */

public class BlockAPGGlassEndTianjinTRT extends BlockAPGGlassEnd implements BlockFlagAPGTianjinTRT
{
    @Override
    protected boolean isAPG() {
        return true;
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.APG_GLASS_END_TIANJIN_TRT.get();
    }
}
