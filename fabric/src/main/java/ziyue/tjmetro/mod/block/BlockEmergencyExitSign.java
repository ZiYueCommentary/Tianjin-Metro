package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static ziyue.tjmetro.mod.block.IBlockExtension.THIRD;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-1
 */

public class BlockEmergencyExitSign extends BlockExtension implements DirectionHelper
{
    public BlockEmergencyExitSign() {
        this(Blocks.createDefaultBlockSettings(true));
    }

    public BlockEmergencyExitSign(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data).with(new Property<>(THIRD.data), IBlockExtension.BlockThirdProperty.LEFT);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(2, 5, 0, 14, 10, 0.25, IBlock.getStatePropertySafe(state, FACING));
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> world.setBlockState(pos, state.cycle(new Property<>(THIRD.data))));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(THIRD);
    }
}
