package ziyue.tjmetro.mapping;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-2
 */

public class DustParticleEffect
{
#if MC_VERSION >= "11904"
    public static final net.minecraft.particle.DustParticleEffect BLUE = new net.minecraft.particle.DustParticleEffect(net.minecraft.util.math.Vec3d.unpackRgb(3336430).toVector3f(), 1.0F); // 50, 232, 238
#elif MC_VERSION >= "11701"
    public static final net.minecraft.particle.DustParticleEffect BLUE = new net.minecraft.particle.DustParticleEffect(new net.minecraft.util.math.Vec3f(net.minecraft.util.math.Vec3d.unpackRgb(3336430)), 1.0F);
#else
    public static final net.minecraft.particle.DustParticleEffect BLUE = new net.minecraft.particle.DustParticleEffect(0.196F, 0.909F, 0.933F, 1.0F);
#endif
}
