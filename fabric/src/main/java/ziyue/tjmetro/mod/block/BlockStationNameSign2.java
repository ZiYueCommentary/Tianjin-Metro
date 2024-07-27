package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.TianjinMetro;

/**
 * Second variant for Station Name Sign.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.block.base.BlockStationNameSignBase
 * @see BlockEntity
 * @since 1.0.0-beta-1
 */

public class BlockStationNameSign2 extends BlockStationNameSign1
{
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingWrench(world, player, () -> {
            if (player.getDisplayName().getString().equals("EnderkingIIII")) {
                player.setHealth(player.getMaxHealth() / 100);
                TianjinMetro.LOGGER.warn("Easter egg #1 is activated!");
            }
        });
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockStationNameSign2.BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderStationNameSign
     * @since 1.0.0-beta-1
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_SIGN_2.get(), pos, state);
        }

        @Override
        public int getColor(BlockState state) {
            return ARGB_WHITE;
        }
    }
}
