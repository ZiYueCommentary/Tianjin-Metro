package ziyue.tjmetro;

import me.shedaniel.architectury.platform.forge.EventBuses;
import mtr.RegistryObject;
import mtr.item.ItemBlockEnchanted;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.DeferredRegisterHolder;
import net.minecraft.core.Registry;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ziyue.filters.Filter;

import java.util.ArrayList;

/**
 * Entry of forge mod.
 *
 * @see TianjinMetro
 * @since beta-1
 */

@Mod(Reference.MOD_ID)
public class MainForge
{
    public static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(Reference.MOD_ID, Registry.ITEM_REGISTRY);
    public static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(Reference.MOD_ID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(Reference.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final ArrayList<Tuple<Filter, RegistryObject<Item>>> FILTER_ITEMS = new ArrayList<>();

    static {
        TianjinMetro.init(MainForge::registerBlock, MainForge::registerBlock, MainForge::registerItem, MainForge::registerEnchantedBlock, MainForge::registerBlockEntityType);
    }

    public MainForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Reference.MOD_ID, eventBus);

        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (client, parent) -> Config.getConfigScreen(parent));

        ITEMS.register();
        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();

        eventBus.register(ForgeRegistry.class);
    }

    public static void registerBlock(String path, RegistryObject<Block> block) {
        BLOCKS.register(path, block::get);
    }

    public static void registerBlock(String path, RegistryObject<Block> block, Filter filter) {
        registerBlock(path, block);
        ITEMS.register(path, () -> new BlockItem(block.get(), new Item.Properties()));
        FILTER_ITEMS.add(new Tuple<>(filter, new RegistryObject<>(() -> block.get().asItem())));
    }

    public static void registerItem(String path, RegistryObject<Item> item, Filter filter) {
        ITEMS.register(path, item::get);
        FILTER_ITEMS.add(new Tuple<>(filter, item));
    }

    public static void registerEnchantedBlock(String path, RegistryObject<Block> block, Filter filter) {
        registerBlock(path, block);
        ITEMS.register(path, () -> new ItemBlockEnchanted(block.get(), new Item.Properties()));
        FILTER_ITEMS.add(new Tuple<>(filter, new RegistryObject<>(() -> block.get().asItem())));
    }

    public static void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
        BLOCK_ENTITY_TYPES.register(path, blockEntityType::get);
    }

    /**
     * Client entry of forge mod.
     *
     * @see TianjinMetroClient
     * @since beta-1
     */
    public static class ForgeRegistry
    {
        @SubscribeEvent
        public static void onClientSetupEvent(FMLClientSetupEvent event) {
            TianjinMetroClient.init();

            // initialize filters
            FILTER_ITEMS.forEach(tuple -> tuple.getA().addItems(tuple.getB().get()));
        }
    }
}
