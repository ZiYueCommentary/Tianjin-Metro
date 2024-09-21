package ziyue.tjmetro.fabric.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.StorageMinecartEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mapping.ContainerAccessor;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mapping.ContainerAccessor
 * @since 1.0.0-beta-2
 */

@Mixin(StorageMinecartEntity.class)
public abstract class StorageMinecartEntityMixin extends AbstractMinecartEntity implements Inventory, NamedScreenHandlerFactory, ContainerAccessor
{
    @Shadow private DefaultedList<ItemStack> inventory;

    protected StorageMinecartEntityMixin(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public DefaultedList<ItemStack> tianjin_Metro$getInventory() {
        return this.inventory;
    }
}
