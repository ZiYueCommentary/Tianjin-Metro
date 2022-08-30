package ziyue.tjmetro.blocks;

import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockList;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * A ceiling with custom color, light is not lit. Support use shears to lit.
 *
 * @author ZiYueCommentary
 * @see BlockCeilingNotLit
 * @see BlockStationColorCeiling
 * @since 1.0b
 */

public class BlockStationColorCeilingNotLit extends BlockCeilingNotLit implements SimpleWaterloggedBlock
{
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final boolean facing = ctx.getHorizontalDirection().getAxis() == Direction.Axis.X;
        return defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, false);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Text.translatable("tooltip.mtr.station_color").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.isHolding(Items.SHEARS)) {
            level.setBlock(blockPos, BlockList.STATION_COLOR_CEILING_LIGHT.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING)).setValue(WATERLOGGED, blockState.getValue(WATERLOGGED)), 1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
