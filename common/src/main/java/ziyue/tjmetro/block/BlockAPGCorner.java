package ziyue.tjmetro.block;

import mtr.Blocks;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.IBlockExtends;

import static mtr.block.IBlock.HALF;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Corner for <b>Automatic Platform Gates</b>.
 *
 * @author ZiYueCommentary
 * @see mtr.block.BlockAPGGlass
 * @since beta-1
 */

public class BlockAPGCorner extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public BlockAPGCorner() {
        this(BlockBehaviour.Properties.copy(Blocks.APG_GLASS.get()));
    }

    public BlockAPGCorner(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        BlockState blockState = defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, blockPlaceContext.getHorizontalDirection());
        blockPlaceContext.getLevel().setBlock(blockPlaceContext.getClickedPos().above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER), 1);
        return blockState.setValue(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        super.playerWillDestroy(level, blockPos, blockState, player);
        if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) IBlockExtends.breakBlock(level, blockPos.below(), this);
        else IBlockExtends.breakBlock(level, blockPos.above(), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return IBlock.getVoxelShapeByDirection(0, 0, 0, 4, 9, 4, blockState.getValue(FACING));
        } else {
            return IBlock.getVoxelShapeByDirection(0, 0, 0, 4, 16, 4, blockState.getValue(FACING));
        }
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
