package ziyue.tjmetro.entity;

import mtr.entity.EntityBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntitySeat extends Entity
{

    public EntitySeat(EntityType<?> entityType, Level level) {
        super(entityType, level);
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
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected void removePassenger(Entity entity) {
        super.removePassenger(entity);
        if (!getLevel().isClientSide) this.kill();
    }
}