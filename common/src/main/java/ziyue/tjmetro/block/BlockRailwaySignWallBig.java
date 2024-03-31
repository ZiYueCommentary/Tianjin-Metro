package ziyue.tjmetro.block;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.block.base.IRailwaySign;

/**
 * Big Railway Sign Wall.
 *
 * @author ZiYueCommentary
 * @see BlockRailwaySignBase
 * @see BlockRailwaySignWall
 * @since beta-1
 */

public class BlockRailwaySignWallBig extends BlockRailwaySignWall
{
    public BlockRailwaySignWallBig(int length) {
        this(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 15).noCollission(), length);
    }

    public BlockRailwaySignWallBig(Properties properties, int length) {
        super(properties, length);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final Direction facing = ctx.getHorizontalDirection();
        return IBlock.isReplaceable(ctx, facing.getClockWise(), getMiddleLength() + 1) ? defaultBlockState().setValue(FACING, facing).setValue(EOS, false) : null;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (world.isClientSide) return;
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i < getMiddleLength(); i++) {
            world.setBlock(pos.relative(facing.getClockWise(), i), BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get().defaultBlockState().setValue(FACING, facing).setValue(EOS, false), 3);
        }
        world.setBlock(pos.relative(facing.getClockWise(), getMiddleLength()), BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get().defaultBlockState().setValue(FACING, facing).setValue(EOS, true), 3);
        world.updateNeighborsAt(pos, Blocks.AIR);
        state.updateNeighbourShapes(world, pos, 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = ((!state.getValue(EOS) && (direction == facing.getClockWise())) || state.is(BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get())) && (direction == facing.getCounterClockWise());
        if (isNext && !(newState.getBlock() instanceof BlockRailwaySignBase)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    protected int getMiddleLength() {
        return length - 1;
    }

    @Override
    protected BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get());
    }

    @Override
    public String getDescriptionId() {
        return "block.tjmetro.railway_sign_wall_big";
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        if (this == BlockList.RAILWAY_SIGN_WALL_BIG_MIDDLE.get())
            return null;
        else
            return new TileEntityRailwaySignWallBig(length, pos, state);
    }

    public static class TileEntityRailwaySignWallBig extends TileEntityRailwaySignWall
    {
        public TileEntityRailwaySignWallBig(int length, BlockPos pos, BlockState state) {
            super(getType(length), length, pos, state);
        }

        protected static BlockEntityType<?> getType(int length) {
            return switch (length) {
                case 2 -> BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_2_TILE_ENTITY.get();
                case 3 -> BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_3_TILE_ENTITY.get();
                case 4 -> BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_4_TILE_ENTITY.get();
                case 5 -> BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_5_TILE_ENTITY.get();
                case 6 -> BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_6_TILE_ENTITY.get();
                case 7 -> BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_7_TILE_ENTITY.get();
                case 8 -> BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_8_TILE_ENTITY.get();
                case 9 -> BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_9_TILE_ENTITY.get();
                case 10 -> BlockEntityTypes.RAILWAY_SIGN_WALL_BIG_10_TILE_ENTITY.get();
                default -> null;
            };
        }
    }
}
