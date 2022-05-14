package ziyue.tjmetro.blocks;

import mtr.Blocks;
import net.minecraft.world.level.block.Block;

public class BlockDecorationLight extends Block
{
    public BlockDecorationLight() {
        super(Properties.copy(Blocks.MARBLE_BLUE.get()).noCollission().lightLevel((state) -> 10));
    }


}
