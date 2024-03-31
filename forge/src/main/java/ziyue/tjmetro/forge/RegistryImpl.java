package ziyue.tjmetro.forge;

import me.shedaniel.architectury.registry.GameRuleFactory;
import mtr.client.ClientData;
import mtr.mappings.RegistryUtilitiesClient;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ziyue.tjmetro.block.base.BlockCustomColorBase;

/**
 * Methods implements of forge mod.
 *
 * @see ziyue.tjmetro.Registry
 * @since beta-1
 */

public class RegistryImpl
{
    public static void registerCustomColorBlock(Block block) {
        RegistryUtilitiesClient.registerBlockColors(new CustomColor(), block);
    }

    public static GameRules.Key<GameRules.BooleanValue> registerBooleanGameRule(String name, GameRules.Category category, boolean defaultValue) {
        return GameRules.register(name, category, GameRuleFactory.createBooleanRule(defaultValue));
    }

    public static GameRules.Key<GameRules.IntegerValue> registerIntegerGameRule(String name, GameRules.Category category, int defaultValue) {
        return GameRules.register(name, category, GameRuleFactory.createIntRule(defaultValue));
    }

    protected static class CustomColor implements BlockColor
    {
        @Override
        public int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos pos, int i) {
            final int defaultColor = 0x7F7F7F;
            if (pos != null) {
                try {
                    BlockEntity entity = blockAndTintGetter.getBlockEntity(pos);
                    int color = entity != null ? ((BlockCustomColorBase.CustomColorBlockEntity) (((BlockCustomColorBase.CustomColorBlockEntity) entity).getRenderAttachmentData())).color : -1;
                    return color != -1 ? color : ClientData.STATIONS.stream().filter(station1 -> station1.inArea(pos.getX(), pos.getZ())).findFirst().map(station2 -> station2.color).orElse(defaultColor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return defaultColor;
        }
    }
}
