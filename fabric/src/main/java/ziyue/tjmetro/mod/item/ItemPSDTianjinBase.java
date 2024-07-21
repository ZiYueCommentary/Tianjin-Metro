package ziyue.tjmetro.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.registry.BlockRegistryObject;
import org.mtr.mod.block.BlockPSDAPGBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.block.BlockPSDDoorTianjin;
import ziyue.tjmetro.mod.block.BlockPSDGlassEndTianjin;
import ziyue.tjmetro.mod.block.BlockPSDGlassTianjin;
import ziyue.tjmetro.mod.block.BlockPSDTopTianjin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static org.mtr.mod.item.ItemPSDAPGBase.blocksNotReplaceable;
import static ziyue.tjmetro.mod.block.BlockPSDTopTianjin.STYLE;

public class ItemPSDTianjinBase extends ItemExtension implements IBlock
{
    public final Block block;

    public ItemPSDTianjinBase(BlockRegistryObject block, ItemSettings settings) {
        super(settings);
        this.block = block.get();
    }

    @Nonnull
    @Override
    public ActionResult useOnBlock2(ItemUsageContext context) {
        final int horizontalBlocks = block.data instanceof BlockPSDDoorTianjin ? 2 : 1;
        if (blocksNotReplaceable(context, horizontalBlocks, 3, getBlockStateFromItem().getBlock())) return ActionResult.FAIL;

        final World world = context.getWorld();
        final Direction playerFacing = context.getPlayerFacing();
        final BlockPos pos = context.getBlockPos().offset(context.getSide());

        for (int x = 0; x < horizontalBlocks; x++) {
            final BlockPos newPos = pos.offset(playerFacing.rotateYClockwise(), x);

            for (int y = 0; y < 2; y++) {
                final BlockState state = getBlockStateFromItem().with(new Property<>(BlockPSDAPGBase.FACING.data), playerFacing.data).with(new Property<>(HALF.data), y == 1 ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
                if (block.data instanceof BlockPSDDoorTianjin) {
                    BlockState neighborState = state.with(new Property<>(SIDE.data), x == 0 ? EnumSide.LEFT : EnumSide.RIGHT);
                    world.setBlockState(newPos.up(y), neighborState);
                } else {
                    world.setBlockState(newPos.up(y), state.with(new Property<>(SIDE_EXTENDED.data), EnumSide.SINGLE));
                }
            }
            world.setBlockState(newPos.up(2), BlockPSDTopTianjin.getActualState(WorldAccess.cast(world), newPos.up(2)).with(new Property<>(STYLE.data), BlockPSDTopTianjin.EnumDoorType.STATION_NAME));
        }

        context.getStack().decrement(1);
        return ActionResult.SUCCESS;
    }

    protected BlockState getBlockStateFromItem() {
        if (this.block.data instanceof BlockPSDDoorTianjin) {
            return BlockList.PSD_DOOR_TIANJIN.get().getDefaultState();
        } else if (this.block.data instanceof BlockPSDGlassTianjin) {
            return BlockList.PSD_GLASS_TIANJIN.get().getDefaultState();
        } else if (this.block.data instanceof BlockPSDGlassEndTianjin) {
            return BlockList.PSD_GLASS_END_TIANJIN.get().getDefaultState();
        }
        return null;
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
        if (this.block.data instanceof BlockPSDDoorTianjin) {
            tooltip.add(TextHelper.translatable("tooltip.mtr.psd_apg_door").formatted(TextFormatting.GRAY));
        } else if (this.block.data instanceof BlockPSDGlassTianjin) {
            tooltip.add(TextHelper.translatable("tooltip.mtr.psd_apg_glass").formatted(TextFormatting.GRAY));
        } else if (this.block.data instanceof BlockPSDGlassEndTianjin) {
            tooltip.add(TextHelper.translatable("tooltip.mtr.psd_apg_glass_end").formatted(TextFormatting.GRAY));
        }
    }

    @Nonnull
    @Override
    public String getTranslationKey2() {
        return "block.tjmetro.psd_tianjin";
    }
}