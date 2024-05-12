package ziyue.tjmetro.block;

import mtr.block.BlockRailwaySignPole;
import mtr.block.IBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * @author ZiYueCommentary
 * @see BlockRailwaySignTianjin
 * @see BlockRailwaySignPole
 * @since beta-1
 */

public class BlockRailwaySignTianjinPole extends BlockRailwaySignPole
{
    public BlockRailwaySignTianjinPole() {
        this(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(1).noOcclusion());
    }

    public BlockRailwaySignTianjinPole(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        final Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return switch (IBlock.getStatePropertySafe(state, TYPE)) {
            case 0 -> IBlock.getVoxelShapeByDirection(3, 0, 7.5, 4, 16, 8.5, facing);
            case 1 -> IBlock.getVoxelShapeByDirection(15, 0, 7.5, 16, 16, 8.5, facing);
            case 2 -> IBlock.getVoxelShapeByDirection(11, 0, 7.5, 12, 16, 8.5, facing);
            case 3 -> IBlock.getVoxelShapeByDirection(7, 0, 7.5, 8, 16, 8.5, facing);
            default -> Shapes.block();
        };
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
        return (block instanceof BlockRailwaySignTianjin && ((BlockRailwaySignTianjin) block).length > 0) || block instanceof BlockRailwaySignTianjinPole || block instanceof BlockRailwaySignTianjinDouble;
    }
}
