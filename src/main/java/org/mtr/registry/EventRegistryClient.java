package org.mtr.registry;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.mtr.MTR;
import org.mtr.MTRClient;

import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

//? if fabric {
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
//? if >= 1.21.4 {
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
//? }
//? }

//? if neoforge {
/*import org.mtr.neoforge.MainEventBusClient;
import org.mtr.neoforge.ModEventBusClient;
*///? }

public final class EventRegistryClient {

	public static void registerStartClientTick(Runnable runnable) {
//? if fabric {
		ClientTickEvents.START_CLIENT_TICK.register(minecraftServer -> runnable.run());
//? }

//? if neoforge {
		/*MainEventBusClient.startClientTickRunnable = runnable;
//
*///? }
	}

	public static void registerEndClientTick(Runnable runnable) {
//? if fabric {
		ClientTickEvents.END_CLIENT_TICK.register(minecraftServer -> runnable.run());
//? }

//? if neoforge {
		/*MainEventBusClient.endClientTickRunnable = runnable;
//
*///? }
	}

	public static void registerStartWorldTick(Consumer<ClientLevel> consumer) {
//? if fabric {
		ClientTickEvents.START_WORLD_TICK.register(consumer::accept);
//? }

//? if neoforge {
		/*MainEventBusClient.startWorldTickRunnable = consumer;
//
*///? }
	}

	public static void registerEndWorldTick(Consumer<ClientLevel> consumer) {
//? if fabric {
		ClientTickEvents.END_WORLD_TICK.register(consumer::accept);
//? }

//? if neoforge {
		/*MainEventBusClient.endWorldTickRunnable = consumer;
//
*///? }
	}

	public static void registerClientJoin(Runnable runnable) {
//? if fabric {
		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> runnable.run());
//? }

//? if neoforge {
		/*MainEventBusClient.clientJoinRunnable = runnable;
//
*///? }
	}

	public static void registerClientDisconnect(Runnable runnable) {
//? if fabric {
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> runnable.run());
//? }

//? if neoforge {
		/*MainEventBusClient.clientDisconnectRunnable = runnable;
//
*///? }
	}

	public static void registerChunkLoad(BiConsumer<ClientLevel, ChunkAccess> consumer) {
//? if fabric {
		ClientChunkEvents.CHUNK_LOAD.register(consumer::accept);
//? }

//? if neoforge {
		/*ModEventBusClient.chunkLoadConsumer = consumer;
//
*///? }
	}

	public static void registerChunkUnload(BiConsumer<ClientLevel, ChunkAccess> consumer) {
//? if fabric {
		ClientChunkEvents.CHUNK_UNLOAD.register(consumer::accept);
//? }

//? if neoforge {
		/*ModEventBusClient.chunkUnloadConsumer = consumer;
//
*///? }
	}

	public static void registerResourceReloadEvent(Runnable runnable) {
//? if fabric {
		final ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(Integer.toHexString(new Random().nextInt()), "resource");
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return identifier;
			}

			@Override
			public void onResourceManagerReload(ResourceManager manager) {
				runnable.run();
			}
		});
//? }

//? if neoforge {
		/*ModEventBusClient.resourceReloadRunnable = runnable;
//
*///? }
	}

	public static void registerWorldRenderEvent(MTRClient.WorldRenderCallback worldRenderCallback) {
//? if fabric {
		WorldRenderEvents.AFTER_ENTITIES.register(worldRenderContext -> {
			final PoseStack matrixStack = worldRenderContext.matrixStack();
			final MultiBufferSource vertexConsumerProvider = worldRenderContext.consumers();
			if (matrixStack != null && vertexConsumerProvider != null) {
				worldRenderCallback.accept(matrixStack, vertexConsumerProvider, worldRenderContext.camera().getPosition());
			}
		});
//? }

//? if neoforge {
		/*MainEventBusClient.worldRenderCallback = worldRenderCallback;
//
*///? }
	}

	public static void registerHudLayerRenderEvent(Consumer<GuiGraphics> hudLayerRenderCallback) {
//? if fabric {
//? if >= 1.21.4 {
		HudLayerRegistrationCallback.EVENT.register(layeredDrawerWrapper -> layeredDrawerWrapper.attachLayerBefore(IdentifiedLayer.CHAT, ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "gui"), (guiGraphics, delta) -> hudLayerRenderCallback.accept(guiGraphics)));
//? } else {
		/*HudRenderCallback.EVENT.register((guiGraphics, delta) -> hudLayerRenderCallback.accept(guiGraphics));
//
*///? }
//? }

//? if neoforge {
		/*MainEventBusClient.hudLayerRenderCallback = hudLayerRenderCallback;
//
*///? }
	}
}
