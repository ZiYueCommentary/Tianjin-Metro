package ziyue.tjmetro.mod.screen;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.RenderRailwaySign;
import org.mtr.mod.resource.SignResource;
import org.mtr.mod.screen.TexturedButtonWidgetHelper;
import ziyue.tjmetro.mod.RegistryClient;
import ziyue.tjmetro.mod.block.BlockRailwaySignSingle;
import ziyue.tjmetro.mod.mixin.CustomResourceAccessor;
import ziyue.tjmetro.mod.packet.PacketUpdateRailwaySignConfig;
import ziyue.tjmetro.mod.packet.PacketUpdateRailwaySignSingleConfig;

import javax.annotation.Nullable;

import static ziyue.tjmetro.mod.screen.RailwaySignScreen.*;

/**
 * @author ZiYueCommentary
 * @see PacketUpdateRailwaySignConfig
 * @since 1.1.3
 */

public class RailwaySignSingleScreen extends ScreenExtension implements IGui
{
    protected int editingIndex;
    protected int page;
    protected int totalPages;
    protected int columns;
    protected int rows;

    protected final BlockPos signPos;
    protected final int index;
    protected String signId;
    protected final ObjectArrayList<String> allSignIds = new ObjectArrayList<>();

    protected final ButtonWidgetExtension buttonEdit;
    protected final ButtonWidgetExtension[] buttonsSelection;
    protected final ButtonWidgetExtension buttonClear;
    protected final TexturedButtonWidgetExtension buttonPrevPage;
    protected final TexturedButtonWidgetExtension buttonNextPage;

    public RailwaySignSingleScreen(BlockPos signPos, int signIndex) {
        super();
        editingIndex = -1;
        this.signPos = signPos;
        final ClientWorld world = MinecraftClient.getInstance().getWorldMapped();

        allSignIds.addAll(CustomResourceAccessor.tjmetro$getSigns().stream()
                .filter(resource -> !resource.hasCustomText)
                .map(resource -> resource.signId).toList());

        if (world != null) {
            final BlockEntity entity = world.getBlockEntity(signPos);
            if (entity != null && entity.data instanceof BlockRailwaySignSingle.BlockEntity entity1) {
                this.index = signIndex;
                signId = entity1.getSignId(signIndex);
            } else {
                this.index = -1;
                signId = null;
            }
        } else {
            throw new NullPointerException("World is null");
        }

        buttonEdit = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("selectWorld.edit"), button -> edit(signIndex));

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

        IDrawing.setPositionAndWidth(buttonEdit, (width - SIGN_SIZE) / 2, SIGN_SIZE, SIGN_SIZE);
        addChild(new ClickableWidget(buttonEdit));

        columns = Math.max((width - SIGN_BUTTON_SIZE * 2) / SIGN_BUTTON_SIZE, 1);
        rows = Math.max((height - SIGN_SIZE - SQUARE_SIZE * 4) / SIGN_BUTTON_SIZE, 1);

        final int xOffsetSmall = (width - SIGN_BUTTON_SIZE * columns) / 2;

        totalPages = loopSigns((index, x, y) -> {
            IDrawing.setPositionAndWidth(buttonsSelection[index], xOffsetSmall + x, BUTTON_Y_START + y, SIGN_BUTTON_SIZE);
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
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        super.render(graphicsHolder, mouseX, mouseY, delta);

        if (signId != null) {
            RenderRailwaySign.drawSign(graphicsHolder, null, signPos, signId, (width - SIGN_SIZE) / 2F, 0, SIGN_SIZE, SIGN_SIZE, SIGN_SIZE, new LongAVLTreeSet(), Direction.UP, 0, (textureId, x, y, size, flipTexture) -> {
                final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
                guiDrawing.beginDrawingTexture(textureId);
                guiDrawing.drawTexture(x, y, x + size, y + size, flipTexture ? 1 : 0, 0, flipTexture ? 0 : 1, 1);
                guiDrawing.finishDrawingTexture();
            });
        }

        if (editingIndex >= 0) {
            final int xOffsetSmall = (width - SIGN_BUTTON_SIZE * columns) / 2;

            loopSigns((index, x, y) -> {
                final String signId = allSignIds.get(index);
                final SignResource sign = RenderRailwaySign.getSign(signId);
                if (sign != null) {
                    final boolean moveRight = sign.hasCustomText && sign.getFlipCustomText();
                    RenderRailwaySign.drawSign(graphicsHolder, null, signPos, signId, xOffsetSmall + x + (moveRight ? SIGN_BUTTON_SIZE * 2 : 0), BUTTON_Y_START + y, SIGN_BUTTON_SIZE, 2, 2, new LongAVLTreeSet(), Direction.UP, 0, (textureId, x1, y1, size, flipTexture) -> {
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
        RegistryClient.sendPacketToServer(new PacketUpdateRailwaySignSingleConfig(signPos, index, signId));
        super.onClose2();
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }

    @Override
    public void resize2(MinecraftClient client, int width, int height) {
        super.resize2(client, width, height);
        buttonEdit.active = true;
        for (ButtonWidgetExtension button : buttonsSelection) {
            button.visible = false;
        }
        editingIndex = -1;
    }

    protected int loopSigns(LoopSignsCallback loopSignsCallback, boolean ignorePage) {
        int pageCount = rows * columns;
        int columnSmall = 0;
        int rowSmall = 0;
        for (int i = 0; i < allSignIds.size(); i++) {
            final boolean onPage = i / pageCount == page;
            buttonsSelection[i].visible = onPage;
            if (ignorePage || onPage) {
                loopSignsCallback.loopSignsCallback(i, columnSmall * SIGN_BUTTON_SIZE, rowSmall * SIGN_BUTTON_SIZE);
            }

            columnSmall++;
            if (columnSmall >= columns) {
                columnSmall = 0;
                rowSmall++;
                if (rowSmall >= rows) {
                    rowSmall = 0;
                }
            }
        }
        return Math.max((allSignIds.size() + pageCount - 1) / pageCount, 1);
    }

    protected void edit(int editingIndex) {
        this.editingIndex = editingIndex;
        buttonClear.visible = true;
        setPage(page);
        buttonEdit.active = false;
    }

    protected void setNewSignId(@Nullable String newSignId) {
        signId = newSignId;
    }

    protected void setPage(int newPage) {
        page = MathHelper.clamp(newPage, 0, totalPages - 1);
        buttonPrevPage.visible = editingIndex >= 0 && page > 0;
        buttonNextPage.visible = editingIndex >= 0 && page < totalPages - 1;
    }

    @FunctionalInterface
    protected interface LoopSignsCallback
    {
        void loopSignsCallback(int index, int x, int y);
    }
}
