package ziyue.tjmetro.forge;

#if MC_VERSION > "11605"
import net.minecraft.world.level.block.entity.BlockEntity;
#else
import net.minecraft.tileentity.TileEntity;
#endif
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.mtr.mapping.mapper.BlockEntityExtension;
import ziyue.tjmetro.mod.DoorAnimationState;

/**
 * Hooks the fixed-rate door animation clock into Forge's client tick.
 * @author Codex
 * @since 1.1.2.261117
 */
public final class DoorAnimationTicker
{
    private DoorAnimationTicker() {
    }

    public static void init() {
        MinecraftForge.EVENT_BUS.register(DoorAnimationTicker.class);
    }

#if MC_VERSION > "11605"
    public static void register(BlockEntity blockEntity) {
#else
    public static void register(TileEntity blockEntity) {
#endif
        if (blockEntity instanceof BlockEntityExtension entity) {
            DoorAnimationState.register(entity);
        }
    }

#if MC_VERSION > "11605"
    public static void unregister(BlockEntity blockEntity) {
#else
    public static void unregister(TileEntity blockEntity) {
#endif
        if (blockEntity instanceof BlockEntityExtension entity) {
            DoorAnimationState.unregister(entity);
        }
    }

    @SubscribeEvent
    public static void afterClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) DoorAnimationState.tick();
    }
}
