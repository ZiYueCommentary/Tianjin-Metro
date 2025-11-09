package ziyue.tjmetro.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.block.BlockPIDSHorizontalBase;
import org.mtr.mod.block.IBlock;
import ziyue.tjmetro.mod.BlockEntityTypes;
import ziyue.tjmetro.mod.Registry;
import ziyue.tjmetro.mod.client.ScrollingText;
import ziyue.tjmetro.mod.packet.PacketOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZiYueCommentary
 * @see BlockEntity
 * @since 1.0.0
 */

public class BlockPIDSTianjin extends BlockPIDSHorizontalBase
{
    public static final Map<Long, Category> CATEGORIES = new HashMap<>();

    public BlockPIDSTianjin() {
        super(2);
    }

    @Override
    public @Nonnull ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlockExtension.checkHoldingBrushOrWrench(world, player, () -> {
            final BlockPos newBlockPos = getBlockPosWithData(world, pos);
            final org.mtr.mapping.holder.BlockEntity entity = world.getBlockEntity(newBlockPos);
            if (entity != null && entity.data instanceof BlockEntity) {
                ((BlockEntity) entity.data).markDirty2();
                Registry.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketOpenBlockEntityScreen(newBlockPos));
            }
        });
    }

    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntity(blockPos, blockState);
    }

    public static boolean canStoreData(World world, BlockPos blockPos) {
        final Direction facing = IBlock.getStatePropertySafe(world, blockPos, FACING);
        return facing == Direction.NORTH || facing == Direction.EAST;
    }

    public static BlockPos getBlockPosWithData(World world, BlockPos blockPos) {
        if (canStoreData(world, blockPos)) {
            return blockPos;
        } else {
            return blockPos.offset(IBlock.getStatePropertySafe(world, blockPos, FACING));
        }
    }

    public static class BlockEntity extends BlockEntityExtension
    {
        protected final LongAVLTreeSet platformIds = new LongAVLTreeSet();
        protected final LongArrayList categories = new LongArrayList();
        protected int displayPage;
        public final ScrollingText scrollingText = new ScrollingText(158F, 47, 4, true);
        public BlockPIDSTianjin.Advertisement advertisement = null;
        public int categoryIndex = 0;
        public int advertisementIndex = 0;

        public static final String PLATFORM_IDS_ID = "platform_ids";
        public static final String DISPLAY_PAGE_ID = "display_page";
        public static final String CATEGORIES_ID = "categories";

        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.PIDS_TIANJIN.get(), pos, state);
            categories.add("tjmetro".hashCode());
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            platformIds.clear();
            final long[] platformIdsArray = compoundTag.getLongArray(PLATFORM_IDS_ID);
            for (final long platformId : platformIdsArray) {
                platformIds.add(platformId);
            }

            categories.clear();
            final long[] categoriesArray = compoundTag.getLongArray(CATEGORIES_ID);
            for (final long category : categoriesArray) {
                categories.add(category);
            }

            displayPage = compoundTag.getInt(DISPLAY_PAGE_ID);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putLongArray(PLATFORM_IDS_ID, new ArrayList<>(platformIds));
            compoundTag.putLongArray(CATEGORIES_ID, new ArrayList<>(categories));
            compoundTag.putInt(DISPLAY_PAGE_ID, displayPage);
        }

        public LongArrayList getCategories() {
            return categories;
        }

        public LongAVLTreeSet getPlatformIds() {
            return platformIds;
        }

        public int getDisplayPage() {
            return displayPage;
        }

        public void setData(LongArrayList categories) {
            this.categories.clear();
            this.categories.addAll(categories);
            markDirty2();
        }

        public void setData(LongAVLTreeSet platformIds, int displayPage) {
            this.platformIds.clear();
            this.platformIds.addAll(platformIds);
            this.displayPage = displayPage;
            markDirty2();
        }
    }

    public record Advertisement(Identifier image, MutableText text)
    {
    }

    public static class Category extends ArrayList<Advertisement>
    {
        public final long id;
        public final Color color;
        public final MutableText name;
        public final MutableText description;

        public Category(long id, Color color, MutableText name, MutableText description) {
            this.id = id;
            this.color = color;
            this.name = name;
            this.description = description;
        }
    }
}
