package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.Items;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.block.base.BlockCustomColorBase;
import ziyue.tjmetro.mod.data.IGuiExtension;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since 1.0.0-beta-4
 */

public class BlockStationNameProjector extends BlockCustomColorBase implements BlockWithEntity, DirectionHelper
{
    public static final IntegerProperty SCALE = IntegerProperty.of("scale", 1, 7);

    public BlockStationNameProjector() {
        this(Blocks.createDefaultBlockSettings(false).nonOpaque().noCollision());
    }

    public BlockStationNameProjector(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingItem(world, player, item -> {
            if (item.data == Items.BRUSH.get().data) {
                world.setBlockState(pos, state.cycle(new Property<>(SCALE.data)));
            } else {
                Registry.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(pos));
            }
        }, null, Items.BRUSH.get(), ItemList.WRENCH.get());
    }

    @Nullable
    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data).with(new Property<>(SCALE.data), 2);
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.tjmetro.custom_color").formatted(TextFormatting.GRAY));
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.station_name_projector"));
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
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
    public static class BlockEntity extends BlockCustomColorBase.BlockEntityBase
    {
        public BlockEntity(BlockPos blockPos, BlockState state) {
            super(BlockEntityTypes.STATION_NAME_PROJECTOR.get(), blockPos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            if (compoundTag.contains(COLOR_ID)) { // Backward compatibility.
                color = compoundTag.getInt(COLOR_ID);
            }
            super.readCompoundTag(compoundTag);
        }

        @Override
        public int getDefaultColor(BlockPos pos) {
            return 0xFFFF0000;
        }
    }
}
