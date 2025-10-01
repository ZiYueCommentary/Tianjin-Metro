package ziyue.tjmetro.centralconfig;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import org.mtr.mapping.holder.Screen;

import java.util.HashSet;
import java.util.Set;

/**
 * Central config management object which owns by the parent mod. Child mods for it should construct {@link ModuleCategory} and register with {@link #registerModuleCategory(ModuleCategory)}.
 *
 * @author ZiYueCommentary
 * @author EasyT_T
 * @see <a href="https://github.com/ZiYueCommentary/central-config">central-config</a>
 * @since 1.0
 */

public final class CentralConfig
{
    private final MasterCategory masterCategory;
    private final Set<ModuleCategory> moduleCategories = new HashSet<>();

    /**
     * Constructing a central config management object.
     *
     * @param masterCategory the config category that owns by the parent mod.
     */
    public CentralConfig(MasterCategory masterCategory) {
        this.masterCategory = masterCategory;
    }

    /**
     * Registering a module category on the config screen.
     *
     * @param moduleCategory category that adds to the config screen
     * @throws IllegalArgumentException two module categories are bound with the same mod ID
     * @see ModuleCategory
     * @since 1.0
     */
    public void registerModuleCategory(ModuleCategory moduleCategory) {
        if (!this.moduleCategories.add(moduleCategory)) {
            throw new IllegalArgumentException("Duplicate mod id: " + moduleCategory.modId);
        }
    }

    /**
     * @see #getConfigScreen(Screen, String)
     */
    public Screen getConfigScreen() {
        return getConfigScreen(new Screen(null), (String) null);
    }

    /**
     * @see #getConfigScreen(Screen, String)
     */
    public Screen getConfigScreen(ModuleCategory category) {
        return getConfigScreen(new Screen(null), category.modId);
    }

    /**
     * @see #getConfigScreen(Screen, String)
     */
    public Screen getConfigScreen(String categoryModId) {
        return getConfigScreen(new Screen(null), categoryModId);
    }

    /**
     * @see #getConfigScreen(Screen, String)
     */
    public Screen getConfigScreen(Screen parent) {
        return getConfigScreen(parent, (String) null);
    }

    /**
     * @see #getConfigScreen(Screen, String)
     */
    public Screen getConfigScreen(Screen parent, ModuleCategory category) {
        return getConfigScreen(parent, category.modId);
    }

    /**
     * Get the config screen that contains all categories. The master category is always the first, others are ordered by string.
     * If {@code categoryModId} is valid, the corresponding category will be selected.
     *
     * @param parent        the screen that opens the config screen
     * @param categoryModId mod ID which binds the module category
     * @return the config screen
     * @since 1.0
     */
    public Screen getConfigScreen(Screen parent, String categoryModId) {
        ConfigBuilder builder = masterCategory.configBuilderSupplier.get().setParentScreen(parent.data);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory defaultCategory = builder.getOrCreateCategory(masterCategory.titleSupplier.get().data);
        masterCategory.categoryBiConsumer.accept(entryBuilder, defaultCategory);
        for (ModuleCategory moduleCategory : moduleCategories) {
            ConfigCategory category = builder.getOrCreateCategory(moduleCategory.titleSupplier.get().data);
            moduleCategory.categoryBiConsumer.accept(entryBuilder, category);
            if (moduleCategory.modId.equals(categoryModId)) builder.setFallbackCategory(category);
        }

        return new Screen(builder.build());
    }
}
