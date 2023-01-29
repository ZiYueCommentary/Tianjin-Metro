package ziyue.tjmetro.mixin.mixins;

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
 * @since 1.0b
 */

@Mixin(FallingBlock.class)
public class FallingBlockMixin extends Block
{
    public FallingBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "onPlace", at = @At("HEAD"), cancellable = true)
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl, CallbackInfo ci) {
        if (level.getGameRules().getBoolean(TianjinMetro.PREVENT_BLOCK_FALLING)) ci.cancel();
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random, CallbackInfo ci) {
        if (serverLevel.getGameRules().getBoolean(TianjinMetro.PREVENT_BLOCK_FALLING)) ci.cancel();
    }
}
