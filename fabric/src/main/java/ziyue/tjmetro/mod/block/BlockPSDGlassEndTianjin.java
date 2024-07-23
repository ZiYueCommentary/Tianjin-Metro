package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.block.BlockPSDAPGGlassEndBase;
import ziyue.tjmetro.mod.ItemList;

import javax.annotation.Nonnull;

public class BlockPSDGlassEndTianjin extends BlockPSDAPGGlassEndBase
{
    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.PSD_GLASS_END_TIANJIN.get();
    }
}
