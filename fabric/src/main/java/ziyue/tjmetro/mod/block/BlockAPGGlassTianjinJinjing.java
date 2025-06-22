package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockAPGGlass;
import org.mtr.mod.block.BlockPSDTop;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagAPGTianjinJinjing;

import javax.annotation.Nonnull;

/**
 * @author ZiYueCommentary
 * @see BlockAPGGlass
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @see BlockEntity
 * @since 1.0.0-prerelease-1
 */

public class BlockAPGGlassTianjinJinjing extends BlockAPGGlass implements BlockFlagAPGTianjinJinjing
{
    @Override
    protected boolean isAPG() {
        return true;
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.APG_GLASS_TIANJIN_JINJING.get();
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderAPGGlassTianjinBMT
     * @since 1.0.0-beta-2
     */
    public static class BlockEntity extends BlockPSDTop.BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.APG_GLASS_TIANJIN_JINJING.get(), pos, state);
        }
    }
}
