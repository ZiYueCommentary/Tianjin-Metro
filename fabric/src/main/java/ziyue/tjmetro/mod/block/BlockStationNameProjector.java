package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.BlockStationNameWallBase;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.data.IGuiExtension;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since 1.0.0-beta-4
 */

public class BlockStationNameProjector extends BlockStationNameWallBase implements BlockWithEntity, DirectionHelper
{
    public static final IntegerProperty SCALE = IntegerProperty.of("scale", 1, 5);

    public BlockStationNameProjector() {
        this(Blocks.createDefaultBlockSettings(false));
    }

    public BlockStationNameProjector(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> world.setBlockState(pos, state.cycle(new Property<>(SCALE.data))));
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data).with(new Property<>(SCALE.data), 2);
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.station_name_projector"));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(SCALE);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see ziyue.tjmetro.mod.render.RenderStationNameProjector
     * @since 1.0.0-beta-4
     */
    public static class BlockEntity extends BlockStationNameWallBase.BlockEntityWallBase
    {
        public BlockEntity(BlockPos blockPos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_PROJECTOR.get(), blockPos, state);
        }

        @Override
        public int getColor(BlockState state) {
            return 0xFFFF0000;
        }
    }
}
