package ziyue.tjmetro.mod.render;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * Throttles render-driven {@code tick()} calls for doors that are fully closed.
 *
 * <p>Why this exists: {@code RenderPSDDoorTianjin(BMT)} and the APG door renderers call
 * {@code BlockEntityBase.tick(tickDelta)} on every frame, and {@code tick()} queries
 * redstone power 4 times per call ({@code REDSTONE_DETECT_DEPTH = 3}, loop i = 2..3,
 * two {@code isEmittingRedstonePower} calls per step). For a closed door nothing
 * changes between frames, so those block lookups are pure waste, and they scale with
 * the number of doors in view. Measured on the render thread of a station with 85
 * platform doors: {@code tick} alone was 11% of samples before throttling, 1.5-4%
 * after (skip budget = 2, verified with a client-side mixin over several sessions).
 *
 * <p>Rules:
 * <ul>
 *   <li>{@code doorValue > 0} (door opening/closing): never skip — the animation is
 *       driven by this tick and must advance every frame;</li>
 *   <li>{@code doorValue <= 0} (fully closed): run the tick once every {@link #THROTTLE}
 *       calls. A redstone change is picked up at most {@code THROTTLE} frames late,
 *       which is imperceptible.</li>
 * </ul>
 *
 * <p>The counter map is keyed by block entity instance. BlockEntityBase does not
 * override equals/hashCode, so entries are identity-based. Entries are never
 * explicitly removed (removed blocks simply stop being rendered); the map is cleared
 * wholesale once it grows past {@link #LIMIT} to bound the leak.
 */
public final class TickThrottle {

    /** Run the tick once every N calls while the door is fully closed. */
    private static final int THROTTLE = 3;
    private static final int LIMIT = 4096;

    private static final Object2IntOpenHashMap<Object> COUNTERS = new Object2IntOpenHashMap<>();

    /**
     * @param entity the block entity whose tick is being throttled
     * @return true if this call should be skipped (the caller must not run tick())
     */
    public static boolean skip(Object entity) {
        if (COUNTERS.size() > LIMIT) {
            COUNTERS.clear();
        }
        final int n = COUNTERS.getInt(entity);
        if (n + 1 < THROTTLE) {
            COUNTERS.put(entity, n + 1);
            return true;
        }
        COUNTERS.put(entity, 0);
        return false;
    }

    private TickThrottle() {
    }
}
