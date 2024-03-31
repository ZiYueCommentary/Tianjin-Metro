package ziyue.tjmetro.items;

import mtr.RegistryObject;
import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.IBlock;
import mtr.item.ItemWithCreativeTabBase;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;
import ziyue.tjmetro.TianjinMetro;
import ziyue.tjmetro.blocks.BlockPSDDoorTianjin;
import ziyue.tjmetro.blocks.BlockPSDGlassEndTianjin;
import ziyue.tjmetro.blocks.BlockPSDGlassTianjin;
import ziyue.tjmetro.blocks.BlockPSDTopTianjin;
import ziyue.tjmetro.client.ClientCache;

import java.util.List;

import static mtr.item.ItemPSDAPGBase.blocksNotReplaceable;
import static ziyue.tjmetro.blocks.BlockPSDTopTianjin.STYLE;
import static ziyue.tjmetro.blocks.BlockPSDTopTianjin.getActualState;

/**
 * @author ZiYueCommentary
 * @see mtr.item.ItemPSDAPGBase
 * @see BlockPSDGlassEndTianjin
 * @see BlockPSDGlassTianjin
 * @see BlockPSDDoorTianjin
 * @since beta-1
 */

public class ItemPSDTianjinBase extends ItemWithCreativeTabBase implements IBlock
{
    public final Block block;

    public ItemPSDTianjinBase(RegistryObject<Block> block) {
        super(TianjinMetro.CREATIVE_MODE_TAB);
        this.block = block.get();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final int horizontalBlocks = block instanceof BlockPSDDoorTianjin ? 2 : 1;
        if (blocksNotReplaceable(context, horizontalBlocks, 3, block)) {
            return InteractionResult.FAIL;
        }

        final Level world = context.getLevel();
        final Direction playerFacing = context.getHorizontalDirection();
        final BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

        for (int x = 0; x < horizontalBlocks; x++) {
            final BlockPos newPos = pos.relative(playerFacing.getClockWise(), x);

            for (int y = 0; y < 2; y++) {
                final BlockState state = block.defaultBlockState().setValue(BlockPSDAPGBase.FACING, playerFacing).setValue(HALF, y == 1 ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
                if (block instanceof BlockPSDDoorTianjin) {
                    BlockState newState = state.setValue(SIDE, x == 0 ? EnumSide.LEFT : EnumSide.RIGHT);
                    world.setBlockAndUpdate(newPos.above(y), newState);
                } else {
                    world.setBlockAndUpdate(newPos.above(y), state.setValue(SIDE_EXTENDED, EnumSide.SINGLE));
                }
            }

            world.setBlockAndUpdate(newPos.above(2), getActualState(world, newPos.above(2)).setValue(STYLE, BlockPSDTopTianjin.EnumDoorType.STATION_NAME));
        }

        context.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if (this.block instanceof BlockPSDDoorTianjin) {
            list.add(Text.translatable("tooltip.mtr.psd_apg_door").withStyle(ChatFormatting.GRAY));
        }
        if (this.block instanceof BlockPSDGlassTianjin) {
            list.add(Text.translatable("tooltip.mtr.psd_apg_glass").withStyle(ChatFormatting.GRAY));
        }
        if (this.block instanceof BlockPSDGlassEndTianjin) {
            list.add(Text.translatable("tooltip.mtr.psd_apg_glass_end").withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public String getDescriptionId() {
        return "block.tjmetro.psd_tianjin";
    }
}
