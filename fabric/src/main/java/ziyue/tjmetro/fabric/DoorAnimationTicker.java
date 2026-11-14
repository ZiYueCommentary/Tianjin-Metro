package ziyue.tjmetro.fabric;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.chunk.WorldChunk;
import ziyue.tjmetro.mod.DoorAnimationState;

/**
 * Client-side animation clock for this mod's platform screen doors.
 * Registers the common animation state with Fabric's client lifecycle.
 *
 * @author Codex
 * @since 1.2.0-alpha
 */
public final class DoorAnimationTicker
{
    private DoorAnimationTicker() {
    }

    public static void init() {
        ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> registerChunk(chunk));
        ClientChunkEvents.CHUNK_UNLOAD.register((world, chunk) -> unregisterChunk(chunk));
        ClientTickEvents.END_WORLD_TICK.register(world -> DoorAnimationState.tick());
    }

    public static void register(BlockEntity blockEntity) {
        if (blockEntity instanceof org.mtr.mapping.mapper.BlockEntityExtension entity) {
            DoorAnimationState.register(entity);
        }
    }

    public static void unregister(BlockEntity blockEntity) {
        if (blockEntity instanceof org.mtr.mapping.mapper.BlockEntityExtension entity) {
            DoorAnimationState.unregister(entity);
        }
    }

    private static void registerChunk(WorldChunk chunk) {
        chunk.getBlockEntities().values().forEach(DoorAnimationTicker::register);
    }

    private static void unregisterChunk(WorldChunk chunk) {
        chunk.getBlockEntities().values().forEach(DoorAnimationTicker::unregister);
    }

}
