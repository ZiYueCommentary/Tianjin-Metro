package ziyue.tjmetro.mod.mixin;

import org.mtr.mapping.holder.*;
import org.mtr.mod.block.BlockPoleCheckBase;
import org.mtr.mod.block.BlockRailwaySign;
import org.mtr.mod.block.BlockRailwaySignPole;
import org.mtr.mod.block.IBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mod.block.BlockRailwaySignTianjinBMT;
import ziyue.tjmetro.mod.block.BlockStationNamePlate;

@Mixin(BlockRailwaySignPole.class)
public abstract class BlockRailwaySignPoleMixin extends BlockPoleCheckBase
{
    @Shadow(remap = false)
    @Final
    public static IntegerProperty TYPE;

    public BlockRailwaySignPoleMixin(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    protected BlockState placeWithState(BlockState stateBelow) {
        Block block = stateBelow.getBlock();
        int type;
        if (block.data instanceof BlockRailwaySign) {
            type = (((BlockRailwaySign) block.data).length + (((BlockRailwaySign) block.data).isOdd ? 2 : 0)) % 4;
        } else if (block.data instanceof BlockRailwaySignTianjinBMT) {
            type = (((BlockRailwaySignTianjinBMT) block.data).length + (((BlockRailwaySignTianjinBMT) block.data).isOdd ? 2 : 0)) % 4;
        } else if (block.data instanceof BlockStationNamePlate) {
            type = 3;
        } else {
            type = IBlock.getStatePropertySafe(stateBelow, TYPE);
        }

        return super.placeWithState(stateBelow).with(new Property<>(TYPE.data), type);
    }

    @Override
    protected boolean isBlock(Block block) {
        final boolean vanilla = block.data instanceof BlockRailwaySign && ((BlockRailwaySign) block.data).length > 0 || block.data instanceof BlockRailwaySignPole;
        final boolean extended = block.data instanceof BlockRailwaySignTianjinBMT && ((BlockRailwaySignTianjinBMT) block.data).length > 0;
        return vanilla || extended;
    }
}
