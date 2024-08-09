package ziyue.tjmetro.mod.block.base;

import org.mtr.mapping.holder.BlockEntityType;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockEntityExtension;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-1
 */

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
