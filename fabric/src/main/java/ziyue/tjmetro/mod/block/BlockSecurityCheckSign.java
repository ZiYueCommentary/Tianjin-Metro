package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.block.base.BlockEntityRenderable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static ziyue.tjmetro.mod.block.IBlockExtension.THIRD;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since 1.0.0-beta-3
 */

public class BlockSecurityCheckSign extends BlockExtension implements DirectionHelper, BlockWithEntity
{
    public BlockSecurityCheckSign() {
        this(Blocks.createDefaultBlockSettings(false));
    }

    public BlockSecurityCheckSign(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> world.setBlockState(pos, state.cycle(new Property<>(THIRD.data))));
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(1.5f, 0f, 0f, 14.5f, 16f, 0.1f, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(THIRD);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data).with(new Property<>(THIRD.data), IBlockExtension.BlockThirdProperty.RIGHT);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    public static class BlockEntity extends BlockEntityRenderable
    {
        public BlockEntity(BlockPos blockPos, BlockState state) {
            super(BlockEntityTypes.SECURITY_CHECK_SIGN.get(), blockPos, state, 0, 0.009375f);
        }
    }
}
