package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.screen.ConfigScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.Configs;

import static ziyue.tjmetro.Configs.noFallingBlocks;
import static ziyue.tjmetro.Configs.writeToFile;

/**
 * Mix with MTR config screen.
 * @author ZiYueCommentary
 * @since 1.0b
 * @see ConfigScreen
 */

@Mixin(ConfigScreen.class)
public class ConfigScreenMixin extends ScreenMapper implements IGui
{
    protected ConfigScreenMixin(Component title) {
        super(title);
    }

    private final Button buttonNoFallingBlocks = new Button(0, 0, 0, BUTTON_HEIGHT, new TextComponent(""), button -> {
        noFallingBlocks = setNoFallingBlocks(!noFallingBlocks);
        setButtonText(button, noFallingBlocks);
    }, (button, poseStack, i, j) -> renderTooltip(poseStack, new TranslatableComponent("tooltip.tjmetro.no_suggest").setStyle(Style.EMPTY.withColor(ChatFormatting.YELLOW)), i, j));

    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

    @Inject(method = "init", at = @At("TAIL"))
    protected void init(CallbackInfo ci) {
        Configs.refreshProperties();
        int pos = 13;
        IDrawing.setPositionAndWidth(buttonNoFallingBlocks, width - SQUARE_SIZE - BUTTON_WIDTH, BUTTON_HEIGHT * (pos++) + SQUARE_SIZE, BUTTON_WIDTH);
        setButtonText(buttonNoFallingBlocks, noFallingBlocks);
        addDrawableChild(buttonNoFallingBlocks);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        drawCenteredString(matrices, font, Text.translatable("gui.tjmetro.options"), width / 2, TEXT_PADDING*32, ARGB_WHITE);

        int pos = 13;
        final int yStart1 = SQUARE_SIZE + TEXT_PADDING / 2;
        try {
            drawString(matrices, font, Text.translatable("options.tjmetro.no_falling_blocks"), SQUARE_SIZE, BUTTON_HEIGHT * (pos++) + yStart1, ARGB_WHITE);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void setButtonText(Button button, boolean state) {
        button.setMessage(Text.translatable(state ? "options.mtr.on" : "options.mtr.off"));
    }

    private static boolean setNoFallingBlocks(boolean value) {
        noFallingBlocks = value;
        writeToFile();
        return noFallingBlocks;
    }
}
