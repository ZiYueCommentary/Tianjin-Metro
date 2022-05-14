package ziyue.tjmetro.mixin.stationnameentrance;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameEntrance;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import ziyue.tjmetro.ShowNameProperty;

import static mtr.block.BlockStationNameEntrance.STYLE;

/**
 * Made for <i>No "station" of station name entrance</i> feature<br>
 * Inspired by <a href="https://github.com/jonafanho/Minecraft-Transit-Railway/issues/172">Issue #172: 奇怪的“站站”、“Station Station”</a>
 * @author ZiYueCommentary
 * @since 1.0b
 * @see BlockStationNameEntrance
 */

@Mixin(BlockStationNameEntrance.class)
public abstract class BlockMixin extends BlockStationNameBase implements IBlock, ShowNameProperty
{
    protected BlockMixin(Properties settings) {
        super(settings);
    }

    private static final BooleanProperty SHOW_NAME = BooleanProperty.create("show_name");

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STYLE, SHOW_NAME);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(player.isHolding(Items.SHEARS)){
            world.setBlockAndUpdate(pos, state.setValue(SHOW_NAME, !state.getValue(SHOW_NAME)));
            propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), SHOW_NAME, 1);
            propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), SHOW_NAME, 1);
            return InteractionResult.SUCCESS;
        }
        return IBlock.checkHoldingBrush(world, player, () -> {
            world.setBlockAndUpdate(pos, state.cycle(STYLE));
            propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), STYLE, 1);
            propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), STYLE, 1);
        });
    }

    @Override
    public boolean getShowNameProperty(BlockState state) {
        return state.getValue(SHOW_NAME);
    }
}
