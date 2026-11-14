package ziyue.tjmetro.forge;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.mtr.mapping.mapper.BlockEntityExtension;
import ziyue.tjmetro.mod.DoorAnimationState;

/**
 * Hooks the fixed-rate door animation clock into Forge's client tick.
 * @author Codex
 * @since 1.2.0-alpha
 */
public final class DoorAnimationTicker
{
    private DoorAnimationTicker() {
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(DoorAnimationTicker.class);
    }

    public static void register(BlockEntity blockEntity) {
        if (blockEntity instanceof BlockEntityExtension entity) {
            DoorAnimationState.register(entity);
        }
    }

    public static void unregister(BlockEntity blockEntity) {
        if (blockEntity instanceof BlockEntityExtension entity) {
            DoorAnimationState.unregister(entity);
        }
    }

    @SubscribeEvent
    public static void afterClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) DoorAnimationState.tick();
    }
}
