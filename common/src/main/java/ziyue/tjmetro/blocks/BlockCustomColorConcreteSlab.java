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
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;
import ziyue.tjmetro.packet.PacketGuiServer;

import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockCustomColorBase
 * @since 1.0b
 */

public class BlockCustomColorConcreteSlab extends SlabBlock implements EntityBlockMapper
{
    public BlockCustomColorConcreteSlab(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new CustomColorConcreteSlabEntity(pos, state);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Text.translatable("tooltip.tjmetro.custom_color").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof BlockCustomColorBase.CustomColorBlockEntity) {
                ((BlockCustomColorBase.CustomColorBlockEntity) entity).syncData();
                PacketGuiServer.openCustomColorScreenS2C((ServerPlayer) player, pos);
            }
        });
    }

    public static class CustomColorConcreteSlabEntity extends BlockCustomColorBase.CustomColorBlockEntity
    {
        public CustomColorConcreteSlabEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_COLOR_CONCRETE_SLAB_TILE_ENTITY.get(), pos, state);
        }
    }
}
