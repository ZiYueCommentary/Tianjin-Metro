package ziyue.tjmetro.blocks;

import mtr.block.BlockRailwaySign;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiServer;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;

import java.util.*;

public class BlockRailwaySignWall extends BlockRailwaySign
{
    public BlockRailwaySignWall(int length) {
        super(length, true);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final Direction facing = IBlock.getStatePropertySafe(state, FACING);
            final Direction hitSide = hit.getDirection();
            if (hitSide == facing || hitSide == facing.getOpposite()) {
                final BlockPos checkPos = findEndWithDirection(world, pos, hitSide.getOpposite(), false);
                if (checkPos != null) {
                    PacketTrainDataGuiServer.openRailwaySignScreenS2C((ServerPlayer) player, checkPos);
                }
            }
        });
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = direction == facing.getClockWise() || state.is(BlockList.RAILWAY_SIGN_WALL_MIDDLE.get()) && direction == facing.getCounterClockWise();
        if (isNext && !(newState.getBlock() instanceof BlockRailwaySign)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);

        final BlockPos checkPos = findEndWithDirection(world, pos, facing, true);
        if (checkPos != null) {
            IBlock.onBreakCreative(world, player, checkPos);
        }

        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide) {
            final Direction facing = IBlock.getStatePropertySafe(state, FACING);
            for (int i = 1; i <= getMiddleLength(); i++) {
                world.setBlock(pos.relative(facing.getClockWise(), i), BlockList.RAILWAY_SIGN_WALL_MIDDLE.get().defaultBlockState().setValue(FACING, facing), 3);
            }
            world.setBlock(pos.relative(facing.getClockWise(), getMiddleLength() + 1), defaultBlockState().setValue(FACING, facing.getOpposite()), 3);
            world.updateNeighborsAt(pos, Blocks.AIR);
            state.updateNeighbourShapes(world, pos, 3);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        if (state.is(BlockList.RAILWAY_SIGN_WALL_MIDDLE.get())) {
            return IBlock.getVoxelShapeByDirection(0, 0, 7, 16, 12, 9, facing);
        } else {
            final int xStart = getXStart();
            final VoxelShape main = IBlock.getVoxelShapeByDirection(xStart - 0.75, 0, 7, 16, 12, 9, facing);
            final VoxelShape pole = IBlock.getVoxelShapeByDirection(xStart - 2, 0, 7, xStart - 0.75, 16, 9, facing);
            return Shapes.or(main, pole);
        }
    }

    @Override
    public String getDescriptionId() {
        return "block.tjmetro.railway_sign_wall";
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Text.translatable("tooltip.mtr.railway_sign_length", length).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        if (this == BlockList.RAILWAY_SIGN_WALL_MIDDLE.get()) {
            return null;
        } else {
            return new TileEntityRailwaySignWall(length, pos, state);
        }
    }

    protected int getMiddleLength() {
        return (length - (4 - getXStart() / 4)) / 2;
    }

    protected BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        int i = 0;
        while (true) {
            final BlockPos checkPos = startPos.relative(direction.getCounterClockWise(), i);
            final BlockState checkState = world.getBlockState(checkPos);
            if (checkState.getBlock() instanceof BlockRailwaySign) {
                final Direction facing = IBlock.getStatePropertySafe(checkState, FACING);
                if (!checkState.is(BlockList.RAILWAY_SIGN_WALL_MIDDLE.get()) && (facing == direction || allowOpposite && facing == direction.getOpposite())) {
                    return checkPos;
                }
            } else {
                return null;
            }
            i++;
        }
    }

    public static class TileEntityRailwaySignWall extends BlockRailwaySign.TileEntityRailwaySign
    {
        private final Set<Long> selectedIds;
        private final String[] signIds;
        private static final String KEY_SELECTED_IDS = "selected_ids";
        private static final String KEY_SIGN_LENGTH = "sign_length";

        public TileEntityRailwaySignWall(int length, BlockPos pos, BlockState state) {
            super(length, true, pos, state);
            signIds = new String[length];
            selectedIds = new HashSet<>();
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            selectedIds.clear();
            Arrays.stream(compoundTag.getLongArray(KEY_SELECTED_IDS)).forEach(selectedIds::add);
            for (int i = 0; i < signIds.length; i++) {
                final String signId = compoundTag.getString(KEY_SIGN_LENGTH + i);
                signIds[i] = signId.isEmpty() ? null : signId;
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putLongArray(KEY_SELECTED_IDS, new ArrayList<>(selectedIds));
            for (int i = 0; i < signIds.length; i++) {
                compoundTag.putString(KEY_SIGN_LENGTH + i, signIds[i] == null ? "" : signIds[i]);
            }
        }

        public AABB getRenderBoundingBox() {
            return new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        public void setData(Set<Long> selectedIds, String[] signTypes) {
            this.selectedIds.clear();
            this.selectedIds.addAll(selectedIds);
            if (signIds.length == signTypes.length) {
                System.arraycopy(signTypes, 0, signIds, 0, signTypes.length);
            }
            setChanged();
            syncData();
        }

        public Set<Long> getSelectedIds() {
            return selectedIds;
        }

        public String[] getSignIds() {
            return signIds;
        }

        protected static BlockEntityType<?> getType(int length) {
            switch (length) {
                case 2:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_2_TILE_ENTITY.get();
                case 3:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_3_TILE_ENTITY.get();
                case 4:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_4_TILE_ENTITY.get();
                case 5:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_5_TILE_ENTITY.get();
                case 6:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_6_TILE_ENTITY.get();
                case 7:
                    return BlockEntityTypes.RAILWAY_SIGN_WALL_7_TILE_ENTITY.get();
                default:
                    return null;
            }
        }
    }
}
