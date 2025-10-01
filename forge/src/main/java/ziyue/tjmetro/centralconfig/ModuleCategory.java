package ziyue.tjmetro.centralconfig;

import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import org.mtr.mapping.holder.MutableText;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * A category that belongs to a child mod. Every category is bound with mod ID as a unique identifier.
 *
 * @author ZiYueCommentary
 * @author EasyT_T
 * @see <a href="https://github.com/ZiYueCommentary/central-config">central-config</a>
 * @since 1.0
 */

public class ModuleCategory
{
    /**
     * The unique key of the category.
     */
    public final String modId;
    protected final Supplier<MutableText> titleSupplier;
    protected final BiConsumer<ConfigEntryBuilder, ConfigCategory> categoryBiConsumer;

    /**
     * Constructing a module category for child mods.
     *
     * @param modId              the mod ID of the child mod
     * @param titleSupplier      category name
     * @param categoryBiConsumer {@code ConfigEntryBuilder} is for adding configs, and {@code ConfigCategory} is the category instance that created with the category name that provided by {@code titleSupplier}.
     */
    public ModuleCategory(String modId, Supplier<MutableText> titleSupplier, BiConsumer<ConfigEntryBuilder, ConfigCategory> categoryBiConsumer) {
        this.modId = modId;
        this.titleSupplier = titleSupplier;
        this.categoryBiConsumer = categoryBiConsumer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ModuleCategory)) return false;
        return this.modId.equals(((ModuleCategory) obj).modId);
    }

    @Override
    public int hashCode() {
        return modId.hashCode();
    }
}
