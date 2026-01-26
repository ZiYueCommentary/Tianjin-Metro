package ziyue.tjmetro.fabric.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.GameRules;
import ziyue.tjmetro.mod.block.BlockSmokeAlarm;
import ziyue.tjmetro.mod.block.IBlockExtension;

#if MC_VERSION > "11701"
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.UpgradeData;
import net.minecraft.world.gen.chunk.BlendingData;
import org.jetbrains.annotations.Nullable;
#endif

#if MC_VERSION > "11902"
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
#else
import net.minecraft.util.registry.Registry;
import net.minecraft.tag.BlockTags;
#endif

/**
 * @author ZiYueCommentary
 * @since 1.1.0
 */

@Mixin(WorldChunk.class)
#if MC_VERSION > "11701"
public abstract class WorldChunkMixin extends Chunk
#else
public abstract class WorldChunkMixin implements Chunk
#endif
{
    @Shadow
    public abstract World getWorld();

#if MC_VERSION > "11902"
    public WorldChunkMixin(ChunkPos pos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry<Biome> biomeRegistry, long inhabitedTime, @Nullable ChunkSection[] sectionArray, @Nullable BlendingData blendingData) {
        super(pos, upgradeData, heightLimitView, biomeRegistry, inhabitedTime, sectionArray, blendingData);
    }
#elif MC_VERSION > "11701"
    public WorldChunkMixin(ChunkPos pos, UpgradeData upgradeData, HeightLimitView heightLimitView, Registry<Biome> biome, long inhabitedTime, @Nullable ChunkSection[] sectionArrayInitializer, @Nullable BlendingData blendingData) {
        super(pos, upgradeData, heightLimitView, biome, inhabitedTime, sectionArrayInitializer, blendingData);
    }
#endif

    /**
     * Listening block changed and activate nearby smoke alarms.
     *
     * @see BlockSmokeAlarm
     */
    @Inject(at = @At("TAIL"), method = "setBlockState")
    private void afterSetBlockState(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
        if (!state.isIn(BlockTags.FIRE) && !state.isIn(BlockTags.CAMPFIRES)) return;
        int radius = this.getWorld().getGameRules().getInt(GameRules.SMOKE_ALARM_RANGE.data);
        for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++) {
            for (int y = pos.getY(); y <= pos.getY() + radius; y++) {
                for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = this.getBlockState(currentPos);
                    if (IBlockExtension.isBlock(new org.mtr.mapping.holder.BlockState(blockState), BlockList.SMOKE_ALARM.get())) {
                        // This is pretty dangerous...
                        this.setBlockState(currentPos, blockState.with(BlockSmokeAlarm.ACTIVATED.data, true), true);
                    }
                }
            }
        }
    }
}
