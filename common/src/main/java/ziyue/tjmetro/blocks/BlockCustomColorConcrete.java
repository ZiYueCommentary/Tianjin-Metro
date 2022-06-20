package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;
import ziyue.tjmetro.blocks.base.CustomColorBlockEntity;

/**
 * @author ZiYueCommentary
 * @see BlockCustomColorBase
 * @since 1.0b
 */

public class BlockCustomColorConcrete extends BlockCustomColorBase
{
    public BlockCustomColorConcrete() {
        super(Properties.copy(Blocks.STATION_COLOR_CONCRETE.get()));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new CustomColorConcreteEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ((CustomColorBlockEntity) level.getBlockEntity(blockPos)).color = 0x1abc9c;
        level.getChunk(blockPos).getPos();
        return InteractionResult.SUCCESS;
    }

    public static class CustomColorConcreteEntity extends CustomColorBlockEntity
    {
        public CustomColorConcreteEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_COLOR_CONCRETE_TILE_ENTITY.get(), pos, state);
        }
    }
}
