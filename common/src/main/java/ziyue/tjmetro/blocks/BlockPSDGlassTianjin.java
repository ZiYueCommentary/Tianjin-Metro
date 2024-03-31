package ziyue.tjmetro.blocks;

import mtr.block.BlockPSDAPGGlassBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import ziyue.tjmetro.ItemList;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.items.ItemPSDTianjinBase
 * @see BlockPSDAPGGlassBase
 * @since beta-1
 */

public class BlockPSDGlassTianjin extends BlockPSDAPGGlassBase
{
    @Override
    public Item asItem() {
        return ItemList.PSD_GLASS_TIANJIN.get();
    }
}
