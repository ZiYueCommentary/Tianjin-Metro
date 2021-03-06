package ziyue.tjmetro.blocks;

import mtr.block.BlockCeilingAuto;
import mtr.block.IBlock;
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
 * Ceiling with station color, support using brush to set <i>light/no light</i>.
 *
 * @author ZiYueCommentary
 * @see BlockCeilingAuto
 * @since 1.0b
 */

public class BlockStationColorCeilingAuto extends BlockCeilingAuto implements SimpleWaterloggedBlock
{
    public BlockStationColorCeilingAuto(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final boolean facing = ctx.getHorizontalDirection().getAxis() == Direction.Axis.X;
        return defaultBlockState().setValue(FACING, facing).setValue(LIGHT, hasLight(facing, ctx.getClickedPos())).setValue(WATERLOGGED, false);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(level, player, () -> level.setBlock(blockPos,
                blockState.getValue(LIGHT) ?
                        BlockList.STATION_COLOR_CEILING_NO_LIGHT.get().defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, blockState.getValue(FACING)) :
                        BlockList.STATION_COLOR_CEILING_LIGHT.get().defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, blockState.getValue(FACING)), 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, LIGHT);
    }

    @Override
    public String getDescriptionId() {
        return super.getDescriptionId().replace("block.tjmetro.", "block.mtr.");
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Text.translatable("tooltip.mtr.station_color").setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    protected static boolean hasLight(boolean facing, BlockPos pos) {
        if (facing) return pos.getZ() % 3 == 0;
        else return pos.getX() % 3 == 0;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
