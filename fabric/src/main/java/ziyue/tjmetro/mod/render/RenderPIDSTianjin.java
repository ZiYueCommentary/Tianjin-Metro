package ziyue.tjmetro.mod.render;

import org.mtr.core.operation.ArrivalResponse;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongCollection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.*;
import org.mtr.mod.data.ArrivalsCacheClient;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.QueuedRenderLayer;
import ziyue.tjmetro.mod.Reference;
import ziyue.tjmetro.mod.TianjinMetro;
import ziyue.tjmetro.mod.block.BlockPIDSTianjin;
import ziyue.tjmetro.mod.client.DynamicTextureCache;
import ziyue.tjmetro.mod.client.IDrawingExtension;
import ziyue.tjmetro.mod.client.ScrollingText;

import static org.mtr.mod.render.RenderPIDS.SWITCH_LANGUAGE_TICKS;

/**
 * @author ZiYueCommentary
 * @see org.mtr.mod.render.RenderPIDS
 * @see BlockPIDSTianjin
 * @since 1.0.0
 */

public class RenderPIDSTianjin<T extends BlockPIDSTianjin.BlockEntity> extends BlockEntityRenderer<T> implements IGui, Utilities
{
    public static final float LEFT_TEXT_X_CENTER = 42.45F;
    public static final float LEFT_TEXT_MAX_WIDTH = 78.55F;

    protected final float maxHeight;
    protected final float maxWidth;
    protected final boolean rotate90;
    protected final float textPadding;
    protected final ScrollingText scrollingText = new ScrollingText(158F, 47, 4, true);
    protected BlockPIDSTianjin.Advertisement advertisement = null;
    protected int categoryIndex = 0;
    protected int advertisementIndex = 0;

    public RenderPIDSTianjin(Argument dispatcher, float maxHeight, int maxWidth, boolean rotate90, float textPadding) {
        super(dispatcher);
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
        this.rotate90 = rotate90;
        this.textPadding = textPadding;
    }

    @Override
    public final void render(T entity, float tickDelta, GraphicsHolder graphicsHolder, int light, int overlay) {
        final World world = entity.getWorld2();
        if (world == null) return;

        final BlockPos blockPos = entity.getPos2();
        if (!BlockPIDSTianjin.canStoreData(world, blockPos)) return;

        final Direction facing = IBlock.getStatePropertySafe(world, blockPos, DirectionHelper.FACING);

        if (entity.getPlatformIds().isEmpty()) {
            final LongArrayList platformIds = new LongArrayList();
            InitClient.findClosePlatform(entity.getPos2().down(4), 5, platform -> platformIds.add(platform.getId()));
            getArrivalsAndRender(entity, blockPos, facing, platformIds);
        } else {
            getArrivalsAndRender(entity, blockPos, facing, entity.getPlatformIds());
        }
    }

    private void getArrivalsAndRender(T entity, BlockPos blockPos, Direction facing, LongCollection platformIds) {
        final ObjectArrayList<ArrivalResponse> arrivalResponseList = ArrivalsCacheClient.INSTANCE.requestArrivals(platformIds);
        MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
            render(entity, blockPos, facing, arrivalResponseList, graphicsHolder, offset);
            render(entity, blockPos.offset(facing), facing.getOpposite(), arrivalResponseList, graphicsHolder, offset);
        });
    }

    private void render(T entity, BlockPos blockPos, Direction facing, ObjectArrayList<ArrivalResponse> arrivalResponseList, GraphicsHolder graphicsHolder, Vector3d offset) {
        final float scale = 320 / maxHeight * textPadding;
        int arrivalIndex = entity.getDisplayPage() * 2;

        graphicsHolder.push();
        graphicsHolder.translate(blockPos.getX() - offset.getXMapped() + 0.5, blockPos.getY() - offset.getYMapped() + 0.85, blockPos.getZ() - offset.getZMapped() + 0.5);
        graphicsHolder.rotateYDegrees((rotate90 ? 90 : 0) - facing.asRotation());
        graphicsHolder.rotateZDegrees(180);
        graphicsHolder.translate(-0.48, 0, -0.48);
        graphicsHolder.scale(1 / scale, 1 / scale, 1 / scale);
        graphicsHolder.rotateXDegrees(22.5F);
        graphicsHolder.translate(0, 0.05, 0);
        renderTexture(graphicsHolder, new Identifier(Reference.MOD_ID, "textures/block/pids_tianjin.png"), 251F, 115F, facing);
        graphicsHolder.translate(0, 0, -0.1);
        renderText(graphicsHolder, RenderTimeDisplay.getFormattedTime(entity.getWorld2().getLunarTime()), HorizontalAlignment.CENTER, VerticalAlignment.CENTER, LEFT_TEXT_X_CENTER, 106.5F, maxWidth * scale / 16, 0.5F, ARGB_WHITE);

        final int languageTicks = (int) Math.floor(InitClient.getGameTick()) / SWITCH_LANGUAGE_TICKS;

        for (int i = 0; i < 2; i++) {
            final ArrivalResponse arrivalResponse = Utilities.getElement(arrivalResponseList, arrivalIndex + i);
            final String[] destinationSplit;
            final int languageIndex;
            if (arrivalResponse == null) {
                continue;
            }

            final String[] tempDestinationSplit = arrivalResponse.getDestination().split("\\|");
            if (arrivalResponse.getRouteNumber().isEmpty()) {
                destinationSplit = tempDestinationSplit;
            } else {
                final String[] tempNumberSplit = arrivalResponse.getRouteNumber().split("\\|");
                int destinationIndex = 0;
                int numberIndex = 0;
                final ObjectArrayList<String> newDestinations = new ObjectArrayList<>();
                while (true) {
                    final String newDestination = String.format("%s %s", tempNumberSplit[numberIndex % tempNumberSplit.length], tempDestinationSplit[destinationIndex % tempDestinationSplit.length]);
                    if (newDestinations.contains(newDestination)) {
                        break;
                    } else {
                        newDestinations.add(newDestination);
                    }
                    destinationIndex++;
                    numberIndex++;
                }
                destinationSplit = newDestinations.toArray(new String[0]);
            }
            final int messageCount = destinationSplit.length;
            languageIndex = languageTicks % messageCount;

            final long arrival = (arrivalResponse.getArrival() - ArrivalsCacheClient.INSTANCE.getMillisOffset() - System.currentTimeMillis()) / 1000;
            final String destination = destinationSplit[languageIndex];
            final boolean isCjk = IGui.isCjk(destination);
            final String destinationFormatted;
            final float yOffset = i * 45F;

            switch (arrivalResponse.getCircularState()) {
                case CLOCKWISE:
                    destinationFormatted = (isCjk ? TranslationProvider.GUI_MTR_CLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_CLOCKWISE_VIA).getString(destination);
                    break;
                case ANTICLOCKWISE:
                    destinationFormatted = (isCjk ? TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA_CJK : TranslationProvider.GUI_MTR_ANTICLOCKWISE_VIA).getString(destination);
                    break;
                default:
                    destinationFormatted = isCjk ? TextHelper.translatable("gui.tjmetro.bound_for_pids_cjk", destination).getString() : TextHelper.translatable("gui.tjmetro.bound_for_pids", destination).getString();
                    break;
            }

            renderText(graphicsHolder, isCjk ? TextHelper.translatable(i == 0 ? "gui.tjmetro.this_train_cjk" : "gui.tjmetro.next_train_cjk").getString() : TextHelper.translatable(i == 0 ? "gui.tjmetro.this_train" : "gui.tjmetro.next_train").getString(), HorizontalAlignment.CENTER, VerticalAlignment.TOP, LEFT_TEXT_X_CENTER, 8F + yOffset, LEFT_TEXT_MAX_WIDTH, 1.5F, ARGB_WHITE);
            renderText(graphicsHolder, destinationFormatted, HorizontalAlignment.CENTER, VerticalAlignment.BOTTOM, LEFT_TEXT_X_CENTER, 45F + yOffset, LEFT_TEXT_MAX_WIDTH, 1.3F, ARGB_WHITE);
            if (arrival <= 15) {
                final String textKey = (arrival < 0 ? "gui.tjmetro.arrived" : "gui.tjmetro.arriving") + (isCjk ? "_cjk" : "");
                renderText(graphicsHolder, TextHelper.translatable(textKey).getString(), HorizontalAlignment.CENTER, VerticalAlignment.TOP, LEFT_TEXT_X_CENTER, 21.5F + yOffset, LEFT_TEXT_MAX_WIDTH, 0.6F, 0xFFEFEF00);
            } else {
                final boolean isMinute = arrival > 60;
                String arrivalTime = String.valueOf(isMinute ? arrival / 60 : arrival);
                String arrivalUnit = TextHelper.translatable((isMinute ? "gui.tjmetro.minute" : "gui.tjmetro.second") + (isCjk ? "_cjk" : "")).getString();
                float arrivalTimeWidth = IDrawingExtension.stringWidthWithFont(graphicsHolder, arrivalTime, 0.4F, 1, false).leftFloat();
                float arrivalUnitWidth = IDrawingExtension.stringWidthWithFont(graphicsHolder, arrivalUnit, 1.5F, 1, false).leftFloat();
                renderText(graphicsHolder, arrivalTime, HorizontalAlignment.LEFT, VerticalAlignment.CENTER, LEFT_TEXT_X_CENTER - (arrivalTimeWidth + arrivalUnitWidth) / 2, 30F + yOffset, LEFT_TEXT_MAX_WIDTH, 0.4F, 0xFFEFEF00);
                renderText(graphicsHolder, arrivalUnit, HorizontalAlignment.RIGHT, VerticalAlignment.CENTER, LEFT_TEXT_X_CENTER + (arrivalTimeWidth + arrivalUnitWidth) / 2, 30F + yOffset, LEFT_TEXT_MAX_WIDTH, 1.5F, ARGB_WHITE);
            }
        }

        if (entity.getCategories().isEmpty()) {
            graphicsHolder.pop();
            return;
        }

        graphicsHolder.translate(86.7, 3.38, 0);
        if (categoryIndex >= entity.getCategories().size() || !BlockPIDSTianjin.CATEGORIES.containsKey(entity.getCategories().getLong(categoryIndex))) {
            TianjinMetro.LOGGER.warn("Invalid advertisement category id: {} at {}. Skipping!", entity.getCategories().getLong(categoryIndex), blockPos.toShortString());
            categoryIndex = (categoryIndex + 1) % entity.getCategories().size();
            advertisementIndex = 0;
        }
        BlockPIDSTianjin.Advertisement newAd = BlockPIDSTianjin.CATEGORIES.get(entity.getCategories().getLong(categoryIndex)).get(advertisementIndex);
        if (advertisement != newAd) {
            advertisement = newAd;
            // The space down below is a hacky way to deal with the error of float.
            scrollingText.changeImage(() -> DynamicTextureCache.instance.getPlainText("   " + advertisement.getText().getString() + "   ", 0xFF1A1D46, ARGB_WHITE));
        }
        renderTexture(graphicsHolder, advertisement.getImage(), 161F, 88.3F, facing);
        graphicsHolder.translate(1.5F, 75.3F, 0);
        boolean shouldSwitch = scrollingText.scrollText(graphicsHolder, facing);
        if (shouldSwitch) nextSlide(entity);

        graphicsHolder.pop();
    }

    protected void nextSlide(T entity) {
        if (advertisementIndex + 1 >= BlockPIDSTianjin.CATEGORIES.get(entity.getCategories().getLong(categoryIndex)).size()) {
            categoryIndex = (categoryIndex + 1) % entity.getCategories().size();
            advertisementIndex = 0;
            return;
        }
        advertisementIndex = (advertisementIndex + 1) % BlockPIDSTianjin.CATEGORIES.get(entity.getCategories().getLong(categoryIndex)).size();
    }

    protected void renderText(GraphicsHolder graphicsHolder, String text, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, float x, float y, float availableWidth, float scale, int color) {
        graphicsHolder.push();
        IDrawingExtension.drawStringWithFont(graphicsHolder, text, horizontalAlignment, verticalAlignment, horizontalAlignment, x, y, availableWidth, -1, scale, color, color, 1, false, GraphicsHolder.getDefaultLight(), false, null);
        graphicsHolder.pop();
    }

    protected void renderTexture(GraphicsHolder graphicsHolder, Identifier identifier, float width, float height, Direction facing) {
        graphicsHolder.push();
        graphicsHolder.createVertexConsumer(RenderLayer.getText(identifier));
        graphicsHolder.drawTextureInWorld(0, height, 0, width, height, 0, width, 0, 0, 0, 0, 0, 0, 0, 1, 1, facing, ARGB_WHITE, 0xF000F0);
        graphicsHolder.pop();
    }
}
