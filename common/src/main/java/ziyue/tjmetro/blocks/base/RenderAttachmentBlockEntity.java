package ziyue.tjmetro.blocks.base;

import org.jetbrains.annotations.Nullable;

/**
 * A fabric interface.
 *
 * @author FabricMC
 * @since 1.0b
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
