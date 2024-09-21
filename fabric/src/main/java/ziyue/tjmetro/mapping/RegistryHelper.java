package ziyue.tjmetro.mapping;

import net.minecraft.item.ItemStack;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.registry.BlockRegistryObject;
import org.mtr.mapping.registry.ItemRegistryObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

public interface RegistryHelper
{
    static ItemRegistryObject RegistryObjectBlock2Item(BlockRegistryObject fabric, Identifier forge)
            throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        // This is a very mad way but I have no choice.
        Class<ItemRegistryObject> clazz = ItemRegistryObject.class;
        Constructor<ItemRegistryObject> constructor = clazz.getDeclaredConstructor(Item.class);
        constructor.setAccessible(true);
        return constructor.newInstance(fabric.get().asItem());
    }

    static ItemStack cloneSingleItemStack(ItemStack itemStack) {
        return new ItemStack(itemStack.getItem());
    }

    static Identifier getIdentifierByItem(net.minecraft.item.Item item) {
#if MC_VERSION >= "11902"
        return new Identifier(net.minecraft.registry.Registries.ITEM.getId(item));
#else
        return new Identifier(net.minecraft.util.registry.Registry.ITEM.getId(item));
#endif
    }

    static ItemStack getItemStackByIdentifier(Identifier identifier) {
#if MC_VERSION >= "11902"
        return net.minecraft.registry.Registries.ITEM.get(identifier.data).getDefaultStack();
#else
        return net.minecraft.util.registry.Registry.ITEM.get(identifier.data).getDefaultStack();
#endif
    }
}
