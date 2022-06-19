package ziyue.tjmetro.blocks;

import mtr.mappings.BlockEntityClientSerializableMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * If color equals -1, then use station color.
 */

public class CustomColorBlockEntity extends BlockEntityClientSerializableMapper
{
    final String COLOR_ID = "color";
    public int color = -1;

    public CustomColorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void readCompoundTag(CompoundTag compoundTag) {
        color = compoundTag.getInt(COLOR_ID);
        super.readCompoundTag(compoundTag);
    }

    @Override
    public void writeCompoundTag(CompoundTag compoundTag) {
        compoundTag.putInt(COLOR_ID, color);
        super.writeCompoundTag(compoundTag);
    }

    public void setData(int color) {
        this.color = color;
        setChanged();
        syncData();
    }
}
