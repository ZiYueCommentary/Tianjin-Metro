package ziyue.tjmetro.mixin;

import net.minecraft.commands.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import net.minecraft.world.SnooperPopulator;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.TianjinMetro;
import ziyue.tjmetro.block.BlockBench;

/**
 * Killing all bench seats before stopping the server.
 * Stopping server without kill seats will make seats unable to be killed.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.block.BlockBench
 * @see ziyue.tjmetro.block.BlockBench.TileEntityBench
 * @since beta-1
 */

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin extends ReentrantBlockableEventLoop<TickTask> implements SnooperPopulator, CommandSource, AutoCloseable
{
    public MinecraftServerMixin(String string) {
        super(string);
    }

    @Inject(at = @At("HEAD"), method = "stopServer")
    private void beforeStopServer(CallbackInfo ci) {
        TianjinMetro.LOGGER.info("Killing {} seat entities", BlockBench.SeatSet.size());
        BlockBench.SeatSet.forEach(Entity::kill);
    }
}
