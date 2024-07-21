package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.TianjinMetro;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A device for clearing specify items from players' inventory.
 *
 * @author ZiYueCommentary
 * @since beta-1
 */

// Not working currently, waiting for another implementation.
public class BlockMetalDetectionDoor extends BlockExtension implements DirectionHelper, IBlock
{
    public BlockMetalDetectionDoor() {
        this(BlockHelper.createBlockSettings(true));
    }

    public BlockMetalDetectionDoor(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final BlockState state = getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
        final BlockPos pos = ctx.getBlockPos();
        final World world = ctx.getWorld();
        if (IBlock.isReplaceable(ctx, Direction.UP, 3)) {
            world.setBlockState(pos.up(1), state.with(new Property<>(THIRD.data), EnumThird.MIDDLE));
            world.setBlockState(pos.up(2), state.with(new Property<>(THIRD.data), EnumThird.UPPER));
            return state.with(new Property<>(THIRD.data), EnumThird.LOWER);
        }
        return null;
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> {
            TianjinMetro.LOGGER.info("There should be a GUI opened here... But there is nothing currently.");
        });
    }

    @Override
    public void onEntityCollision2(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision2(state, world, pos, entity);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        if (IBlock.getStatePropertySafe(state, THIRD) == EnumThird.UPPER) {
            return IBlock.getVoxelShapeByDirection(0, 0, 1, 16, 6, 15, direction);
        } else {
            final VoxelShape left = IBlock.getVoxelShapeByDirection(0, 0, 1, 1, 16, 15, direction);
            final VoxelShape right = IBlock.getVoxelShapeByDirection(15, 0, 1, 16, 16, 15, direction);
            return VoxelShapes.union(left, right);
        }
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        switch (IBlock.getStatePropertySafe(state, THIRD)) {
            case UPPER -> {
                IBlockExtension.breakBlock(world, pos.down(1), BlockList.METAL_DETECTION_DOOR.get());
                IBlockExtension.breakBlock(world, pos.down(2), BlockList.METAL_DETECTION_DOOR.get());
            }
            case MIDDLE -> {
                IBlockExtension.breakBlock(world, pos.up(1), BlockList.METAL_DETECTION_DOOR.get());
                IBlockExtension.breakBlock(world, pos.down(1), BlockList.METAL_DETECTION_DOOR.get());
            }
            case LOWER -> {
                IBlockExtension.breakBlock(world, pos.up(1), BlockList.METAL_DETECTION_DOOR.get());
                IBlockExtension.breakBlock(world, pos.up(2), BlockList.METAL_DETECTION_DOOR.get());
            }
        }
        super.onBreak2(world, pos, state, player);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(THIRD);
    }
}
