package ziyue.tjmetro.blocks.base;

import mtr.mappings.BlockEntityClientSerializableMapper;
import mtr.mappings.EntityBlockMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author ZiYueCommentary
 * @since 1.0b
 */

public abstract class BlockCustomColorBase extends Block implements EntityBlockMapper
{
    public BlockCustomColorBase(Properties properties) {
        super(properties);
    }

    @Override
    public String getDescriptionId() {
        return super.getDescriptionId().replace("block.tjmetro.custom_color_", "block.minecraft.");
    }

    /**
     * If color equals <code>-1</code>, then use station color.
     *
     * @author ZiYueCommentary
     * @since 1.0b
     */
    public static class CustomColorBlockEntity extends BlockEntityClientSerializableMapper
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

}
