package mtr.fabric;

import mtr.MTRClient;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.EntityRendererMapper;
import mtr.mappings.FabricRegistryUtilities;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Consumer;
import java.util.function.Function;

public class RegistryClientImpl {

	public static void registerBlockRenderType(RenderType type, Block block) {
		BlockRenderLayerMap.INSTANCE.putBlock(block, type);
	}

	public static void registerItemModelPredicate(String id, Item item, String tag) {
		FabricRegistryUtilities.registerItemModelPredicate(id, item, tag);
	}

	public static <T extends BlockEntityMapper> void registerTileEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> function) {
		FabricRegistryUtilities.registerTileEntityRenderer(type, function);
	}

	public static <T extends Entity> void registerEntityRenderer(EntityType<T> type, Function<Object, EntityRendererMapper<T>> function) {
		FabricRegistryUtilities.registerEntityRenderer(type, function::apply);
	}

	public static void registerKeyBinding(KeyMapping keyMapping) {
		KeyBindingHelper.registerKeyBinding(keyMapping);
	}

	public static void registerBlockColors(Block block) {
		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> MTRClient.getStationColor(pos), block);
	}

	public static void registerNetworkReceiver(ResourceLocation resourceLocation, Consumer<FriendlyByteBuf> consumer) {
		ClientPlayNetworking.registerGlobalReceiver(resourceLocation, (client, handler, packet, responseSender) -> consumer.accept(packet));
	}

	public static void registerPlayerJoinEvent(Consumer<LocalPlayer> consumer) {
		ClientEntityEvents.ENTITY_LOAD.register((entity, clientWorld) -> {
			if (entity == Minecraft.getInstance().player) {
				consumer.accept((LocalPlayer) entity);
			}
		});
	}

	public static void registerTickEvent(Consumer<Minecraft> consumer) {
		ClientTickEvents.START_CLIENT_TICK.register(consumer::accept);
	}

	public static void sendToServer(ResourceLocation id, FriendlyByteBuf packet) {
		ClientPlayNetworking.send(id, packet);
	}
}
