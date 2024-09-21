package ziyue.tjmetro.mapping;

/**
 * An entity for GUI of Metal Detection Door. This entity is a minecart-with-chest.
 *
 * @author ZiYueCommentary
 * @see BlockMetalDetectionDoor
 * @since 1.0.0-beta-2
 */

#if MC_VERSION >= "11902"

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.World;
import ziyue.tjmetro.mod.block.BlockMetalDetectionDoor;

import javax.annotation.Nullable;

public class MetalDetectionDoorEntity extends MinecartChest
{
    public final BlockMetalDetectionDoor.BlockEntity blockEntity;

    public MetalDetectionDoorEntity(World world, BlockPos blockPos, BlockMetalDetectionDoor.BlockEntity blockEntity) {
        super(world.data, blockPos.getX(), -1, blockPos.getZ());
        this.blockEntity = blockEntity;
        for (int i = 0; i < blockEntity.inventory.size(); i++) {
            this.getItemStacks().set(i, blockEntity.inventory.get(i));
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new ChestMenu(MenuType.GENERIC_9x1, syncId, playerInventory, this, 1) {
            @Override
            public void removed(Player player) {
                super.removed(player);
                final MetalDetectionDoorEntity entity = (MetalDetectionDoorEntity) this.getContainer();
                entity.blockEntity.setData(new DefaultedItemStackList(entity.getItemStacks()));
                entity.getItemStacks().clear();
                entity.kill();
            }
        };
    }

    @Override
    public boolean isChestVehicleStillValid(Player p_219955_) {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return Component.translatable("gui.tjmetro.metal_detection_door");
    }
}

#elif MC_VERSION >= "11701"

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.World;
import ziyue.tjmetro.mod.block.BlockMetalDetectionDoor;

import javax.annotation.Nullable;

public class MetalDetectionDoorEntity extends MinecartChest
{
    public final BlockMetalDetectionDoor.BlockEntity blockEntity;

    public MetalDetectionDoorEntity(World world, BlockPos blockPos, BlockMetalDetectionDoor.BlockEntity blockEntity) {
        super(world.data, blockPos.getX(), -1, blockPos.getZ());
        this.blockEntity = blockEntity;
        for (int i = 0; i < blockEntity.inventory.size(); i++) {
            ((ContainerAccessor) this).tianjin_Metro$getItemStacks().set(i, blockEntity.inventory.get(i));
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new ChestMenu(MenuType.GENERIC_9x1, syncId, playerInventory, this, 1)
        {
            @Override
            public void removed(Player player) {
                super.removed(player);
                final MetalDetectionDoorEntity entity = (MetalDetectionDoorEntity) this.getContainer();
                entity.blockEntity.setData(new DefaultedItemStackList(((ContainerAccessor) entity).tianjin_Metro$getItemStacks()));
                ((ContainerAccessor) entity).tianjin_Metro$getItemStacks().clear();
                entity.kill();
            }
        };
    }

    @Override
    public boolean stillValid(Player p_38230_) {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return new TranslatableComponent("gui.tjmetro.metal_detection_door");
    }
}

#else

import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.World;
import ziyue.tjmetro.mod.block.BlockMetalDetectionDoor;

import javax.annotation.Nullable;

public class MetalDetectionDoorEntity extends ChestMinecartEntity
{
    public final BlockMetalDetectionDoor.BlockEntity blockEntity;

    public MetalDetectionDoorEntity(World world, BlockPos blockPos, BlockMetalDetectionDoor.BlockEntity blockEntity) {
        super(world.data, blockPos.getX(), -1, blockPos.getZ());
        this.blockEntity = blockEntity;
        for (int i = 0; i < blockEntity.inventory.size(); i++) {
            ((ContainerAccessor) this).tianjin_Metro$getItemStacks().set(i, blockEntity.inventory.get(i));
        }
    }

    @Override
    public Container createMenu(int syncId, PlayerInventory playerInventory) {
        return new ChestContainer(ContainerType.GENERIC_9x1, syncId, playerInventory, this, 1)
        {
            @Override
            public void removed(PlayerEntity player) {
                super.removed(player);
                final MetalDetectionDoorEntity entity = (MetalDetectionDoorEntity) this.getContainer();
                entity.blockEntity.setData(new DefaultedItemStackList(((ContainerAccessor) entity).tianjin_Metro$getItemStacks()));
                ((ContainerAccessor) entity).tianjin_Metro$getItemStacks().clear();
                entity.kill();
            }
        };
    }

    @Override
    public boolean stillValid(PlayerEntity p_38230_) {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return new TranslationTextComponent("gui.tjmetro.metal_detection_door");
    }
}

#endif