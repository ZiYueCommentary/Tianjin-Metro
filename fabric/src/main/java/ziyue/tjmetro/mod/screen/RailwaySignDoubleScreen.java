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
import ziyue.tjmetro.mod.block.BlockRailwaySignWallDouble;
import ziyue.tjmetro.mod.block.base.BlockRailwaySignBase;
import ziyue.tjmetro.mod.block.base.IRailwaySign;
import ziyue.tjmetro.mod.packet.PacketUpdateRailwaySignDoubleConfig;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see PacketUpdateRailwaySignDoubleConfig
 * @since 1.0.0-beta-1
 */

public class RailwaySignDoubleScreen extends ScreenExtension implements IGui
{
    protected int line;
    protected int editingIndex;
    protected int page;
    protected int totalPages;
    protected int columns;
    protected int rows;

    protected final BlockPos signPos;
    protected final boolean isRailwaySign;
    protected final int length;
    protected final String[][] signIds;
    protected final List<LongAVLTreeSet> selectedIds;
    protected final ObjectImmutableList<DashboardListItem> exitsForList;
    protected final ObjectImmutableList<DashboardListItem> platformsForList;
    protected final ObjectArraySet<DashboardListItem> routesForList;
    protected final ObjectArraySet<DashboardListItem> stationsForList;
    protected final ObjectArrayList<String> allSignIds = new ObjectArrayList<>();

    protected final ButtonWidgetExtension[][] buttonsEdit;
    protected final ButtonWidgetExtension[] buttonsSelection;
    protected final ButtonWidgetExtension buttonClear;
    protected final TexturedButtonWidgetExtension buttonPrevPage;
    protected final TexturedButtonWidgetExtension buttonNextPage;

    protected static final int SIGN_SIZE = 32;
    protected static final int SIGN_BUTTON_SIZE = 16;
    protected static final int BUTTON_Y_START = (SQUARE_SIZE + SIGN_SIZE) * 2 + SIGN_BUTTON_SIZE / 2;

    public RailwaySignDoubleScreen(BlockPos signPos) {
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
            platformsForList = PIDSTianjinConfigScreen.getPlatformsForList(new ObjectArrayList<>(station.savedRails));

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
            if (entity != null && entity.data instanceof BlockRailwaySignWallDouble.BlockEntity entity1) {
                signIds = entity1.getSignIds();
                selectedIds = entity1.getSelectedIds();
                isRailwaySign = true;
            } else {
                signIds = new String[2][0];
                selectedIds = new ArrayList<>();
                selectedIds.add(new LongAVLTreeSet());
                selectedIds.add(new LongAVLTreeSet());
                isRailwaySign = false;
            }
            final Block block = world.getBlockState(signPos).getBlock();
            if (block.data instanceof BlockRailwaySignBase block1) {
                length = block1.length;
            } else {
                length = 0;
            }
        } else {
            length = 0;
            signIds = new String[0][0];
            selectedIds = new ArrayList<>();
            selectedIds.add(new LongAVLTreeSet());
            selectedIds.add(new LongAVLTreeSet());
            isRailwaySign = false;
        }

        buttonsEdit = new ButtonWidgetExtension[2][length];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < buttonsEdit[i].length; j++) {
                final int line = i;
                final int index = j;
                buttonsEdit[i][j] = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("selectWorld.edit"), button -> edit(line, index));
            }
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

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < buttonsEdit[i].length; j++) {
                IDrawing.setPositionAndWidth(buttonsEdit[i][j], (width - SIGN_SIZE * length) / 2 + j * SIGN_SIZE, i * (SIGN_SIZE + SQUARE_SIZE) + SIGN_SIZE, SIGN_SIZE);
                addChild(new ClickableWidget(buttonsEdit[i][j]));
            }
        }

        columns = Math.max((width - SIGN_BUTTON_SIZE * 3) / (SIGN_BUTTON_SIZE * 8) * 2, 1);
        rows = Math.max((height - SIGN_SIZE * 2 - SQUARE_SIZE * 4 - SQUARE_SIZE / 2) / SIGN_BUTTON_SIZE, 1);

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

        if (!isRailwaySign) {
            MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(this::onClose2, platformsForList, selectedIds.get(line), true, false, null)));
        }
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        super.render(graphicsHolder, mouseX, mouseY, delta);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < signIds[i].length; j++) {
                if (signIds[i][j] != null) {
                    RenderRailwaySign.drawSign(graphicsHolder, null, signPos, signIds[i][j], (width - SIGN_SIZE * length) / 2F + j * SIGN_SIZE, i * (SQUARE_SIZE + SIGN_SIZE), SIGN_SIZE, RenderRailwaySign.getMaxWidth(signIds[i], j, false), RenderRailwaySign.getMaxWidth(signIds[i], j, true), selectedIds.get(i), Direction.UP, 0, (textureId, x, y, size, flipTexture) -> {
                        final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
                        guiDrawing.beginDrawingTexture(textureId);
                        guiDrawing.drawTexture(x, y, x + size, y + size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1);
                        guiDrawing.finishDrawingTexture();
                    });
                }
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
                    RenderRailwaySign.drawSign(graphicsHolder, null, signPos, signId, (isBig ? xOffsetBig : xOffsetSmall) + x + (moveRight ? SIGN_BUTTON_SIZE * 2 : 0), BUTTON_Y_START + y, SIGN_BUTTON_SIZE, 2, 2, selectedIds.get(line), Direction.UP, 0, (textureId, x1, y1, size, flipTexture) -> {
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
        RegistryClient.sendPacketToServer(new PacketUpdateRailwaySignDoubleConfig(signPos, selectedIds, signIds));
        super.onClose2();
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }

    @Override
    public void resize2(MinecraftClient client, int width, int height) {
        super.resize2(client, width, height);
        for (int i = 0; i < 2; i++) {
            for (ButtonWidgetExtension button : buttonsEdit[i]) {
                button.active = true;
            }
        }
        for (ButtonWidgetExtension button : buttonsSelection) {
            button.visible = false;
        }
        editingIndex = -1;
    }

    protected int loopSigns(RailwaySignScreen.LoopSignsCallback loopSignsCallback, boolean ignorePage) {
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

    protected void edit(int line, int editingIndex) {
        this.line = line;
        this.editingIndex = editingIndex;
        for (int i = 0; i < 2; i++) {
            for (ButtonWidgetExtension button : buttonsEdit[i]) {
                button.active = true;
            }
        }
        buttonClear.visible = true;
        setPage(page);
        buttonsEdit[line][editingIndex].active = false;
    }

    protected void setNewSignId(@Nullable String newSignId) {
        if (editingIndex >= 0 && editingIndex < signIds[0].length) {
            signIds[line][editingIndex] = newSignId;
            final boolean isExitLetter = IRailwaySign.signIsExit(newSignId);
            final boolean isPlatform = IRailwaySign.signIsPlatform(newSignId);
            final boolean isLine = IRailwaySign.signIsLine(newSignId);
            final boolean isStation = IRailwaySign.signIsStation(newSignId);
            if ((isExitLetter || isPlatform || isLine || isStation)) {
                MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(this::onClose2, new ObjectImmutableList<>(isExitLetter ? exitsForList : isPlatform ? platformsForList : isLine ? routesForList : stationsForList), selectedIds.get(line), false, false, null)));
            }
        }
    }

    protected void setPage(int newPage) {
        page = MathHelper.clamp(newPage, 0, totalPages - 1);
        buttonPrevPage.visible = editingIndex >= 0 && page > 0;
        buttonNextPage.visible = editingIndex >= 0 && page < totalPages - 1;
    }
}
