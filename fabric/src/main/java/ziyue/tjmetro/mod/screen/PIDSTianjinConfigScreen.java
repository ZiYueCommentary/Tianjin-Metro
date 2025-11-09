package ziyue.tjmetro.mod.screen;

import org.mtr.core.data.Platform;
import org.mtr.core.data.Station;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.screen.DashboardListItem;
import org.mtr.mod.screen.DashboardListSelectorScreen;
import ziyue.tjmetro.mod.RegistryClient;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.block.BlockPIDSTianjin;
import ziyue.tjmetro.mod.packet.PacketUpdatePIDSTianjinConfig;

import java.util.Collections;
import java.util.stream.Collectors;

public class PIDSTianjinConfigScreen extends ScreenExtension implements IGui
{
    private final BlockPos blockPos;
    private final TextFieldWidgetExtension displayPageInput;
    private final CheckboxWidgetExtension selectAllCheckbox;
    private final ButtonWidgetExtension filterButton;
    private final LongAVLTreeSet filterPlatformIds;
    private final int displayPage;
    private final ButtonWidgetExtension categoryButton;
    private final int filteredAdsCount;

    public PIDSTianjinConfigScreen(BlockPos blockPos) {
        super();
        this.blockPos = blockPos;

        selectAllCheckbox = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
        });
        selectAllCheckbox.setMessage2(TranslationProvider.GUI_MTR_AUTOMATICALLY_DETECT_NEARBY_PLATFORM.getText());

        final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
        final BlockPIDSTianjin.BlockEntity blockEntity = (BlockPIDSTianjin.BlockEntity) clientWorld.getBlockEntity(blockPos).data;
        filterPlatformIds = blockEntity.getPlatformIds();
        displayPage = blockEntity.getDisplayPage();
        filteredAdsCount = blockEntity.getCategories().size();

        filterButton = getPlatformFilterButton(blockPos, selectAllCheckbox, filterPlatformIds, this);
        categoryButton = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> MinecraftClient.getInstance().openScreen(new Screen(new CategorySelectorScreen(blockEntity, this))));
        displayPageInput = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 3, TextCase.DEFAULT, "\\D", "1");
    }

    @Override
    protected void init2() {
        super.init2();
        IDrawing.setPositionAndWidth(selectAllCheckbox, SQUARE_SIZE, SQUARE_SIZE, PANEL_WIDTH);
        selectAllCheckbox.setChecked(filterPlatformIds.isEmpty());
        addChild(new ClickableWidget(selectAllCheckbox));

        IDrawing.setPositionAndWidth(filterButton, SQUARE_SIZE, SQUARE_SIZE * 3, PANEL_WIDTH / 2);
        filterButton.setMessage2(new Text(TextHelper.translatable("selectWorld.edit").data));
        addChild(new ClickableWidget(filterButton));

        IDrawing.setPositionAndWidth(displayPageInput, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 5 + TEXT_FIELD_PADDING / 2, PANEL_WIDTH / 2 - TEXT_FIELD_PADDING);
        displayPageInput.setText2(String.valueOf(displayPage + 1));
        addChild(new ClickableWidget(displayPageInput));

        IDrawing.setPositionAndWidth(categoryButton, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 7 + TEXT_FIELD_PADDING, PANEL_WIDTH / 2);
        categoryButton.setMessage2(new Text(TextHelper.translatable("selectWorld.edit").data));
        addChild(new ClickableWidget(categoryButton));
    }

    @Override
    public void onClose2() {
        if (selectAllCheckbox.isChecked2()) {
            filterPlatformIds.clear();
        }
        int displayPage = 0;
        try {
            displayPage = Math.max(0, Integer.parseInt(displayPageInput.getText2()) - 1);
        } catch (Exception e) {
            TianjinMetro.LOGGER.error("", e);
        }
        RegistryClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdatePIDSTianjinConfig(blockPos, filterPlatformIds, displayPage));
        super.onClose2();
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        graphicsHolder.drawText(TranslationProvider.GUI_MTR_DISPLAY_PAGE.getMutableText(), SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
        graphicsHolder.drawText(TranslationProvider.GUI_MTR_FILTERED_PLATFORMS.getMutableText(selectAllCheckbox.isChecked2() ? 0 : filterPlatformIds.size()), SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
        graphicsHolder.drawText(TextHelper.translatable("gui.tjmetro.filtered_ads", filteredAdsCount), SQUARE_SIZE, SQUARE_SIZE * 6 + TEXT_PADDING * 2, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
        super.render(graphicsHolder, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }

    public static ButtonWidgetExtension getPlatformFilterButton(BlockPos blockPos, CheckboxWidgetExtension selectAllCheckbox, LongAVLTreeSet filterPlatformIds, ScreenExtension thisScreen) {
        return new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, button -> {
            final Station station = InitClient.findStation(blockPos);

            final ObjectImmutableList<DashboardListItem> platformsForList;
            if (station != null) {
                platformsForList = getPlatformsForList(new ObjectArrayList<>(station.savedRails));
            } else {
                ObjectArrayList<Platform> nearbyPlatforms = new ObjectArrayList<>();
                InitClient.findClosePlatform(blockPos.down(4), 5, nearbyPlatforms::add);
                platformsForList = getPlatformsForList(nearbyPlatforms);
            }

            if (selectAllCheckbox.isChecked2()) {
                filterPlatformIds.clear();
            }

            MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(() -> selectAllCheckbox.setChecked(filterPlatformIds.isEmpty()), new ObjectImmutableList<>(platformsForList), filterPlatformIds, false, false, thisScreen)));
        });
    }

    public static ObjectImmutableList<DashboardListItem> getPlatformsForList(ObjectArrayList<Platform> platforms) {
        final ObjectArrayList<DashboardListItem> platformsForList = new ObjectArrayList<>();
        Collections.sort(platforms);
        platforms.forEach(platform -> platformsForList.add(new DashboardListItem(platform.getId(), platform.getName() + " " + IGui.mergeStations(MinecraftClientData.getInstance().simplifiedRoutes
                .stream()
                .filter(simplifiedRoute -> simplifiedRoute.getPlatformIndex(platform.getId()) >= 0)
                .map(simplifiedRoute -> Utilities.getElement(simplifiedRoute.getPlatforms(), -1).getStationName())
                .collect(Collectors.toList())
        ), 0)));
        return new ObjectImmutableList<>(platformsForList);
    }
}
