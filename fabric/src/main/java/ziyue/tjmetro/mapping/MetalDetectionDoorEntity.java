package ziyue.tjmetro.mapping;

/**
 * An entity for GUI of Metal Detection Door. This entity is a minecart-with-chest.
 *
 * @author ZiYueCommentary
 * @see BlockMetalDetectionDoor
 * @since 1.0.0-beta-2
 */

#if MC_VERSION >= "11904"

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.World;
import ziyue.tjmetro.mod.block.BlockMetalDetectionDoor;

public class MetalDetectionDoorEntity extends ChestMinecartEntity
{
    public final BlockMetalDetectionDoor.BlockEntity blockEntity;

    public MetalDetectionDoorEntity(World world, BlockPos blockPos, BlockMetalDetectionDoor.BlockEntity blockEntity) {
        super(world.data, blockPos.getX(), -1, blockPos.getZ());
        this.blockEntity = blockEntity;
        for (int i = 0; i < blockEntity.inventory.size(); i++) {
            this.getInventory().set(i, blockEntity.inventory.get(i));
        }
    }

    @Override
    public ScreenHandler getScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, playerInventory, this, 1)
        {
            @Override
            public void onClosed(PlayerEntity player) {
                super.onClosed(player);
                final MetalDetectionDoorEntity entity = (MetalDetectionDoorEntity) this.getInventory();
                entity.blockEntity.setData(new DefaultedItemStackList(entity.getInventory()));
                entity.getInventory().clear();
                entity.kill();
            }
        };
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.tjmetro.metal_detection_door");
    }
}

#else

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.World;
import ziyue.tjmetro.mod.block.BlockMetalDetectionDoor;

public class MetalDetectionDoorEntity extends ChestMinecartEntity
{
    public final BlockMetalDetectionDoor.BlockEntity blockEntity;

    public MetalDetectionDoorEntity(World world, BlockPos blockPos, BlockMetalDetectionDoor.BlockEntity blockEntity) {
        super(world.data, blockPos.getX(), -1, blockPos.getZ());
        this.blockEntity = blockEntity;
        for (int i = 0; i < blockEntity.inventory.size(); i++) {
            ((ContainerAccessor) this).tianjin_Metro$getInventory().set(i, blockEntity.inventory.get(i));
        }
    }

    @Override
    public ScreenHandler getScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X1, syncId, playerInventory, this, 1)
        {
            @Override
            public void close(PlayerEntity player) {
                super.close(player);
                final MetalDetectionDoorEntity entity = (MetalDetectionDoorEntity) this.getInventory();
                entity.blockEntity.setData(new DefaultedItemStackList(((ContainerAccessor) entity).tianjin_Metro$getInventory()));
                ((ContainerAccessor) entity).tianjin_Metro$getInventory().clear();
                entity.kill();
            }
        };
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public int getMaxCountPerStack() {
        return 1;
    }

    @Override
    public Text getDisplayName() {
#if MC_VERSION >= "11902"
        return net.minecraft.text.Text.translatable("gui.tjmetro.metal_detection_door");
#else
        return new net.minecraft.text.TranslatableText("gui.tjmetro.metal_detection_door");
#endif
    }
}

#endif