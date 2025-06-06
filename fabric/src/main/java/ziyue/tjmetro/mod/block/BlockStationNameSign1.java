package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.block.base.BlockStationNameSignBase;

/**
 * First variant for Station Name Sign.
 *
 * @author ZiYueCommentary
 * @see BlockStationNameSignBase
 * @see BlockEntity
 * @since 1.0.0-beta-1
 */

public class BlockStationNameSign1 extends BlockStationNameSignBase
{
    public BlockStationNameSign1() {
        this(Blocks.createDefaultBlockSettings(true));
    }

    public BlockStationNameSign1(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> world.setBlockState(pos, state.cycle(new Property<>(COLOR.data))));
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderStationNameSign
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_SIGN_1.get(), pos, state);
        }

        @Override
        public int getColor(BlockState state) {
            switch (IBlock.getStatePropertySafe(state, COLOR)) {
                case 1:
                    return ARGB_LIGHT_GRAY;
                case 2:
                    return ARGB_BLACK;
                default:
                    return ARGB_WHITE;
            }
        }
    }
}
