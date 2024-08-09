package ziyue.tjmetro.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.mapping.BooleanGameRule;
import ziyue.tjmetro.mod.TianjinMetro;

#if MC_VERSION <= "11605"
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import java.util.Random;

@Mixin(FallingBlock.class)
public abstract class FallingBlockMixin extends Block
{
    public FallingBlockMixin(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
    private void beforeTick(BlockState p_225534_1_, ServerWorld world, BlockPos p_225534_3_, Random p_225534_4_, CallbackInfo ci) {
        if (BooleanGameRule.getValue(new org.mtr.mapping.holder.ServerWorld(world), TianjinMetro.NO_FALLING_BLOCK)) {
            ci.cancel();
        }
    }
}
#else
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

@Mixin(FallingBlock.class)
public abstract class FallingBlockMixin extends Block implements Fallable
{
    public FallingBlockMixin(Properties p_49795_) {
        super(p_49795_);
    }

    @Inject(at = @At("HEAD"), method = "tick", cancellable = true)
#if MC_VERSION >= "11902"
    private void beforeTick(BlockState p_221124_, ServerLevel world, BlockPos p_221126_, net.minecraft.util.RandomSource p_221127_, CallbackInfo ci)
#else
    private void beforeTick(BlockState p_53216_, ServerLevel world, BlockPos p_53218_, Random p_53219_, CallbackInfo ci)
#endif {
        if (BooleanGameRule.getValue(new org.mtr.mapping.holder.ServerWorld(world), TianjinMetro.NO_FALLING_BLOCK)) {
            ci.cancel();
        }
    }
}
#endif
