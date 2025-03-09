package org.mtr.neoforge;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.chunk.WorldChunk;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.mtr.MTR;
import org.mtr.MTRClient;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@EventBusSubscriber(modid = MTR.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class MainEventBusClient {

	public static Runnable startClientTickRunnable = null;
	public static Runnable endClientTickRunnable = null;
	public static Runnable clientJoinRunnable = null;
	public static Runnable clientDisconnectRunnable = null;
	public static Consumer<ClientWorld> startWorldTickRunnable = null;
	public static Consumer<ClientWorld> endWorldTickRunnable = null;
	public static BiConsumer<ClientWorld, WorldChunk> chunkLoadConsumer = null;
	public static BiConsumer<ClientWorld, WorldChunk> chunkUnloadConsumer = null;
	public static MTRClient.WorldRenderCallback worldRenderCallback = null;

	@SubscribeEvent
	public static void clientTickStart(ClientTickEvent.Pre event) {
		if (startClientTickRunnable != null) {
			startClientTickRunnable.run();
		}
	}

	@SubscribeEvent
	public static void clientTickEnd(ClientTickEvent.Post event) {
		if (endClientTickRunnable != null) {
			endClientTickRunnable.run();
		}
	}

	@SubscribeEvent
	public static void worldTickStart(LevelTickEvent.Pre event) {
		if (startWorldTickRunnable != null && event.getLevel() instanceof ClientWorld clientWorld) {
			startWorldTickRunnable.accept(clientWorld);
		}
	}

	@SubscribeEvent
	public static void worldTickEnd(LevelTickEvent.Post event) {
		if (endWorldTickRunnable != null && event.getLevel() instanceof ClientWorld clientWorld) {
			endWorldTickRunnable.accept(clientWorld);
		}
	}

	@SubscribeEvent
	public static void clientJoin(ClientPlayerNetworkEvent.LoggingIn event) {
		if (clientJoinRunnable != null) {
			clientJoinRunnable.run();
		}
	}

	@SubscribeEvent
	public static void clientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
		if (clientDisconnectRunnable != null) {
			clientDisconnectRunnable.run();
		}
	}

	@SubscribeEvent
	public static void chunkLoad(ChunkEvent.Load event) {
		if (chunkLoadConsumer != null && event.getLevel() instanceof ClientWorld clientWorld && event.getChunk() instanceof WorldChunk worldChunk) {
			chunkLoadConsumer.accept(clientWorld, worldChunk);
		}
	}

	@SubscribeEvent
	public static void chunkUnload(ChunkEvent.Unload event) {
		if (chunkUnloadConsumer != null && event.getLevel() instanceof ClientWorld clientWorld && event.getChunk() instanceof WorldChunk worldChunk) {
			chunkUnloadConsumer.accept(clientWorld, worldChunk);
		}
	}

	@SubscribeEvent
	public static void worldRendering(RenderLevelStageEvent event) {
		if (worldRenderCallback != null && event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			worldRenderCallback.accept(event.getPoseStack(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers(), event.getCamera().getPos());
		}
	}
}
