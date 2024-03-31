package ziyue.tjmetro.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.TianjinMetro;

import java.util.Random;

import static net.minecraft.world.level.block.FallingBlock.isFree;

/**
 * Made for <b>No Falling Blocks</b> feature.
 *
 * @author ZiYueCommentary
 * @see FallingBlock
 * @since beta-1
 */

@Mixin(FallingBlock.class)
public abstract class FallingBlockMixin extends Block
{
    public FallingBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(at = @At("HEAD"), method = "onPlace", cancellable = true)
    public void beforeOnPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl, CallbackInfo ci) {
        if (level.getGameRules().getBoolean(TianjinMetro.PREVENT_BLOCK_FALLING)) ci.cancel();
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    public void beforeTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random, CallbackInfo ci) {
        if (serverLevel.getGameRules().getBoolean(TianjinMetro.PREVENT_BLOCK_FALLING)) ci.cancel();
    }
}
