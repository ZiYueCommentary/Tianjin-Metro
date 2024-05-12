package ziyue.tjmetro.block;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.IBlockExtends;
import ziyue.tjmetro.block.base.IRailwaySign;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.*;

public class BlockRailwaySignTianjinDouble extends BlockRailwaySignTianjin
{
    public BlockRailwaySignTianjinDouble(int length, boolean isOdd) {
        super(length, isOdd);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final BlockPos checkPos = findEndWithDirection(world, pos, facing, false);
        return IBlockExtends.checkHoldingBrushOrWrench(world, player, () -> {
            if (checkPos != null) {
                PacketGuiServer.openRailwaySignDoubleScreenS2C((ServerPlayer) player, checkPos);
            }
        });
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        return IRailwaySign.updateShape(state, direction, newState, BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_MIDDLE.get());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        IRailwaySign.setPlacedBy(world, pos, state, BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_MIDDLE.get(), getMiddleLength());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (state.is(BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_MIDDLE.get()))
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 16, 9, facing);
        else
            return IBlock.getVoxelShapeByDirection(getXStart() - 0.5, 0, 7, 16, 16, 9, facing);
    }

    @Override
    protected BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_MIDDLE.get());
    }

    @Override
    public String getDescriptionId() {
        return "block.tjmetro.railway_sign_tianjin_double";
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        if (this == BlockList.RAILWAY_SIGN_TIANJIN_DOUBLE_MIDDLE.get())
            return null;
        else
            return new TileEntityRailwaySignTianjinDouble(length, isOdd, pos, state);
    }

    public static class TileEntityRailwaySignTianjinDouble extends BlockEntityClientSerializableMapper
    {
        protected final List<Set<Long>> selectedIds;
        protected final String[][] signIds;
        protected static final String KEY_SELECTED_IDS = "selected_ids";
        protected static final String KEY_SIGN_LENGTH = "sign_length";

        public TileEntityRailwaySignTianjinDouble(int length, boolean isOdd, BlockPos pos, BlockState state) {
            super(getType(length, isOdd), pos, state);
            signIds = new String[2][length];
            selectedIds = new ArrayList<>();
            selectedIds.add(new HashSet<>());
            selectedIds.add(new HashSet<>());
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            selectedIds.forEach(Set::clear);
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

        public void setData(List<Set<Long>> selectedIds, String[][] signTypes) {
            this.selectedIds.clear();
            this.selectedIds.addAll(selectedIds);
            if (signIds[0].length == signTypes[0].length) { // Both lines have the same length
                System.arraycopy(signTypes, 0, signIds, 0, signTypes.length);
            }
            setChanged();
            syncData();
        }

        public List<Set<Long>> getSelectedIds() {
            return selectedIds;
        }

        public String[][] getSignIds() {
            return signIds;
        }

        public static BlockEntityType<?> getType(int length, boolean isOdd) {
            return switch (length) {
                case 2 -> isOdd ? null : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_2_EVEN_TILE_ENTITY.get();
                case 3 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_3_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_3_EVEN_TILE_ENTITY.get();
                case 4 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_4_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_4_EVEN_TILE_ENTITY.get();
                case 5 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_5_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_5_EVEN_TILE_ENTITY.get();
                case 6 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_6_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_6_EVEN_TILE_ENTITY.get();
                case 7 ->
                        isOdd ? BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_7_ODD_TILE_ENTITY.get() : BlockEntityTypes.RAILWAY_SIGN_TIANJIN_DOUBLE_7_EVEN_TILE_ENTITY.get();
                default -> null;
            };
        }
    }
}
