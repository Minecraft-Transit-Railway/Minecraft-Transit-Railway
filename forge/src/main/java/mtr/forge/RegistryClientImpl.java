package mtr.forge;

import mtr.MTRClient;
import mtr.mappings.*;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;
import java.util.function.Function;

public class RegistryClientImpl {

	public static void registerBlockRenderType(RenderType type, Block block) {
		RegistryUtilitiesClient.registerRenderType(type, block);
	}

	public static void registerItemModelPredicate(String id, Item item, String tag) {
		RegistryUtilitiesClient.registerItemModelPredicate(id, item, tag);
	}

	public static <T extends BlockEntityMapper> void registerTileEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> function) {
		RegistryUtilitiesClient.registerTileEntityRenderer(type, function);
	}

	public static <T extends Entity> void registerEntityRenderer(EntityType<T> type, Function<Object, EntityRendererMapper<T>> function) {
		RegistryUtilitiesClient.registerEntityRenderer(type, function::apply);
	}

	public static void registerKeyBinding(KeyMapping keyMapping) {
		ForgeUtilities.registerKeyBinding(keyMapping);
	}

	public static void registerBlockColors(Block block) {
		RegistryUtilitiesClient.registerBlockColors(new StationColor(), block);
	}

	public static void registerNetworkReceiver(ResourceLocation resourceLocation, Consumer<FriendlyByteBuf> consumer) {
		NetworkUtilities.registerReceiverS2C(resourceLocation, (packet, context) -> consumer.accept(packet));
	}

	public static void registerPlayerJoinEvent(Consumer<LocalPlayer> consumer) {
		RegistryUtilitiesClient.registerPlayerJoinEvent(consumer);
	}

	public static void registerTickEvent(Consumer<Minecraft> consumer) {
		RegistryUtilitiesClient.registerClientTickEvent(consumer);
	}

	public static void sendToServer(ResourceLocation id, FriendlyByteBuf packet) {
		NetworkUtilities.sendToServer(id, packet);
	}

	private static class StationColor implements BlockColor {

		@Override
		public int getColor(BlockState blockState, BlockAndTintGetter blockAndTintGetter, BlockPos pos, int i) {
			return MTRClient.getStationColor(pos);
		}
	}
}
