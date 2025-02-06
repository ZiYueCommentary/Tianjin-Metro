package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockStationNameWallBase;
import org.mtr.mod.block.BlockStationNameWallWhite;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockStationNameWallWhite
 * @see BlockEntity
 * @since 1.0.0-beta-1
 */

public class BlockStationNameWallLegacy extends BlockStationNameWallBase implements BlockWithEntity
{
    public BlockStationNameWallLegacy() {
        this(Blocks.createDefaultBlockSettings(false));
    }

    public BlockStationNameWallLegacy(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> world.setBlockState(pos, state.cycle(new Property<>(COLOR.data))));
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(COLOR);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderStationNameWallLegacy
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityWallBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_WALL_LEGACY.get(), pos, state);
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
