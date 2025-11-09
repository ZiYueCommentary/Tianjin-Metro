package ziyue.tjmetro.mapping;

import net.minecraft.world.GameRules;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.HolderBase;

/**
 * @author ZiYueCommentary
 * @since 1.1.0
 */

public class IntegerGameRule extends HolderBase<GameRules.Key<GameRules.IntRule>>
{
    public IntegerGameRule(GameRules.Key<GameRules.IntRule> data) {
        super(data);
    }

    public static int getValue(ServerWorld world, IntegerGameRule rule) {
        return world.data.getGameRules().getInt(rule.data);
    }
}
