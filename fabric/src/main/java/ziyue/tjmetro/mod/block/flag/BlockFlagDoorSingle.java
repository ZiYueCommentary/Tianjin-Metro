package ziyue.tjmetro.mod.block.flag;

import org.mtr.mapping.holder.*;
import org.mtr.mod.block.BlockPSDAPGGlassEndBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.generated.lang.TranslationProvider;

import static org.mtr.mod.block.BlockPSDAPGDoorBase.END;
import static org.mtr.mod.block.BlockPSDAPGDoorBase.UNLOCKED;
import static org.mtr.mod.block.IBlock.HALF;

/**
 * @since 1.1.1
 */
public interface BlockFlagDoorSingle
{
    boolean isLeft();

    static BlockState getStateForNeighborUpdate(BlockState state, WorldAccess world, BlockPos pos) {
        final boolean end = world.getBlockState(pos.offset(IBlock.getSideDirection(state).getOpposite())).getBlock().data instanceof BlockPSDAPGGlassEndBase;
        return state.with(new Property<>(END.data), end);
    }

    static ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
            for (int y = -1; y <= 1; y++) {
                final BlockState scanState = world.getBlockState(pos.up(y));
                if (state.isOf(scanState.getBlock())) {
                    world.setBlockState(pos.up(y), scanState.with(new Property<>(UNLOCKED.data), !unlocked));
                }
            }
            player.sendMessage((unlocked ? TranslationProvider.GUI_MTR_PSD_APG_DOOR_LOCKED : TranslationProvider.GUI_MTR_PSD_APG_DOOR_UNLOCKED).getText(), true);
        });
    }

    static void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockPos offsetPos = IBlock.getStatePropertySafe(state, HALF) == IBlock.DoubleBlockHalf.UPPER ? pos.down() : pos.up();
        IBlock.onBreakCreative(world, player, offsetPos);
        world.breakBlock(pos, false, Entity.cast(player));
    }
}
