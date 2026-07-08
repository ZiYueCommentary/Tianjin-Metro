package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.flag.BlockFlagDoorSingle;
import ziyue.tjmetro.mod.block.flag.BlockFlagPSDTianjin;

import javax.annotation.Nonnull;

/**
 * @author ZiYueCommentary
 * @see org.mtr.mod.block.BlockPSDDoor
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @see BlockEntity
 * @since 1.1.1
 */

public class BlockPSDDoorSingle extends BlockPSDAPGDoorBase implements BlockFlagPSDTianjin, BlockFlagDoorSingle
{
    public final boolean isLeft;

    public BlockPSDDoorSingle(boolean isLeft) {
        this.isLeft = isLeft;
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return isLeft ? ItemList.PSD_DOOR_SINGLE_LEFT.get() : ItemList.PSD_DOOR_SINGLE_RIGHT.get();
    }

    @Nonnull
    @Override
    public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return BlockFlagDoorSingle.getStateForNeighborUpdate(state, world, pos);
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockFlagDoorSingle.onBreak(world, pos, state, player);
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return BlockFlagDoorSingle.onUse(state, world, pos, player);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState, isLeft);
    }

    @Override
    public boolean isLeft() {
        return isLeft;
    }

    /**
     * @author ZiYueCommentary
     * @see org.mtr.mod.render.RenderPSDAPGDoor
     * @since 1.1.1
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public final boolean isLeft;

        public BlockEntity(BlockPos pos, BlockState state, boolean isLeft) {
            super(isLeft ? BlockEntityTypes.PSD_DOOR_SINGLE_LEFT.get() : BlockEntityTypes.PSD_DOOR_SINGLE_RIGHT.get(), pos, state);
            this.isLeft = isLeft;
        }
    }
}
