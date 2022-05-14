package ziyue.tjmetro;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Stores the method used for the <i>No "station" of station name entrance</i> feature
 * @author ZiYueCommentary
 * @since 1.0b
 * @see ziyue.tjmetro.mixin.stationnameentrance.BlockMixin
 * @see ziyue.tjmetro.mixin.stationnameentrance.RenderMixin
 */

public interface ShowNameProperty {
    boolean getShowNameProperty(BlockState state);
}
