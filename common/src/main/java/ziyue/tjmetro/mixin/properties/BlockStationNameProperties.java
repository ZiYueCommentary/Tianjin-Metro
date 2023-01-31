package ziyue.tjmetro.mixin.properties;

import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.mixin.mixins.BlockStationNameEntranceMixin;
import ziyue.tjmetro.mixin.mixins.RenderStationNameTiledMixin;

/**
 * Stores the method used for the <b>No "station" of Station Name Entrance</b> feature
 *
 * @author ZiYueCommentary
 * @see BlockStationNameEntranceMixin
 * @see RenderStationNameTiledMixin
 * @since beta-1
 */

public interface BlockStationNameProperties
{
    boolean getShowNameProperty(BlockState state);
}
