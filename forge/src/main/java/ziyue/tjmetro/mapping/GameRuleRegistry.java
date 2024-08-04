package ziyue.tjmetro.mapping;

#if MC_VERSION <= "11605"
    import net.minecraft.world.GameRules;
#else
import net.minecraft.world.level.GameRules;
#endif
import net.minecraft.world.GameRules;
import ziyue.tjmetro.forge.property.PropertyCreateBooleanGameRule;

public class GameRuleRegistry
{
    public static BooleanGameRule registerBoolean(String name, boolean defaultValue) {
        return new BooleanGameRule(GameRules.register(name, GameRules.Category.MISC, ((PropertyCreateBooleanGameRule)new GameRules.BooleanValue(null, false)).createBoolean(defaultValue)));
    }
}
