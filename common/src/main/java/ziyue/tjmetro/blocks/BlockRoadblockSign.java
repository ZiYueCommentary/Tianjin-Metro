package ziyue.tjmetro.blocks;

import mtr.Items;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.blocks.base.CustomContentBlockEntity;
import ziyue.tjmetro.packet.PacketGuiServer;

/**
 * Roadblock with content.
 *
 * @author ZiYueCommentary
 * @see BlockRoadblock
 * @since 1.0b
 */

public class BlockRoadblockSign extends BlockRoadblock implements EntityBlockMapper
{
    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        Runnable runnable = () -> {
            Direction direction = blockState.getValue(FACING);
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof TileEntityRoadBlockSign) {
                ((TileEntityRoadBlockSign) entity).syncData();
                PacketGuiServer.openCustomContentScreenS2C((ServerPlayer) player, blockState.getValue(IS_RIGHT) ?
                        direction == Direction.NORTH ? pos.west() : direction == Direction.SOUTH ? pos.east() : direction == Direction.WEST ? pos.south() : pos.north() :
                        pos);
            }
        };
        return IBlock.checkHoldingItem(world, player, item -> runnable.run(), null, Items.BRUSH.get());
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockRoadblockSign.TileEntityRoadBlockSign(pos, state);
    }

    /**
     * @see CustomContentBlockEntity
     */
    public static class TileEntityRoadBlockSign extends CustomContentBlockEntity
    {
        public TileEntityRoadBlockSign(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.ROADBLOCK_SIGN_ENTITY.get(), pos, state, 0, 0.535f);
        }

        @Override
        public boolean shouldRender() {
            return true;
        }
    }
}
