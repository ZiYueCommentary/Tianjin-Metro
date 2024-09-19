package ziyue.tjmetro.mapping;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

public class DustParticleEffect
{
#if MC_VERSION >= "11904"
    public static final net.minecraft.core.particles.DustParticleOptions BLUE = new net.minecraft.core.particles.DustParticleOptions(net.minecraft.world.phys.Vec3.fromRGB24(3336430).toVector3f(), 1.0F); // 50, 232, 238
#elif MC_VERSION >= "11701"
    public static final net.minecraft.core.particles.DustParticleOptions BLUE = new net.minecraft.core.particles.DustParticleOptions(new com.mojang.math.Vector3f(net.minecraft.world.phys.Vec3.fromRGB24(3336430)), 1.0F);
#else
    public static final net.minecraft.particles.IParticleData BLUE = new net.minecraft.particles.RedstoneParticleData(0.196F, 0.909F, 0.933F, 1.0F);
#endif
}
