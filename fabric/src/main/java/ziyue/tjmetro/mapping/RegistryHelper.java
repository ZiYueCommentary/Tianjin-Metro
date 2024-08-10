package ziyue.tjmetro.mapping;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.registry.BlockRegistryObject;
import org.mtr.mapping.registry.ItemRegistryObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Converting block to item. This may not be stable since it uses reflection.
 *
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

// This is a very mad way but I have no choice.
public interface RegistryHelper
{
    static ItemRegistryObject RegistryObjectBlock2Item(BlockRegistryObject fabric, Identifier forge)
            throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Class<ItemRegistryObject> clazz = ItemRegistryObject.class;
        Constructor<ItemRegistryObject> constructor = clazz.getDeclaredConstructor(Item.class);
        constructor.setAccessible(true);
        return constructor.newInstance(fabric.get().asItem());
    }
}
