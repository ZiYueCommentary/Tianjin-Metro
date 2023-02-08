package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.mappings.Text;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.Reference;
import ziyue.tjmetro.filters.Filter;
import ziyue.tjmetro.filters.IconButton;
import ziyue.tjmetro.mixin.properties.CreativeModeInventoryScreenProperties;

import java.util.Comparator;
import java.util.Map;

/**
 * Render filters.
 *
 * @author ZiYueCommentary
 * @see Filter
 * @see EffectRenderingInventoryScreenMixin
 * @see CreativeModeInventoryScreenProperties
 * @since beta-1
 */

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> implements CreativeModeInventoryScreenProperties
{
    private static final ResourceLocation ICONS = new ResourceLocation(Reference.MOD_ID, "textures/gui/filters.png");

    @Shadow
    @Final
    private Map<ResourceLocation, Tag<Item>> visibleTags;

    @Shadow
    protected abstract void renderTooltip(PoseStack poseStack, ItemStack itemStack, int i, int j);

    @Shadow
    private float scrollOffs;

    @Shadow
    private static int selectedTab;

    public CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(at = @At("HEAD"), method = "render")
    protected void beforeRender(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        Filter.FILTERS.forEach((map, filter1) -> showButtons(filter1, false));
        Filter.FILTERS.forEach((map, filter) -> filter.forEach(button -> button.visible = false));
        if (!hasFilters(getSelectedTab())) return;
        updateTab();
        Filter.FilterList filter = Filter.FILTERS.get(getSelectedTab());
        showButtons(filter, true);
        for (int o = 0; o < filter.size(); o++) {
            if (o >= filter.filterIndex && o < filter.filterIndex + 4) {
                filter.get(o).x = leftPos - 28;
                filter.get(o).y = topPos + 29 * (o - filter.filterIndex) + 10;
                filter.get(o).visible = true;
            } else filter.get(o).visible = false;
        }
        filter.btnScrollUp.active = filter.filterIndex > 0;
        filter.btnScrollDown.active = filter.filterIndex + 4 < filter.size();
    }

    @Inject(at = @At("TAIL"), method = "render")
    protected void afterRender(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        if (!hasFilters(getSelectedTab())) return;
        Filter.FilterList filter = Filter.FILTERS.get(getSelectedTab());
        if (filter.btnScrollUp.isHovered()) renderTooltip(poseStack, filter.btnScrollUp.getMessage(), i, j);
        if (filter.btnScrollDown.isHovered()) renderTooltip(poseStack, filter.btnScrollDown.getMessage(), i, j);
        if (filter.btnEnableAll.isHovered()) renderTooltip(poseStack, filter.btnEnableAll.getMessage(), i, j);
        if (filter.btnDisableAll.isHovered()) renderTooltip(poseStack, filter.btnDisableAll.getMessage(), i, j);
        filter.forEach(filter1 -> {
            if (filter1.isHovered()) renderTooltip(poseStack, filter1.getMessage(), i, j);
        });
    }

    @Inject(at = @At("TAIL"), method = "init")
    protected void init(CallbackInfo ci) {
        Filter.FILTERS.forEach((map, filter) -> {
            filter.btnScrollUp = new IconButton(this.leftPos - 22, this.topPos - 12, Text.translatable("button.tjmetro.scroll_up").withStyle(ChatFormatting.WHITE), button -> filter.filterIndex--, ICONS, 0, 0);
            filter.btnScrollDown = new IconButton(this.leftPos - 22, this.topPos + 127, Text.translatable("button.tjmetro.scroll_down").withStyle(ChatFormatting.WHITE), button -> filter.filterIndex++, ICONS, 16, 0);
            filter.btnEnableAll = new IconButton(this.leftPos - 50, this.topPos + 10, Text.translatable("button.tjmetro.enable_all").withStyle(ChatFormatting.WHITE), button -> Filter.FILTERS.get(getSelectedTab()).forEach(filter1 -> filter1.enabled = true), ICONS, 32, 0);
            filter.btnDisableAll = new IconButton(this.leftPos - 50, this.topPos + 32, Text.translatable("button.tjmetro.disable_all").withStyle(ChatFormatting.WHITE), button -> Filter.FILTERS.get(getSelectedTab()).forEach(filter1 -> filter1.enabled = false), ICONS, 48, 0);
            addButton(filter.btnScrollUp);
            addButton(filter.btnScrollDown);
            addButton(filter.btnEnableAll);
            addButton(filter.btnDisableAll);

            filter.forEach(this::addButton);
        });
    }

    protected void showButtons(Filter.FilterList list, boolean visible) {
        if (list.size() > 4) {
            list.btnScrollUp.visible = visible;
            list.btnScrollDown.visible = visible;
        } else {
            list.btnScrollUp.visible = false;
            list.btnScrollDown.visible = false;
        }
        list.btnEnableAll.visible = visible;
        list.btnDisableAll.visible = visible;
    }

    protected void updateTab() {
        if (!hasFilters(getSelectedTab())) return;
        visibleTags.clear();
        menu.items.clear(); //clear tab
        Filter.FILTERS.get(getSelectedTab()).forEach(
                filter -> {
                    if (filter.enabled) menu.items.addAll(filter.items); //add items
                }
        );
        menu.items.sort(Comparator.comparingInt(o -> Item.getId(o.getItem()))); //sort items
        float previousOffset = this.scrollOffs;
        this.menu.scrollTo(0.0f); //refresh (maybe?)
        this.scrollOffs = previousOffset;
        this.menu.scrollTo(previousOffset);
    }

    @Override
    public int getSelectedTab() {
        return selectedTab;
    }

    @Override
    public boolean hasFilters(int tabId) {
        return Filter.FILTERS.containsKey(tabId);
    }
}