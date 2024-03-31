package ziyue.tjmetro.block;

import mtr.Blocks;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.IBlockExtends;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * @author ZiYueCommentary
 * @since beta-1
 */

public class BlockEmergencyExitSign extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock
{
    public static final EnumProperty<BlockEmergencyExitSignStyle> STYLE = EnumProperty.create("style", BlockEmergencyExitSignStyle.class);

    public BlockEmergencyExitSign() {
        this(BlockBehaviour.Properties.copy(Blocks.STATION_COLOR_POLE.get()).lightLevel(state -> 5));
    }

    public BlockEmergencyExitSign(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, STYLE);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(2, 5, 0, 14, 10, 0.25, blockState.getValue(FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(WATERLOGGED, false).setValue(FACING, blockPlaceContext.getHorizontalDirection()).setValue(STYLE, BlockEmergencyExitSignStyle.LEFT);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlockExtends.checkHoldingBrushOrWrench(level, player, () -> level.setBlockAndUpdate(blockPos, blockState.cycle(STYLE)));
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    /**
     * Styles for <b>Emergency Sign</b>.
     *
     * @author ZiYueCommentary
     * @see BlockEmergencyExitSign
     * @since beta-1
     */
    public enum BlockEmergencyExitSignStyle implements StringRepresentable
    {
        LEFT("left"),
        RIGHT("right"),
        BOTH("both");

        final String name;

        BlockEmergencyExitSignStyle(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
