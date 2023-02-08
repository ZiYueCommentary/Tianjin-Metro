package ziyue.tjmetro.filters;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import mtr.CreativeModeTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ziyue.tjmetro.mixin.mixins.CreativeModeInventoryScreenMixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Filter for creative mode tab.
 * This filter support register for any creative mode tabs, including vanilla tabs.
 * Inspired by <a href="https://github.com/MrCrayfish/Filters">Filters Mod</a> and <a href="https://github.com/MrCrayfish/MrCrayfishFurnitureMod">MrCrayfish's Furniture Mod</a>.
 *
 * @author ZiYueCommentary
 * @see CreativeModeInventoryScreenMixin
 * @see ziyue.tjmetro.mixin.mixins.EffectRenderingInventoryScreenMixin
 * @see ziyue.tjmetro.mixin.properties.CreativeModeInventoryScreenProperties
 * @see <a href="https://github.com/MrCrayfish/Filters">Filters Mod</a>
 * @see <a href="https://github.com/MrCrayfish/MrCrayfishFurnitureMod">MrCrayfish's Furniture Mod</a>
 * @since beta-1
 */

public class Filter extends Button
{
    public static final HashMap<Integer, FilterList> FILTERS = new HashMap<>();

    protected static final ResourceLocation TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    public Supplier<ItemStack> icon;
    public final List<ItemStack> items;
    public boolean enabled = true;

    protected Filter(Component component, Supplier<ItemStack> icon, List<ItemStack> items) {
        super(0, 0, 32, 28, component, Button::onPress);
        this.icon = icon;
        this.items = items;
    }

    @Override
    public void onPress() {
        enabled = !enabled;
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(TABS);

        GlStateManager._blendColor(1f, 1f, 1f, this.alpha);
        GlStateManager._disableLighting();
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
        GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);

        int width = this.enabled ? 32 : 28;
        int textureX = 28;
        int textureY = this.enabled ? 32 : 0;
        this.drawRotatedTexture(poseStack.last().pose(), x, y, textureX, textureY, width);

        RenderSystem.enableRescaleNormal();
        ItemRenderer renderer = mc.getItemRenderer();
        renderer.blitOffset = 100f;
        renderer.renderAndDecorateItem(icon.get(), x + 8, y + 6);
        renderer.renderGuiItemDecorations(mc.font, icon.get(), x + 8, y + 6);
        renderer.blitOffset = 0f;
    }

    protected void drawRotatedTexture(Matrix4f pose, int x, int y, int textureX, int textureY, int width) {
        float scaleX = 0.00390625F;
        float scaleY = 0.00390625F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(7, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(pose, x, y + height, 0f).uv(((float) (textureX + height) * scaleX), ((float) (textureY) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x + width, y + height, 0f).uv(((float) (textureX + height) * scaleX), ((float) (textureY + width) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x + width, y, 0f).uv(((float) (textureX) * scaleX), ((float) (textureY + width) * scaleY)).endVertex();
        bufferBuilder.vertex(pose, x, y, 0f).uv(((float) (textureX) * scaleX), ((float) (textureY) * scaleY)).endVertex();
        tesselator.end();
    }

    /**
     * Register a filter for specific creative mode tab.
     *
     * @param creativeModeTab Specific creative mode tab
     * @param filterName      Name of the filter
     * @param filterIcon      Icon for filter
     * @return Filter instance
     * @author ZiYueCommentary
     * @since beta-1
     */
    public static Filter registerFilter(CreativeModeTabs.Wrapper creativeModeTab, Component filterName, Supplier<ItemStack> filterIcon) {
        Filter filter = new Filter(filterName, filterIcon, new ArrayList<>());
        FilterList filterList = FILTERS.getOrDefault(creativeModeTab.get().getId(), FilterList.empty());
        filterList.add(filter);
        FILTERS.put(creativeModeTab.get().getId(), filterList);
        return filter;
    }

    /**
     * Filter list for creative mode tabs.
     * This list including unique buttons and index of tabs. This feature can separate button states and scrolling states with other tabs.
     *
     * @author ZiYueCommentary
     * @see ArrayList
     * @since beta-1
     */
    public static class FilterList extends ArrayList<Filter>
    {
        public Button btnScrollUp, btnScrollDown, btnEnableAll, btnDisableAll;
        public int filterIndex = 0;

        public static FilterList empty() {
            return new FilterList();
        }
    }
}
