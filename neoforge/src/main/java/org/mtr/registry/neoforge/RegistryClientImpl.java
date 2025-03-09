package org.mtr.registry.neoforge;

import io.netty.buffer.Unpooled;
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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.neoforge.MainEventBusClient;
import org.mtr.neoforge.ModEventBus;
import org.mtr.neoforge.ModEventBusClient;
import org.mtr.packet.CustomPacket;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketBufferSender;
import org.mtr.packet.PacketHandler;
import org.mtr.registry.ObjectHolder;

import java.util.Arrays;
import java.util.function.Function;

public final class RegistryClientImpl {

	private static final Identifier PACKETS_IDENTIFIER = Identifier.of(MTR.MOD_ID, "packet");

	public static <T extends BlockEntity, U extends T> void registerBlockEntityRenderer(ObjectHolder<BlockEntityType<U>> blockEntityType, BlockEntityRendererFactory<T> factory) {
		ModEventBusClient.BLOCK_ENTITY_RENDERERS.add(event -> event.registerBlockEntityRenderer(blockEntityType.createAndGet(), factory));
	}

	public static <T extends Entity, U extends T> void registerEntityRenderer(ObjectHolder<EntityType<U>> entityType, EntityRendererFactory<T> factory) {
		ModEventBusClient.BLOCK_ENTITY_RENDERERS.add(event -> event.registerEntityRenderer(entityType.createAndGet(), factory));
	}

	public static void registerBlockRenderType(RenderLayer renderLayer, ObjectHolder<Block> block) {
		ModEventBusClient.CLIENT_OBJECTS_TO_REGISTER.add(() -> RenderLayers.setRenderLayer(block.createAndGet(), renderLayer));
	}

	public static void registerKeyBinding(KeyBinding keyBinding) {
		ModEventBusClient.KEY_BINDINGS.add(keyBinding);
	}

	@SafeVarargs
	public static void registerBlockColors(BlockColorProvider blockColorProvider, ObjectHolder<Block>... blocks) {
		ModEventBusClient.BLOCK_COLORS.add(event -> event.getBlockColors().registerColorProvider(blockColorProvider, Arrays.stream(blocks).map(ObjectHolder::createAndGet).toArray(Block[]::new)));
	}

	public static void setupPackets() {
		final CustomPayload.Id<CustomPacket> id = new CustomPayload.Id<>(PACKETS_IDENTIFIER);
		ModEventBus.PAYLOAD_HANDLERS.add(payloadRegistrar -> payloadRegistrar.playToClient(id, PacketCodec.of(CustomPacket::encode, registryByteBuf -> new CustomPacket(id, registryByteBuf)), new DirectionalPayloadHandler<>((customPacket, context) -> PacketBufferReceiver.receive(customPacket.packetByteBuf(), packetBufferReceiver -> {
			final Function<PacketBufferReceiver, ? extends PacketHandler> getInstance = ModEventBus.PACKETS.get(packetBufferReceiver.readString());
			if (getInstance != null) {
				getInstance.apply(packetBufferReceiver).runClient();
			}
		}, MinecraftClient.getInstance()::execute), (customPacket, context) -> {
		})));
	}

	public static <T extends PacketHandler> void sendPacketToServer(T data) {
		final CustomPayload.Id<CustomPacket> id = new CustomPayload.Id<>(ModEventBus.PACKETS_IDENTIFIER);
		final PacketBufferSender packetBufferSender = new PacketBufferSender(Unpooled::buffer);
		packetBufferSender.writeString(data.getClass().getName());
		data.write(packetBufferSender);
		packetBufferSender.send(byteBuf -> PacketDistributor.sendToServer(new CustomPacket(id, byteBuf instanceof PacketByteBuf ? (PacketByteBuf) byteBuf : new PacketByteBuf(byteBuf))), MinecraftClient.getInstance()::execute);
	}

	public static void registerWorldRenderEvent(MTRClient.WorldRenderCallback worldRenderCallback) {
		MainEventBusClient.worldRenderCallback = worldRenderCallback;
	}
}
