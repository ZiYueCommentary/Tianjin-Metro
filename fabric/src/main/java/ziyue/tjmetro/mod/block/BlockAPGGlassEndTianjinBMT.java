package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.block.BlockAPGGlassEnd;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagAPGTianjinBMT;

import javax.annotation.Nonnull;

public class BlockAPGGlassEndTianjinBMT extends BlockAPGGlassEnd implements BlockFlagAPGTianjinBMT
{
    @Override
    protected boolean isAPG() {
        return true;
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.APG_GLASS_END_TIANJIN_BMT.get();
    }
}
