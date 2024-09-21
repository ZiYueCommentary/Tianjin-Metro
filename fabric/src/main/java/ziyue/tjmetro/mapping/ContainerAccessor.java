package ziyue.tjmetro.mapping;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import ziyue.tjmetro.fabric.mixin.StorageMinecartEntityMixin;

/**
 * @author ZiYueCommentary
 * @see StorageMinecartEntityMixin
 * @since 1.0.0-beta-2
 */

public interface ContainerAccessor
{
    DefaultedList<ItemStack> tianjin_Metro$getInventory();
}
