package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.mappings.Text;
import net.minecraft.client.gui.components.Button;
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
import ziyue.tjmetro.TianjinMetro;
import ziyue.tjmetro.filters.Filter;
import ziyue.tjmetro.filters.IconButton;

import java.util.Comparator;
import java.util.Map;

/*
 * 当创造物品栏当前显示的是天津地铁模组物品栏时，游戏会首先将创造物品栏清空
 * 随后遍历Filter类里的FILTERS获取所有分类
 * 如果分类的enabled参数为true，则将分类里的物品加入到物品栏里
 */

/**
 * A beautiful filter, maybe this is the most technical content feature for me.
 * Inspired by <a href="https://github.com/MrCrayfish/Filters">Filters Mod</a> and <a href="https://github.com/MrCrayfish/MrCrayfishFurnitureMod">MrCrayfish's Furniture Mod</a>.
 *
 * @author ZiYueCommentary
 * @see Filter
 * @see <a href="https://github.com/MrCrayfish/Filters">Filters Mod</a>
 * @see <a href="https://github.com/MrCrayfish/MrCrayfishFurnitureMod">MrCrayfish's Furniture Mod</a>
 * @since beta-1
 */

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu>
{
    private static final ResourceLocation ICONS = new ResourceLocation(Reference.MOD_ID, "textures/gui/filters.png");
    private Button btnScrollUp, btnScrollDown, btnEnableAll, btnDisableAll;
    private static int filterIndex = 0;

    @Shadow
    public abstract int getSelectedTab();

    @Shadow
    @Final
    private Map<ResourceLocation, Tag<Item>> visibleTags;

    @Shadow
    protected abstract void renderTooltip(PoseStack poseStack, ItemStack itemStack, int i, int j);

    @Shadow
    private float scrollOffs;

    public CreativeScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(at = @At("HEAD"), method = "render")
    protected void beforeRender(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        if (this.getSelectedTab() == TianjinMetro.TAB.get().getId()) {
            updateTabs();
            showButtons(true);
            for (int o = 0; o < Filter.FILTERS.size(); o++) {
                if (o >= filterIndex && o < filterIndex + 4) {
                    Filter.FILTERS.get(o).x = leftPos - 28;
                    Filter.FILTERS.get(o).y = topPos + 29 * (o - filterIndex) + 10;
                    Filter.FILTERS.get(o).visible = true;
                } else Filter.FILTERS.get(o).visible = false;
            }
            btnScrollUp.active = filterIndex > 0;
            btnScrollDown.active = filterIndex + 4 < Filter.FILTERS.size();
        } else {
            showButtons(false);
            Filter.FILTERS.forEach(filter -> filter.visible = false);
        }
    }

    @Inject(at = @At("TAIL"), method = "render")
    protected void afterRender(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        if (btnScrollUp.isHovered()) renderTooltip(poseStack, btnScrollUp.getMessage(), i, j);
        if (btnScrollDown.isHovered()) renderTooltip(poseStack, btnScrollDown.getMessage(), i, j);
        if (btnEnableAll.isHovered()) renderTooltip(poseStack, btnEnableAll.getMessage(), i, j);
        if (btnDisableAll.isHovered()) renderTooltip(poseStack, btnDisableAll.getMessage(), i, j);
        Filter.FILTERS.forEach(filter -> {
            if (filter.isHovered()) renderTooltip(poseStack, filter.getMessage(), i, j);
        });
    }

    @Inject(at = @At("TAIL"), method = "init")
    protected void init(CallbackInfo ci) {
        btnScrollUp = new IconButton(this.leftPos - 22, this.topPos - 12, Text.translatable("button.tjmetro.scroll_up"), button -> filterIndex--, ICONS, 0, 0);
        btnScrollDown = new IconButton(this.leftPos - 22, this.topPos + 127, Text.translatable("button.tjmetro.scroll_down"), button -> filterIndex++, ICONS, 16, 0);
        btnEnableAll = new IconButton(this.leftPos - 50, this.topPos + 10, Text.translatable("button.tjmetro.enable_all"), button -> Filter.FILTERS.forEach(filter -> filter.enabled = true), ICONS, 32, 0);
        btnDisableAll = new IconButton(this.leftPos - 50, this.topPos + 32, Text.translatable("button.tjmetro.disable_all"), button -> Filter.FILTERS.forEach(filter -> filter.enabled = false), ICONS, 48, 0);
        addButton(btnScrollUp);
        addButton(btnScrollDown);
        addButton(btnEnableAll);
        addButton(btnDisableAll);
        Filter.FILTERS.forEach(this::addButton);
    }

    protected void showButtons(boolean visible) {
        if (Filter.FILTERS.size() > 4) {
            btnScrollUp.visible = visible;
            btnScrollDown.visible = visible;
        } else {
            btnScrollUp.visible = false;
            btnScrollDown.visible = false;
        }
        btnEnableAll.visible = visible;
        btnDisableAll.visible = visible;
    }

    protected void updateTabs() {
        visibleTags.clear();
        menu.items.clear(); //clear tab
        Filter.FILTERS.forEach(
                filter -> {
                    if (filter.enabled) menu.items.addAll(filter.filter); //add items
                }
        );
        menu.items.sort(Comparator.comparingInt(o -> Item.getId(o.getItem()))); //sort items
        float previousOffset = scrollOffs;
        this.scrollOffs = 0.0f;
        this.menu.scrollTo(0.0f); //refresh (maybe?)
        this.scrollOffs = previousOffset;
        this.menu.scrollTo(previousOffset);
    }
}
