package ziyue.tjmetro.mapping;

import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.HolderBase;

/**
 * @author ZiYueCommentary
 * @since 1.1.0
 */

#if MC_VERSION <= "11605"
import net.minecraft.world.GameRules;

public class IntegerGameRule extends HolderBase<GameRules.RuleKey<GameRules.IntegerValue>>
{
    public IntegerGameRule(GameRules.RuleKey<GameRules.IntegerValue> data) {
        super(data);
    }

    public static int getValue(ServerWorld world, IntegerGameRule rule) {
        return world.data.getGameRules().getInt(rule.data);
    }
}
#else
import net.minecraft.world.level.GameRules;

public class IntegerGameRule extends HolderBase<GameRules.Key<GameRules.IntegerValue>>
{
    public IntegerGameRule(GameRules.Key<GameRules.IntegerValue> data) {
        super(data);
    }

    public static int getValue(ServerWorld world, IntegerGameRule rule) {
        return world.data.getGameRules().getInt(rule.data);
    }
}
#endif
