package ziyue.tjmetro.mixin.mixins;

import mtr.Blocks;
import mtr.block.BlockCeiling;
import mtr.mappings.BlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import ziyue.tjmetro.BlockList;

import static mtr.block.BlockCeiling.FACING;

/**
 * Use shears to switch lit of <b>BlockCeiling</b>.
 *
 * @author ZiYueCommentary
 * @see BlockCeiling
 * @see BlockCeilingAutoMixin
 * @since 1.0b
 */

@Mixin(BlockCeiling.class)
public class BlockCeilingLightMixin extends BlockMapper
{
    public BlockCeilingLightMixin(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.isHolding(Items.SHEARS)) {
            if (blockState.getBlock() == Blocks.CEILING_LIGHT.get()) {
                level.setBlock(blockPos, BlockList.CEILING_NOT_LIT.get().defaultBlockState().setValue(FACING, blockState.getValue(FACING)), 1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
