package ziyue.tjmetro.forge.property;

#if MC_VERSION <= "11605"
import net.minecraft.world.GameRules;

public interface PropertyCreateBooleanGameRule
{
    GameRules.RuleType<GameRules.BooleanValue> createBoolean(boolean value);
}
#else
import net.minecraft.world.level.GameRules;

public interface PropertyCreateBooleanGameRule
{
    GameRules.Type<GameRules.BooleanValue> createBoolean(boolean value);
}
#endif