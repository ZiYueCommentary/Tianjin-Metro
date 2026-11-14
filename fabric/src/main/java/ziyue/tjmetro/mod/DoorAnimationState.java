package ziyue.tjmetro.mod;

import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import org.mtr.mod.block.IBlock;
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
    private static final Map<BlockPSDAPGDoorBase.BlockEntityBase, AnimationValue> VALUES = new IdentityHashMap<>();

    private DoorAnimationState() {
    }

    public static void register(BlockEntityExtension blockEntity) {
        if (blockEntity instanceof BlockPSDAPGDoorBase.BlockEntityBase door && isTianjinDoor(door)) {
            VALUES.computeIfAbsent(door, ignored -> new AnimationValue(door.getDoorValue()));
        }
    }

    public static void unregister(BlockEntityExtension blockEntity) {
        if (blockEntity instanceof BlockPSDAPGDoorBase.BlockEntityBase door) {
            VALUES.remove(door);
        }
    }

    public static double getInterpolatedDoorValue(BlockPSDAPGDoorBase.BlockEntityBase door, float tickDelta) {
        final AnimationValue value = VALUES.get(door);
        if (value == null) return door.getDoorValue();
        final float progress = Math.max(0, Math.min(1, tickDelta));
        return value.previous + (value.current - value.previous) * progress;
    }

    public static void tick() {
        for (final AnimationValue value : VALUES.values()) {
            value.previous = value.current;
        }
        for (final BlockPSDAPGDoorBase.BlockEntityBase door : VALUES.keySet()) {
            if (isAnimationController(door)) door.tick(ANIMATION_TICK_DELTA);
        }
        for (final Map.Entry<BlockPSDAPGDoorBase.BlockEntityBase, AnimationValue> entry : VALUES.entrySet()) {
            entry.getValue().current = entry.getKey().getDoorValue();
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

    private static boolean isAnimationController(BlockPSDAPGDoorBase.BlockEntityBase door) {
        if (IBlock.getStatePropertySafe(door.getCachedState2(), IBlock.HALF) == IBlock.DoubleBlockHalf.UPPER) return false;
        if (door instanceof BlockAPGDoorSingle.BlockEntity || door instanceof BlockPSDDoorSingle.BlockEntity) return true;
        return IBlock.getStatePropertySafe(door.getCachedState2(), IBlock.SIDE) != IBlock.EnumSide.RIGHT;
    }

    private static final class AnimationValue
    {
        private double previous;
        private double current;

        private AnimationValue(double value) {
            previous = value;
            current = value;
        }
    }
}
