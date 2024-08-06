package ziyue.tjmetro.mod;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.holder.ItemConvertible;
import org.mtr.mapping.holder.RenderLayer;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.EntityExtension;
import org.mtr.mapping.mapper.EntityRenderer;
import org.mtr.mapping.registry.*;
import org.mtr.mod.InitClient;
import ziyue.tjmetro.mod.block.base.BlockCustomColorBase;

import java.util.function.Function;

public final class RegistryClient
{
    public static final org.mtr.mapping.registry.RegistryClient REGISTRY_CLIENT = new org.mtr.mapping.registry.RegistryClient(Registry.REGISTRY);

    public static void registerBlockRenderType(RenderLayer renderLayer, BlockRegistryObject block) {
        REGISTRY_CLIENT.registerBlockRenderType(renderLayer, block);
    }

    public static void registerBlockStationColor(BlockRegistryObject... blocks) {
        REGISTRY_CLIENT.registerBlockColors((state, world, pos, tintIndex) -> InitClient.getStationColor(pos), blocks);
    }

    public static void registerItemCustomColor(int color, Item item) {
        ColorProviderRegistry.ITEM.register((stack, index) -> color, item.data);
    }

    public static void registerBlockCustomColor(BlockRegistryObject... blocks) {
        for (BlockRegistryObject block : blocks) {
            REGISTRY_CLIENT.registerBlockColors((state, world, pos, tintIndex) -> {
                try {
                    if (world.getBlockEntity(pos).data instanceof BlockCustomColorBase.BlockEntityBase) {
                        final BlockCustomColorBase.BlockEntityBase entity = (BlockCustomColorBase.BlockEntityBase) world.getBlockEntity(pos).data;
                        return entity.color == -1 ? entity.getDefaultColor(pos) : entity.color;
                    }
                } catch (Exception ignored) {
                }
                return 8355711;
            }, block);
        }
    }

    public static <T extends BlockEntityTypeRegistryObject<U>, U extends BlockEntityExtension> void registerBlockEntityRenderer(T blockEntityType, Function<BlockEntityRenderer.Argument, BlockEntityRenderer<U>> rendererInstance) {
        REGISTRY_CLIENT.registerBlockEntityRenderer(blockEntityType, rendererInstance);
    }

    public static <T extends EntityTypeRegistryObject<U>, U extends EntityExtension> void registerEntityRenderer(T entityType, Function<EntityRenderer.Argument, EntityRenderer<U>> rendererInstance) {
        REGISTRY_CLIENT.registerEntityRenderer(entityType, rendererInstance);
    }

    public static void setupPackets(String id) {
        REGISTRY_CLIENT.setupPackets(new Identifier(Reference.MOD_ID, id));
    }

    public static <T extends PacketHandler> void sendPacketToServer(T data) {
        REGISTRY_CLIENT.sendPacketToServer(data);
    }
}
