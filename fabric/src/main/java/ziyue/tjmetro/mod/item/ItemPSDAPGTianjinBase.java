package ziyue.tjmetro.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.registry.BlockRegistryObject;
import org.mtr.mod.block.*;
import ziyue.tjmetro.mod.block.*;
import ziyue.tjmetro.mod.block.base.BlockFlagAPGTianjin;
import ziyue.tjmetro.mod.block.base.BlockFlagPSDTianjin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static org.mtr.mod.item.ItemPSDAPGBase.blocksNotReplaceable;
import static ziyue.tjmetro.mod.block.BlockPSDTopTianjin.STYLE;

public class ItemPSDAPGTianjinBase extends ItemExtension implements IBlock
{
    public final Block block;

    public ItemPSDAPGTianjinBase(BlockRegistryObject block, ItemSettings settings) {
        super(settings);
        this.block = block.get();
    }

    @Nonnull
    @Override
    public ActionResult useOnBlock2(ItemUsageContext context) {
        final int horizontalBlocks = block.data instanceof BlockPSDAPGDoorBase ? 2 : 1;
        if (blocksNotReplaceable(context, horizontalBlocks, 3, this.block)) return ActionResult.FAIL;

        final World world = context.getWorld();
        final Direction playerFacing = context.getPlayerFacing();
        final BlockPos pos = context.getBlockPos().offset(context.getSide());

        for (int x = 0; x < horizontalBlocks; x++) {
            final BlockPos newPos = pos.offset(playerFacing.rotateYClockwise(), x);

            for (int y = 0; y < 2; y++) {
                final BlockState state = this.block.getDefaultState().with(new Property<>(BlockPSDAPGBase.FACING.data), playerFacing.data).with(new Property<>(HALF.data), y == 1 ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
                if (block.data instanceof BlockPSDAPGDoorBase) {
                    BlockState neighborState = state.with(new Property<>(SIDE.data), x == 0 ? EnumSide.LEFT : EnumSide.RIGHT);
                    world.setBlockState(newPos.up(y), neighborState);
                } else {
                    world.setBlockState(newPos.up(y), state.with(new Property<>(SIDE_EXTENDED.data), EnumSide.SINGLE));
                }
            }
            if (this.block.data instanceof BlockFlagPSDTianjin) {
                world.setBlockState(newPos.up(2), BlockPSDTopTianjin.getActualState(WorldAccess.cast(world), newPos.up(2)).with(new Property<>(STYLE.data), BlockPSDTopTianjin.EnumDoorType.STATION_NAME));
            }
        }

        context.getStack().decrement(1);
        return ActionResult.SUCCESS;
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
        if (this.block.data instanceof BlockPSDAPGDoorBase) {
            tooltip.add(TextHelper.translatable("tooltip.mtr.psd_apg_door").formatted(TextFormatting.GRAY));
        } else if (this.block.data instanceof BlockPSDAPGGlassEndBase) {
            tooltip.add(TextHelper.translatable("tooltip.mtr.psd_apg_glass_end").formatted(TextFormatting.GRAY));
        } else if (this.block.data instanceof BlockPSDAPGGlassBase) {
            tooltip.add(TextHelper.translatable("tooltip.mtr.psd_apg_glass").formatted(TextFormatting.GRAY));
        }
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        if (this.block.data instanceof BlockFlagPSDTianjin) {
            return "block.tjmetro.psd_tianjin";
        } else if (this.block.data instanceof BlockFlagAPGTianjin) {
            return "block.tjmetro.apg_tianjin";
        } else {
            return "block.tjmetro.apg_tianjin_bmt";
        }
    }
}
