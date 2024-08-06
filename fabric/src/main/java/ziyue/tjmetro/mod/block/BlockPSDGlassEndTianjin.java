package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.Item;
import org.mtr.mod.block.BlockPSDAPGGlassEndBase;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagPSDTianjin;

import javax.annotation.Nonnull;

public class BlockPSDGlassEndTianjin extends BlockPSDAPGGlassEndBase implements BlockFlagPSDTianjin
{
    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.PSD_GLASS_END_TIANJIN.get();
    }
}
