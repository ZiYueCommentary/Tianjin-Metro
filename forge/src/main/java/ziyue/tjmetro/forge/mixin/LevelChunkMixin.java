package ziyue.tjmetro.forge.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ziyue.tjmetro.mod.BlockList;
import ziyue.tjmetro.mod.GameRules;
import ziyue.tjmetro.mod.block.BlockSmokeAlarm;
import ziyue.tjmetro.mod.block.IBlockExtension;

#if MC_VERSION > "11605"
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

#if MC_VERSION > "11701"
import net.minecraft.core.Registry;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
#endif

/**
 * @author ZiYueCommentary
 * @since 1.1.0
 */

@Mixin(LevelChunk.class)
#if MC_VERSION > "11701"
public abstract class LevelChunkMixin extends ChunkAccess implements net.minecraftforge.common.capabilities.ICapabilityProviderImpl<LevelChunk>
#else
public abstract class LevelChunkMixin implements ChunkAccess
#endif
{
    @Shadow
    public abstract Level getLevel();

#if MC_VERSION > "11701"
    public LevelChunkMixin(ChunkPos p_187621_, UpgradeData p_187622_, LevelHeightAccessor p_187623_, Registry<Biome> p_187624_, long p_187625_, @Nullable LevelChunkSection[] p_187626_, @Nullable BlendingData p_187627_) {
        super(p_187621_, p_187622_, p_187623_, p_187624_, p_187625_, p_187626_, p_187627_);
    }
#endif

#else

import net.minecraft.world.chunk.Chunk;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Chunk.class)
public abstract class LevelChunkMixin
{

    @Shadow
    public abstract World getLevel();

    @Shadow
    public abstract BlockState getBlockState(BlockPos par1);

    @Shadow
    public abstract BlockState setBlockState(BlockPos pos, BlockState state, boolean moved);

#endif

    /**
     * Listening block changed and activate nearby smoke alarms.
     *
     * @see BlockSmokeAlarm
     */
    @Inject(at = @At("TAIL"), method = "setBlockState")
    private void afterSetBlockState(BlockPos pos, BlockState state, boolean p_62867_, CallbackInfoReturnable<BlockState> cir) {
        if (!state.is(BlockTags.FIRE) && !state.is(BlockTags.CAMPFIRES)) return;
        int radius = this.getLevel().getGameRules().getInt(GameRules.SMOKE_ALARM_RANGE.data);
        for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++) {
            for (int y = pos.getY(); y <= pos.getY() + radius; y++) {
                for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    BlockState blockState = this.getBlockState(currentPos);
                    if (IBlockExtension.isBlock(new org.mtr.mapping.holder.BlockState(blockState), BlockList.SMOKE_ALARM.get())) {
                        // This is pretty dangerous...
                        this.setBlockState(currentPos, blockState.setValue(BlockSmokeAlarm.ACTIVATED.data, true), true);
                        this.getLevel().neighborChanged(currentPos.above(2), blockState.getBlock(), currentPos.below());
                    }
                }
            }
        }
    }
}
