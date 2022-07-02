package ziyue.tjmetro;

import me.shedaniel.architectury.platform.forge.EventBuses;
import mtr.RegistryObject;
import mtr.item.ItemBlockEnchanted;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.DeferredRegisterHolder;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * All register methods are <b>public</b>.
 *
 * @see TianjinMetro
 */

@Mod(Reference.MOD_ID)
public class MainForge
{
    public static final DeferredRegisterHolder<Item> ITEMS = new DeferredRegisterHolder<>(Reference.MOD_ID, Registry.ITEM_REGISTRY);
    public static final DeferredRegisterHolder<Block> BLOCKS = new DeferredRegisterHolder<>(Reference.MOD_ID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegisterHolder<BlockEntityType<?>> BLOCK_ENTITY_TYPES = new DeferredRegisterHolder<>(Reference.MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final DeferredRegisterHolder<SoundEvent> SOUND_EVENTS = new DeferredRegisterHolder<>(Reference.MOD_ID, Registry.SOUND_EVENT_REGISTRY);
    public static final DeferredRegisterHolder<EntityType<?>> ENTITY_TYPES = new DeferredRegisterHolder<>(Reference.MOD_ID, Registry.ENTITY_TYPE_REGISTRY);

    static {
        TianjinMetro.init(MainForge::registerItem, MainForge::registerBlock, MainForge::registerBlock, MainForge::registerEnchantedBlock, MainForge::registerBlockEntityType, MainForge::registerEntityType);
    }

    public MainForge() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Reference.MOD_ID, eventBus);

        ITEMS.register();
        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();
        SOUND_EVENTS.register();

        eventBus.register(ForgeRegistry.class);
    }

    public static void registerItem(String path, RegistryObject<Item> item) {
        ITEMS.register(path, item::register);
    }

    public static void registerBlock(String path, RegistryObject<Block> block) {
        BLOCKS.register(path, block::register);
    }

    public static void registerBlock(String path, RegistryObject<Block> block, CreativeModeTab itemGroup) {
        registerBlock(path, block);
        ITEMS.register(path, () -> new BlockItem(block.get(), new Item.Properties().tab(itemGroup)));
    }

    public static <T extends BlockEntityMapper> void registerBlockEntityType(String path, RegistryObject<? extends BlockEntityType<? extends BlockEntityMapper>> blockEntityType) {
        BLOCK_ENTITY_TYPES.register(path, blockEntityType::register);
    }

    public static void registerEnchantedBlock(String path, RegistryObject<Block> block, CreativeModeTab itemGroup) {
        registerBlock(path, block);
        ITEMS.register(path, () -> new ItemBlockEnchanted(block.get(), new Item.Properties().tab(itemGroup)));
    }

    public static void registerEntityType(String path, RegistryObject<? extends EntityType<? extends Entity>> entityType) {
        ENTITY_TYPES.register(path, entityType::register);
    }

    public static class ForgeRegistry
    {
        @SubscribeEvent
        public static void onClientSetupEvent(FMLClientSetupEvent event) {
            TianjinMetroClient.init();
        }
    }
}
