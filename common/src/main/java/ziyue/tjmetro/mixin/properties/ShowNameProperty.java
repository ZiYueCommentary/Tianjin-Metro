package ziyue.tjmetro.mixin.properties;

import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.mixin.mixins.entrance.EntranceMixin;
import ziyue.tjmetro.mixin.mixins.entrance.RenderMixin;

/**
 * Stores the method used for the <b>No "station" of Station Name Entrance</b> feature
 *
 * @author ZiYueCommentary
 * @see EntranceMixin
 * @see RenderMixin
 * @since 1.0b
 */

public interface ShowNameProperty
{
    boolean getShowNameProperty(BlockState state);
}
