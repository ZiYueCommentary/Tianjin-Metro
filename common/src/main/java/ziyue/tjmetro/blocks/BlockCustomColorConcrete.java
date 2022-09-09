package ziyue.tjmetro.blocks;

import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;

/**
 * @author ZiYueCommentary
 * @see BlockCustomColorBase
 * @since 1.0b
 */

public class BlockCustomColorConcrete extends BlockCustomColorBase
{
    public BlockCustomColorConcrete(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new CustomColorConcreteEntity(pos, state);
    }

    public static class CustomColorConcreteEntity extends CustomColorBlockEntity
    {
        public CustomColorConcreteEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_COLOR_CONCRETE_TILE_ENTITY.get(), pos, state);
        }
    }
}
