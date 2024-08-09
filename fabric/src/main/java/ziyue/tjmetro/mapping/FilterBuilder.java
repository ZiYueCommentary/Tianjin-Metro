package ziyue.tjmetro.mapping;

import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.PressAction;
import org.mtr.mapping.registry.CreativeModeTabHolder;
import ziyue.filters.Filter;

import java.util.function.Supplier;

/**
 * @since 1.0.0-beta-1
 */

public interface FilterBuilder
{
    static Filter registerFilter(CreativeModeTabHolder creativeModeTab, MutableText filterName, Supplier<ItemStack> filterIcon) {
        return ziyue.filters.FilterBuilder.registerFilter(creativeModeTab.creativeModeTab, filterName.data, () -> filterIcon.get().data);
    }

    static Filter registerUncategorizedItemsFilter(CreativeModeTabHolder creativeModeTab) {
        return ziyue.filters.FilterBuilder.registerUncategorizedItemsFilter(creativeModeTab.creativeModeTab);
    }

    static void filtersVisibility(CreativeModeTabHolder creativeModeTab, boolean visible) {
        ziyue.filters.FilterBuilder.filtersVisibility(creativeModeTab.creativeModeTab, visible);
    }

    static void setReservedButton(CreativeModeTabHolder creativeModeTab, MutableText tooltip, PressAction onPress) {
        ziyue.filters.FilterBuilder.setReservedButton(creativeModeTab.creativeModeTab, tooltip.data, onPress);
    }
}
