package ziyue.tjmetro.mapping;

import net.minecraftforge.fml.ModList;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-5
 */

public interface ModLoaderHelper
{
    static boolean hasClothConfig() {
        return ModList.get().isLoaded("cloth_config");
    }
}
