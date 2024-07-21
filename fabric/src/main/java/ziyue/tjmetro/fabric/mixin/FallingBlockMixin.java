//package ziyue.tjmetro.fabric.mixin;
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.FallingBlock;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.util.math.BlockPos;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import ziyue.tjmetro.mapping.BooleanGameRule;
//import ziyue.tjmetro.mod.TianjinMetro;
//
//@Mixin(FallingBlock.class)
//public abstract class FallingBlockMixin extends Block
//{
//    public FallingBlockMixin(Settings settings) {
//        super(settings);
//    }
//
//    @Inject(at = @At("HEAD"), method = "scheduledTick", cancellable = true)
//#if MC_VERSION < "11904"
//    private void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, java.util.Random random, CallbackInfo ci)
//#else
//    private void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random, CallbackInfo ci)
//#endif
//    {
//        if (BooleanGameRule.getValue(new org.mtr.mapping.holder.ServerWorld(world), TianjinMetro.NO_FALLING_BLOCK))
//            ci.cancel();
//    }
//}
