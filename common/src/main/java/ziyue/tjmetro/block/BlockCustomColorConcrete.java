package ziyue.tjmetro.block;

import mtr.Blocks;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.block.base.BlockCustomColorBase;

/**
 * @author ZiYueCommentary
 * @see BlockCustomColorBase
 * @since beta-1
 */

public class BlockCustomColorConcrete extends BlockCustomColorBase
{
    public BlockCustomColorConcrete() {
        this(BlockBehaviour.Properties.copy(Blocks.STATION_COLOR_CONCRETE.get()));
    }

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
