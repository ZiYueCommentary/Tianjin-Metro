package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagPSDTianjinBMT;

import javax.annotation.Nonnull;

/**
 * @author ZiYueCommentary
 * @see org.mtr.mod.block.BlockPSDDoor
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @see BlockEntity
 * @since 1.0.0-beta-2
 */

public class BlockPSDDoorTianjinBMT extends BlockPSDAPGDoorBase implements BlockFlagPSDTianjinBMT
{
    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.PSD_DOOR_TIANJIN_BMT.get();
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see org.mtr.mod.render.RenderPSDAPGDoor
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.PSD_DOOR_TIANJIN_BMT.get(), pos, state);
        }
    }
}
