package ziyue.tjmetro.mod.block.base;

import org.mtr.mapping.holder.BlockEntityType;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockEntityExtension;

public class BlockEntityRenderable extends BlockEntityExtension
{
    public final float yOffset;
    public final float zOffset;

    public BlockEntityRenderable(BlockEntityType<?> type, BlockPos blockPos, BlockState state, float yOffset, float zOffset) {
        super(type, blockPos, state);
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }
}
