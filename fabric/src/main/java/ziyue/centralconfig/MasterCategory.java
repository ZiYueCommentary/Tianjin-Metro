package ziyue.centralconfig;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import org.mtr.mapping.holder.MutableText;
import ziyue.centralconfig.CentralConfig;
import ziyue.centralconfig.ModuleCategory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * The master category that belongs to the parent mod which owns {@link CentralConfig}. This one is always the first category.
 *
 * @author ZiYueCommentary
 * @author EasyT_T
 * @see ModuleCategory
 * @since 1.0
 */

public class MasterCategory extends ModuleCategory
{
    protected final Supplier<ConfigBuilder> configBuilderSupplier;

    /**
     * @see ModuleCategory#ModuleCategory(String, Supplier, BiConsumer)
     * @param configBuilderSupplier configurations for the config screen itself
     */
    public MasterCategory(String modId, Supplier<MutableText> titleSupplier, BiConsumer<ConfigEntryBuilder, ConfigCategory> categorySupplier, Supplier<ConfigBuilder> configBuilderSupplier) {
        super(modId, titleSupplier, categorySupplier);
        this.configBuilderSupplier = configBuilderSupplier;
    }
}
