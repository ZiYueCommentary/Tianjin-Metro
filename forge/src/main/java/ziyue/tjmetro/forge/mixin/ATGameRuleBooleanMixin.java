package ziyue.tjmetro.forge.mixin;

// Dumb accessTransformer can't do anything.

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.forge.property.PropertyCreateBooleanGameRule;

#if MC_VERSION <= "11605"
import net.minecraft.world.GameRules;
@Mixin(GameRules.BooleanValue.class)
public abstract class ATGameRuleBooleanMixin extends GameRules.RuleValue<GameRules.BooleanValue> implements PropertyCreateBooleanGameRule
{
    @Shadow
    private static GameRules.RuleType<GameRules.BooleanValue> create(boolean p_223568_0_) {
        return null;
    }

    public ATGameRuleBooleanMixin(GameRules.RuleType<GameRules.BooleanValue> p_i51530_1_) {
        super(p_i51530_1_);
    }

    @Override
    public GameRules.RuleType<GameRules.BooleanValue> createBoolean(boolean value) {
        return create(value);
    }
}
#else
import net.minecraft.world.level.GameRules;
@Mixin(GameRules.BooleanValue.class)
public abstract class ATGameRuleBooleanMixin extends GameRules.Value<GameRules.BooleanValue> implements PropertyCreateBooleanGameRule
{
    @Shadow
    static GameRules.Type<GameRules.BooleanValue> create(boolean p_46251_) {
        return null;
    }

    public ATGameRuleBooleanMixin(GameRules.Type<GameRules.BooleanValue> p_46362_) {
        super(p_46362_);
    }

    @Override
    public GameRules.Type<GameRules.BooleanValue> createBoolean(boolean value) {
        return create(value);
    }
}
#endif