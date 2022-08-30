package ziyue.tjmetro.blocks.base;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RenderAttachmentBlockEntity {
	/**
	 * @return The model state data provided by this block entity. Can be null.
	 */
	@Nullable
	Object getRenderAttachmentData();
}
