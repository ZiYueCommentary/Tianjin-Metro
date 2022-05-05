package ziyue.tjmetro;

import me.shedaniel.architectury.platform.forge.EventBuses;
import mtr.mappings.EntityRendererMapper;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeUtilities {

	private static Runnable renderTickAction = () -> {
	};

	public static void registerModEventBus(String modId, IEventBus eventBus) {
		EventBuses.registerModEventBus(modId, eventBus);
	}

	public static Packet<?> createAddEntityPacket(Entity entity) {
		return NetworkHooks.getEntitySpawningPacket(entity);
	}

	public static void renderTickAction(Runnable runnable) {
		renderTickAction = runnable;
	}

	public static <T extends Entity> void registerEntityRenderer(Supplier<EntityType<? extends T>> entityType, Function<Object, EntityRendererMapper<T>> entityRendererProvider) {
	}

	public static class RenderTick {

		@SubscribeEvent
		public static void onRenderTickEvent(net.minecraftforge.client.event.RenderWorldLastEvent event) {
			renderTickAction.run();
		}
	}

	public static class RegisterEntityRenderer {
	}
}
