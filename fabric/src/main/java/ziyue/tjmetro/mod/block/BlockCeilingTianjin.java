package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockWaterloggable;

import javax.annotation.Nonnull;

public class BlockCeilingTianjin extends BlockWaterloggable
{
    public BlockCeilingTianjin() {
        this(Blocks.createDefaultBlockSettings(true, state -> 15));
    }

    public BlockCeilingTianjin(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0, 7, 0, 16, 10, 16);
    }
}
