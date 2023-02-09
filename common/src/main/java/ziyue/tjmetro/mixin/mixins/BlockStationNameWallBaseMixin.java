package ziyue.tjmetro.mixin.mixins;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameWallBase;
import mtr.block.IBlock;
import mtr.mappings.Text;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.IDrawingExtends;
import ziyue.tjmetro.mixin.properties.BlockStationNameProperties;

import java.util.List;

/**
 * @author ZiYueCommentary
 * @see mtr.block.BlockStationNameWallBase
 * @since beta-1
 */

@Mixin(BlockStationNameWallBase.class)
public abstract class BlockStationNameWallBaseMixin extends BlockStationNameBase implements BlockStationNameProperties, IBlock
{
    private static final BooleanProperty SHOW_NAME = BooleanProperty.create("show_name");

    protected BlockStationNameWallBaseMixin(Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return IBlock.checkHoldingItem(world, player, item -> {
            world.setBlockAndUpdate(pos, state.setValue(SHOW_NAME, !state.getValue(SHOW_NAME)));
            propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), SHOW_NAME, 1);
            propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), SHOW_NAME, 1);
        }, null, Items.SHEARS);
    }

    @Inject(at = @At("TAIL"), method = "createBlockStateDefinition")
    protected void afterCreateBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SHOW_NAME);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        IDrawingExtends.addHoldShiftTooltip(tooltip, Text.translatable("tooltip.tjmetro.station_name_wall"), true);
    }

    @Override
    public boolean getShowNameProperty(BlockState state) {
        return state.getValue(SHOW_NAME);
    }
}
