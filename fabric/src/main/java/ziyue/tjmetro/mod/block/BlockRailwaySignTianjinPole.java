package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockPoleCheckBase;
import org.mtr.mod.block.BlockRailwaySignPole;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockRailwaySignPole
 * @since 1.0.0-beta-1
 */

public class BlockRailwaySignTianjinPole extends BlockPoleCheckBase
{
    public static final IntegerProperty TYPE = IntegerProperty.of("type", 0, 4);

    public BlockRailwaySignTianjinPole() {
        this(Blocks.createDefaultBlockSettings(false));
    }

    public BlockRailwaySignTianjinPole(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    protected BlockState placeWithState(BlockState stateBelow) {
        final int type;
        final Block block = stateBelow.getBlock();
        if (block.data instanceof BlockRailwaySignTianjin) {
            final BlockRailwaySignTianjin sign = (BlockRailwaySignTianjin) block.data;
            type = (sign.length + (sign.isOdd ? 2 : 0)) % 4;
        } else if (block.data instanceof BlockPIDSTianjin) {
            type = 4;
        }else {
            type = IBlock.getStatePropertySafe(stateBelow, TYPE);
        }
        return super.placeWithState(stateBelow).with(new Property<>(TYPE.data), type);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(TYPE);
        properties.add(FACING);
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
            case 4 -> IBlock.getVoxelShapeByDirection(7.5, 0, 7.5, 8.5, 16, 8.5, facing);
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    protected boolean isBlock(Block block) {
        return block.data instanceof BlockRailwaySignTianjin && (((BlockRailwaySignTianjin) block.data).length > 0) || block.data instanceof BlockRailwaySignTianjinPole || block.data instanceof BlockPIDSTianjin;
    }

    @Override
    protected Text getTooltipBlockText() {
        return TranslationProvider.BLOCK_MTR_RAILWAY_SIGN.getText();
    }
}
