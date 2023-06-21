package ziyue.tjmetro.mixin.properties;

/**
 * Properties of "Experimental MTR config screen".
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mixin.mixins.ConfigScreenMixin
 * @see ziyue.tjmetro.mixin.mixins.MinecraftMixin
 * @see ziyue.tjmetro.Config
 * @since beta-1
 */

public interface ConfigScreenProperties
{
    boolean hasTimeAndWindControls();

    boolean useTimeAndWindSync();
}
