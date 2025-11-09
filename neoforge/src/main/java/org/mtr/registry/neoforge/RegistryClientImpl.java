package org.mtr.registry.neoforge;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import org.mtr.MTR;
import org.mtr.neoforge.ModEventBus;
import org.mtr.neoforge.ModEventBusClient;
import org.mtr.packet.CustomPacketS2C;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketBufferSender;
import org.mtr.packet.PacketHandler;
import org.mtr.registry.ObjectHolder;

import java.util.Arrays;
import java.util.function.Function;

public final class RegistryClientImpl {

	public static <T extends BlockEntity, U extends T> void registerBlockEntityRenderer(ObjectHolder<BlockEntityType<U>> blockEntityType, BlockEntityRendererFactory<T> factory) {
		ModEventBusClient.BLOCK_ENTITY_RENDERERS.add(event -> event.registerBlockEntityRenderer(blockEntityType.get(), factory));
	}

	public static <T extends Entity, U extends T> void registerEntityRenderer(ObjectHolder<EntityType<U>> entityType, EntityRendererFactory<T> factory) {
		ModEventBusClient.BLOCK_ENTITY_RENDERERS.add(event -> event.registerEntityRenderer(entityType.get(), factory));
	}

	public static void registerBlockRenderType(RenderLayer renderLayer, ObjectHolder<Block> block) {
		ModEventBusClient.CLIENT_OBJECTS_TO_REGISTER.add(() -> RenderLayers.setRenderLayer(block.get(), renderLayer));
	}

	public static void registerKeyBinding(KeyBinding keyBinding) {
		ModEventBusClient.KEY_BINDINGS.add(keyBinding);
	}

	@SafeVarargs
	public static void registerBlockColors(BlockColorProvider blockColorProvider, ObjectHolder<Block>... blocks) {
		ModEventBusClient.BLOCK_COLORS.add(event -> event.getBlockColors().registerColorProvider(blockColorProvider, Arrays.stream(blocks).map(ObjectHolder::get).toArray(Block[]::new)));
	}

	public static void setupPackets() {
		ModEventBus.PAYLOAD_HANDLERS.add(payloadRegistrar -> payloadRegistrar.playBidirectional(MTR.PACKET_IDENTIFIER_S2C, PacketCodec.tuple(PacketCodecs.BYTE_ARRAY, CustomPacketS2C::buffer, CustomPacketS2C::new), new DirectionalPayloadHandler<>((customPacketS2C, context) -> PacketBufferReceiver.receive(customPacketS2C.buffer(), packetBufferReceiver -> {
			final Function<PacketBufferReceiver, ? extends PacketHandler> getInstance = ModEventBus.PACKETS.get(packetBufferReceiver.readString());
			if (getInstance != null) {
				getInstance.apply(packetBufferReceiver).runClient();
			}
		}, MinecraftClient.getInstance()::execute), (customPacketS2C, context) -> {
		})));
	}

	public static <T extends PacketHandler> void sendPacketToServer(T data) {
		final PacketBufferSender packetBufferSender = new PacketBufferSender();
		packetBufferSender.writeString(data.getClass().getName());
		data.write(packetBufferSender);
		packetBufferSender.send(bytes -> PacketDistributor.sendToServer(new CustomPacketS2C(bytes)), MinecraftClient.getInstance()::execute);
	}
}
