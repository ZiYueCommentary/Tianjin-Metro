package ziyue.tjmetro;

import mtr.RegistryObject;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import ziyue.tjmetro.entity.SeatEntity;

/**
 * @since beta-1
 */

public interface EntityTypes
{
    RegistryObject<EntityType<SeatEntity>> BENCH = new RegistryObject<>(() -> EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC).sized(0.0f, 0.0f).build("bench"));
}
