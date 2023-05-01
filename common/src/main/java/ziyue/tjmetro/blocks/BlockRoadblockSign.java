package ziyue.tjmetro.blocks;

import mtr.Blocks;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.TianjinMetro;
import ziyue.tjmetro.blocks.base.BlockCustomContentBlockBase;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.List;

/**
 * Roadblock with signs.
 *
 * @author ZiYueCommentary
 * @see BlockRoadblock
 * @since beta-1
 */

public class BlockRoadblockSign extends BlockRoadblock implements EntityBlockMapper
{
    public BlockRoadblockSign() {
        this(BlockBehaviour.Properties.copy(Blocks.LOGO.get()).lightLevel((state) -> 0));
    }

    public BlockRoadblockSign(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof TileEntityRoadBlockSign entity1) {
                entity1.syncData();
                PacketGuiServer.openCustomContentScreenS2C((ServerPlayer) player, pos);
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
     * @see BlockCustomContentBlockBase.CustomContentBlockEntity
     */
    public static class TileEntityRoadBlockSign extends BlockCustomContentBlockBase.CustomContentBlockEntity
    {
        public TileEntityRoadBlockSign(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.ROADBLOCK_SIGN_ENTITY.get(), pos, state, 0, 0.535f);
        }

        @Override
        public void setData(String content) {
            this.content = content;
            BlockPos blockPos = getBlockPos().relative(getBlockState().getValue(FACING).getClockWise());
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof TileEntityRoadBlockSign entityRoadBlockSign) {
                entityRoadBlockSign.content = this.content;
                entityRoadBlockSign.setChanged();
                entityRoadBlockSign.syncData();
            } else {
                TianjinMetro.LOGGER.error("TileEntityRoadBlockSign: Unable to set data for block entity at " + blockPos.toShortString());
            }
            setChanged();
            syncData();
        }
    }
}
