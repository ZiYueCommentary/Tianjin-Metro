package ziyue.tjmetro.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.data.IGuiExtension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;

public class BlockSmokeAlarm extends BlockExtension implements BlockWithEntity
{
    public static BooleanProperty ACTIVATED = BooleanProperty.of("activated");

    public BlockSmokeAlarm() {
        this(Blocks.createDefaultBlockSettings(false, state -> IBlock.getStatePropertySafe(state, ACTIVATED) ? 5 : 0));
    }

    public BlockSmokeAlarm(BlockSettings blockSettings) {
        super(blockSettings);
    }

    @Override
    public @Nullable BlockState getPlacementState2(ItemPlacementContext ctx) {
        return super.getPlacementState2(ctx).with(new Property<>(ACTIVATED.data), false);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(VoxelShapes.union(
                VoxelShapes.cuboid(0.35, 0.9375, 0.35, 0.65, 1, 0.65),
                VoxelShapes.cuboid(0.38, 0.9, 0.38, 0.62, 0.9375, 0.62)
        ), VoxelShapes.cuboid(0.43125, 0.875, 0.43125, 0.49375, 0.9, 0.49375));
    }

    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(ACTIVATED);
    }

    @Override
    public boolean emitsRedstonePower2(BlockState state) {
        return IBlock.getStatePropertySafe(state, ACTIVATED);
    }

    @Override
    public int getStrongRedstonePower2(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return IBlock.getStatePropertySafe(state, ACTIVATED) ? 15 : 0;
    }

    @Override
    public int getWeakRedstonePower2(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return IBlock.getStatePropertySafe(state, ACTIVATED) ? 15 : 0;
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        IGuiExtension.addHoldShiftTooltip(tooltip, TextHelper.translatable("tooltip.tjmetro.smoke_alarm"));
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> world.setBlockState(pos, state.with(new Property<>(ACTIVATED.data), false)));
    }

    public static class BlockEntity extends BlockEntityExtension
    {
        public static BiConsumer<BlockPos, Boolean> updateSoundSource = null;

        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SMOKE_ALARM.get(), pos, state);
        }

        @Override
        public void blockEntityTick() {
            if (getWorld2() != null && getWorld2().isClient() && updateSoundSource != null) {
                updateSoundSource.accept(getPos2(), !IBlock.getStatePropertySafe(getCachedState2(), ACTIVATED));
            }
        }

        @Override
        public void markRemoved2() {
            if (getWorld2() != null && getWorld2().isClient() && updateSoundSource != null) {
                updateSoundSource.accept(getPos2(), true);
            }
            super.markRemoved2();
        }
    }
}
