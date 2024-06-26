package ziyue.tjmetro.block;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.BlockList;
import ziyue.tjmetro.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.block.base.IRailwaySign;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Railway Sign Wall, must be even.
 *
 * @author ZiYueCommentary
 * @see BlockRailwaySignBase
 * @see BlockRailwaySignWallBig
 * @since beta-1
 */

public class BlockRailwaySignWall extends BlockRailwaySignBase
{
    public static final BooleanProperty EOS = BooleanProperty.create("eos"); // end of sign

    public BlockRailwaySignWall(int length) {
        this(Properties.of(Material.METAL, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().strength(2).lightLevel(state -> 15).noCollission(), length);
    }

    public BlockRailwaySignWall(Properties properties, int length) {
        super(properties, length, false);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final BlockPos checkPos = findEndWithDirection(world, pos, facing, false);
        return IBlockExtends.checkHoldingBrushOrWrench(world, player, () -> {
            if (checkPos != null) {
                PacketGuiServer.openRailwaySignScreenS2C((ServerPlayer) player, checkPos);
            }
        });
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
            world.setBlock(pos.relative(facing.getClockWise(), i), BlockList.RAILWAY_SIGN_WALL_MIDDLE.get().defaultBlockState().setValue(FACING, facing).setValue(EOS, false), 3);
        }
        world.setBlock(pos.relative(facing.getClockWise(), getMiddleLength()), BlockList.RAILWAY_SIGN_WALL_MIDDLE.get().defaultBlockState().setValue(FACING, facing).setValue(EOS, true), 3);
        world.updateNeighborsAt(pos, Blocks.AIR);
        state.updateNeighbourShapes(world, pos, 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        final boolean isNext = ((!state.getValue(EOS) && (direction == facing.getClockWise())) || state.is(BlockList.RAILWAY_SIGN_WALL_MIDDLE.get())) && (direction == facing.getCounterClockWise());
        if (isNext && !(newState.getBlock() instanceof BlockRailwaySignBase)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    public int getXStart() {
        return 0;
    }

    @Override
    protected int getMiddleLength() {
        return length / 2 - 1;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, state.getValue(FACING));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Text.translatable("tooltip.mtr.railway_sign_length", length).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
    }

    @Override
    protected BlockPos findEndWithDirection(Level world, BlockPos startPos, Direction direction, boolean allowOpposite) {
        return IRailwaySign.findEndWithDirection(world, startPos, direction, allowOpposite, BlockList.RAILWAY_SIGN_WALL_MIDDLE.get());
    }

    @Override
    public String getDescriptionId() {
        return "block.tjmetro.railway_sign_wall";
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        if (this == BlockList.RAILWAY_SIGN_WALL_MIDDLE.get())
            return null;
        else
            return new TileEntityRailwaySignWall(length, pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED, EOS);
    }

    public static class TileEntityRailwaySignWall extends TileEntityRailwaySign
    {
        public TileEntityRailwaySignWall(int length, BlockPos pos, BlockState state) {
            super(getType(length), length, pos, state);
        }

        public TileEntityRailwaySignWall(BlockEntityType<?> type, int length, BlockPos pos, BlockState state) {
            super(type, length, pos, state);
        }

        protected static BlockEntityType<?> getType(int length) {
            return switch (length) {
                case 4 -> BlockEntityTypes.RAILWAY_SIGN_WALL_4_TILE_ENTITY.get();
                case 6 -> BlockEntityTypes.RAILWAY_SIGN_WALL_6_TILE_ENTITY.get();
                case 8 -> BlockEntityTypes.RAILWAY_SIGN_WALL_8_TILE_ENTITY.get();
                case 10 -> BlockEntityTypes.RAILWAY_SIGN_WALL_10_TILE_ENTITY.get();
                default -> null;
            };
        }
    }
}
