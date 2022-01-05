package mtr.forge;

import mtr.mappings.NetworkUtilities;
import mtr.mappings.RegistryUtilities;
import mtr.mixin.PlayerTeleportationStateAccessor;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class RegistryImpl {

	public static CreativeModeTab getItemGroup(ResourceLocation id, Supplier<ItemStack> supplier) {
		return RegistryUtilities.createCreativeTab(id, supplier);
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
