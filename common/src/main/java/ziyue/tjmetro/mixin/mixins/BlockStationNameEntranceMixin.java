package ziyue.tjmetro.mixin.mixins;

import mtr.block.BlockStationNameBase;
import mtr.block.BlockStationNameEntrance;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ziyue.tjmetro.IDrawingExtends;
import ziyue.tjmetro.mixin.properties.BlockStationNameProperties;

import java.util.List;

/**
 * Made for <b>No "station" of station name entrance</b> feature.
 * Inspired by <a href="https://github.com/jonafanho/Minecraft-Transit-Railway/issues/172">Issue #172: 奇怪的“站站”、“Station Station”</a>.
 *
 * @author ZiYueCommentary
 * @see BlockStationNameEntrance
 * @see RenderStationNameTiledMixin
 * @since beta-1
 */

@Mixin(BlockStationNameEntrance.class)
public abstract class BlockStationNameEntranceMixin extends BlockStationNameBase implements IBlock, BlockStationNameProperties
{
    protected BlockStationNameEntranceMixin(Properties settings) {
        super(settings);
    }

    private static final BooleanProperty SHOW_NAME = BooleanProperty.create("show_name");

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SHOW_NAME);
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (player.isHolding(Items.SHEARS)) {
            world.setBlockAndUpdate(pos, state.setValue(SHOW_NAME, !state.getValue(SHOW_NAME)));
            propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getClockWise(), SHOW_NAME, 1);
            propagate(world, pos, IBlock.getStatePropertySafe(state, FACING).getCounterClockWise(), SHOW_NAME, 1);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter blockGetter, List<Component> tooltip, TooltipFlag tooltipFlag) {
        IDrawingExtends.addHoldShiftTooltip(tooltip, Text.literal("放在地铁站入口的大型站名牌，可以用剪刀隐藏/显示站名牌的“站Station”。该功能由天津地铁模组添加。"), true);
    }

    @Override
    public boolean getShowNameProperty(BlockState state) {
        return state.getValue(SHOW_NAME);
    }
}
