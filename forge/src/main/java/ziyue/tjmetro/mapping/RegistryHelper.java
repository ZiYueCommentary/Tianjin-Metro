package ziyue.tjmetro.mapping;

import org.mtr.mapping.holder.Identifier;
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
        Constructor<ItemRegistryObject> constructor = clazz.getDeclaredConstructor(Identifier.class);
        constructor.setAccessible(true);
        return constructor.newInstance(forge);
    }

#if MC_VERSION >= "11701"
    
    static net.minecraft.world.item.ItemStack cloneSingleItemStack(net.minecraft.world.item.ItemStack itemStack) {
        return new net.minecraft.world.item.ItemStack(itemStack.getItem());
    }

    static Identifier getIdentifierByItem(net.minecraft.world.item.Item item) {
#if MC_VERSION >= "11904"
        return new Identifier(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item));
#else
        return new Identifier(net.minecraft.core.Registry.ITEM.getKey(item));
#endif
    }

    static net.minecraft.world.item.ItemStack getItemStackByIdentifier(Identifier identifier) {
#if MC_VERSION >= "11904"
        return net.minecraft.core.registries.BuiltInRegistries.ITEM.get(identifier.data).getDefaultInstance();
#else
        return net.minecraft.core.Registry.ITEM.get(identifier.data).getDefaultInstance();
#endif
    }
    
#else

    static net.minecraft.item.ItemStack cloneSingleItemStack(net.minecraft.item.ItemStack itemStack) {
        return new net.minecraft.item.ItemStack(itemStack.getItem());
    }

    static Identifier getIdentifierByItem(net.minecraft.item.Item item) {
        return new Identifier(net.minecraft.util.registry.Registry.ITEM.getKey(item));
    }

    static net.minecraft.item.ItemStack getItemStackByIdentifier(Identifier identifier) {
        return net.minecraft.util.registry.Registry.ITEM.get(identifier.data).getDefaultInstance();
    }
    
#endif
}
