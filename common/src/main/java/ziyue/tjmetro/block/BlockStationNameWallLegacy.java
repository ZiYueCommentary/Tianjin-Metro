package ziyue.tjmetro.block;

import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ziyue.tjmetro.BlockEntityTypes;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * Legacy version of <i>BlockStationNameWall</i>.
 *
 * @author ZiYueCommentary
 * @see BlockStationNameBase
 * @see mtr.block.BlockStationNameWallWhite
 * @see mtr.block.BlockStationNameWallGray
 * @see mtr.block.BlockStationNameWallBlack
 * @since beta-1
 */

public class BlockStationNameWallLegacy extends BlockStationNameBase implements SimpleWaterloggedBlock
{
    public BlockStationNameWallLegacy() {
        this(BlockBehaviour.Properties.copy(mtr.Blocks.STATION_NAME_WALL_WHITE.get()).noCollission());
    }

    public BlockStationNameWallLegacy(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlockExtends.checkHoldingBrushOrWrench(world, player, () -> world.setBlockAndUpdate(pos, state.cycle(COLOR)));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return world.getBlockState(pos.relative(facing)).isFaceSturdy(world, pos.relative(facing), facing.getOpposite());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        final Direction side = ctx.getClickedFace();
        if (side != Direction.UP && side != Direction.DOWN) {
            return defaultBlockState().setValue(FACING, side.getOpposite()).setValue(WATERLOGGED, false);
        } else {
            return null;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        if (direction.getOpposite() == IBlock.getStatePropertySafe(state, FACING).getOpposite() && !state.canSurvive(world, pos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return state;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityStationNameLegacy(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, FACING, WATERLOGGED);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Text.translatable("tooltip.tjmetro.station_name").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    public static class TileEntityStationNameLegacy extends TileEntityStationNameBase
    {
        public TileEntityStationNameLegacy(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_WALL_LEGACY_TILE_ENTITY.get(), pos, state, 0, 0, false);
        }

        @Override
        public int getColor(BlockState state) {
            return switch (getBlockState().getValue(COLOR)) {
                case 0 -> ARGB_WHITE;
                case 1 -> ARGB_GRAY;
                default -> ARGB_BLACK;
            };
        }
    }
}
