package ziyue.tjmetro.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.IExtends;
import ziyue.tjmetro.blocks.base.CustomContentBlockBase;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.List;

/**
 * Roadblock with content.
 *
 * @author ZiYueCommentary
 * @see BlockRoadblock
 * @since 1.0b
 */

public class BlockRoadblockSign extends BlockRoadblock implements EntityBlockMapper
{
    public BlockRoadblockSign(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof TileEntityRoadBlockSign) {
                ((TileEntityRoadBlockSign) entity).syncData();
                PacketGuiServer.openCustomContentScreenS2C((ServerPlayer) player, blockState.getValue(IS_RIGHT) ? pos.relative(blockState.getValue(FACING).getCounterClockWise()) : pos);
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Text.translatable("tooltip.tjmetro.custom_content").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockRoadblockSign.TileEntityRoadBlockSign(pos, state);
    }

    /**
     * @see CustomContentBlockBase.CustomContentBlockEntity
     */
    public static class TileEntityRoadBlockSign extends CustomContentBlockBase.CustomContentBlockEntity
    {
        public TileEntityRoadBlockSign(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.ROADBLOCK_SIGN_ENTITY.get(), pos, state, 0, 0.535f);
        }

        @Override
        public void setData(String content) {
            this.content = content;
            ((TileEntityRoadBlockSign) level.getBlockEntity(getBlockPos().relative(getBlockState().getValue(FACING).getClockWise()))).content = content;
            setChanged();
            syncData();
        }

        @Override
        public boolean shouldRender() {
            return !getBlockState().getValue(IS_RIGHT);
        }
    }
}
