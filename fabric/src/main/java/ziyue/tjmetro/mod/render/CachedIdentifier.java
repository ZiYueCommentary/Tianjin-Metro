package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.Identifier;
import ziyue.tjmetro.mod.Reference;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Door renderers construct a handful of Identifiers on every frame for every door.
 * The path string changes only between a few fixed combinations, but each
 * {@code new Identifier} still runs Minecraft's path validation and splitting
 * (class_2960.method_20208 measured at 8.3% of render-thread samples in a
 * 85-platform-door station). Identifiers are immutable, so sharing instances
 * per path is safe.
 */
public final class CachedIdentifier {

    private static final Map<String, Identifier> CACHE = new ConcurrentHashMap<>();

    public static Identifier of(String path) {
        return CACHE.computeIfAbsent(path, CachedIdentifier::create);
    }

    private static Identifier create(String path) {
        return new Identifier(Reference.MOD_ID, path);
    }

    private CachedIdentifier() {
    }
}
