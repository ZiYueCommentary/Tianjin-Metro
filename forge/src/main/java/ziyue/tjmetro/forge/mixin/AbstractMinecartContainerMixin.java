package ziyue.tjmetro.forge.mixin;

/**
 * @author ZiYueCommentary
 * @see ContainerAccessor
 * @since 1.0.0-beta-2
 */

#if MC_VERSION >= "11902"

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mapping.ContainerAccessor;

@Mixin(AbstractMinecartContainer.class)
public abstract class AbstractMinecartContainerMixin extends AbstractMinecart implements Container, MenuProvider, ContainerAccessor
{
    @Shadow
    private NonNullList<ItemStack> itemStacks;

    protected AbstractMinecartContainerMixin(EntityType<?> p_38213_, Level p_38214_) {
        super(p_38213_, p_38214_);
    }

    @Override
    public NonNullList<ItemStack> tianjin_Metro$getItemStacks() {
        return this.itemStacks;
    }
}

#elif MC_VERSION >= "11701"

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mapping.ContainerAccessor;

@Mixin(AbstractMinecartContainer.class)
public abstract class AbstractMinecartContainerMixin extends AbstractMinecart implements Container, MenuProvider, ContainerAccessor
{
    @Shadow
    private NonNullList<ItemStack> itemStacks;

    protected AbstractMinecartContainerMixin(EntityType<?> p_38213_, Level p_38214_) {
        super(p_38213_, p_38214_);
    }

    @Override
    public NonNullList<ItemStack> tianjin_Metro$getItemStacks() {
        return this.itemStacks;
    }
}

#else

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.item.minecart.ContainerMinecartEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import ziyue.tjmetro.mapping.ContainerAccessor;

@Mixin(ContainerMinecartEntity.class)
public abstract class AbstractMinecartContainerMixin extends AbstractMinecartEntity implements IInventory, INamedContainerProvider, ContainerAccessor
{
    @Shadow
    private NonNullList<ItemStack> itemStacks;

    protected AbstractMinecartContainerMixin(EntityType<?> p_i48538_1_, World p_i48538_2_) {
        super(p_i48538_1_, p_i48538_2_);
    }

    @Override
    public NonNullList<ItemStack> tianjin_Metro$getItemStacks() {
        return this.itemStacks;
    }
}

#endif