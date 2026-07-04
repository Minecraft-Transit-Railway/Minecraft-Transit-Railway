package org.mtr.neoforge;

//? if neoforge {

/*import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
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
	public static Consumer<ClientLevel> startWorldTickRunnable = null;
	public static Consumer<ClientLevel> endWorldTickRunnable = null;
	public static MTRClient.WorldRenderCallback worldRenderCallback = null;
	public static Consumer<GuiGraphics> hudLayerRenderCallback = null;

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
		if (startWorldTickRunnable != null && event.getLevel() instanceof ClientLevel clientWorld) {
			startWorldTickRunnable.accept(clientWorld);
		}
	}

	@SubscribeEvent
	public static void worldTickEnd(LevelTickEvent.Post event) {
		if (endWorldTickRunnable != null && event.getLevel() instanceof ClientLevel clientWorld) {
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
	public static void worldRendering(RenderLevelStageEvent event) {
		if (worldRenderCallback != null && event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			worldRenderCallback.accept(event.getPoseStack(), Minecraft.getInstance().renderBuffers().bufferSource(), event.getCamera().getPosition());
		}
	}

	@SubscribeEvent
	public static void guiRendering(RenderGuiEvent.Pre event) {
		if (hudLayerRenderCallback != null) {
			hudLayerRenderCallback.accept(event.getGuiGraphics());
		}
	}
}

*///? }
