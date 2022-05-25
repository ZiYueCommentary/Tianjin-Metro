package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.block.BlockStationNameBase;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
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
import ziyue.tjmetro.packet.PacketGuiServer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public abstract class BlockStationNameSignBase extends BlockStationNameBase implements SimpleWaterloggedBlock
{
    public BlockStationNameSignBase(){
        super(Properties.copy(Blocks.STATION_NAME_TALL_WALL.get()));
    }

    public abstract BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLOR, FACING, WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        Runnable runnable = () -> {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof TileEntityStationNameWall) {
                ((TileEntityStationNameWall) entity).syncData();
                PacketGuiServer.openCustomContentScreenS2C((ServerPlayer) player, pos);
            }
        };
        //如果用if语句判断之后再执行就会导致崩溃，syncData和player转换都有问题
        //所以为什么这个可以？？
        //我特么为这破事折腾两天
        //2022/5/25
        return IBlock.checkHoldingItem(world, player, item -> runnable.run(), null, Items.STICK);
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

    public abstract static class TileEntityStationNameWall extends CustomContentBlockEntity
    {
        public TileEntityStationNameWall(BlockEntityType<?> entity, BlockPos pos, BlockState state) {
            super(entity, pos, state);
        }
    }
}
