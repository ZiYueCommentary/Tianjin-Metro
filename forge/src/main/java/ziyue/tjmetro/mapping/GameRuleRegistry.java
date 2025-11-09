package ziyue.tjmetro.mapping;

#if MC_VERSION <= "11605"
import net.minecraft.world.GameRules;
#else
import net.minecraft.world.level.GameRules;
#endif
import ziyue.tjmetro.forge.mixin.GameRuleBooleanAccessor;
import ziyue.tjmetro.forge.mixin.GameRuleIntegerAccessor;

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
        return new BooleanGameRule(GameRules.register(name, GameRules.Category.MISC, GameRuleBooleanAccessor.invokeCreate(defaultValue)));
    }

    /**
     * Creates an integer gamerule.
     *
     * @author ZiYueCommentary
     * @since 1.1.0
     */
    public static IntegerGameRule registerInteger(String name, int defaultValue) {
        return new IntegerGameRule(GameRules.register(name, GameRules.Category.MISC, GameRuleIntegerAccessor.invokeCreate(defaultValue)));
    }
}
