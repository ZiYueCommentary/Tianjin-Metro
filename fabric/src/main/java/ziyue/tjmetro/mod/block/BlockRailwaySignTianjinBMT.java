package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Blocks;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.block.base.IRailwaySign;
import ziyue.tjmetro.mod.data.IGuiExtension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @see BlockRailwaySignBase
 * @since 1.0.0-beta-1
 */

public class BlockRailwaySignTianjinBMT extends BlockRailwaySignBase
{
    public BlockRailwaySignTianjinBMT(int length, boolean isOdd) {
        super(Blocks.createDefaultBlockSettings(false, state -> 0), length, isOdd);
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return IRailwaySign.getStateForNeighborUpdate(state, direction, neighborState, BlockList.RAILWAY_SIGN_TIANJIN_BMT_MIDDLE.get());
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        IRailwaySign.onPlaced(world, pos, state, BlockList.RAILWAY_SIGN_TIANJIN_BMT_MIDDLE.get(), getMiddleLength());
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IRailwaySign.getOutlineShape(state, getXStart(), BlockList.RAILWAY_SIGN_TIANJIN_BMT_MIDDLE.get());
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.railway_sign_tianjin_bmt";
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        super.addTooltips(stack, world, tooltip, options);
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.railway_sign_bmt"));
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

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderRailwaySignTianjinBMT
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(int length, boolean isOdd, BlockPos pos, BlockState state) {
            super(getType(length, isOdd), length, pos, state);
        }

        public static BlockEntityType<?> getType(int length, boolean isOdd) {
            switch (length) {
                case 2:
                    return isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_2_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_2_EVEN.get();
                case 3:
                    return isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_3_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_3_EVEN.get();
                case 4:
                    return isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_4_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_4_EVEN.get();
                case 5:
                    return isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_5_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_5_EVEN.get();
                case 6:
                    return isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_6_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_6_EVEN.get();
                case 7:
                    return isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_7_ODD.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_BMT_7_EVEN.get();
                default:
                    return null;
            }
        }
    }
}
