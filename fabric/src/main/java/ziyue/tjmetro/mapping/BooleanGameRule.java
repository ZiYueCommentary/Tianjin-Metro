package ziyue.tjmetro.mapping;

import net.minecraft.world.GameRules;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.HolderBase;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

public class BooleanGameRule extends HolderBase<GameRules.Key<GameRules.BooleanRule>>
{
    public BooleanGameRule(GameRules.Key<GameRules.BooleanRule> data) {
        super(data);
    }

    public static boolean getValue(ServerWorld world, BooleanGameRule rule) {
        return world.data.getGameRules().getBoolean(rule.data);
    }
}
