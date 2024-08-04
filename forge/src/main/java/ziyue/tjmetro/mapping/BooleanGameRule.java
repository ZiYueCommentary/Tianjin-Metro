package ziyue.tjmetro.mapping;

import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.HolderBase;

#if MC_VERSION <= "11605"
import net.minecraft.world.GameRules;

public class BooleanGameRule extends HolderBase<GameRules.RuleKey<GameRules.BooleanValue>>
{
    public BooleanGameRule(GameRules.RuleKey<GameRules.BooleanValue> data) {
        super(data);
    }

    public static boolean getValue(ServerWorld world, BooleanGameRule rule) {
        return world.data.getGameRules().getBoolean(rule.data);
    }
}
#else
import net.minecraft.world.level.GameRules;

public class BooleanGameRule extends HolderBase<GameRules.Key<GameRules.BooleanValue>>
{
    public BooleanGameRule(GameRules.Key<GameRules.BooleanValue> data) {
        super(data);
    }

    public static boolean getValue(ServerWorld world, BooleanGameRule rule) {
        return world.data.getGameRules().getBoolean(rule.data);
    }
}
#endif
