package ziyue.tjmetro.inventory;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Container menu with custom-max-stack-size slot.
 *
 * @author ZiYueCommentary
 * @see net.minecraft.world.inventory.ChestMenu
 * @since beta-1
 */

public class ContainerMenu extends AbstractContainerMenu
{
    protected final Container container;
    protected final int containerRows;

    public ContainerMenu(MenuType<?> menuType, int containerId, Inventory inventory, Container container, int size, int maxStackSize) {
        super(menuType, containerId);
        checkContainerSize(container, size * 9);
        this.container = container;
        this.containerRows = size;
        container.startOpen(inventory.player);
        int k = (this.containerRows - 4) * 18;

        for (int l = 0; l < this.containerRows; ++l) {
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(container, m + l * 9, 8 + m * 18, 18 + l * 18)
                {
                    @Override
                    public int getMaxStackSize() {
                        return maxStackSize;
                    }
                });
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int m = 0; m < 9; ++m) {
                this.addSlot(new Slot(inventory, m + l * 9 + 9, 8 + m * 18, 103 + l * 18 + k));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 161 + k));
        }

    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (i < this.containerRows * 9) {
                if (!this.moveItemStackTo(itemStack2, this.containerRows * 9, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack2, 0, this.containerRows * 9, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    public Container getContainer() {
        return this.container;
    }

    @Environment(EnvType.CLIENT)
    public int getRowCount() {
        return this.containerRows;
    }
}
