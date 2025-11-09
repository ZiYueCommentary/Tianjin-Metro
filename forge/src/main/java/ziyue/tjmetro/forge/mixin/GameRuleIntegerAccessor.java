package ziyue.tjmetro.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

// A cool way that I learned from Fabric API.

#if MC_VERSION <= "11605"
import net.minecraft.world.GameRules;
@Mixin(GameRules.IntegerValue.class)
public interface GameRuleIntegerAccessor
{
    @Invoker
    static GameRules.RuleType<GameRules.IntegerValue> invokeCreate(int p_46251_) {
        throw new AssertionError("This shouldn't happen!");
    }
}
#else
        import net.minecraft.world.level.GameRules;
@Mixin(GameRules.IntegerValue.class)
public interface GameRuleIntegerAccessor
{
    @Invoker
    static GameRules.Type<GameRules.IntegerValue> invokeCreate(int p_46251_) {
        throw new AssertionError("This shouldn't happen!");
    }
}
#endif