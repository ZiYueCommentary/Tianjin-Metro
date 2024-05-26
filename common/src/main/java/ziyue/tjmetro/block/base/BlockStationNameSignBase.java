package ziyue.tjmetro.block.base;

import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.mappings.EntityBlockMapper;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.block.IBlockExtends;
import ziyue.tjmetro.data.IGuiExtends;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * @author ZiYueCommentary
 * @since beta-1
 */

public abstract class BlockStationNameSignBase extends BlockStationNameBase implements SimpleWaterloggedBlock, EntityBlockMapper
{
    public BlockStationNameSignBase(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, FACING, WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlockExtends.checkHoldingWrench(world, player, () -> {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof TileEntityStationNameBase entity1) {
                entity1.syncData();
                PacketGuiServer.openCustomContentScreenS2C((ServerPlayer) player, pos);
            }
        });
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return IBlock.getVoxelShapeByDirection(1f, 3.5f, 0f, 15f, 14.5f, 0.5f, IBlock.getStatePropertySafe(blockState, FACING));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection()).setValue(WATERLOGGED, false);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return true;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Text.translatable("tooltip.tjmetro.custom_content").withStyle(ChatFormatting.GRAY));
        tooltip.add(Text.translatable("tooltip.tjmetro.station_name").withStyle(ChatFormatting.GRAY));
        IGuiExtends.addHoldShiftTooltip(tooltip, Text.translatable("tooltip.tjmetro.station_name_sign"), true);
    }

    /**
     * @see BlockCustomContentBlockBase.CustomContentBlockEntity
     */
    public abstract static class TileEntityStationNameBase extends BlockCustomContentBlockBase.CustomContentBlockEntity
    {
        public TileEntityStationNameBase(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
            super(entity, pos, state, 0, 0.05f);
        }
    }
}
