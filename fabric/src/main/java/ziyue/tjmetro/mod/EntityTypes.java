package ziyue.tjmetro.mod;

import org.mtr.mapping.registry.EntityTypeRegistryObject;
import ziyue.tjmetro.mod.entity.EntitySeat;

/**
 * @since 1.0.0-beta-1
 */

public interface EntityTypes
{
    EntityTypeRegistryObject<EntitySeat> SEAT = Registry.registerEntityType("seat", EntitySeat::new, 0, 0);

    static void registerEntities() {
        // Calling this class to initialize constants
        TianjinMetro.LOGGER.info("Registering entities");
    }
}
