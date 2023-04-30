package ziyue.tjmetro;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;

/**
 * Register for some methods that have different implement ways.
 *
 * @see mtr.RegistryClient
 * @since beta-1
 */

public class Registry
{
    /**
     * Register block that can custom block color.
     *
     * @author ZiYueCommentary
     * @since beta-1
     */
    @ExpectPlatform
    public static void registerCustomColorBlock(Block block) {
        throw new AssertionError();
    }

    /**
     * Register a boolean value game rule.
     *
     * @param name         name of game rule
     * @param category     category of game rule in <b>Create New World</b>'s <b>Edit Game Rules</b>
     * @param defaultValue default value of game rule
     * @return a boolean value game rule
     * @author ZiYueCommentary
     * @since beta-1
     */
    @ExpectPlatform
    public static GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue) {
        throw new AssertionError();
    }

    /**
     * Register a integer value game rule.
     *
     * @param name         name of game rule
     * @param category     category of game rule in <b>Create New World</b>'s <b>Edit Game Rules</b>
     * @param defaultValue default value of game rule
     * @return a integer value game rule
     * @author ZiYueCommentary
     * @since beta-1
     */
    @ExpectPlatform
    public static GameRules.Key<GameRules.IntegerValue> registerIntegerGameRule(String name, GameRules.Category category, int defaultValue) {
        throw new AssertionError();
    }

    /**
     * @author ZiYueCommentary
     * @see #registerBooleanGameRule(String, GameRules.Category, boolean)
     * @since beta-1
     */
    public static GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, boolean defaultValue) {
        return registerBooleanGameRule(name, GameRules.Category.MISC, defaultValue);
    }

    /**
     * @author ZiYueCommentary
     * @see #registerIntegerGameRule(String, GameRules.Category, int)
     * @since beta-1
     */
    public static GameRules.Key<GameRules.IntegerValue> registerIntegerGameRule(String name, int defaultValue) {
        return registerIntegerGameRule(name, GameRules.Category.MISC, defaultValue);
    }
}
