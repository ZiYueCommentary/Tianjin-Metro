package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.block.base.IRailwaySign;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockRailwaySignTianjin extends BlockRailwaySignBase
{
    public BlockRailwaySignTianjin(int length, boolean isOdd) {
        super(BlockHelper.createBlockSettings(true, blockState -> 15), length, isOdd);
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return IRailwaySign.getStateForNeighborUpdate(state, direction, neighborState, BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get());
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        IRailwaySign.onPlaced(world, pos, state, BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get(), getMiddleLength());
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (IBlockExtension.isBlock(state, BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get())) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 9, 9, facing);
        } else {
            final VoxelShape main = IBlock.getVoxelShapeByDirection(getXStart() - 0.5, 0, 7, 16, 9, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(getXStart() + 3, 0, 7.5, getXStart() + 4, 16, 8.5, facing);
            return VoxelShapes.union(main, pole);
        }
    }

    @Override
    protected BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get());
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

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.railway_sign_tianjin";
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == BlockList.RAILWAY_SIGN_TIANJIN_MIDDLE.get().data)
            return null;
        else
            return new BlockEntity(length, isOdd, blockPos, blockState);
    }

    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(int length, boolean isOdd, BlockPos pos, BlockState state) {
            super(getType(length, isOdd), length, pos, state);
        }

        public static BlockEntityType<?> getType(int length, boolean isOdd) {
            return switch (length) {
                case 2 -> isOdd ? null : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_2_EVEN.get();
                case 3 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_3_EVEN.get();
                case 4 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_4_EVEN.get();
                case 5 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_5_EVEN.get();
                case 6 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_6_EVEN.get();
                case 7 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_7_EVEN.get();
                default -> null;
            };
        }
    }
}
