package ziyue.tjmetro.block;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.ItemList;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.item.ItemPSDTianjinBase
 * @see BlockPSDAPGDoorBase
 * @since beta-1
 */

public class BlockPSDDoorTianjin extends BlockPSDAPGDoorBase
{
    @Override
    public Item asItem() {
        return ItemList.PSD_DOOR_TIANJIN.get();
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileEntityPSDTianjinDoor(blockPos, blockState);
    }

    public static class TileEntityPSDTianjinDoor extends BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase
    {
        public TileEntityPSDTianjinDoor(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.PSD_DOOR_TIANJIN_TILE_ENTITY.get(), pos, state);
        }
    }
}
