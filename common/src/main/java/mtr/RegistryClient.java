package mtr;

import dev.architectury.injectables.annotations.ExpectPlatform;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.BlockEntityRendererMapper;
import mtr.mappings.EntityRendererMapper;
import net.minecraft.client.KeyMapping;
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

public class RegistryClient {

	@ExpectPlatform
	public static void registerBlockRenderType(RenderType type, Block block) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerItemModelPredicate(String id, Item item, String tag) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends BlockEntityMapper> void registerTileEntityRenderer(BlockEntityType<T> type, Function<BlockEntityRenderDispatcher, BlockEntityRendererMapper<T>> function) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends Entity> void registerEntityRenderer(EntityType<T> type, Function<Object, EntityRendererMapper<T>> function) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerKeyBinding(KeyMapping keyMapping) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerBlockColors(Block block) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerNetworkReceiver(ResourceLocation resourceLocation, Consumer<FriendlyByteBuf> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerPlayerJoinEvent(Consumer<LocalPlayer> consumer) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendToServer(ResourceLocation id, FriendlyByteBuf packet) {
		throw new AssertionError();
	}
}
