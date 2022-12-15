package mtr.forge;

import mtr.mappings.ForgeUtilities;
import mtr.mappings.NetworkUtilities;
import mtr.mappings.RegistryUtilities;
import mtr.mixin.PlayerTeleportationStateAccessor;
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

public class RegistryImpl {

	public static boolean isFabric() {
		return false;
	}

	public static Supplier<CreativeModeTab> getCreativeModeTab(ResourceLocation id, Supplier<ItemStack> supplier) {
		return ForgeUtilities.createCreativeModeTab(id, supplier, String.format("itemGroup.%s.%s", id.getNamespace(), id.getPath()));
	}

	public static void registerCreativeModeTab(ResourceLocation resourceLocation, Item item) {
		ForgeUtilities.registerCreativeModeTab(resourceLocation, item);
	}

	public static Packet<?> createAddEntityPacket(Entity entity) {
		return ForgeUtilities.createAddEntityPacket(entity);
	}

	public static void registerNetworkReceiver(ResourceLocation resourceLocation, NetworkUtilities.PacketCallback packetCallback) {
		NetworkUtilities.registerReceiverC2S(resourceLocation, packetCallback);
	}

	public static void registerPlayerJoinEvent(Consumer<ServerPlayer> consumer) {
		RegistryUtilities.registerPlayerJoinEvent(consumer);
		RegistryUtilities.registerPlayerChangeDimensionEvent(consumer);
	}

	public static void registerPlayerQuitEvent(Consumer<ServerPlayer> consumer) {
		RegistryUtilities.registerPlayerQuitEvent(consumer);
	}

	public static void registerServerStartingEvent(Consumer<MinecraftServer> consumer) {
		RegistryUtilities.registerServerStartingEvent(consumer);
	}

	public static void registerServerStoppingEvent(Consumer<MinecraftServer> consumer) {
		RegistryUtilities.registerServerStoppingEvent(consumer);
	}

	public static void registerTickEvent(Consumer<MinecraftServer> consumer) {
		RegistryUtilities.registerTickEvent(consumer);
	}

	public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf packet) {
		NetworkUtilities.sendToPlayer(player, id, packet);
	}

	public static void setInTeleportationState(Player player, boolean isRiding) {
		((PlayerTeleportationStateAccessor) player).setInTeleportationState(isRiding);
	}
}
