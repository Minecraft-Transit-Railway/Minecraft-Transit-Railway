package org.mtr.registry;

//? if >= 26.1 {
/*import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
*///? }
import net.minecraft.core.BlockPos;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
//? if >= 26.1 {
/*import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;
*///? } else {
import net.minecraft.client.color.block.BlockColor;
//? }
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.mtr.packet.CustomPacketC2S;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketBufferSender;
import org.mtr.packet.PacketHandler;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.ToIntFunction;

//? if fabric {
//? if >= 26.1 {
/*import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
*///? } else {
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
//? }
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//? if >= 26.1 {
/*import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
*///? } else {
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
//? }
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import org.mtr.MTR;
import org.mtr.fabric.MTRFabric;
//? }

//? if neoforge {
/*import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.neoforge.network.PacketDistributor;
import org.mtr.neoforge.ModEventBus;
import org.mtr.neoforge.ModEventBusClient;
*///? }

public final class RegistryClient {

//? if >= 26.1 {
	/*// The provider names the render state it produces as well as the block entity it draws.
	public static <T extends BlockEntity, U extends T, S extends BlockEntityRenderState> void registerBlockEntityRenderer(ObjectHolder<BlockEntityType<U>> blockEntityType, BlockEntityRendererProvider<T, S> factory) {
*///? } else {
	public static <T extends BlockEntity, U extends T> void registerBlockEntityRenderer(ObjectHolder<BlockEntityType<U>> blockEntityType, BlockEntityRendererProvider<T> factory) {
//? }
//? if fabric {
		BlockEntityRenderers.register(blockEntityType.get(), factory);
//? }

//? if neoforge {
		/*ModEventBusClient.BLOCK_ENTITY_RENDERERS.add(event -> event.registerBlockEntityRenderer(blockEntityType.get(), factory));
//
*///? }
	}

	/**
	 * The layer a block is drawn in. Named here rather than taken as a Minecraft render type because
	 * the two versions disagree about what that is, and because from 26.1 it is not a runtime choice
	 * at all.
	 */
	public enum BlockRenderLayer {
		CUTOUT, TRANSLUCENT
	}

	/**
	 * Declares the layer a block is drawn in.
	 *
	 * <p>From 26.1 this does nothing. Neither loader offers a way to say it in code any more:
	 * Fabric's map and NeoForge's setter are both gone, and a block declares its own layer through
	 * the {@code render_type} field of its model instead. The calls are left in place so that the
	 * older versions keep working and so the intent stays visible in one list.</p>
	 */
	public static void registerBlockRenderType(BlockRenderLayer blockRenderLayer, ObjectHolder<Block> block) {
//? if >= 26.1 {
		/*// Declared in the block model rather than here.
*///? } else {
		final RenderType renderLayer = blockRenderLayer == BlockRenderLayer.TRANSLUCENT ? RenderType.translucent() : RenderType.cutout();

//? if fabric {
		BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderLayer);
//? }

//? if neoforge {
		/*ModEventBusClient.CLIENT_OBJECTS_TO_REGISTER.add(() -> ItemBlockRenderTypes.setRenderLayer(block.get(), renderLayer));
//
*///? }
//? }
	}

	public static void registerKeyBinding(KeyMapping keyBinding) {
//? if fabric {
		KeyBindingHelper.registerKeyBinding(keyBinding);
//? }

//? if neoforge {
		/*ModEventBusClient.KEY_BINDINGS.add(keyBinding);
//
*///? }
	}

	// Takes the position-to-colour function rather than a Minecraft interface, because that
	// interface changed shape in 26.1: BlockColor's single method carried a tint index and a
	// position, while BlockTintSource splits into a positionless colour and a colourInWorld. Both
	// are built from the same function here, and the positionless case passes null, which the
	// station colour lookup already answers with its own default.

	// Built here rather than at each registration site so the version guard sits outside the
	// per-loader blocks. A guard nested inside a commented-out block cannot be parsed.
//? if >= 26.1 {
	/*private static BlockTintSource createTintSource(ToIntFunction<BlockPos> blockColorProvider) {
		return new BlockTintSource() {
			@Override
			public int color(BlockState blockState) {
				return blockColorProvider.applyAsInt(null);
			}

			@Override
			public int colorInWorld(BlockState blockState, BlockAndTintGetter blockRenderView, BlockPos blockPos) {
				return blockColorProvider.applyAsInt(blockPos);
			}
		};
	}
*///? } else {
	private static BlockColor createTintSource(ToIntFunction<BlockPos> blockColorProvider) {
		return (blockState, blockRenderView, blockPos, tintIndex) -> blockColorProvider.applyAsInt(blockPos);
	}
//? }
	@SafeVarargs
	public static void registerBlockColors(ToIntFunction<BlockPos> blockColorProvider, ObjectHolder<Block>... blocks) {
//? if fabric {
//? if >= 26.1 {
		/*BlockColorRegistry.register(List.of(createTintSource(blockColorProvider)), Arrays.stream(blocks).map(ObjectHolder::get).toArray(Block[]::new));
*///? } else {
		ColorProviderRegistry.BLOCK.register(createTintSource(blockColorProvider), Arrays.stream(blocks).map(ObjectHolder::get).toArray(Block[]::new));
//? }
//? }

//? if neoforge {
		/*ModEventBusClient.BLOCK_COLORS.add(event -> event.getBlockColors().register(createTintSource(blockColorProvider), Arrays.stream(blocks).map(ObjectHolder::get).toArray(Block[]::new)));
//
*///? }
	}

	public static void setupPackets() {
//? if fabric {
		ClientPlayNetworking.registerGlobalReceiver(MTR.PACKET_IDENTIFIER_S2C, (customPacketS2C, context) -> PacketBufferReceiver.receive(customPacketS2C.buffer(), packetBufferReceiver -> {
			final Function<PacketBufferReceiver, ? extends PacketHandler> getInstance = MTRFabric.PACKETS.get(packetBufferReceiver.readString());
			if (getInstance != null) {
				getInstance.apply(packetBufferReceiver).runClient();
			}
		}, Minecraft.getInstance()::execute));
//? }

//? if neoforge {
		/*RegistryServer.s2cClientHandler = (customPacketS2C, context) -> PacketBufferReceiver.receive(customPacketS2C.buffer(), packetBufferReceiver -> {
			final Function<PacketBufferReceiver, ? extends PacketHandler> getInstance = ModEventBus.PACKETS.get(packetBufferReceiver.readString());
			if (getInstance != null) {
				getInstance.apply(packetBufferReceiver).runClient();
			}
		}, Minecraft.getInstance()::execute);
*///? }
	}

	public static <T extends PacketHandler> void sendPacketToServer(T data) {
//? if fabric {
		final PacketBufferSender packetBufferSender = new PacketBufferSender();
		packetBufferSender.writeString(data.getClass().getName());
		data.write(packetBufferSender);
		packetBufferSender.send(bytes -> ClientPlayNetworking.send(new CustomPacketC2S(bytes)), Minecraft.getInstance()::execute);
//? }

//? if neoforge {
		/*final PacketBufferSender packetBufferSender = new PacketBufferSender();
		packetBufferSender.writeString(data.getClass().getName());
		data.write(packetBufferSender);
		packetBufferSender.send(bytes -> PacketDistributor.sendToServer(new CustomPacketC2S(bytes)), Minecraft.getInstance()::execute);
*///? }
	}
}
