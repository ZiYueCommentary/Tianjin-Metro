package ziyue.tjmetro.block;

import mtr.Blocks;
import mtr.block.IBlock;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ziyue.tjmetro.BlockEntityTypes;
import ziyue.tjmetro.ItemList;
import ziyue.tjmetro.block.base.BlockStationNameSignBase;

/**
 * First variant for <b>Station Name Sign</b>.
 *
 * @author ZiYueCommentary
 * @see BlockStationNameSignBase
 * @since beta-1
 */

public class BlockStationNameSign1 extends BlockStationNameSignBase
{
    public BlockStationNameSign1() {
        this(BlockBehaviour.Properties.copy(Blocks.STATION_NAME_TALL_WALL.get()));
    }

    public BlockStationNameSign1(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.isHolding(ItemList.WRENCH.get())) return super.use(state, world, pos, player, interactionHand, blockHitResult);
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
