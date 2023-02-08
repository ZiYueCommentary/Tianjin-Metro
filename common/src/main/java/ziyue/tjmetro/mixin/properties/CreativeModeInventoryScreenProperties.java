package ziyue.tjmetro.mixin.properties;

/**
 * Methods for filters rendering.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mixin.mixins.CreativeModeInventoryScreenMixin
 * @see ziyue.tjmetro.mixin.mixins.EffectRenderingInventoryScreenMixin
 * @see ziyue.tjmetro.filters.Filter
 * @since beta-1
 */

public interface CreativeModeInventoryScreenProperties
{
    int getSelectedTab();

    boolean hasFilters(int tabId);
}
