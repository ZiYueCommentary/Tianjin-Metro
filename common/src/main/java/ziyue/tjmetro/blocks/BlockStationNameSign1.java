package ziyue.tjmetro.blocks;

import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.blocks.base.BlockStationNameSignBase;

/**
 * First variant for <b>Station Name Sign</b>.
 *
 * @author ZiYueCommentary
 * @see BlockStationNameSignBase
 * @since 1.0b
 */

public class BlockStationNameSign1 extends BlockStationNameSignBase
{
    public BlockStationNameSign1(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.isHolding(Items.STICK)) return super.use(state, world, pos, player, interactionHand, blockHitResult);
        return IBlock.checkHoldingBrush(world, player, () -> world.setBlockAndUpdate(pos, state.cycle(COLOR)));
    }

    @Override
    public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockStationNameSign1.TileEntityStationNameWall(pos, state);
    }

    public static class TileEntityStationNameWall extends BlockStationNameSignBase.TileEntityStationNameBase
    {
        public TileEntityStationNameWall(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_SIGN_ENTITY_1.get(), pos, state);
        }
    }
}
