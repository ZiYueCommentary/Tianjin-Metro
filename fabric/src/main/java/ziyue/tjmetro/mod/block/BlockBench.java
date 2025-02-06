package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.entity.EntitySeat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Bench is a wooden chair on which players can sit down.
 * Note that the bench in Minecraft 1.16.5 does not support sitting down.
 *
 * @author ZiYueCommentary
 * @see EntitySeat
 * @see <a href="https://github.com/ZiYueCommentary/Tianjin-Metro/wiki/Bench">Wiki page</a>
 * @since 1.0.0-beta-1
 */

public class BlockBench extends BlockExtension implements DirectionHelper, IBlock
{
    public BlockBench() {
        this(Blocks.createDefaultBlockSettings(false));
    }

    public BlockBench(BlockSettings blockSettings) {
        super(blockSettings);
    }

    // Not working in 1.16.5 due to fabric's bug.
#if MC_VERSION > "11605"
    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        Entity entity = new Entity(new EntitySeat(world, pos.getX() + 0.5, pos.getY() + 0.55, pos.getZ() + 0.5));
        world.spawnEntity(entity);
        player.startRiding(entity);
        return ActionResult.SUCCESS;
    }
#endif

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction direction = ctx.getPlayerFacing();
        final BlockPos pos = ctx.getBlockPos();
        return getDefaultState2().with(new Property<>(FACING.data), direction.data).with(new Property<>(SIDE_EXTENDED.data), getPos(direction, pos, ctx.getWorld()));
    }

    @Override
    public void neighborUpdate2(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        world.setBlockState(pos, state.with(new Property<>(SIDE_EXTENDED.data), getPos(IBlock.getStatePropertySafe(state, FACING), pos, world)));
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        final VoxelShape top = IBlock.getVoxelShapeByDirection(0.0, 8.0, 1.0, 16.0, 9.5, 15.0, direction);
        final VoxelShape left = IBlock.getVoxelShapeByDirection(12.0, 0.0, 2.0, 14.0, 8.0, 14.0, direction);
        final VoxelShape right = IBlock.getVoxelShapeByDirection(2.0, 0.0, 2.0, 4.0, 8.0, 14.0, direction);
        switch (IBlock.getStatePropertySafe(state, SIDE_EXTENDED)) {
            case SINGLE:
                return VoxelShapes.union(VoxelShapes.union(top, left), right);
            case LEFT:
                return VoxelShapes.union(top, left);
            case RIGHT:
                return VoxelShapes.union(top, right);
            default:
                return top;
        }
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(SIDE_EXTENDED);
    }

    public IBlock.EnumSide getPos(Direction direction, BlockPos blockPos, World world) {
        final BlockState counterClockWiseState = world.getBlockState(blockPos.offset(direction.rotateYCounterclockwise()));
        final BlockState clockWiseState = world.getBlockState(blockPos.offset(direction.rotateYClockwise()));
        final boolean[] blockSame = {
                (IBlockExtension.isBlock(counterClockWiseState, BlockList.BENCH.get())) && (IBlock.getStatePropertySafe(counterClockWiseState, FACING) == direction),
                (IBlockExtension.isBlock(clockWiseState, BlockList.BENCH.get())) && (IBlock.getStatePropertySafe(clockWiseState, FACING) == direction)
        };
        return blockSame[0] && blockSame[1] ? IBlock.EnumSide.MIDDLE : blockSame[0] ? IBlock.EnumSide.LEFT : blockSame[1] ? IBlock.EnumSide.RIGHT : IBlock.EnumSide.SINGLE;
    }
}
