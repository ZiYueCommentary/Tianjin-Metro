package ziyue.tjmetro.entity.base;

import mtr.mappings.BlockEntityClientSerializableMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * The container block entity with extended <b>BlockEntityClientSerializableMapper</b>.
 *
 * @author ZiYueCommentary
 * @see net.minecraft.world.level.block.entity.BaseContainerBlockEntity
 * @see BlockEntityClientSerializableMapper
 * @since beta-1
 */

public abstract class BaseContainerBlockEntityMapper extends BlockEntityClientSerializableMapper implements Container, MenuProvider, Nameable
{
    protected LockCode lockKey;
    protected Component name;

    protected BaseContainerBlockEntityMapper(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        this.lockKey = LockCode.NO_LOCK;
    }

    @Override
    public void readCompoundTag(CompoundTag compoundTag) {
        super.readCompoundTag(compoundTag);
        this.lockKey = LockCode.fromTag(compoundTag);
        if (compoundTag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(compoundTag.getString("CustomName"));
        }
    }

    @Override
    public void writeCompoundTag(CompoundTag compoundTag) {
        super.writeCompoundTag(compoundTag);
        this.lockKey.addToTag(compoundTag);
        if (this.name != null) {
            compoundTag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    public void setCustomName(Component component) {
        this.name = component;
    }

    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }

    protected abstract Component getDefaultName();

    public boolean canOpen(Player player) {
        return canUnlock(player, this.lockKey, this.getDisplayName());
    }

    public static boolean canUnlock(Player player, LockCode lockCode, Component component) {
        if (!player.isSpectator() && !lockCode.unlocksWith(player.getMainHandItem())) {
            player.displayClientMessage(Component.translatable("container.isLocked", component), true);
            player.playNotifySound(SoundEvents.CHEST_LOCKED, SoundSource.BLOCKS, 1.0F, 1.0F);
            return false;
        } else {
            return true;
        }
    }

    @Nullable
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return this.canOpen(player) ? this.createMenu(i, inventory) : null;
    }

    protected abstract AbstractContainerMenu createMenu(int i, Inventory inventory);
}
