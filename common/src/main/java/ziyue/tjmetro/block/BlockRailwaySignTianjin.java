package ziyue.tjmetro.block;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.block.base.IRailwaySign;

public class BlockRailwaySignTianjin extends BlockRailwaySignBase
{
    public BlockRailwaySignTianjin(int length, boolean isOdd) {
        this(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 15), length, isOdd);
    }

    public BlockRailwaySignTianjin(Properties properties, int length, boolean isOdd) {
        super(properties, length, isOdd);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        return IRailwaySign.updateShape(state, direction, newState, BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (world.isClientSide) return;
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i <= getMiddleLength(); i++) {
            world.setBlock(pos.relative(facing.getClockWise(), i), BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get().defaultBlockState().setValue(FACING, facing), 3);
        }
        world.setBlock(pos.relative(facing.getClockWise(), getMiddleLength() + 1), state.getBlock().defaultBlockState().setValue(FACING, facing.getOpposite()), 3);
        world.updateNeighborsAt(pos, Blocks.AIR);
        state.updateNeighbourShapes(world, pos, 3);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (state.is(BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get())) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 9, 9, facing);
        } else {
            final VoxelShape main = IBlock.getVoxelShapeByDirection(getXStart() - 0.5, 0, 7, 16, 9, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(getXStart() + 3, 0, 7.5, getXStart() + 4, 16, 8.5, facing);
            return Shapes.or(main, pole);
        }
    }

    @Override
    public int getXStart() {
        return switch (length % 4) {
            case 1 -> isOdd ? 4 : 12;
            case 2 -> isOdd ? 0 : 8;
            case 3 -> isOdd ? 12 : 4;
            default -> isOdd ? 8 : 0;
        };
    }

    @Override
    protected BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get());
    }

    @Override
    public String getDescriptionId() {
        return "block.tjmetro.railway_sign_tianjin";
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        if (this == BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get()) {
            return null;
        } else {
            return new TileEntityRailwaySignTianjin(length, isOdd, pos, state);
        }
    }

    public static class TileEntityRailwaySignTianjin extends TileEntityRailwaySign
    {
        public TileEntityRailwaySignTianjin(int length, boolean isOdd, BlockPos pos, BlockState state) {
            super(getType(length, isOdd), length, pos, state);
        }

        public static BlockEntityType<?> getType(int length, boolean isOdd) {
            return switch (length) {
                case 2 ->
                        isOdd ? null : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_2_EVEN_TILE_ENTITY.get();
                case 3 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_EVEN_TILE_ENTITY.get();
                case 4 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_EVEN_TILE_ENTITY.get();
                case 5 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_EVEN_TILE_ENTITY.get();
                case 6 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_EVEN_TILE_ENTITY.get();
                case 7 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_EVEN_TILE_ENTITY.get();
                default -> null;
            };
        }
    }
}
