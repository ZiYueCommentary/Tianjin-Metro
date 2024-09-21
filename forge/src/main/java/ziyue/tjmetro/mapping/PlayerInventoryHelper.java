package ziyue.tjmetro.mapping;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

#if MC_VERSION >= "11701"

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mtr.mapping.holder.PlayerEntity;

import java.util.function.Function;

public class PlayerInventoryHelper
{
    public static void clearItems(PlayerEntity player, Function<Item, Boolean> filter) {
        final NonNullList<ItemStack> items = player.data.getInventory().items;
        for (int i = 0; i < items.size(); i++) {
            if (filter.apply(items.get(i).getItem())) items.set(i, ItemStack.EMPTY);
        }

        final NonNullList<ItemStack> armor = player.data.getInventory().armor;
        for (int i = 0; i < armor.size(); i++) {
            if (filter.apply(armor.get(i).getItem())) armor.set(i, ItemStack.EMPTY);
        }

        final NonNullList<ItemStack> offhand = player.data.getInventory().offhand;
        for (int i = 0; i < offhand.size(); i++) {
            if (filter.apply(offhand.get(i).getItem())) offhand.set(i, ItemStack.EMPTY);
        }
    }
}

#else

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.mtr.mapping.holder.PlayerEntity;

import java.util.function.Function;

public class PlayerInventoryHelper
{
    public static void clearItems(PlayerEntity player, Function<Item, Boolean> filter) {
        final NonNullList<ItemStack> items = player.data.inventory.items;
        for (int i = 0; i < items.size(); i++) {
            if (filter.apply(items.get(i).getItem())) items.set(i, ItemStack.EMPTY);
        }

        final NonNullList<ItemStack> armor = player.data.inventory.armor;
        for (int i = 0; i < armor.size(); i++) {
            if (filter.apply(armor.get(i).getItem())) armor.set(i, ItemStack.EMPTY);
        }

        final NonNullList<ItemStack> offhand = player.data.inventory.offhand;
        for (int i = 0; i < offhand.size(); i++) {
            if (filter.apply(offhand.get(i).getItem())) offhand.set(i, ItemStack.EMPTY);
        }
    }
}

#endif
