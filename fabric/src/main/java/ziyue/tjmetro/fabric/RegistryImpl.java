package ziyue.tjmetro.fabric;

import mtr.client.ClientData;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import ziyue.tjmetro.blocks.base.BlockCustomColorBase;

/**
 * Methods implements of fabric mod.
 *
 * @see ziyue.tjmetro.Registry
 * @since beta-1
 */

public class RegistryImpl
{
    public static void registerCustomColorBlock(Block block) {
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            final int defaultColor = 0x7F7F7F;
            if (pos != null) {
                try {
                    BlockEntity entity = world.getBlockEntity(pos);
                    int color = entity != null ? ((BlockCustomColorBase.CustomColorBlockEntity) (((BlockCustomColorBase.CustomColorBlockEntity) entity).getRenderAttachmentData())).color : -1;
                    return color != -1 ? color : (ClientData.STATIONS.stream().filter(station1 -> station1.inArea(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.color).orElse(defaultColor));
                } catch (Exception ignored) {
                }
            }
            return defaultColor;
        }, block);
    }

    public static GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRuleRegistry.register(name, category, GameRuleFactory.createBooleanRule(defaultValue));
    }

    public static GameRules.Key<GameRules.IntegerValue> registerIntegerGameRule(String name, GameRules.Category category, int defaultValue) {
        return GameRuleRegistry.register(name, category, GameRuleFactory.createIntRule(defaultValue));
    }
}
