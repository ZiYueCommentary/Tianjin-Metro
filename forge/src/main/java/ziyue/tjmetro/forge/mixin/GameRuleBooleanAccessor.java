package ziyue.tjmetro.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

// A cool way that I learned from Fabric API.

#if MC_VERSION <= "11605"
import net.minecraft.world.GameRules;
@Mixin(GameRules.BooleanValue.class)
public interface GameRuleBooleanAccessor
{
    @Invoker
    static GameRules.RuleType<GameRules.BooleanValue> invokeCreate(boolean p_46251_) {
        throw new AssertionError("This shouldn't happen!");
    }
}
#else
import net.minecraft.world.level.GameRules;
@Mixin(GameRules.BooleanValue.class)
public interface GameRuleBooleanAccessor
{
    @Invoker
    static GameRules.Type<GameRules.BooleanValue> invokeCreate(boolean p_46251_) {
        throw new AssertionError("This shouldn't happen!");
    }
}
#endif