package ziyue.tjmetro.mod;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockItemExtension;
import org.mtr.mapping.mapper.EntityExtension;
import org.mtr.mapping.registry.*;
import org.mtr.mapping.tool.PacketBufferReceiver;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @since 1.0.0-beta-1
 */

public final class Registry
{
    public static final org.mtr.mapping.registry.Registry REGISTRY = new org.mtr.mapping.registry.Registry();
    public static final org.mtr.mapping.registry.Registry REGISTRY_TABS = new org.mtr.mapping.registry.Registry();

    public static CreativeModeTabHolder createCreativeModeTabHolder(String id, Supplier<ItemStack> icon) {
        return REGISTRY_TABS.createCreativeModeTabHolder(new Identifier(Reference.MOD_ID, id), icon);
    }

    public static BlockRegistryObject registerBlock(String id, Supplier<Block> supplier) {
        return REGISTRY.registerBlock(new Identifier(Reference.MOD_ID, id), supplier);
    }

    public static ItemRegistryObject registerItem(String id, Function<ItemSettings, Item> function, CreativeModeTabHolder creativeModeTab) {
        return REGISTRY.registerItem(new Identifier(Reference.MOD_ID, id), function, creativeModeTab);
    }

    public static BlockRegistryObject registerBlockWithBlockItem(String id, Supplier<Block> block, CreativeModeTabHolder creativeModeTab) {
        return registerBlockWithBlockItem(id, block, BlockItemExtension::new, creativeModeTab);
    }

    public static BlockRegistryObject registerBlockWithBlockItem(String id, Supplier<Block> block, BiFunction<Block, ItemSettings, BlockItemExtension> function, CreativeModeTabHolder creativeModeTab) {
        return REGISTRY.registerBlockWithBlockItem(new Identifier(Reference.MOD_ID, id), block, function, creativeModeTab);
    }

    @SafeVarargs
    public static <T extends BlockEntityExtension> BlockEntityTypeRegistryObject<T> registerBlockEntityType(String id, BiFunction<BlockPos, BlockState, T> function, Supplier<Block>... blockSuppliers) {
        return REGISTRY.registerBlockEntityType(new Identifier(Reference.MOD_ID, id), function, blockSuppliers);
    }

    public static <T extends EntityExtension> EntityTypeRegistryObject<T> registerEntityType(String id, BiFunction<EntityType<?>, World, T> function, float width, float height) {
        return REGISTRY.registerEntityType(new Identifier(Reference.MOD_ID, id), function, width, height);
    }

    public static void setupPackets(String id) {
        REGISTRY.setupPackets(new Identifier(Reference.MOD_ID, id));
    }

    public static <T extends PacketHandler> void registerPacket(Class<T> classObject, Function<PacketBufferReceiver, T> getInstance) {
        REGISTRY.registerPacket(classObject, getInstance);
    }

    public static <T extends PacketHandler> void sendPacketToClient(ServerPlayerEntity serverPlayerEntity, T data) {
        REGISTRY.sendPacketToClient(serverPlayerEntity, data);
    }

    public static void init() {
        REGISTRY.init();
    }
}
