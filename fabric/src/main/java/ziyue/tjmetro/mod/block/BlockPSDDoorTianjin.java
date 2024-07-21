package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import ziyue.tjmetro.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockPSDDoorTianjin extends BlockPSDAPGDoorBase
{
    @Nonnull
    @Override
    public Item asItem2() {
        return super.asItem2();
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.PSD_DOOR_TIANJIN.get(), pos, state);
        }
    }
}
