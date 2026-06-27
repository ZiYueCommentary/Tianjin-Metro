package ziyue.tjmetro.mod.mixin;

import org.mtr.core.data.Route;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Text;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketSetRouteIdHasDisabledAnnouncements;
import org.mtr.mod.packet.PacketUpdateData;
import org.mtr.mod.screen.EditNameColorScreenBase;
import org.mtr.mod.screen.EditRouteScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.mod.Reference;

import static ziyue.tjmetro.mod.data.IGuiExtension.CHECKBOX_WIDTH;

/**
 * @author ZiYueCommentary
 * @see EditRouteScreen
 * @since 1.1.1
 */

@Mixin(EditRouteScreen.class)
public abstract class EditRouteScreenMixin extends EditNameColorScreenBase<Route> implements IGui
{
    @Shadow(remap = false)
    @Final
    private TextFieldWidgetExtension textFieldLightRailRouteNumber;
    @Shadow(remap = false)
    @Final
    private CheckboxWidgetExtension buttonIsRouteHidden;
    @Shadow(remap = false)
    @Final
    private CheckboxWidgetExtension buttonDisableNextStationAnnouncements;
    @Shadow(remap = false)
    @Final
    private boolean isCircular;
    @Shadow(remap = false)
    @Final
    private CheckboxWidgetExtension buttonIsClockwiseRoute;
    @Shadow(remap = false)
    @Final
    private CheckboxWidgetExtension buttonIsAntiClockwiseRoute;
    @Unique
    private final ButtonWidgetExtension buttonDuplicateSettings = new ButtonWidgetExtension(0, 0, 0, 20, (button) -> duplicateSettings());

    public EditRouteScreenMixin(Route data, TranslationProvider.TranslationHolder nameKey, TranslationProvider.TranslationHolder colorKey, ScreenExtension previousScreenExtension) {
        super(data, nameKey, colorKey, previousScreenExtension);
    }

    @Inject(at = @At("TAIL"), method = "<init>", remap = false)
    private void afterConstruct(Route route, ScreenExtension previousScreenExtension, CallbackInfo ci) {
        buttonDuplicateSettings.setMessage2(new Text(TextHelper.translatable("button.tjmetro.duplicate_route").data));
    }

    @Inject(at = @At("TAIL"), method = "init2", remap = false)
    private void afterInit(CallbackInfo ci) {
        IDrawing.setPositionAndWidth(buttonDuplicateSettings, SQUARE_SIZE * 2 + CHECKBOX_WIDTH + 25, SQUARE_SIZE * 3, CHECKBOX_WIDTH);
        this.addChild(new ClickableWidget(buttonDuplicateSettings));
    }

    @Inject(at = @At("TAIL"), method = "render", remap = false)
    private void afterRender(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
        guiDrawing.beginDrawingTexture(new Identifier(Reference.MOD_ID, "textures/sign/tianjin_metro_mod_logo.png"));
        final double x1 = SQUARE_SIZE * 2 + CHECKBOX_WIDTH;
        final double y1 = SQUARE_SIZE * 3;
        guiDrawing.drawTexture(x1, y1, x1 + 20, y1 + 20, 0, 0, 1, 1);
        guiDrawing.finishDrawingTexture();
    }

    @Unique
    private void duplicateSettings() {
        final Route duplicated = new Route(data.getTransportMode(), MinecraftClientData.getDashboardInstance());
        duplicated.setName(String.format(TextHelper.translatable("gui.tjmetro.duplicated_route").getString(), data.getName()));
        duplicated.setColor(data.getColor());
        duplicated.getRoutePlatforms().addAll(0, data.getRoutePlatforms());
        duplicated.setRouteNumber(textFieldLightRailRouteNumber.getText2());
        duplicated.setHidden(buttonIsRouteHidden.isChecked2());
        final boolean routeIdHasDisabledAnnouncements = buttonDisableNextStationAnnouncements.isChecked2();
        MinecraftClientData.getInstance().setRouteIdHasDisabledAnnouncements(duplicated.getId(), routeIdHasDisabledAnnouncements);

        if (isCircular) {
            duplicated.setCircularState(buttonIsClockwiseRoute.isChecked2() ? Route.CircularState.CLOCKWISE : (buttonIsAntiClockwiseRoute.isChecked2() ? Route.CircularState.ANTICLOCKWISE : Route.CircularState.NONE));
        } else {
            duplicated.setCircularState(Route.CircularState.NONE);
        }

        InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(duplicated)));
        InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketSetRouteIdHasDisabledAnnouncements(duplicated.getId(), routeIdHasDisabledAnnouncements));

        this.onClose2();
    }
}
