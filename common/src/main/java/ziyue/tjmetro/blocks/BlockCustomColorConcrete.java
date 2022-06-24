package ziyue.tjmetro.blocks;

import mtr.Blocks;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;
import ziyue.tjmetro.packet.PacketGuiServer;

/**
 * @author ZiYueCommentary
 * @see BlockCustomColorBase
 * @since 1.0b
 */

public class BlockCustomColorConcrete extends BlockCustomColorBase
{
    public BlockCustomColorConcrete() {
        super(Properties.copy(Blocks.STATION_COLOR_CONCRETE.get()));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new CustomColorConcreteEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof CustomColorBlockEntity) {
                ((CustomColorBlockEntity) entity).syncData();
                PacketGuiServer.openCustomColorScreenS2C((ServerPlayer) player, pos);
            }
        });
    }

    public static class CustomColorConcreteEntity extends CustomColorBlockEntity
    {
        public CustomColorConcreteEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_COLOR_CONCRETE_TILE_ENTITY.get(), pos, state);
        }
    }
}
