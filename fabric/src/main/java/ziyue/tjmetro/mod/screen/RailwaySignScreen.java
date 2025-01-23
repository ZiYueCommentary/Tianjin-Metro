package ziyue.tjmetro.mod.screen;

import org.mtr.core.data.Station;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.RenderRailwaySign;
import org.mtr.mod.resource.SignResource;
import org.mtr.mod.screen.*;
import ziyue.tjmetro.mod.RegistryClient;
import ziyue.tjmetro.mod.block.BlockStationNameEntranceTianjin;
import ziyue.tjmetro.mod.block.BlockStationNamePlate;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.block.base.IRailwaySign;
import ziyue.tjmetro.mod.packet.PacketUpdateRailwaySignConfig;

import javax.annotation.Nullable;

/**
 * @author ZiYueCommentary
 * @see PacketUpdateRailwaySignConfig
 * @since 1.0.0-beta-1
 */

public class RailwaySignScreen extends ScreenExtension implements IGui
{
    protected int editingIndex;
    protected int page;
    protected int totalPages;
    protected int columns;
    protected int rows;

    protected final BlockPos signPos;
    protected final Type type;
    protected final int length;
    protected final String[] signIds;
    protected final LongAVLTreeSet selectedIds;
    protected final ObjectImmutableList<DashboardListItem> exitsForList;
    protected final ObjectImmutableList<DashboardListItem> platformsForList;
    protected final ObjectArraySet<DashboardListItem> routesForList;
    protected final ObjectArraySet<DashboardListItem> stationsForList;
    protected final ObjectArrayList<String> allSignIds = new ObjectArrayList<>();

    protected final ButtonWidgetExtension[] buttonsEdit;
    protected final ButtonWidgetExtension[] buttonsSelection;
    protected final ButtonWidgetExtension buttonClear;
    protected final TexturedButtonWidgetExtension buttonPrevPage;
    protected final TexturedButtonWidgetExtension buttonNextPage;

    protected static final int SIGN_SIZE = 32;
    protected static final int SIGN_BUTTON_SIZE = 16;
    protected static final int BUTTON_Y_START = SIGN_SIZE + SQUARE_SIZE + SQUARE_SIZE / 2;

    public RailwaySignScreen(BlockPos signPos) {
        super();
        editingIndex = -1;
        this.signPos = signPos;
        final ClientWorld world = MinecraftClient.getInstance().getWorldMapped();

        allSignIds.addAll(CustomResourceLoader.getSortedSignIds());

        final Station station = InitClient.findStation(signPos);
        if (station == null) {
            exitsForList = ObjectImmutableList.of();
            platformsForList = ObjectImmutableList.of();
            stationsForList = new ObjectArraySet<>();
            routesForList = new ObjectArraySet<>();
        } else {
            exitsForList = new ObjectImmutableList<>(EditStationScreen.getExitsForDashboardList(EditStationScreen.getStationExits(station, true)));
            platformsForList = PIDSConfigScreen.getPlatformsForList(station);

            final ObjectArraySet<Station> connectingStationsIncludingThisOne = new ObjectArraySet<>(station.connectedStations);
            connectingStationsIncludingThisOne.add(station);
            stationsForList = MinecraftClientData.convertDataSet(connectingStationsIncludingThisOne);

            final LongAVLTreeSet platformIds = new LongAVLTreeSet();
            connectingStationsIncludingThisOne.forEach(connectingStation -> connectingStation.savedRails.forEach(platform -> platformIds.add(platform.getId())));
            routesForList = new ObjectArraySet<>();
            final IntAVLTreeSet addedColors = new IntAVLTreeSet();
            MinecraftClientData.getInstance().simplifiedRoutes.forEach(simplifiedRoute -> {
                final int color = simplifiedRoute.getColor();
                if (!addedColors.contains(color) && simplifiedRoute.getPlatforms().stream().anyMatch(simplifiedRoutePlatform -> platformIds.contains(simplifiedRoutePlatform.getPlatformId()))) {
                    routesForList.add(new DashboardListItem(color, simplifiedRoute.getName().split("\\|\\|")[0], color));
                    addedColors.add(color);
                }
            });
        }

        if (world != null) {
            final BlockEntity entity = world.getBlockEntity(signPos);
            if (entity != null && entity.data instanceof BlockRailwaySignBase.BlockEntityBase) {
                final BlockRailwaySignBase.BlockEntityBase entity1 = (BlockRailwaySignBase.BlockEntityBase) entity.data;
                signIds = entity1.getSignIds();
                selectedIds = entity1.getSelectedIds();
                type = Type.RAILWAY_SIGN;
            } else {
                signIds = new String[0];
                selectedIds = new LongAVLTreeSet();
                if (entity != null) {
                    if (entity.data instanceof BlockStationNameEntranceTianjin.BlockEntity) {
                        final BlockStationNameEntranceTianjin.BlockEntity sign = (BlockStationNameEntranceTianjin.BlockEntity) entity.data;
                        selectedIds.add(sign.getSelectedId());
                        type = Type.STATION_NAME_ENTRANCE;
                    } else if (entity.data instanceof BlockStationNamePlate.BlockEntity) {
                        final BlockStationNamePlate.BlockEntity plate = (BlockStationNamePlate.BlockEntity) entity.data;
                        selectedIds.add(plate.getPlatformId());
                        type = Type.STATION_NAME_PLATE;
                    } else {
                        type = Type.STATION_NAME_ENTRANCE;
                    }
                } else {
                    type = null;
                }
            }
            final Block block = world.getBlockState(signPos).getBlock();
            if (block.data instanceof BlockRailwaySignBase) {
                final BlockRailwaySignBase block1 = (BlockRailwaySignBase) block.data;
                length = block1.length;
            } else {
                length = 0;
            }
        } else {
            throw new NullPointerException("World is null");
        }

        buttonsEdit = new ButtonWidgetExtension[length];
        for (int i = 0; i < buttonsEdit.length; i++) {
            final int index = i;
            buttonsEdit[i] = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("selectWorld.edit"), button -> edit(index));
        }

        buttonsSelection = new ButtonWidgetExtension[allSignIds.size()];
        for (int i = 0; i < allSignIds.size(); i++) {
            final int index = i;
            buttonsSelection[i] = new ButtonWidgetExtension(0, 0, 0, SIGN_BUTTON_SIZE, button -> setNewSignId(allSignIds.get(index)));
        }

        buttonClear = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.reset_sign"), button -> setNewSignId(null));
        buttonPrevPage = TexturedButtonWidgetHelper.create(0, 0, 0, SQUARE_SIZE, new Identifier("textures/gui/sprites/mtr/icon_left.png"), new Identifier("textures/gui/sprites/mtr/icon_left_highlighted.png"), button -> setPage(page - 1));
        buttonNextPage = TexturedButtonWidgetHelper.create(0, 0, 0, SQUARE_SIZE, new Identifier("textures/gui/sprites/mtr/icon_right.png"), new Identifier("textures/gui/sprites/mtr/icon_right_highlighted.png"), button -> setPage(page + 1));
    }

    @Override
    protected void init2() {
        super.init2();

        for (int i = 0; i < buttonsEdit.length; i++) {
            IDrawing.setPositionAndWidth(buttonsEdit[i], (width - SIGN_SIZE * length) / 2 + i * SIGN_SIZE, SIGN_SIZE, SIGN_SIZE);
            addChild(new ClickableWidget(buttonsEdit[i]));
        }

        columns = Math.max((width - SIGN_BUTTON_SIZE * 3) / (SIGN_BUTTON_SIZE * 8) * 2, 1);
        rows = Math.max((height - SIGN_SIZE - SQUARE_SIZE * 4) / SIGN_BUTTON_SIZE, 1);

        final int xOffsetSmall = (width - SIGN_BUTTON_SIZE * (columns * 4 + 3)) / 2 + SIGN_BUTTON_SIZE;
        final int xOffsetBig = xOffsetSmall + SIGN_BUTTON_SIZE * (columns + 1);

        totalPages = loopSigns((index, x, y, isBig) -> {
            IDrawing.setPositionAndWidth(buttonsSelection[index], (isBig ? xOffsetBig : xOffsetSmall) + x, BUTTON_Y_START + y, isBig ? SIGN_BUTTON_SIZE * 3 : SIGN_BUTTON_SIZE);
            buttonsSelection[index].visible = false;
            addChild(new ClickableWidget(buttonsSelection[index]));
        }, true);

        final int buttonClearX = (width - PANEL_WIDTH - SQUARE_SIZE * 4) / 2;
        final int buttonY = height - SQUARE_SIZE * 2;

        IDrawing.setPositionAndWidth(buttonClear, buttonClearX, buttonY, PANEL_WIDTH);
        buttonClear.visible = false;
        addChild(new ClickableWidget(buttonClear));

        IDrawing.setPositionAndWidth(buttonPrevPage, buttonClearX + PANEL_WIDTH, buttonY, SQUARE_SIZE);
        buttonPrevPage.visible = false;
        addChild(new ClickableWidget(buttonPrevPage));
        IDrawing.setPositionAndWidth(buttonNextPage, buttonClearX + PANEL_WIDTH + SQUARE_SIZE * 3, buttonY, SQUARE_SIZE);
        buttonNextPage.visible = false;
        addChild(new ClickableWidget(buttonNextPage));

        if (type != Type.RAILWAY_SIGN) {
            final DashboardListSelectorScreen screen;
            switch (type) {
                case STATION_NAME_ENTRANCE:
                    screen = new DashboardListSelectorScreen(this::onClose2, exitsForList, selectedIds, true, false, null);
                    break;
                case STATION_NAME_PLATE:
                    screen = new DashboardListSelectorScreen(this::onClose2, platformsForList, selectedIds, true, false, null);
                    break;
                case STATION_NAVIGATOR:
                    screen = new DashboardListSelectorScreen(this::onClose2, new ObjectImmutableList<>(routesForList), selectedIds, false, false, null);
                    break;
                default:
                    throw new IllegalStateException("Unknown enum type: " + type);
            }
            MinecraftClient.getInstance().openScreen(new Screen(screen));
        }
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        super.render(graphicsHolder, mouseX, mouseY, delta);

        for (int i = 0; i < signIds.length; i++) {
            if (signIds[i] != null) {
                RenderRailwaySign.drawSign(graphicsHolder, null, signPos, signIds[i], (width - SIGN_SIZE * length) / 2F + i * SIGN_SIZE, 0, SIGN_SIZE, RenderRailwaySign.getMaxWidth(signIds, i, false), RenderRailwaySign.getMaxWidth(signIds, i, true), selectedIds, Direction.UP, 0, (textureId, x, y, size, flipTexture) -> {
                    final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
                    guiDrawing.beginDrawingTexture(textureId);
                    guiDrawing.drawTexture(x, y, x + size, y + size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1);
                    guiDrawing.finishDrawingTexture();
                });
            }
        }

        if (editingIndex >= 0) {
            final int xOffsetSmall = (width - SIGN_BUTTON_SIZE * (columns * 4 + 3)) / 2 + SIGN_BUTTON_SIZE;
            final int xOffsetBig = xOffsetSmall + SIGN_BUTTON_SIZE * (columns + 1);

            loopSigns((index, x, y, isBig) -> {
                final String signId = allSignIds.get(index);
                final SignResource sign = RenderRailwaySign.getSign(signId);
                if (sign != null) {
                    final boolean moveRight = sign.hasCustomText && sign.getFlipCustomText();
                    RenderRailwaySign.drawSign(graphicsHolder, null, signPos, signId, (isBig ? xOffsetBig : xOffsetSmall) + x + (moveRight ? SIGN_BUTTON_SIZE * 2 : 0), BUTTON_Y_START + y, SIGN_BUTTON_SIZE, 2, 2, selectedIds, Direction.UP, 0, (textureId, x1, y1, size, flipTexture) -> {
                        final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
                        guiDrawing.beginDrawingTexture(sign.getTexture());
                        guiDrawing.drawTexture(x1, y1, x1 + size, y1 + size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1);
                        guiDrawing.finishDrawingTexture();
                    });
                }
            }, false);

            graphicsHolder.drawCenteredText(String.format("%s/%s", page + 1, totalPages), (width - PANEL_WIDTH - SQUARE_SIZE * 4) / 2 + PANEL_WIDTH + SQUARE_SIZE * 2, height - SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
        }
    }

    @Override
    public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
        setPage(page + (int) Math.signum(-amount));
        return super.mouseScrolled2(mouseX, mouseY, amount);
    }

    @Override
    public void onClose2() {
        RegistryClient.sendPacketToServer(new PacketUpdateRailwaySignConfig(signPos, selectedIds, signIds));
        super.onClose2();
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }

    @Override
    public void resize2(MinecraftClient client, int width, int height) {
        super.resize2(client, width, height);
        for (ButtonWidgetExtension button : buttonsEdit) {
            button.active = true;
        }
        for (ButtonWidgetExtension button : buttonsSelection) {
            button.visible = false;
        }
        editingIndex = -1;
    }

    protected int loopSigns(LoopSignsCallback loopSignsCallback, boolean ignorePage) {
        int pageCount = rows * columns;
        int indexSmall = 0;
        int indexBig = 0;
        int columnSmall = 0;
        int columnBig = 0;
        int rowSmall = 0;
        int rowBig = 0;
        int totalPagesSmallCount = 1;
        int totalPagesBigCount = 1;
        for (int i = 0; i < allSignIds.size(); i++) {
            final SignResource sign = RenderRailwaySign.getSign(allSignIds.get(i));
            final boolean isBig = sign != null && sign.hasCustomText;

            final boolean onPage = (isBig ? indexBig : indexSmall) / pageCount == page;
            buttonsSelection[i].visible = onPage;
            if (ignorePage || onPage) {
                loopSignsCallback.loopSignsCallback(i, (isBig ? columnBig * 3 : columnSmall) * SIGN_BUTTON_SIZE, (isBig ? rowBig : rowSmall) * SIGN_BUTTON_SIZE, isBig);
            }

            if (isBig) {
                columnBig++;
                if (totalPagesBigCount < 0) {
                    totalPagesBigCount = -totalPagesBigCount + 1;
                }
                if (columnBig >= columns) {
                    columnBig = 0;
                    rowBig++;
                    if (rowBig >= rows) {
                        rowBig = 0;
                        totalPagesBigCount = -totalPagesBigCount;
                    }
                }
                indexBig++;
            } else {
                columnSmall++;
                if (totalPagesSmallCount < 0) {
                    totalPagesSmallCount = -totalPagesSmallCount + 1;
                }
                if (columnSmall >= columns) {
                    columnSmall = 0;
                    rowSmall++;
                    if (rowSmall >= rows) {
                        rowSmall = 0;
                        totalPagesSmallCount = -totalPagesSmallCount;
                    }
                }
                indexSmall++;
            }
        }
        return Math.max(Math.abs(totalPagesBigCount), Math.abs(totalPagesSmallCount));
    }

    protected void edit(int editingIndex) {
        this.editingIndex = editingIndex;
        for (ButtonWidgetExtension button : buttonsEdit) {
            button.active = true;
        }
        buttonClear.visible = true;
        setPage(page);
        buttonsEdit[editingIndex].active = false;
    }

    protected void setNewSignId(@Nullable String newSignId) {
        if (editingIndex >= 0 && editingIndex < signIds.length) {
            signIds[editingIndex] = newSignId;
            final boolean isExitLetter = IRailwaySign.signIsExit(newSignId);
            final boolean isPlatform = IRailwaySign.signIsPlatform(newSignId);
            final boolean isLine = IRailwaySign.signIsLine(newSignId);
            final boolean isStation = IRailwaySign.signIsStation(newSignId);
            if ((isExitLetter || isPlatform || isLine || isStation)) {
                MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(this::onClose2, new ObjectImmutableList<>(isExitLetter ? exitsForList : isPlatform ? platformsForList : isLine ? routesForList : stationsForList), selectedIds, false, false, null)));
            }
        }
    }

    protected void setPage(int newPage) {
        page = MathHelper.clamp(newPage, 0, totalPages - 1);
        buttonPrevPage.visible = editingIndex >= 0 && page > 0;
        buttonNextPage.visible = editingIndex >= 0 && page < totalPages - 1;
    }

    @FunctionalInterface
    protected interface LoopSignsCallback
    {
        void loopSignsCallback(int index, int x, int y, boolean isBig);
    }

    public enum Type
    {
        RAILWAY_SIGN,
        STATION_NAME_ENTRANCE,
        STATION_NAME_PLATE,
        STATION_NAVIGATOR
    }
}
