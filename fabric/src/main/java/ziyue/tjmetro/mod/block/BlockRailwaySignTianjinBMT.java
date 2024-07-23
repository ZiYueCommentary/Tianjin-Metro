package ziyue.tjmetro.mod.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.block.base.IRailwaySign;

public class BlockRailwaySignTianjinBMT extends BlockRailwaySignBase
{
    public BlockRailwaySignTianjinBMT(int length, boolean isOdd) {
        super(BlockHelper.createBlockSettings(false, state -> 15), length, isOdd);
    }

    @NotNull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return IRailwaySign.getStateForNeighborUpdate(state, direction, neighborState, BlockList.RAILWAY_SIGN_TIANJIN_BMT_MIDDLE.get());
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        IRailwaySign.onPlaced(world, pos, state, BlockList.RAILWAY_SIGN_TIANJIN_BMT_MIDDLE.get(), getMiddleLength());
    }

    @NotNull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (IBlockExtension.isBlock(state, BlockList.RAILWAY_SIGN_TIANJIN_BMT_MIDDLE.get())) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
        } else {
            final int xStart = getXStart();
            final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
            return VoxelShapes.union(main, pole);
        }

    }

    @NotNull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.railway_sign_tianjin_bmt";
    }

    @Override
    protected BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_TIANJIN_BMT_MIDDLE.get());
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == BlockList.RAILWAY_SIGN_TIANJIN_BMT_MIDDLE.get().data)
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
                case 2 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_2_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_2_EVEN.get();
                case 3 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_3_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_3_EVEN.get();
                case 4 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_4_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_4_EVEN.get();
                case 5 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_5_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_5_EVEN.get();
                case 6 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_6_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_6_EVEN.get();
                case 7 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_7_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_7_EVEN.get();
                default -> null;
            };
        }
    }
}
