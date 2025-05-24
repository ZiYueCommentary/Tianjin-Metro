package ziyue.tjmetro.mod.block;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.ItemList;
import ziyue.tjmetro.mod.block.base.BlockFlagAPGTianjin;
import ziyue.tjmetro.mod.block.base.BlockFlagAPGTianjinTRT;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @see org.mtr.mod.block.BlockAPGDoor
 * @see ziyue.tjmetro.mod.item.ItemPSDAPGTianjinBase
 * @since 1.0.0-beta-2
 */

public class BlockAPGDoorTianjinTRT extends BlockPSDAPGDoorBase implements BlockFlagAPGTianjinTRT
{
    public static final EnumProperty<LightProperty> LIGHT = EnumProperty.of("light", LightProperty.class);

    @Override
    protected boolean isAPG() {
        return true;
    }

    @Nonnull
    @Override
    public Item asItem2() {
        return ItemList.APG_DOOR_TIANJIN_TRT.get();
    }

    // Note that I chose traditional way to render lights.
    @Nonnull
    @Override
    public BlockRenderType getRenderType2(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        super.addBlockProperties(properties);
        properties.add(LIGHT);
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    /**
     * @author ZiYueCommentary
     * @see org.mtr.mod.render.RenderPSDAPGDoor
     * @since 1.0.0-beta-2
     */
    public static class BlockEntity extends BlockEntityBase
    {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.APG_DOOR_TIANJIN_TRT.get(), pos, state);
        }
    }

    /**
     * @author ZiYueCommentary
     * @since 1.0.0-beta-5
     */
    public enum LightProperty implements StringIdentifiable
    {
        NO_LIGHT("no_light"),
        LIGHT_OFF("light_off"),
        LIGHT_ON("light_on");

        final String name;

        LightProperty(String name) {
            this.name = name;
        }

        @Nonnull
        @Override
        public String asString2() {
            return name;
        }
    }
}
