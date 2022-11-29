package ziyue.tjmetro.blocks;

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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.IExtends;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Corner for <b>Automatic Platform Gates</b>.
 *
 * @author ZiYueCommentary
 * @see mtr.block.BlockAPGGlass
 * @since 1.0b
 */

public class BlockAPGCorner extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public static final BooleanProperty TOP = BooleanProperty.create("top");

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
        blockPlaceContext.getLevel().setBlock(blockPlaceContext.getClickedPos().above(), blockState.setValue(TOP, true), 1);
        return blockState.setValue(TOP, false);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        super.playerWillDestroy(level, blockPos, blockState, player);
        if (blockState.getValue(TOP)) IExtends.breakBlock(level, blockPos.below(), this);
        else IExtends.breakBlock(level, blockPos.above(), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TOP, WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        if (blockState.getValue(TOP))
            return IBlock.getVoxelShapeByDirection(0, 0, 0, 4, 9, 4, blockState.getValue(FACING));
        else return IBlock.getVoxelShapeByDirection(0, 0, 0, 4, 16, 4, blockState.getValue(FACING));
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }
}
