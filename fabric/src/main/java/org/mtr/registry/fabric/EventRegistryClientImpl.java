package org.mtr.registry.fabric;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.world.chunk.WorldChunk;
import org.mtr.MTRClient;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class EventRegistryClientImpl {

	public static void registerStartClientTick(Runnable runnable) {
		ClientTickEvents.START_CLIENT_TICK.register(minecraftServer -> runnable.run());
	}

	public static void registerEndClientTick(Runnable runnable) {
		ClientTickEvents.END_CLIENT_TICK.register(minecraftServer -> runnable.run());
	}

	public static void registerStartWorldTick(Consumer<ClientWorld> consumer) {
		ClientTickEvents.START_WORLD_TICK.register(consumer::accept);
	}

	public static void registerEndWorldTick(Consumer<ClientWorld> consumer) {
		ClientTickEvents.END_WORLD_TICK.register(consumer::accept);
	}

	public static void registerClientJoin(Runnable runnable) {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> runnable.run());
	}

	public static void registerClientDisconnect(Runnable runnable) {
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> runnable.run());
	}

	public static void registerChunkLoad(BiConsumer<ClientWorld, WorldChunk> consumer) {
		ClientChunkEvents.CHUNK_LOAD.register(consumer::accept);
	}

	public static void registerChunkUnload(BiConsumer<ClientWorld, WorldChunk> consumer) {
		ClientChunkEvents.CHUNK_UNLOAD.register(consumer::accept);
	}

	public static void registerResourceReloadEvent(Runnable runnable) {
		final Identifier identifier = Identifier.of(Integer.toHexString(new Random().nextInt()), "resource");
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return identifier;
			}

			@Override
			public void reload(ResourceManager manager) {
				runnable.run();
			}
		});
	}

	public static void registerWorldRenderEvent(MTRClient.WorldRenderCallback worldRenderCallback) {
		WorldRenderEvents.AFTER_ENTITIES.register(worldRenderContext -> {
			final MatrixStack matrixStack = worldRenderContext.matrixStack();
			final VertexConsumerProvider vertexConsumerProvider = worldRenderContext.consumers();
			if (matrixStack != null && vertexConsumerProvider != null) {
				worldRenderCallback.accept(matrixStack, vertexConsumerProvider, worldRenderContext.camera().getPos());
			}
		});
	}
}
