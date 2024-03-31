package ziyue.tjmetro.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.IBlockExtends;
import ziyue.tjmetro.blocks.base.BlockRailwaySignBase;
import ziyue.tjmetro.blocks.base.IRailwaySign;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.*;

/**
 * Railway Sign Wall, must be even.
 *
 * @author ZiYueCommentary
 * @see BlockRailwaySignBase
 * @see BlockRailwaySignWall
 * @see BlockRailwaySignWallBig
 * @since beta-1
 */

public class BlockRailwaySignWallDouble extends BlockRailwaySignWall
{
    public BlockRailwaySignWallDouble(int length) {
        super(length);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final BlockPos checkPos = findEndWithDirection(world, pos, facing, false);
        return IBlockExtends.checkHoldingBrushOrWrench(world, player, () -> {
            if (checkPos != null) {
                PacketGuiServer.openRailwaySignWallDoubleScreenS2C((ServerPlayer) player, checkPos);
            }
        });
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (world.isClientSide) return;
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        for (int i = 1; i < getMiddleLength(); i++) {
            world.setBlock(pos.relative(facing.getClockWise(), i), BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get().defaultBlockState().setValue(FACING, facing).setValue(EOS, false), 3);
        }
        world.setBlock(pos.relative(facing.getClockWise(), getMiddleLength()), BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get().defaultBlockState().setValue(FACING, facing).setValue(EOS, true), 3);
        world.updateNeighborsAt(pos, Blocks.AIR);
        state.updateNeighbourShapes(world, pos, 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = ((!state.getValue(EOS) && (direction == facing.getClockWise())) || state.is(BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get())) && (direction == facing.getCounterClockWise());
        if (isNext && !(newState.getBlock() instanceof BlockRailwaySignBase)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    protected BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get());
    }

    @Override
    public String getDescriptionId() {
        return "block.tjmetro.railway_sign_wall_double";
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        if (this == BlockList.RAILWAY_SIGN_WALL_DOUBLE_MIDDLE.get())
            return null;
        else
            return new TileEntityRailwaySignWallDouble(length, pos, state);
    }

    public static class TileEntityRailwaySignWallDouble extends BlockEntityClientSerializableMapper
    {
        protected final List<Set<Long>> selectedIds;
        protected final String[][] signIds;
        protected static final String KEY_SELECTED_IDS = "selected_ids";
        protected static final String KEY_SIGN_LENGTH = "sign_length";

        public TileEntityRailwaySignWallDouble(int length, BlockPos pos, BlockState state) {
            super(getType(length), pos, state);
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

        protected static BlockEntityType<?> getType(int length) {
            return switch (length) {
                case 4 -> BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_4_TILE_ENTITY.get();
                case 6 -> BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_6_TILE_ENTITY.get();
                case 8 -> BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_8_TILE_ENTITY.get();
                case 10 -> BlockEntityTypes.RAILWAY_SIGN_WALL_DOUBLE_10_TILE_ENTITY.get();
                default -> null;
            };
        }
    }
}
