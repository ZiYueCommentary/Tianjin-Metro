package ziyue.tjmetro.mod;

import org.mtr.mapping.registry.EntityTypeRegistryObject;
import ziyue.tjmetro.mod.entity.EntitySeat;

/**
 * @since beta-1
 */

public interface EntityTypes
{
    EntityTypeRegistryObject<EntitySeat> SEAT = Registry.registerEntityType("seat", EntitySeat::new, Float.MIN_VALUE, Float.MIN_VALUE);

    static void registerEntities() {
        // Calling this class to initialize constants
        TianjinMetro.LOGGER.info("Registering entities");
    }
}
