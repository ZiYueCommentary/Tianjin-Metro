package ziyue.tjmetro.mapping;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.mtr.mapping.holder.PlayerEntity;

import java.util.function.Function;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

public interface PlayerInventoryHelper
{
    static void clearItems(PlayerEntity player, Function<Item, Boolean> filter) {
#if MC_VERSION <= "11605"
        final DefaultedList<ItemStack> main = player.data.inventory.main;
        for (int i = 0; i < main.size(); i++) {
            if (filter.apply(main.get(i).getItem())) main.set(i, ItemStack.EMPTY);
        }

        final DefaultedList<ItemStack> armor = player.data.inventory.armor;
        for (int i = 0; i < armor.size(); i++) {
            if (filter.apply(armor.get(i).getItem())) armor.set(i, ItemStack.EMPTY);
        }

        final DefaultedList<ItemStack> offHand = player.data.inventory.offHand;
        for (int i = 0; i < offHand.size(); i++) {
            if (filter.apply(offHand.get(i).getItem())) offHand.set(i, ItemStack.EMPTY);
        }
#else
        final DefaultedList<ItemStack> main = player.data.getInventory().main;
        for (int i = 0; i < main.size(); i++) {
            if (filter.apply(main.get(i).getItem())) main.set(i, ItemStack.EMPTY);
        }

        final DefaultedList<ItemStack> armor = player.data.getInventory().armor;
        for (int i = 0; i < armor.size(); i++) {
            if (filter.apply(armor.get(i).getItem())) armor.set(i, ItemStack.EMPTY);
        }

        final DefaultedList<ItemStack> offHand = player.data.getInventory().offHand;
        for (int i = 0; i < offHand.size(); i++) {
            if (filter.apply(offHand.get(i).getItem())) offHand.set(i, ItemStack.EMPTY);
        }
#endif
    }
}
