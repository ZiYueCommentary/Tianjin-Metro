package ziyue.tjmetro.mod.block;

import org.jetbrains.annotations.Nullable;
import org.mtr.mapping.holder.BlockView;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.TooltipContext;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.block.BlockTicketBarrier;
import ziyue.tjmetro.mod.data.IGuiExtension;

import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockTicketBarrier
 * @since 1.1.3
 */
public class BlockTicketBarrierOutboundTransfer extends BlockTicketBarrier {
    public BlockTicketBarrierOutboundTransfer() {
        super(false);
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.outbound_transfer"));
    }
}
