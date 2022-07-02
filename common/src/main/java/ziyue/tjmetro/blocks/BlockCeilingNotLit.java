package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.block.BlockCeiling;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * A ceiling, light is not lit. Support use shears to lit.
 *
 * @author ZiYueCommentary
 * @see BlockCeiling
 * @since 1.0b
 */

public class BlockCeilingNotLit extends BlockCeiling
{
    public BlockCeilingNotLit() {
        super(BlockBehaviour.Properties.copy(Blocks.CEILING.get()).lightLevel((state) -> 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getAxis() == Direction.Axis.X).setValue(WATERLOGGED, false);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.isHolding(Items.SHEARS)) {
            level.setBlock(blockPos, Blocks.CEILING_LIGHT.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING)), 1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
