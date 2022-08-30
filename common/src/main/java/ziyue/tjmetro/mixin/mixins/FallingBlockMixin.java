package ziyue.tjmetro.mixin.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import ziyue.tjmetro.TianjinMetro;

import java.util.Random;

import static net.minecraft.world.level.block.FallingBlock.isFree;

/**
 * Made for <i>No Falling Blocks</i> feature.
 *
 * @author ZiYueCommentary
 * @see FallingBlock
 * @since 1.0b
 */

@Mixin(FallingBlock.class)
public class FallingBlockMixin extends Block
{
    public FallingBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!level.getGameRules().getBoolean(TianjinMetro.PREVENT_BLOCK_FALLING)) {
            level.getBlockTicks().scheduleTick(blockPos, this, 2);
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if (!serverLevel.getGameRules().getBoolean(TianjinMetro.PREVENT_BLOCK_FALLING)) {
            if (isFree(serverLevel.getBlockState(blockPos.below())) && blockPos.getY() >= 0) {
                FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(serverLevel, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, serverLevel.getBlockState(blockPos));
                serverLevel.addFreshEntity(fallingBlockEntity);
            }
        }
    }
}
