package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @since beta-1
 */

public class BlockRolling extends BlockExtension
{
    public static final BooleanProperty FACING_DOUBLE = BooleanProperty.of("facing");
    public static final BooleanProperty BOTTOM = BooleanProperty.of("bottom");
    public static final BooleanProperty CHANGED = BooleanProperty.of("changed");

    public BlockRolling() {
        this(BlockHelper.createBlockSettings(false).strength(5.0F, 6.0F));
    }

    public BlockRolling(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2()
                .with(new Property<>(FACING_DOUBLE.data), ctx.getPlayerFacing().getAxis() == Axis.X)
                .with(new Property<>(BOTTOM.data), !IBlockExtension.isBlock(ctx.getWorld().getBlockState(ctx.getBlockPos().down()), BlockList.ROLLING.get()))
                .with(new Property<>(CHANGED.data), false);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (IBlock.getStatePropertySafe(state, FACING_DOUBLE))
            return Block.createCuboidShape(6, 0, 0, 10, 16, 16);
        else
            return Block.createCuboidShape(0, 0, 6, 16, 16, 10);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING_DOUBLE);
        properties.add(BOTTOM);
        properties.add(CHANGED);
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> world.setBlockState(pos, state.cycle(new Property<>(BOTTOM.data)).with(new Property<>(CHANGED.data), true)));
    }

    @Override
    public void neighborUpdate2(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (IBlock.getStatePropertySafe(state, CHANGED)) return;

        world.setBlockState(pos, state.with(new Property<>(BOTTOM.data), !IBlockExtension.isBlock(world.getBlockState(pos.down()), BlockList.ROLLING.get())));
    }
}
