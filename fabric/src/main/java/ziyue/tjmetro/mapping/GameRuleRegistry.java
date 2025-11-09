package ziyue.tjmetro.mapping;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.minecraft.world.GameRules;

/**
 * @since 1.0.0-beta-2
 */

public class GameRuleRegistry
{
    /**
     * Creates a boolean gamerule.
     *
     * @author ZiYueCommentary
     * @since 1.0.0-beta-2
     */
    public static BooleanGameRule registerBoolean(String name, boolean defaultValue) {
        return new BooleanGameRule(net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry.register(name, GameRules.Category.MISC, GameRuleFactory.createBooleanRule(defaultValue)));
    }

    /**
     * Creates an integer gamerule.
     *
     * @author ZiYueCommentary
     * @since 1.1.0
     */
    public static IntegerGameRule registerInteger(String name, int defaultValue) {
        return new IntegerGameRule(net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry.register(name, GameRules.Category.MISC, GameRuleFactory.createIntRule(defaultValue)));
    }
}
