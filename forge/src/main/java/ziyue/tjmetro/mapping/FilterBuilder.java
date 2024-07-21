package ziyue.tjmetro.mapping;

import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.registry.CreativeModeTabHolder;
import ziyue.filters.Filter;

import java.util.function.Supplier;

public interface FilterBuilder
{
    static Filter registerFilter(CreativeModeTabHolder creativeModeTab, MutableText filterName, Supplier<ItemStack> filterIcon) {
        return ziyue.filters.FilterBuilder.registerFilter(null, filterName.data, () -> filterIcon.get().data); // Nah, tab is not important here.
    }

    static Filter registerUncategorizedItemsFilter(CreativeModeTabHolder creativeModeTab) {
        return ziyue.filters.FilterBuilder.registerUncategorizedItemsFilter(null);
    }
}
