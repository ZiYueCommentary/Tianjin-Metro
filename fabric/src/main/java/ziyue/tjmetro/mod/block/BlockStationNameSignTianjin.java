package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockRouteSignBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static org.mtr.mod.block.IBlock.SIDE;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since 1.0.0
 */

public class BlockStationNameSignTianjin extends BlockExtension implements DirectionHelper, BlockWithEntity
{
    public BlockStationNameSignTianjin() {
        this(Blocks.createDefaultBlockSettings(true, state -> 10));
    }

    public BlockStationNameSignTianjin(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public @Nullable BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction direction = ctx.getPlayerFacing();
        final BlockState state = getDefaultState2().with(new Property<>(FACING.data), direction.data);
        if (IBlock.isReplaceable(ctx, direction.rotateYClockwise(), 2)) {
            ctx.getWorld().setBlockState(ctx.getBlockPos().offset(direction.rotateYClockwise(), 1), state.with(new Property<>(SIDE.data), IBlock.EnumSide.RIGHT));
            return state.with(new Property<>(SIDE.data), IBlock.EnumSide.LEFT);
        }
        return null;
    }

    @Override
    public @Nonnull ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos)));
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        final IBlock.EnumSide side = IBlock.getStatePropertySafe(state, SIDE);
        IBlockExtension.breakBlock(world, pos.offset(side == IBlock.EnumSide.LEFT ? direction.rotateYClockwise() : direction.rotateYCounterclockwise(), 1));
        super.onBreak2(world, pos, state, player);
    }

    @Override
    public @Nonnull VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction direction = IBlock.getStatePropertySafe(state, FACING);
        if (IBlock.getStatePropertySafe(state, SIDE) == IBlock.EnumSide.LEFT) {
            return IBlock.getVoxelShapeByDirection(4, 0, 0, 16, 16, 1, direction);
        } else {
            return IBlock.getVoxelShapeByDirection(0, 0, 0, 12, 16, 1, direction);
        }
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(SIDE);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderStationNameSignTianjin
     * @since 1.0.0
     */
    public static class BlockEntity extends BlockRouteSignBase.BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_SIGN_TIANJIN.get(), pos, state);
            InitClient.findClosePlatform(getPos2(), 5, platform -> setPlatformId(platform.getId()));
        }

        public void setData(long platformId) {
            this.setPlatformId(platformId);
            final BlockPos pos;
            if (IBlock.getStatePropertySafe(this.getCachedState2(), SIDE) == IBlock.EnumSide.LEFT) {
                pos = this.getPos2().offset(IBlock.getStatePropertySafe(this.getCachedState2(), FACING).rotateYClockwise());
            } else {
                pos = this.getPos2().offset(IBlock.getStatePropertySafe(this.getCachedState2(), FACING).rotateYCounterclockwise());
            }

            org.mtr.mapping.holder.BlockEntity blockEntity = this.getWorld2().getBlockEntity(pos);
            if (blockEntity.data instanceof BlockEntity) {
                final BlockEntity entity = (BlockEntity) blockEntity.data;
                entity.setPlatformId(platformId);
                entity.markDirty2();
            } else {
                TianjinMetro.LOGGER.error("BlockStationNameSignTianjin.BlockEntity: Unable to set data for block entity at " + pos.toShortString());
            }
            markDirty2();
        }
    }
}
