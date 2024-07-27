package ziyue.tjmetro.mod.render;

import org.mtr.mapping.holder.Frustum;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.EntityRenderer;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.data.IGui;
import ziyue.tjmetro.mod.entity.EntitySeat;

/**
 * @author ZiYueCommentary
 * @see EntitySeat
 * @see ziyue.tjmetro.mod.block.BlockBench
 * @since 1.0.0-beta-1
 */

public class RenderSeat extends EntityRenderer<EntitySeat> implements IGui
{
    public RenderSeat(Argument argument) {
        super(argument);
    }

    @Override
    public void render(EntitySeat entitySeat, float v, float v1, GraphicsHolder graphicsHolder, int i) {
    }

    @Override
    public Identifier getTexture2(EntitySeat entitySeat) {
        return null;
    }

    @Override
    public boolean shouldRender2(EntitySeat entity, Frustum frustum, double x, double y, double z) {
        return false;
    }
}
