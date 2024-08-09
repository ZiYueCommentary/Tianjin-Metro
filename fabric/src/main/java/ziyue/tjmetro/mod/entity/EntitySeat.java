package ziyue.tjmetro.mod.entity;

import org.mtr.mapping.holder.Entity;
import org.mtr.mapping.holder.EntityType;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.EntityExtension;
import ziyue.tjmetro.mod.EntityTypes;

/**
 * @author ZiYueCommentary
 * @see ziyue.tjmetro.mod.render.RenderSeat
 * @since 1.0.0-beta-1
 */

public class EntitySeat extends EntityExtension
{
    public EntitySeat(World world, double x, double y, double z) {
        this(EntityTypes.SEAT.get(), world);
        this.setPosition2(x, y, z);
    }

    public EntitySeat(EntityType<?> type, World world) {
        super(type, world);
        this.setNoGravity2(true);
        this.setInvisible2(true);
    }

    @Override
    protected void initDataTracker2() {
    }

    @Override
    public boolean doesNotCollide2(double offsetX, double offsetY, double offsetZ) {
        return true;
    }

    @Override
    protected void removePassenger2(Entity passenger) {
        super.removePassenger2(passenger);
        if (!getEntityWorld2().isClient()) this.kill2();
    }
}
