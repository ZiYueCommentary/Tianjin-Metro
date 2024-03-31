package ziyue.tjmetro.block;

import mtr.block.BlockRailwaySignPole;
import mtr.block.IBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class BlockRailwaySignTianjinPole extends BlockRailwaySignPole
{
    public BlockRailwaySignTianjinPole() {
        this(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1).noOcclusion());
    }

    public BlockRailwaySignTianjinPole(Properties settings) {
        super(settings);
    }

    @Override
    protected BlockState placeWithState(BlockState stateBelow) {
        final int type;
        final Block block = stateBelow.getBlock();
        if (block instanceof BlockRailwaySignTianjin block1) {
            type = (block1.length + (block1.isOdd ? 2 : 0)) % 4;
        } else {
            type = IBlock.getStatePropertySafe(stateBelow, TYPE);
        }
        return super.placeWithState(stateBelow).setValue(TYPE, type);
    }

    @Override
    protected boolean isBlock(Block block) {
        return (block instanceof BlockRailwaySignTianjin && ((BlockRailwaySignTianjin) block).length > 0) || block instanceof BlockRailwaySignTianjinPole;
    }
}
