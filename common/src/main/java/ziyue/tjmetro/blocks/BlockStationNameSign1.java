package ziyue.tjmetro.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ziyue.tjmetro.BlockEntityTypes;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public class BlockStationNameSign1 extends BlockStationNameSignBase
{
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final boolean isWhite = IBlock.getStatePropertySafe(state, COLOR) == 0;
            final int newColorProperty = isWhite ? 2 : 0;

            updateProperties(world, pos, newColorProperty);
        });
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state)
    {
        return new BlockStationNameSign1.TileEntityStationNameWall(pos, state);
    }

    public static class TileEntityStationNameWall extends TileEntityStationNameBase
    {
        public TileEntityStationNameWall(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_SIGN_ENTITY_1.get(), pos, state, 0, 0.05f);
        }

        @Override
        public boolean shouldRender() {
            return true;
        }
    }

    private static void updateProperties(Level world, BlockPos pos, int colorProperty)
    {
        world.setBlockAndUpdate(pos, world.getBlockState(pos).setValue(COLOR, colorProperty));
    }
}
