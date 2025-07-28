package ziyue.tjmetro.mod.screen;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Icons;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.screen.DashboardListItem;
import org.mtr.mod.screen.DashboardListSelectorScreen;
import ziyue.tjmetro.mod.RegistryClient;
import ziyue.tjmetro.mod.block.BlockPIDSTianjin;
import ziyue.tjmetro.mod.packet.PacketUpdatePIDSAdsConfig;

import java.util.List;

public class CategorySelectorScreen extends DashboardListSelectorScreen implements Icons
{
    private final ButtonWidgetExtension buttonOpenTutorial;
    private final BlockPIDSTianjin.BlockEntity entity;
    protected final List<BlockPIDSTianjin.Category> categories;

    public CategorySelectorScreen(BlockPIDSTianjin.BlockEntity entity, ScreenExtension previousScreenExtension) {
        super(CategoryForList.getCategoriesForList(), entity.getCategories(), false, true, previousScreenExtension);
        this.buttonOpenTutorial = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("button.tjmetro.open_tutorial"), button -> Util.getOperatingSystem().open("")); // TODO github wiki
        this.entity = entity;
        this.categories = BlockPIDSTianjin.CATEGORIES.values().stream().toList();
    }

    @Override
    protected void init2() {
        super.init2();
        final int spareSpace = Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2);
        availableList.x = SQUARE_SIZE * 2 + spareSpace;
        selectedList.x = SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH;
        IDrawing.setPositionAndWidth(buttonDone, SQUARE_SIZE * 2 + spareSpace, height - SQUARE_SIZE * 2, PANEL_WIDTH);
        IDrawing.setPositionAndWidth(buttonOpenTutorial, SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH, height - SQUARE_SIZE * 2, PANEL_WIDTH);
        addChild(new ClickableWidget(buttonOpenTutorial));
    }

    @Override
    public void renderAdditional(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        final int spareSpace = Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2);
        graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_AVAILABLE.getMutableText(), SQUARE_SIZE * 2 + spareSpace + PANEL_WIDTH / 2, SQUARE_SIZE, ARGB_WHITE);
        graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_SELECTED.getMutableText(), SQUARE_SIZE * 3 + spareSpace + PANEL_WIDTH * 3 / 2, SQUARE_SIZE, ARGB_WHITE);

        final int index = availableList.getHoverItemIndex();
        if (index < 0 || index >= categories.size()) return;
        final BlockPIDSTianjin.Category category = categories.get(index);
        int y = SQUARE_SIZE;
        y = drawWrappedText(graphicsHolder, category.name, y, ARGB_WHITE);
        final String description = category.description.getString();
        if (!description.isEmpty()) {
            for (final String text : description.split("[|\n]")) {
                y = drawWrappedText(graphicsHolder, TextHelper.literal(text), y, ARGB_LIGHT_GRAY);
            }
        }
    }

    @Override
    public void onClose2() {
        RegistryClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdatePIDSAdsConfig(entity.getPos2(), new LongArrayList(selectedIds.stream().toList())));
        super.onClose2();
    }

    protected int drawWrappedText(GraphicsHolder graphicsHolder, MutableText component, int y, int color) {
        final List<OrderedText> splitText = GraphicsHolder.wrapLines(component, Math.max(0, width - SQUARE_SIZE * 4 - PANEL_WIDTH * 2));
        int newY = y;
        for (final OrderedText formattedCharSequence : splitText) {
            final int nextY = newY + TEXT_HEIGHT + 2;
            if (nextY > height - SQUARE_SIZE - TEXT_HEIGHT) {
                graphicsHolder.drawText("...", SQUARE_SIZE, newY, color, false, GraphicsHolder.getDefaultLight());
                return height;
            } else {
                graphicsHolder.drawText(formattedCharSequence, SQUARE_SIZE, newY, color, false, GraphicsHolder.getDefaultLight());
            }
            newY = nextY;
        }
        return newY + TEXT_PADDING;
    }

    public static class CategoryForList extends DashboardListItem
    {
        public CategoryForList(BlockPIDSTianjin.Category category) {
            super(category.id, category.name.getString(), ARGB_BLACK | category.color.getRGB());
        }

        public static ObjectImmutableList<DashboardListItem> getCategoriesForList() {
            final ObjectArrayList<CategoryForList> categories = new ObjectArrayList<>();
            BlockPIDSTianjin.CATEGORIES.values().forEach(category -> categories.add(new CategoryForList(category)));
            return new ObjectImmutableList<>(categories);
        }
    }
}
