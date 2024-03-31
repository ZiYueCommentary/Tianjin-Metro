package ziyue.tjmetro.block.base;

import org.jetbrains.annotations.Nullable;

/**
 * A fabric interface.
 *
 * @author FabricMC
 * @since beta-1
 */

@FunctionalInterface
public interface RenderAttachmentBlockEntity
{
    /**
     * @return The model state data provided by this block entity. Can be null.
     */
    @Nullable
    Object getRenderAttachmentData();
}
