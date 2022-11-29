package ziyue.tjmetro.filters;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import mtr.mappings.Text;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import ziyue.tjmetro.BlockList;

import java.util.Arrays;
import java.util.List;

/*
 * MrCrayfish的Filters模组和家具模组的物品栏分类依靠物品标签
 * 然而Fabric似乎没有像Forge那样直观地获取标签列表的方法，所以要换个思路
 * 这里的方法是创建一个FILTERS列表，里面有指定创造物品栏里的所有分类
 * 该类继承了按钮类是因为按下分类按钮会改变enabled参数
 * 如果把按钮单独列为一个类，那么按钮就需要存储属于它的分类，而java也没有指针这么个东西，所以按下去之后也没用（改变的是按钮里的分类而不是物品栏显示的分类）
 * 所以这里干脆就把按钮和分类合并了
 */

/**
 * Filter, contains filters list.
 *
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mixin.mixins.CreativeScreenMixin
 * @since 1.0b
 */

public class Filter extends Button
{
    public static final Filter BUILDING = new Filter(Text.translatable("filter.tjmetro.building"), BlockList.ROLLING.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    BlockList.ROLLING.get().asItem().getDefaultInstance(),
                    BlockList.PLATFORM_TJ_1.get().asItem().getDefaultInstance(),
                    BlockList.PLATFORM_TJ_1_INDENTED.get().asItem().getDefaultInstance(),
                    BlockList.PLATFORM_TJ_2.get().asItem().getDefaultInstance(),
                    BlockList.PLATFORM_TJ_2_INDENTED.get().asItem().getDefaultInstance(),
                    BlockList.MARBLE_GRAY.get().asItem().getDefaultInstance(),
                    BlockList.MARBLE_GRAY_SLAB.get().asItem().getDefaultInstance(),
                    BlockList.MARBLE_GRAY_STAIRS.get().asItem().getDefaultInstance(),
                    BlockList.MARBLE_YELLOW.get().asItem().getDefaultInstance(),
                    BlockList.MARBLE_YELLOW_SLAB.get().asItem().getDefaultInstance(),
                    BlockList.MARBLE_YELLOW_STAIRS.get().asItem().getDefaultInstance(),
                    BlockList.CUSTOM_COLOR_CONCRETE.get().asItem().getDefaultInstance(),
                    BlockList.CUSTOM_COLOR_CONCRETE_STAIRS.get().asItem().getDefaultInstance(),
                    BlockList.CUSTOM_COLOR_CONCRETE_SLAB.get().asItem().getDefaultInstance(),
                    BlockList.HIGH_SPEED_REPEATER.get().asItem().getDefaultInstance(),
                    BlockList.METAL_DETECTION_DOOR.get().asItem().getDefaultInstance(),
                    BlockList.ROADBLOCK.get().asItem().getDefaultInstance(),
                    BlockList.ROADBLOCK_SIGN.get().asItem().getDefaultInstance()
            ));
    public static final Filter SIGNS = new Filter(Text.translatable("filter.tjmetro.signs"), BlockList.STATION_NAME_SIGN_1.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    BlockList.STATION_NAME_SIGN_1.get().asItem().getDefaultInstance(),
                    BlockList.STATION_NAME_SIGN_2.get().asItem().getDefaultInstance(),
                    BlockList.STATION_NAME_WALL_LEGACY.get().asItem().getDefaultInstance()
            ));
    public static final Filter DECORATION = new Filter(Text.translatable("filter.tjmetro.decoration"), BlockList.LOGO.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    BlockList.LOGO.get().asItem().getDefaultInstance(),
                    BlockList.APG_CORNER.get().asItem().getDefaultInstance(),
                    BlockList.BENCH.get().asItem().getDefaultInstance(),
                    BlockList.PLAYER_DETECTOR.get().asItem().getDefaultInstance(),
                    BlockList.DECORATION_LIGHT.get().asItem().getDefaultInstance(),
                    BlockList.TIME_DISPLAY.get().asItem().getDefaultInstance(),
                    BlockList.EMERGENCY_EXIT_SIGN.get().asItem().getDefaultInstance()
            ));
    public static final Filter CEILINGS = new Filter(Text.translatable("filter.tjmetro.ceilings"), BlockList.STATION_COLOR_CEILING.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    BlockList.CEILING_NOT_LIT.get().asItem().getDefaultInstance(),
                    BlockList.STATION_COLOR_CEILING_LIGHT.get().asItem().getDefaultInstance(),
                    BlockList.STATION_COLOR_CEILING.get().asItem().getDefaultInstance(),
                    BlockList.STATION_COLOR_CEILING_NOT_LIT.get().asItem().getDefaultInstance(),
                    BlockList.STATION_COLOR_CEILING_NO_LIGHT.get().asItem().getDefaultInstance()
            ));
    public static final Filter RAILWAYS = new Filter(Text.translatable("filter.tjmetro.railway_signs"), BlockList.RAILWAY_SIGN_WALL_2.get().asItem().getDefaultInstance(),
            Arrays.asList(
                    BlockList.RAILWAY_SIGN_WALL_2.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_WALL_4.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_WALL_6.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_WALL_8.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_WALL_10.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_2_EVEN.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_3_EVEN.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_4_EVEN.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_5_EVEN.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_6_EVEN.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_7_EVEN.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_2_ODD.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_3_ODD.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_4_ODD.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_5_ODD.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_6_ODD.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_7_ODD.get().asItem().getDefaultInstance(),
                    BlockList.RAILWAY_SIGN_TIANJIN_POLE.get().asItem().getDefaultInstance()
            ));

    public static final List<Filter> FILTERS = Arrays.asList(DECORATION, CEILINGS, BUILDING, SIGNS, RAILWAYS);

    private static final ResourceLocation TABS = new ResourceLocation("textures/gui/container/creative_inventory/tabs.png");

    public final ItemStack itemStack;
    public final List<ItemStack> filter;
    public boolean enabled = true;

    public Filter(Component component, ItemStack itemStack, List<ItemStack> filter) {
        super(0, 0, 32, 28, component, Button::onPress);
        this.itemStack = itemStack;
        this.filter = filter;
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
        this.drawRotatedTexture(poseStack.last().pose(), x, y, textureX, textureY, width, 28);

        RenderSystem.enableRescaleNormal();
        ItemRenderer renderer = mc.getItemRenderer();
        renderer.blitOffset = 100f;
        renderer.renderAndDecorateItem(itemStack, x + 8, y + 6);
        renderer.renderGuiItemDecorations(mc.font, itemStack, x + 8, y + 6);
        renderer.blitOffset = 0f;
    }

    private void drawRotatedTexture(Matrix4f pose, int x, int y, int textureX, int textureY, int width, int i) {
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
}
