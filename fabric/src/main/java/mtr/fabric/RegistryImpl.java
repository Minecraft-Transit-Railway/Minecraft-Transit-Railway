package mtr.fabric;

import mtr.mappings.FabricRegistryUtilities;
import mtr.mappings.NetworkUtilities;
import mtr.mixin.PlayerTeleportationStateAccessor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistryImpl {

	public static boolean isFabric() {
		return true;
	}

	public static Supplier<CreativeModeTab> getCreativeModeTab(ResourceLocation id, Supplier<ItemStack> supplier) {
		return () -> FabricRegistryUtilities.createCreativeModeTab(id, supplier);
	}

	public static void registerCreativeModeTab(ResourceLocation resourceLocation, Item item) {
	}

	public static Packet<?> createAddEntityPacket(Entity entity) {
		return new ClientboundAddEntityPacket(entity);
	}

	public static void registerNetworkReceiver(ResourceLocation resourceLocation, NetworkUtilities.PacketCallback packetCallback) {
		ServerPlayNetworking.registerGlobalReceiver(resourceLocation, (server, player, handler, packet, responseSender) -> packetCallback.packetCallback(server, player, packet));
	}

	public static void registerPlayerJoinEvent(Consumer<ServerPlayer> consumer) {
		ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
			if (entity instanceof ServerPlayer) {
				consumer.accept((ServerPlayer) entity);
			}
		});
	}

	public static void registerPlayerQuitEvent(Consumer<ServerPlayer> consumer) {
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> consumer.accept(handler.player));
	}

	public static void registerServerStartingEvent(Consumer<MinecraftServer> consumer) {
		ServerLifecycleEvents.SERVER_STARTING.register(consumer::accept);
	}

	public static void registerServerStoppingEvent(Consumer<MinecraftServer> consumer) {
		ServerLifecycleEvents.SERVER_STOPPING.register(consumer::accept);
	}

	public static void registerTickEvent(Consumer<MinecraftServer> consumer) {
		ServerTickEvents.START_SERVER_TICK.register(consumer::accept);
	}

	public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf packet) {
		ServerPlayNetworking.send(player, id, packet);
	}

	public static void setInTeleportationState(Player player, boolean isRiding) {
		((PlayerTeleportationStateAccessor) player).setInTeleportationState(isRiding);
	}
}
