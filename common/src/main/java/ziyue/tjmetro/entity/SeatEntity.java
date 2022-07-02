package ziyue.tjmetro.entity;

import mtr.mappings.EntityRendererMapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import ziyue.tjmetro.EntityTypes;

public class SeatEntity extends Entity
{
    public SeatEntity(Level world) {
        super(EntityTypes.BENCH.get(), world);
        this.noCulling = true;
    }

    public SeatEntity(Level level, double d, double e, double f) {
        this(level);
        this.setPos(d, e + (double)((1.0F - this.getBbHeight()) / 2.0F), f);
        this.setDeltaMovement(Vec3.ZERO);
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
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        return super.interact(player, interactionHand);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public void tick() {
        if(this.getPassengers().isEmpty()) remove();
        super.tick();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, Block.getId(this.getBlockStateOn()));
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
