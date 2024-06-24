package mtr;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mtr.mappings.NetworkUtilities;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
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

public class Registry {

	@ExpectPlatform
	public static boolean isFabric() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Supplier<CreativeModeTab> getCreativeModeTab(ResourceLocation id, Supplier<ItemStack> supplier) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerCreativeModeTab(ResourceLocation resourceLocation, Item item) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static Packet<?> createAddEntityPacket(Entity entity) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerNetworkReceiver(ResourceLocation resourceLocation, NetworkUtilities.PacketCallback packetCallback) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerPlayerJoinEvent(Consumer<ServerPlayer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerPlayerQuitEvent(Consumer<ServerPlayer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerServerStartingEvent(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerServerStoppingEvent(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerTickEvent(Consumer<MinecraftServer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf packet) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void setInTeleportationState(Player player, boolean isRiding) {
		throw new AssertionError();
	}
}
