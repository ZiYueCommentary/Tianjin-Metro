package ziyue.tjmetro.mapping;

import net.fabricmc.loader.api.FabricLoader;

/**
 * @author ZiYueCommentary
 * @since 1.0.0-beta-5
 */

public interface ModLoaderHelper
{
    static boolean hasClothConfig() {
        return FabricLoader.getInstance().isModLoaded("cloth-config");
    }
}
