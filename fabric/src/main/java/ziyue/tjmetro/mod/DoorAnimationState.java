package ziyue.tjmetro.mod;

import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import ziyue.tjmetro.mod.block.*;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Client-side fixed-rate animation state for Tianjin Metro platform doors.
 *
 * @author Codex
 * @since 1.2.0-alpha
 */
public final class DoorAnimationState
{
    /**
     * Two units give a two-second complete opening at 20 TPS.
     */
    private static final float ANIMATION_TICK_DELTA = 2;
    private static final Map<BlockPSDAPGDoorBase.BlockEntityBase, Double> PREVIOUS_VALUES = new IdentityHashMap<>();

    private DoorAnimationState() {
    }

    public static void register(BlockEntityExtension blockEntity) {
        if (blockEntity instanceof BlockPSDAPGDoorBase.BlockEntityBase door && isTianjinDoor(door)) {
            PREVIOUS_VALUES.putIfAbsent(door, door.getDoorValue());
        }
    }

    public static void unregister(BlockEntityExtension blockEntity) {
        if (blockEntity instanceof BlockPSDAPGDoorBase.BlockEntityBase door) {
            PREVIOUS_VALUES.remove(door);
        }
    }

    public static double getInterpolatedDoorValue(BlockPSDAPGDoorBase.BlockEntityBase door, float tickDelta) {
        final double current = door.getDoorValue();
        final double previous = PREVIOUS_VALUES.getOrDefault(door, current);
        final float progress = Math.max(0, Math.min(1, tickDelta));
        return previous + (current - previous) * progress;
    }

    public static void tick() {
        for (final Map.Entry<BlockPSDAPGDoorBase.BlockEntityBase, Double> entry : PREVIOUS_VALUES.entrySet()) {
            final BlockPSDAPGDoorBase.BlockEntityBase door = entry.getKey();
            entry.setValue(door.getDoorValue());
            door.tick(ANIMATION_TICK_DELTA);
        }
    }

    private static boolean isTianjinDoor(BlockPSDAPGDoorBase.BlockEntityBase door) {
        return door instanceof BlockPSDDoorTianjin.BlockEntity
                || door instanceof BlockPSDDoorTianjinBMT.BlockEntity
                || door instanceof BlockPSDDoorTianjinJinjing.BlockEntity
                || door instanceof BlockAPGDoorTianjin.BlockEntity
                || door instanceof BlockAPGDoorTianjinBMT.BlockEntity
                || door instanceof BlockAPGDoorTianjinTRT.BlockEntity
                || door instanceof BlockAPGDoorTianjinJinjing.BlockEntity
                || door instanceof BlockAPGDoorSingle.BlockEntity
                || door instanceof BlockPSDDoorSingle.BlockEntity;
    }
}
