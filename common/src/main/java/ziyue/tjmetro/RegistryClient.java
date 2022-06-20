package ziyue.tjmetro;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;

public class RegistryClient
{
    @ExpectPlatform
    public static void registerCustomColorBlock(Block block) {
        throw new AssertionError();
    }
}
