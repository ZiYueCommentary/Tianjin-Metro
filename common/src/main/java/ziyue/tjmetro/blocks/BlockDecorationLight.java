package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;

//todo so i must make this because this placeholder has exist for half year
public class BlockDecorationLight extends BlockCustomColorBase
{
    public BlockDecorationLight() {
        super(Properties.copy(Blocks.MARBLE_BLUE.get()).noCollission().lightLevel((state) -> 15));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new DecorationLightEntity(pos, state);
    }

    public static class DecorationLightEntity extends CustomColorBlockEntity
    {
        public DecorationLightEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_COLOR_CONCRETE_TILE_ENTITY.get(), pos, state);
        }
    }
}