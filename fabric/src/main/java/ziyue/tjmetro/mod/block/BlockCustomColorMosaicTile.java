package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockSettings;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.Blocks;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.block.base.BlockCustomColorBase;

/**
 * @author ZiYueCommentary
 * @see BlockCustomColorBase
 * @see BlockEntity
 * @since 1.1.0
 */

public class BlockCustomColorMosaicTile extends BlockCustomColorBase
{
    public BlockCustomColorMosaicTile() {
        this(Blocks.createDefaultBlockSettings(false));
    }

    public BlockCustomColorMosaicTile(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.screen.ColorPickerScreen
     * @since 1.1.0
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(BlockPos blockPos, BlockState blockState) {
            super(BlockEntityTypes.CUSTOM_COLOR_MOSAIC_TILE.get(), blockPos, blockState);
        }
    }
}
