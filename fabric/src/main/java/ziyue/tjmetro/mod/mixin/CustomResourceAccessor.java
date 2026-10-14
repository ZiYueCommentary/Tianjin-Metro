package ziyue.tjmetro.mod.mixin;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.SignResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author ZiYueCommentary
 * @since 1.1.3
 */

@Mixin(CustomResourceLoader.class)
public interface CustomResourceAccessor
{
    @Accessor(value = "SIGNS", remap = false)
    static ObjectArrayList<SignResource> tjmetro$getSigns() {
        throw new AssertionError("Untransformed @Accessor");
    }
}
