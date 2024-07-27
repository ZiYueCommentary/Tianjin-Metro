package ziyue.tjmetro.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.block.base.IRailwaySign;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockRailwaySignWallDouble extends BlockRailwaySignWall
{
    public BlockRailwaySignWallDouble(int length) {
        super(length);
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return IRailwaySign.getStateForNeighborUpdate(state, direction, neighborState, BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get());
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.isClient()) return;

        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i < getMiddleLength(); i++) {
            world.setBlockState(pos.offset(facing.rotateYClockwise(), i), BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data).with(new Property<>(EOS.data), false), 3);
        }
        world.setBlockState(pos.offset(facing.rotateYClockwise(), getMiddleLength()), BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get().getDefaultState().with(new Property<>(FACING.data), facing.data).with(new Property<>(EOS.data), true), 3);
        world.updateNeighbors(pos, Blocks.getAirMapped());
        state.updateNeighbors(new WorldAccess(world.data), pos, 3);
    }

    @Override
    protected BlockPos findEndWithDirection(World world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get());
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.railway_sign_wall_double";
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (this == BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get().data)
            return null;
        else
            return new BlockEntity(length, blockPos, blockState);
    }

    public static class BlockEntity extends BlockEntityExtension
    {
        protected final List<LongAVLTreeSet> selectedIds;
        protected final String[][] signIds;
        protected static final String KEY_SELECTED_IDS = "selected_ids";
        protected static final String KEY_SIGN_LENGTH = "sign_length";

        public BlockEntity(int length, BlockPos pos, BlockState state) {
            super(getType(length), pos, state);
            signIds = new String[2][length];
            selectedIds = new ArrayList<>();
            selectedIds.add(new LongAVLTreeSet());
            selectedIds.add(new LongAVLTreeSet());
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            selectedIds.forEach(LongAVLTreeSet::clear);
            for (int i = 0; i < 2; i++) {
                Arrays.stream(compoundTag.getLongArray(KEY_SELECTED_IDS + i)).forEach(selectedIds.get(i)::add);
                for (int j = 0; j < signIds[i].length; j++) {
                    final String signId = compoundTag.getString(KEY_SIGN_LENGTH + i + j);
                    signIds[i][j] = signId.isEmpty() ? null : signId;
                }
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            for (int i = 0; i < 2; i++) {
                compoundTag.putLongArray(KEY_SELECTED_IDS + i, new ArrayList<>(selectedIds.get(i)));
                for (int j = 0; j < signIds[i].length; j++) {
                    compoundTag.putString(KEY_SIGN_LENGTH + i + j, signIds[i][j] == null ? "" : signIds[i][j]);
                }
            }
        }

        public void setData(List<LongAVLTreeSet> selectedIds, String[][] signTypes) {
            this.selectedIds.clear();
            this.selectedIds.addAll(selectedIds);
            if (signIds[0].length == signTypes[0].length) { // Both lines have the same length
                System.arraycopy(signTypes, 0, signIds, 0, signTypes.length);
            }
            markDirty2();
        }

        public List<LongAVLTreeSet> getSelectedIds() {
            return selectedIds;
        }

        public String[][] getSignIds() {
            return signIds;
        }

        protected static BlockEntityType<?> getType(int length) {
            switch (length) {
                case 4:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_4.get();
                case 6:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_6.get();
                case 8:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_8.get();
                case 10:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_10.get();
                default:
                    return null;
            }
        }
    }
}
