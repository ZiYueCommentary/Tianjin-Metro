package ziyue.tjmetro.mapping;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.mtr.mapping.annotation.MappedMethod;
import org.mtr.mapping.tool.HolderBase;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

public class DefaultedItemStackList extends HolderBase<DefaultedList<ItemStack>>
{
    public DefaultedItemStackList(DefaultedItemStackList data) {
        super(data.data);
    }

    public DefaultedItemStackList(DefaultedList<ItemStack> data) {
        super(data);
    }

    public static DefaultedItemStackList ofSize(int size) {
        return new DefaultedItemStackList(DefaultedList.ofSize(size, ItemStack.EMPTY));
    }

    @MappedMethod
    public int size() {
        return this.data.size();
    }

    @MappedMethod
    public void set(int i, ItemStack stack) {
        this.data.set(i, stack);
    }

    @MappedMethod
    public ItemStack get(int i) {
        return this.data.get(i);
    }
}
