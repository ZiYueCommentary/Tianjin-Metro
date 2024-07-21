package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mod.block.BlockRailwaySignPole;
import org.mtr.mod.block.IBlock;

import javax.annotation.Nonnull;

public class BlockRailwaySignTianjinPole extends BlockRailwaySignPole
{
    public BlockRailwaySignTianjinPole() {
        this(BlockHelper.createBlockSettings(false));
    }

    public BlockRailwaySignTianjinPole(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    protected BlockState placeWithState(BlockState stateBelow) {
        final int type;
        final Block block = stateBelow.getBlock();
        if (block.data instanceof BlockRailwaySignTianjin sign) {
            type = (sign.length + (sign.isOdd ? 2 : 0)) % 4;
        } else {
            type = IBlock.getStatePropertySafe(stateBelow, TYPE);
        }
        return super.placeWithState(stateBelow).with(new Property<>(TYPE.data), type);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return switch (IBlock.getStatePropertySafe(state, TYPE)) {
            case 0 -> IBlock.getVoxelShapeByDirection(3, 0, 7.5, 4, 16, 8.5, facing);
            case 1 -> IBlock.getVoxelShapeByDirection(15, 0, 7.5, 16, 16, 8.5, facing);
            case 2 -> IBlock.getVoxelShapeByDirection(11, 0, 7.5, 12, 16, 8.5, facing);
            case 3 -> IBlock.getVoxelShapeByDirection(7, 0, 7.5, 8, 16, 8.5, facing);
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    protected boolean isBlock(Block block) {
        return block.data instanceof BlockRailwaySignTianjin sign && (sign.length > 0) || block.data instanceof BlockRailwaySignTianjinPole;
    }
}
