package ziyue.tjmetro.mapping;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.forge.mixin.AbstractMinecartContainerMixin
 * @since 1.0.0-beta-2
 */

#if MC_VERSION >= "11902"

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface ContainerAccessor
{
    NonNullList<ItemStack> tianjin_Metro$getItemStacks();
}

#elif MC_VERSION >= "11701"

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public interface ContainerAccessor
{
    NonNullList<ItemStack> tianjin_Metro$getItemStacks();
}

#else

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface ContainerAccessor
{
    NonNullList<ItemStack> tianjin_Metro$getItemStacks();
}

#endif