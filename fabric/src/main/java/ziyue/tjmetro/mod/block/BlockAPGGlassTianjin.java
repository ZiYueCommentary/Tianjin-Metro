package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockAPGGlass;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagAPGTianjin;

import javax.annotation.Nonnull;

public class BlockAPGGlassTianjin extends BlockAPGGlass implements BlockFlagAPGTianjin
{
    @Override
    protected boolean isAPG() {
        return true;
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.APG_GLASS_TIANJIN.get();
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }
}
