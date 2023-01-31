package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.block.BlockCeilingAuto;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Ceiling with station color.
 *
 * @author ZiYueCommentary
 * @see BlockCeilingAuto
 * @since beta-1
 */

public class BlockStationColorCeilingAuto extends BlockCeilingAuto implements SimpleWaterloggedBlock
{
    public BlockStationColorCeilingAuto() {
        this(BlockBehaviour.Properties.copy(Blocks.CEILING.get()));
    }

    public BlockStationColorCeilingAuto(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final boolean facing = ctx.getHorizontalDirection().getAxis() == Direction.Axis.X;
        return defaultBlockState().setValue(FACING, facing).setValue(LIGHT, hasLight(facing, ctx.getClickedPos())).setValue(WATERLOGGED, false);
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
        list.add(Text.translatable("tooltip.mtr.station_color").withStyle(ChatFormatting.GRAY));
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
