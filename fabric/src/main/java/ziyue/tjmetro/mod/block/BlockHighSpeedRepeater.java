package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mapping.DustParticleEffect;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-1
 */

public class BlockHighSpeedRepeater extends BlockExtension implements DirectionHelper
{
    public static final BooleanProperty LOCKED = BooleanProperty.of("locked");
    public static final BooleanProperty POWERED = BooleanProperty.of("powered");
    public static final IntegerProperty POWER = IntegerProperty.of("power", 0, 15);

    public BlockHighSpeedRepeater() {
        this(org.mtr.mod.Blocks.createDefaultBlockSettings(false));
    }

    public BlockHighSpeedRepeater(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    }

    @Override
    public int getStrongRedstonePower2(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return direction == IBlock.getStatePropertySafe(state, FACING) && IBlock.getStatePropertySafe(state, POWERED) ? 15 : 0;
    }

    @Override
    public int getWeakRedstonePower2(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return getStrongRedstonePower2(state, world, pos, direction);
    }

    @Override
    public void neighborUpdate2(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (IBlock.getStatePropertySafe(state, LOCKED)) {
            world.setBlockState(pos, state.with(new Property<>(LOCKED.data), this.isLocked(world, pos, state)));
            return;
        }
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        final BlockPos blockPos1 = pos.offset(direction);
        final boolean powered = world.getBlockState(blockPos1).getStrongRedstonePower(new BlockView(world.data), blockPos1, direction) > 0;
        final boolean isLocked = this.isLocked(world, pos, state);
        world.setBlockState(pos, state.with(new Property<>(POWERED.data), powered).with(new Property<>(LOCKED.data), isLocked));
    }

    protected int getInputLevel(World world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos);
        if (IBlockExtension.isBlock(blockState, Blocks.getRedstoneBlockMapped())) return 15;
        if (IBlockExtension.isBlock(blockState, Blocks.getRedstoneWireMapped())) {
            return IBlock.getStatePropertySafe(blockState, POWER);
        }
        return world.getBlockState(pos).getStrongRedstonePower(new BlockView(world.data), pos, dir);
    }

    @Override
    public boolean emitsRedstonePower2(BlockState state) {
        return true;
    }

    @Override
    public void onBlockAdded2(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        this.updateTarget(world, pos, state);
    }

    @Override
    public void onStateReplaced2(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (moved || state.isOf(newState.getBlock())) return;
        super.onStateReplaced2(state, world, pos, newState, moved);
        this.updateTarget(world, pos, state);
    }

    protected void updateTarget(World world, BlockPos pos, BlockState state) {
        Direction direction = IBlock.getStatePropertySafe(state, FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        world.updateNeighbor(blockPos, this.asBlock2(), pos);
        world.updateNeighborsExcept(blockPos, this.asBlock2(), direction);
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction direction = ctx.getPlayerFacing().getOpposite();
        final BlockPos blockPos = ctx.getBlockPos().offset(direction);
        final boolean powered = ctx.getWorld().getBlockState(blockPos).getStrongRedstonePower(new BlockView(ctx.getWorld().data), blockPos, direction) > 0;
        final BlockState state = getDefaultState2().with(new Property<>(FACING.data), direction.data).with(new Property<>(POWERED.data), powered);
        return state.with(new Property<>(LOCKED.data), this.isLocked(ctx.getWorld(), ctx.getBlockPos(), state));
    }

    public boolean isLocked(World world, BlockPos pos, BlockState state) {
        Direction direction = IBlock.getStatePropertySafe(state, FACING);
        Direction direction2 = direction.rotateYClockwise();
        Direction direction3 = direction.rotateYCounterclockwise();
        return Math.max(this.getInputLevel(world, pos.offset(direction2), direction2), this.getInputLevel(world, pos.offset(direction3), direction3)) > 0;
    }

    @Override
    public void randomDisplayTick2(BlockState state, World world, BlockPos pos, Random random) {
        if (!IBlock.getStatePropertySafe(state, POWERED)) return;
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        final double d = (double) pos.getX() + 0.5 + (random.data.nextDouble() - 0.5) * 0.2;
        final double e = (double) pos.getY() + 0.4 + (random.data.nextDouble() - 0.5) * 0.2;
        final double f = (double) pos.getZ() + 0.5 + (random.data.nextDouble() - 0.5) * 0.2;
        float g = random.data.nextBoolean() ? 7 : -5.0f;
        final double h = (g /= 16.0f) * (float) direction.getOffsetX();
        final double i = g * (float) direction.getOffsetZ();
        world.addParticle(new ParticleEffect(DustParticleEffect.BLUE), d + h, e, f + i, 0.0, 0.0, 0.0);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(POWERED);
        properties.add(LOCKED);
    }
}
