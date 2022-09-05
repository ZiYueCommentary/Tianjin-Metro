package ziyue.tjmetro.entity;

import mtr.Registry;
import mtr.mappings.EntityRendererMapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import ziyue.tjmetro.EntityTypes;

//todo fix bug
public class SeatEntity extends Entity
{
    public SeatEntity(Level world) {
        super(EntityTypes.BENCH.get(), world);
        this.noCulling = true;
    }

    public SeatEntity(Level level, double d, double e, double f) {
        this(level);
        this.setPos(d, e + (double) ((1.0F - this.getBbHeight()) / 2.0F), f);
        this.xo = d;
        this.yo = e;
        this.zo = f;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void tick() {
        if (this.getPassengers().isEmpty()) remove();
        super.tick();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return Registry.createAddEntityPacket(this);
    }

    public static class RenderSeat extends EntityRendererMapper<SeatEntity>
    {
        public RenderSeat(Object parameter) {
            super(parameter);
        }

        @Override
        public ResourceLocation getTextureLocation(SeatEntity entity) {
            return null;
        }
    }
}
