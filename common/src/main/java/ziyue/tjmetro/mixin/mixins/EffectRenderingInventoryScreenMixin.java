package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.filters.Filter;
import ziyue.tjmetro.mixin.properties.CreativeModeInventoryScreenProperties;

/**
 * Change effect labels' position in "creative mode tab with filters".
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.filters.Filter
 * @see CreativeModeInventoryScreenMixin
 * @see CreativeModeInventoryScreenProperties
 * @since beta-1
 */

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T>
{
    public EffectRenderingInventoryScreenMixin(T abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(at = @At("HEAD"), method = "renderEffects")
    private void beforeRenderEffects(PoseStack poseStack, CallbackInfo ci) {
        if (minecraft.screen instanceof CreativeModeInventoryScreen) {
            CreativeModeInventoryScreenProperties itemPickerMenu = (CreativeModeInventoryScreenProperties) minecraft.screen;
            if (Filter.isTabHasFilters(itemPickerMenu.getSelectedTab())) {
                this.leftPos = this.leftPos - 55; // move effect labels to avoid overlap with filters
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "renderEffects")
    private void afterRenderEffects(PoseStack poseStack, CallbackInfo ci) {
        if (minecraft.screen instanceof CreativeModeInventoryScreen) {
            CreativeModeInventoryScreenProperties itemPickerMenu = (CreativeModeInventoryScreenProperties) minecraft.screen;
            if (Filter.isTabHasFilters(itemPickerMenu.getSelectedTab())) {
                this.leftPos = this.leftPos + 55;
            }
        }
    }
}
