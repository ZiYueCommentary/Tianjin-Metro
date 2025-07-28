package ziyue.tjmetro.mod.client;

import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.RenderLayer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.RouteMapGenerator;
import org.mtr.mod.data.IGui;

import java.util.function.Supplier;

/**
 * @author ZiYueCommentary
 * @see org.mtr.mod.client.ScrollingText
 * @since 1.0.0
 */

public class ScrollingText implements IGui
{
    protected float ticksOffset;
    public Supplier<DynamicTextureCache.DynamicResource> imageSupplier;

    protected final double availableWidth;
    protected final double availableHeight;
    protected final int scrollSpeed;
    protected final boolean isFullPixel;
    public static final float EPSILON = 0.03F;

    public ScrollingText(double availableWidth, double availableHeight, int scrollSpeed, boolean isFullPixel) {
        this.availableWidth = availableWidth;
        this.availableHeight = availableHeight;
        this.scrollSpeed = scrollSpeed;
        this.isFullPixel = isFullPixel;
    }

    public void changeImage(Supplier<DynamicTextureCache.DynamicResource> imageSupplier) {
        if (this.imageSupplier != imageSupplier) {
            this.imageSupplier = imageSupplier;
            ticksOffset = InitClient.getGameTick();
        }
    }

    public boolean scrollText(GraphicsHolder graphicsHolder, Direction facing) {
        if (imageSupplier != null) {
            graphicsHolder.push();
            final int pixelScale = isFullPixel ? 1 : RouteMapGenerator.PIXEL_SCALE;
            final double scale = availableHeight / imageSupplier.get().height;
            final int widthSteps = (int) Math.floor(availableWidth / scale / pixelScale);
            final int imageSteps = imageSupplier.get().width / pixelScale;
            final int totalSteps = widthSteps + imageSteps;
            final int step = Math.round((InitClient.getGameTick() - ticksOffset) * scrollSpeed) % totalSteps;
            final double width = Math.min(Math.min(availableWidth, imageSupplier.get().width * scale), Math.min(step * pixelScale * scale, (totalSteps - step) * pixelScale * scale));
            final float u1 = Math.max((float) (step - widthSteps) / imageSteps, 0);
            final float u2 = Math.min((float) step / imageSteps, 1);
            graphicsHolder.createVertexConsumer(RenderLayer.getText(imageSupplier.get().identifier));
            IDrawing.drawTexture(graphicsHolder, (float) (Math.max(widthSteps - step, 0) * scale * pixelScale), 0, (float) width, (float) availableHeight, u1, 0, u2, 1, facing, ARGB_WHITE, 0xF000F0);
            graphicsHolder.pop();
            return (1.0F - u1) <= EPSILON && (1.0F - u2) <= EPSILON;
        }
        return true;
    }
}