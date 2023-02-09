package ziyue.tjmetro.mixin.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.Blocks;
import mtr.Items;
import net.minecraft.core.Registry;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ziyue.tjmetro.Filters;
import ziyue.tjmetro.Reference;
import ziyue.tjmetro.filters.Filter;

import static ziyue.tjmetro.TianjinMetro.LOGGER;

/**
 * Print references about <b>Tianjin Metro mod</b>.
 * This mixin is for testing originally.
 *
 * @author ZiYueCommentary
 * @see Reference
 * @see RenderSystem
 * @since beta-1
 */

@Mixin(RenderSystem.class)
public class FinishInitializationMixin
{
    @Inject(at = @At("TAIL"), method = "finishInitialization")
    private static void init(CallbackInfo callbackInfo) {
        LOGGER.info("----------------" + Reference.NAME + "----------------");
        LOGGER.info("Hello from ZiYueCommentary!");
        LOGGER.info("Mod ID: " + Reference.MOD_ID);
        LOGGER.info("Version: " + Reference.VERSION);
        LOGGER.info("Modloader: " + (mtr.Registry.isFabric() ? "Fabric" : "Forge"));

        registerMTRCoreFilters();
        registerMTRRailwayFacilitiesFilters();
        registerMTREscalatorsLiftsFilters();
        registerMTRStationBuildingBlocksFilters();

        // collecting uncategorized items
        Registry.ITEM.forEach(item -> {
            CreativeModeTab itemCategory = item.getItemCategory();
            if (itemCategory != null) {
                if (Filter.FILTERS.containsKey(itemCategory.getId())) {
                    Filter.FilterList filters = Filter.FILTERS.get(itemCategory.getId());
                    if ((filters.uncategorizedItems != null) && (!Filter.isItemCategorized(itemCategory.getId(), item))) {
                        filters.uncategorizedItems.items.add(item);
                    }
                }
            }
        });

        // adding uncategorized items filter to filter list
        Filter.FILTERS.forEach((tabId, filterList) -> {
            if ((filterList.uncategorizedItems != null) && (filterList.uncategorizedItems.items.size() != 0)) {
                filterList.add(filterList.uncategorizedItems);
            }
        });
    }

    private static void registerMTRCoreFilters() {
        Filters.MTR_CORE_DASHBOARDS.addItems(
                Items.RAILWAY_DASHBOARD.get(),
                Items.CABLE_CAR_DASHBOARD.get(),
                Items.BOAT_DASHBOARD.get(),
                Items.AIRPLANE_DASHBOARD.get()
        );
        Filters.MTR_CORE_RAILS.addItems(
                Items.RAIL_CONNECTOR_20.get(),
                Items.RAIL_CONNECTOR_20_ONE_WAY.get(),
                Items.RAIL_CONNECTOR_40.get(),
                Items.RAIL_CONNECTOR_40_ONE_WAY.get(),
                Items.RAIL_CONNECTOR_60.get(),
                Items.RAIL_CONNECTOR_60_ONE_WAY.get(),
                Items.RAIL_CONNECTOR_80.get(),
                Items.RAIL_CONNECTOR_80_ONE_WAY.get(),
                Items.RAIL_CONNECTOR_120.get(),
                Items.RAIL_CONNECTOR_120_ONE_WAY.get(),
                Items.RAIL_CONNECTOR_160.get(),
                Items.RAIL_CONNECTOR_160_ONE_WAY.get(),
                Items.RAIL_CONNECTOR_200.get(),
                Items.RAIL_CONNECTOR_200_ONE_WAY.get(),
                Items.RAIL_CONNECTOR_300.get(),
                Items.RAIL_CONNECTOR_300_ONE_WAY.get(),
                Items.RAIL_CONNECTOR_PLATFORM.get(),
                Items.RAIL_CONNECTOR_SIDING.get(),
                Items.RAIL_CONNECTOR_TURN_BACK.get(),
                Items.RAIL_CONNECTOR_CABLE_CAR.get(),
                Items.RAIL_CONNECTOR_RUNWAY.get(),
                Items.RAIL_REMOVER.get()
        );
        Filters.MTR_CORE_SIGNALS.addItems(
                Items.SIGNAL_CONNECTOR_WHITE.get(),
                Items.SIGNAL_CONNECTOR_ORANGE.get(),
                Items.SIGNAL_CONNECTOR_MAGENTA.get(),
                Items.SIGNAL_CONNECTOR_LIGHT_BLUE.get(),
                Items.SIGNAL_CONNECTOR_YELLOW.get(),
                Items.SIGNAL_CONNECTOR_GREEN.get(),
                Items.SIGNAL_CONNECTOR_LIME.get(),
                Items.SIGNAL_CONNECTOR_PINK.get(),
                Items.SIGNAL_CONNECTOR_GRAY.get(),
                Items.SIGNAL_CONNECTOR_LIGHT_GRAY.get(),
                Items.SIGNAL_CONNECTOR_CYAN.get(),
                Items.SIGNAL_CONNECTOR_PURPLE.get(),
                Items.SIGNAL_CONNECTOR_BLUE.get(),
                Items.SIGNAL_CONNECTOR_BROWN.get(),
                Items.SIGNAL_CONNECTOR_LIME.get(),
                Items.SIGNAL_CONNECTOR_RED.get(),
                Items.SIGNAL_CONNECTOR_BLACK.get(),
                Items.SIGNAL_REMOVER_WHITE.get(),
                Items.SIGNAL_REMOVER_ORANGE.get(),
                Items.SIGNAL_REMOVER_MAGENTA.get(),
                Items.SIGNAL_REMOVER_LIGHT_BLUE.get(),
                Items.SIGNAL_REMOVER_YELLOW.get(),
                Items.SIGNAL_REMOVER_GREEN.get(),
                Items.SIGNAL_REMOVER_LIME.get(),
                Items.SIGNAL_REMOVER_PINK.get(),
                Items.SIGNAL_REMOVER_GRAY.get(),
                Items.SIGNAL_REMOVER_LIGHT_GRAY.get(),
                Items.SIGNAL_REMOVER_CYAN.get(),
                Items.SIGNAL_REMOVER_PURPLE.get(),
                Items.SIGNAL_REMOVER_BLUE.get(),
                Items.SIGNAL_REMOVER_BROWN.get(),
                Items.SIGNAL_REMOVER_LIME.get(),
                Items.SIGNAL_REMOVER_RED.get(),
                Items.SIGNAL_REMOVER_BLACK.get()
        );
        Filters.MTR_CORE_CREATORS.addItems(
                Items.BRIDGE_CREATOR_3.get(),
                Items.BRIDGE_CREATOR_5.get(),
                Items.BRIDGE_CREATOR_7.get(),
                Items.BRIDGE_CREATOR_9.get(),
                Items.TUNNEL_CREATOR_4_3.get(),
                Items.TUNNEL_CREATOR_4_5.get(),
                Items.TUNNEL_CREATOR_4_7.get(),
                Items.TUNNEL_CREATOR_4_9.get(),
                Items.TUNNEL_CREATOR_5_3.get(),
                Items.TUNNEL_CREATOR_5_5.get(),
                Items.TUNNEL_CREATOR_5_7.get(),
                Items.TUNNEL_CREATOR_5_9.get(),
                Items.TUNNEL_CREATOR_6_3.get(),
                Items.TUNNEL_CREATOR_6_5.get(),
                Items.TUNNEL_CREATOR_6_7.get(),
                Items.TUNNEL_CREATOR_6_9.get(),
                Items.TUNNEL_WALL_CREATOR_4_3.get(),
                Items.TUNNEL_WALL_CREATOR_4_5.get(),
                Items.TUNNEL_WALL_CREATOR_4_7.get(),
                Items.TUNNEL_WALL_CREATOR_4_9.get(),
                Items.TUNNEL_WALL_CREATOR_5_3.get(),
                Items.TUNNEL_WALL_CREATOR_5_5.get(),
                Items.TUNNEL_WALL_CREATOR_5_7.get(),
                Items.TUNNEL_WALL_CREATOR_5_9.get(),
                Items.TUNNEL_WALL_CREATOR_6_3.get(),
                Items.TUNNEL_WALL_CREATOR_6_5.get(),
                Items.TUNNEL_WALL_CREATOR_6_7.get(),
                Items.TUNNEL_WALL_CREATOR_6_9.get()
        );
        Filters.MTR_CORE_NODES.addItems(
                Blocks.RAIL_NODE.get().asItem(),
                Blocks.BOAT_NODE.get().asItem(),
                Blocks.CABLE_CAR_NODE_LOWER.get().asItem(),
                Blocks.CABLE_CAR_NODE_STATION.get().asItem(),
                Blocks.CABLE_CAR_NODE_UPPER.get().asItem(),
                Blocks.AIRPLANE_NODE.get().asItem()
        );
        Filters.MTR_CORE_MISCELLANEOUS.addItems(
                Items.BRUSH.get(),
                Items.DRIVER_KEY.get(),
                Items.RESOURCE_PACK_CREATOR.get()
        );
    }

    private static void registerMTRRailwayFacilitiesFilters() {
        Filters.MTR_RAILWAY_FACILITIES_GATES.addItems(
                Blocks.APG_DOOR.get().asItem(),
                Blocks.APG_GLASS.get().asItem(),
                Blocks.APG_GLASS_END.get().asItem(),
                Blocks.PSD_DOOR_1.get().asItem(),
                Blocks.PSD_DOOR_2.get().asItem(),
                Blocks.PSD_GLASS_1.get().asItem(),
                Blocks.PSD_GLASS_2.get().asItem(),
                Blocks.PSD_GLASS_END_1.get().asItem(),
                Blocks.PSD_GLASS_END_2.get().asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_PIDS.addItems(
                Blocks.ARRIVAL_PROJECTOR_1_SMALL.get().asItem(),
                Blocks.ARRIVAL_PROJECTOR_1_MEDIUM.get().asItem(),
                Blocks.ARRIVAL_PROJECTOR_1_LARGE.get().asItem(),
                Blocks.PIDS_1.get().asItem(),
                Blocks.PIDS_2.get().asItem(),
                Blocks.PIDS_3.get().asItem(),
                Blocks.PIDS_POLE.get().asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_CEILINGS.addItems(
                Blocks.CEILING.get().asItem(),
                Blocks.CEILING_LIGHT.get().asItem(),
                Blocks.CEILING_NO_LIGHT.get().asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_MISCELLANEOUS.addItems(
                Blocks.CLOCK.get().asItem(),
                Blocks.CLOCK_POLE.get().asItem(),
                Blocks.RUBBISH_BIN_1.get().asItem(),
                Blocks.STATION_COLOR_POLE.get().asItem(),
                Blocks.TACTILE_MAP.get().asItem(),
                Blocks.TRAIN_ANNOUNCER.get().asItem(),
                Blocks.TRAIN_CARGO_LOADER.get().asItem(),
                Blocks.TRAIN_CARGO_UNLOADER.get().asItem(),
                Blocks.TRAIN_REDSTONE_SENSOR.get().asItem(),
                Blocks.TRAIN_SCHEDULE_SENSOR.get().asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_FENCES.addItems(
                Blocks.GLASS_FENCE_CIO.get().asItem(),
                Blocks.GLASS_FENCE_CKT.get().asItem(),
                Blocks.GLASS_FENCE_HEO.get().asItem(),
                Blocks.GLASS_FENCE_MOS.get().asItem(),
                Blocks.GLASS_FENCE_PLAIN.get().asItem(),
                Blocks.GLASS_FENCE_SHM.get().asItem(),
                Blocks.GLASS_FENCE_STAINED.get().asItem(),
                Blocks.GLASS_FENCE_STW.get().asItem(),
                Blocks.GLASS_FENCE_TSH.get().asItem(),
                Blocks.GLASS_FENCE_WKS.get().asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_RAILWAY_SIGNS.addItems(
                Blocks.RAILWAY_SIGN_2_EVEN.get().asItem(),
                Blocks.RAILWAY_SIGN_2_ODD.get().asItem(),
                Blocks.RAILWAY_SIGN_3_EVEN.get().asItem(),
                Blocks.RAILWAY_SIGN_3_ODD.get().asItem(),
                Blocks.RAILWAY_SIGN_4_EVEN.get().asItem(),
                Blocks.RAILWAY_SIGN_4_ODD.get().asItem(),
                Blocks.RAILWAY_SIGN_5_EVEN.get().asItem(),
                Blocks.RAILWAY_SIGN_5_ODD.get().asItem(),
                Blocks.RAILWAY_SIGN_6_EVEN.get().asItem(),
                Blocks.RAILWAY_SIGN_6_ODD.get().asItem(),
                Blocks.RAILWAY_SIGN_7_EVEN.get().asItem(),
                Blocks.RAILWAY_SIGN_7_ODD.get().asItem(),
                Blocks.RAILWAY_SIGN_POLE.get().asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_ROUTE_SIGNS.addItems(
                Blocks.ROUTE_SIGN_STANDING_LIGHT.get().asItem(),
                Blocks.ROUTE_SIGN_STANDING_METAL.get().asItem(),
                Blocks.ROUTE_SIGN_WALL_LIGHT.get().asItem(),
                Blocks.ROUTE_SIGN_WALL_METAL.get().asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_SIGNAL_LIGHTS.addItems(
                Blocks.SIGNAL_LIGHT_2_ASPECT_1.get().asItem(),
                Blocks.SIGNAL_LIGHT_2_ASPECT_2.get().asItem(),
                Blocks.SIGNAL_LIGHT_2_ASPECT_3.get().asItem(),
                Blocks.SIGNAL_LIGHT_2_ASPECT_4.get().asItem(),
                Blocks.SIGNAL_LIGHT_3_ASPECT_1.get().asItem(),
                Blocks.SIGNAL_LIGHT_3_ASPECT_2.get().asItem(),
                Blocks.SIGNAL_LIGHT_4_ASPECT_1.get().asItem(),
                Blocks.SIGNAL_LIGHT_4_ASPECT_2.get().asItem(),
                Blocks.SIGNAL_SEMAPHORE_1.get().asItem(),
                Blocks.SIGNAL_SEMAPHORE_2.get().asItem(),
                Blocks.SIGNAL_POLE.get().asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_STATION_NAME_SIGNS.addItems(
                Blocks.STATION_NAME_ENTRANCE.get().asItem(),
                Blocks.STATION_NAME_WALL_WHITE.get().asItem(),
                Blocks.STATION_NAME_WALL_BLACK.get().asItem(),
                Blocks.STATION_NAME_WALL_GRAY.get().asItem(),
                Blocks.STATION_NAME_TALL_BLOCK.get().asItem(),
                Blocks.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.get().asItem(),
                Blocks.STATION_NAME_TALL_WALL.get().asItem(),
                Blocks.STATION_COLOR_POLE.get().asItem()
        );
        Filters.MTR_RAILWAY_FACILITIES_TICKETS.addItems(
                Blocks.TICKET_BARRIER_ENTRANCE_1.get().asItem(),
                Blocks.TICKET_BARRIER_EXIT_1.get().asItem(),
                Blocks.TICKET_MACHINE.get().asItem(),
                Blocks.TICKET_PROCESSOR.get().asItem(),
                Blocks.TICKET_PROCESSOR_ENQUIRY.get().asItem(),
                Blocks.TICKET_PROCESSOR_ENTRANCE.get().asItem(),
                Blocks.TICKET_PROCESSOR_EXIT.get().asItem(),
                Blocks.TICKET_PROCESSOR_ENQUIRY.get().asItem()
        );
    }

    private static void registerMTREscalatorsLiftsFilters() {
        Filters.MTR_ESCALATORS_LIFTS_ESCALATORS.addItems(
                Items.ESCALATOR.get()
        );
        Filters.MTR_ESCALATORS_LIFTS_LIFTS.addItems(
                Items.LIFT_REFRESHER.get(),
                Items.LIFT_BUTTONS_LINK_CONNECTOR.get(),
                Items.LIFT_BUTTONS_LINK_REMOVER.get(),
                Items.LIFT_DOOR_1.get(),
                Items.LIFT_DOOR_ODD_1.get(),
                Blocks.LIFT_BUTTONS_1.get().asItem(),
                Blocks.LIFT_TRACK_1.get().asItem(),
                Blocks.LIFT_TRACK_FLOOR_1.get().asItem(),
                Blocks.LIFT_PANEL_EVEN_1.get().asItem(),
                Blocks.LIFT_PANEL_EVEN_2.get().asItem(),
                Blocks.LIFT_PANEL_ODD_1.get().asItem(),
                Blocks.LIFT_PANEL_ODD_2.get().asItem()
        );
    }

    private static void registerMTRStationBuildingBlocksFilters() {
        Filters.MTR_STATION_BUILDING_BLOCKS_MISCELLANEOUS.addItems(
                Blocks.LOGO.get().asItem(),
                Blocks.METAL.get().asItem(),
                Blocks.METAL_SLAB.get().asItem()
        );
        Filters.MTR_STATION_BUILDING_BLOCKS_MARBLES.addItems(
                Blocks.MARBLE_BLUE.get().asItem(),
                Blocks.MARBLE_BLUE_SLAB.get().asItem(),
                Blocks.MARBLE_SANDY.get().asItem(),
                Blocks.MARBLE_SANDY_SLAB.get().asItem()
        );
        Filters.MTR_STATION_BUILDING_BLOCKS_PLATFORMS.addItems(
                Blocks.PLATFORM.get().asItem(),
                Blocks.PLATFORM_INDENTED.get().asItem(),
                Blocks.PLATFORM_NA_1.get().asItem(),
                Blocks.PLATFORM_NA_2.get().asItem(),
                Blocks.PLATFORM_NA_1_INDENTED.get().asItem(),
                Blocks.PLATFORM_NA_2_INDENTED.get().asItem(),
                Blocks.PLATFORM_UK_1.get().asItem(),
                Blocks.PLATFORM_UK_1_INDENTED.get().asItem()
        );
        Filters.MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_BLOCKS.addItems(
                Blocks.STATION_COLOR_ANDESITE.get().asItem(),
                Blocks.STATION_COLOR_BEDROCK.get().asItem(),
                Blocks.STATION_COLOR_BIRCH_WOOD.get().asItem(),
                Blocks.STATION_COLOR_BONE_BLOCK.get().asItem(),
                Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK.get().asItem(),
                Blocks.STATION_COLOR_CHISELED_STONE_BRICKS.get().asItem(),
                Blocks.STATION_COLOR_CLAY.get().asItem(),
                Blocks.STATION_COLOR_COAL_ORE.get().asItem(),
                Blocks.STATION_COLOR_COBBLESTONE.get().asItem(),
                Blocks.STATION_COLOR_CONCRETE.get().asItem(),
                Blocks.STATION_COLOR_CONCRETE_POWDER.get().asItem(),
                Blocks.STATION_COLOR_CRACKED_STONE_BRICKS.get().asItem(),
                Blocks.STATION_COLOR_DARK_PRISMARINE.get().asItem(),
                Blocks.STATION_COLOR_DIORITE.get().asItem(),
                Blocks.STATION_COLOR_GRAVEL.get().asItem(),
                Blocks.STATION_COLOR_IRON_BLOCK.get().asItem(),
                Blocks.STATION_COLOR_METAL.get().asItem(),
                Blocks.STATION_COLOR_PLANKS.get().asItem(),
                Blocks.STATION_COLOR_POLISHED_ANDESITE.get().asItem(),
                Blocks.STATION_COLOR_POLISHED_DIORITE.get().asItem(),
                Blocks.STATION_COLOR_PURPUR_BLOCK.get().asItem(),
                Blocks.STATION_COLOR_PURPUR_PILLAR.get().asItem(),
                Blocks.STATION_COLOR_QUARTZ_BLOCK.get().asItem(),
                Blocks.STATION_COLOR_QUARTZ_BRICKS.get().asItem(),
                Blocks.STATION_COLOR_QUARTZ_PILLAR.get().asItem(),
                Blocks.STATION_COLOR_SMOOTH_QUARTZ.get().asItem(),
                Blocks.STATION_COLOR_SMOOTH_STONE.get().asItem(),
                Blocks.STATION_COLOR_SNOW_BLOCK.get().asItem(),
                Blocks.STATION_COLOR_STAINED_GLASS.get().asItem(),
                Blocks.STATION_COLOR_STONE.get().asItem(),
                Blocks.STATION_COLOR_STONE_BRICKS.get().asItem(),
                Blocks.STATION_COLOR_WOOL.get().asItem()
        );
        Filters.MTR_STATION_BUILDING_BLOCKS_STATION_COLOR_SLABS.addItems(
                Blocks.STATION_COLOR_ANDESITE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_BEDROCK_SLAB.get().asItem(),
                Blocks.STATION_COLOR_BIRCH_WOOD_SLAB.get().asItem(),
                Blocks.STATION_COLOR_BONE_BLOCK_SLAB.get().asItem(),
                Blocks.STATION_COLOR_CHISELED_QUARTZ_BLOCK_SLAB.get().asItem(),
                Blocks.STATION_COLOR_CHISELED_STONE_BRICKS_SLAB.get().asItem(),
                Blocks.STATION_COLOR_CLAY_SLAB.get().asItem(),
                Blocks.STATION_COLOR_COAL_ORE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_COBBLESTONE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_CONCRETE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_CONCRETE_POWDER_SLAB.get().asItem(),
                Blocks.STATION_COLOR_CRACKED_STONE_BRICKS_SLAB.get().asItem(),
                Blocks.STATION_COLOR_DARK_PRISMARINE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_DIORITE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_GRAVEL_SLAB.get().asItem(),
                Blocks.STATION_COLOR_IRON_BLOCK_SLAB.get().asItem(),
                Blocks.STATION_COLOR_METAL_SLAB.get().asItem(),
                Blocks.STATION_COLOR_PLANKS_SLAB.get().asItem(),
                Blocks.STATION_COLOR_POLISHED_ANDESITE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_POLISHED_DIORITE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_PURPUR_BLOCK_SLAB.get().asItem(),
                Blocks.STATION_COLOR_PURPUR_PILLAR_SLAB.get().asItem(),
                Blocks.STATION_COLOR_QUARTZ_BLOCK_SLAB.get().asItem(),
                Blocks.STATION_COLOR_QUARTZ_BRICKS_SLAB.get().asItem(),
                Blocks.STATION_COLOR_QUARTZ_PILLAR_SLAB.get().asItem(),
                Blocks.STATION_COLOR_SMOOTH_QUARTZ_SLAB.get().asItem(),
                Blocks.STATION_COLOR_SMOOTH_STONE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_SNOW_BLOCK_SLAB.get().asItem(),
                Blocks.STATION_COLOR_STAINED_GLASS_SLAB.get().asItem(),
                Blocks.STATION_COLOR_STONE_SLAB.get().asItem(),
                Blocks.STATION_COLOR_STONE_BRICKS_SLAB.get().asItem(),
                Blocks.STATION_COLOR_WOOL_SLAB.get().asItem()
        );
    }
}
