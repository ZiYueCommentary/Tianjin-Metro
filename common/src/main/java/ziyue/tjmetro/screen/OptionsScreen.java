package ziyue.tjmetro.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.components.Button;
import ziyue.tjmetro.Options;

public class OptionsScreen extends ScreenMapper implements IGui
{
    protected boolean enableMTRFilters;

    protected final Button buttonEnableMTRFilters;

    public static final int BUTTON_WIDTH = 60;
    public static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

    public OptionsScreen() {
        super(Text.literal(""));

        buttonEnableMTRFilters = UtilitiesClient.newButton(BUTTON_HEIGHT, Text.literal(""), button -> {
            enableMTRFilters = Options.enableMTRFilters(!enableMTRFilters);
            setButtonText(button, enableMTRFilters);
        });
    }

    @Override
    protected void init() {
        super.init();
        Options.refreshProperties();
        enableMTRFilters = Options.enableMTRFilters();

        final int offsetY = 0;

        IDrawing.setPositionAndWidth(buttonEnableMTRFilters, width - SQUARE_SIZE - BUTTON_WIDTH, SQUARE_SIZE + offsetY, BUTTON_WIDTH);
        setButtonText(buttonEnableMTRFilters, enableMTRFilters);
        addDrawableChild(buttonEnableMTRFilters);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(matrices);
            drawCenteredString(matrices, font, Text.translatable("gui.tjmetro.options"), width / 2, TEXT_PADDING, ARGB_WHITE);

            final int yStart1 = SQUARE_SIZE + TEXT_PADDING / 2;
            drawString(matrices, font, Text.translatable("options.tjmetro.enable_mtr_filters"), SQUARE_SIZE, yStart1, ARGB_WHITE);

            super.render(matrices, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    public static void setButtonText(Button button, boolean state) {
        button.setMessage(Text.translatable(state ? "options.mtr.on" : "options.mtr.off"));
    }
}
