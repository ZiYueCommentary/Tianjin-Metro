package ziyue.tjmetro.mod.mixin;

import org.mtr.core.data.Depot;
import org.mtr.core.data.Siding;
import org.mtr.core.data.TransportMode;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.Icons;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.RailType;
import org.mtr.mod.packet.PacketUpdateData;
import org.mtr.mod.screen.SavedRailScreenBase;
import org.mtr.mod.screen.SidingScreen;
import org.mtr.mod.screen.WidgetShorterSlider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.mod.Reference;

/**
 * @author ZiYueCommentary
 * @since 1.1.0
 */

@Mixin(SidingScreen.class)
public abstract class SidingScreenMixin extends SavedRailScreenBase<Siding, Depot> implements Icons
{
    @Shadow(remap = false)
    @Final
    private TextFieldWidgetExtension textFieldMaxTrains;

    @Shadow(remap = false)
    @Final
    private WidgetShorterSlider sliderAccelerationConstant;

    @Shadow(remap = false)
    @Final
    private WidgetShorterSlider sliderDecelerationConstant;

    @Shadow(remap = false)
    @Final
    private static int SLIDER_SCALE;

    @Shadow(remap = false)
    @Final
    private CheckboxWidgetExtension buttonIsManual;

    @Shadow(remap = false)
    @Final
    private CheckboxWidgetExtension buttonUnlimitedTrains;

    @Shadow(remap = false)
    @Final
    private WidgetShorterSlider sliderDelayedVehicleSpeedIncreasePercentage;

    @Shadow(remap = false)
    @Final
    private WidgetShorterSlider sliderDelayedVehicleReduceDwellTimePercentage;

    @Shadow(remap = false)
    @Final
    private CheckboxWidgetExtension buttonEarlyVehicleIncreaseDwellTime;

    @Shadow(remap = false)
    private static RailType convertMaxManualSpeed(int maxManualSpeed) {
        return null;
    }

    @Shadow(remap = false)
    @Final
    private WidgetShorterSlider sliderMaxManualSpeed;

    public SidingScreenMixin(Siding savedRailBase, TransportMode transportMode, ScreenExtension previousScreenExtension, MutableText... additionalTexts) {
        super(savedRailBase, transportMode, previousScreenExtension, additionalTexts);
    }

    @Unique
    private final ButtonWidgetExtension buttonDuplicateSettings = new ButtonWidgetExtension(0, 0, 0, 20, (button) -> duplicateSettings());

    @Inject(at = @At("TAIL"), method = "<init>", remap = false)
    private void afterConstruct(Siding siding, TransportMode transportMode, ScreenExtension previousScreenExtension, CallbackInfo ci) {
        buttonDuplicateSettings.setMessage2(new Text(TextHelper.translatable("button.tjmetro.duplicate_siding_settings").data));
    }

    @Inject(at = @At("TAIL"), method = "init2", remap = false)
    private void afterInit(CallbackInfo ci) {
//        IDrawing.setPositionAndWidth(buttonDuplicateSettings, SQUARE_SIZE + 25, SQUARE_SIZE * 13 + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, width - textWidth - SQUARE_SIZE * 2);
        this.addChild(new ClickableWidget(buttonDuplicateSettings));
    }

    @Inject(at = @At("HEAD"), method = "render", remap = false)
    private void beforeRender(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        IDrawing.setPositionAndWidth(buttonDuplicateSettings, SQUARE_SIZE + 25, SQUARE_SIZE * (buttonIsManual.isChecked2() ? 13 : 10) + TEXT_FIELD_PADDING * 2 + TEXT_PADDING, width - textWidth - SQUARE_SIZE * 2);
    }

    @Inject(at = @At("TAIL"), method = "render", remap = false)
    private void afterRender(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
        guiDrawing.beginDrawingTexture(new Identifier(Reference.MOD_ID, "textures/sign/tianjin_metro_mod_logo.png"));
        final double x1 = SQUARE_SIZE;
        final double y1 = SQUARE_SIZE * (buttonIsManual.isChecked2() ? 13 : 10) + TEXT_FIELD_PADDING * 2 + TEXT_PADDING;
        guiDrawing.drawTexture(x1, y1, x1 + 20, y1 + 20, 0, 0, 1, 1);
        guiDrawing.finishDrawingTexture();
    }

    @Unique
    private void duplicateSettings() {
        final Depot depot = MinecraftClientData.getDashboardInstance().depotIdMap.get(this.savedRailBase.area.getId());
        depot.savedRails.forEach(siding -> {
            siding.setVehicleCars(savedRailBase.getVehicleCars());
            int maxTrains;
            try {
                maxTrains = Math.max(0, Integer.parseInt(textFieldMaxTrains.getText2()));
            } catch (Exception ignored) {
                maxTrains = 0;
            }

            double accelerationConstant;
            try {
                accelerationConstant = Utilities.round(MathHelper.clamp((float) sliderAccelerationConstant.getIntValue() / SLIDER_SCALE + Siding.MIN_ACCELERATION, Siding.MIN_ACCELERATION, Siding.MAX_ACCELERATION), 8);
            } catch (Exception ignored) {
                accelerationConstant = Siding.ACCELERATION_DEFAULT;
            }

            double decelerationConstant;
            try {
                decelerationConstant = Utilities.round(MathHelper.clamp((float) sliderDecelerationConstant.getIntValue() / SLIDER_SCALE + Siding.MIN_ACCELERATION, Siding.MIN_ACCELERATION, Siding.MAX_ACCELERATION), 8);
            } catch (Exception ignored) {
                decelerationConstant = Siding.ACCELERATION_DEFAULT;
            }

            if (buttonIsManual.isChecked2()) {
                siding.setIsManual(true);
            } else if (buttonUnlimitedTrains.isChecked2()) {
                siding.setUnlimitedVehicles(true);
            } else {
                siding.setMaxVehicles(maxTrains);
            }
            siding.setAcceleration(accelerationConstant);
            siding.setDeceleration(decelerationConstant);
            siding.setDelayedVehicleSpeedIncreasePercentage(sliderDelayedVehicleSpeedIncreasePercentage.getIntValue());
            siding.setDelayedVehicleReduceDwellTimePercentage(sliderDelayedVehicleReduceDwellTimePercentage.getIntValue());
            siding.setEarlyVehicleIncreaseDwellTime(buttonEarlyVehicleIncreaseDwellTime.isChecked2());

            siding.setMaxManualSpeed(Utilities.kilometersPerHourToMetersPerMillisecond(convertMaxManualSpeed(sliderMaxManualSpeed.getIntValue()).speedLimit));
            siding.setManualToAutomaticTime(sliderDwellTimeMin.getIntValue() * SECONDS_PER_MINUTE * Utilities.MILLIS_PER_SECOND + sliderDwellTimeSec.getIntValue() * Utilities.MILLIS_PER_SECOND / 2);

            InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addSiding(siding)));
        });
        this.onClose2();
    }
}
