package ziyue.tjmetro.mixin.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

import static net.minecraft.world.level.block.FallingBlock.isFree;
import static ziyue.tjmetro.Configs.noFallingBlocks;

/**
 * Made for <i>No Falling Blocks</i> feature.
 * @author ZiYueCommentary
 * @since 1.0b
 * @see FallingBlock
 */

@Mixin(FallingBlock.class)
public abstract class FallingBlockMixin extends Block
{
    public FallingBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if(!noFallingBlocks/*level.getGameRules().getBoolean(TianjinMetro.PREVENT_BLOCK_FALLING)*/)
            level.getBlockTicks().scheduleTick(blockPos, this, 2);
    }

    /**
     * @author ZiYueCommentary
     * @since 1.0b
     * @reason Core of <i>No Falling Blocks</i> feature.
     */
    @Overwrite
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if(!noFallingBlocks/*level.getGameRules().getBoolean(TianjinMetro.PREVENT_BLOCK_FALLING)*/) {
            if (isFree(serverLevel.getBlockState(blockPos.below())) && blockPos.getY() >= 0) {
                FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(serverLevel, (double) blockPos.getX() + 0.5, (double) blockPos.getY(), (double) blockPos.getZ() + 0.5, serverLevel.getBlockState(blockPos));
                serverLevel.addFreshEntity(fallingBlockEntity);
            }
        }
    }
}
