package ziyue.tjmetro.mixin.properties;

import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.mixin.mixins.entrance.BlockMixin;
import ziyue.tjmetro.mixin.mixins.entrance.RenderMixin;

/**
 * Stores the method used for the <i>No "station" of station name entrance</i> feature
 * @author ZiYueCommentary
 * @since 1.0b
 * @see BlockMixin
 * @see RenderMixin
 */

public interface ShowNameProperty {
    boolean getShowNameProperty(BlockState state);
}
