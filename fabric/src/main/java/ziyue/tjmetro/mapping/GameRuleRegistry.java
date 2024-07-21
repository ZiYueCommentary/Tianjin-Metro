package ziyue.tjmetro.mapping;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.minecraft.world.GameRules;

public class GameRuleRegistry
{
    public static BooleanGameRule registerBoolean(String name, boolean defaultValue) {
        return new BooleanGameRule(net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry.register(name, GameRules.Category.MISC, GameRuleFactory.createBooleanRule(defaultValue)));
    }
}
