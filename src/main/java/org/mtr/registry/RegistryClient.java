package org.mtr.registry;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
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

//? if fabric {
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
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

	public static <T extends BlockEntity, U extends T> void registerBlockEntityRenderer(ObjectHolder<BlockEntityType<U>> blockEntityType, BlockEntityRendererProvider<T> factory) {
//? if fabric {
		BlockEntityRenderers.register(blockEntityType.get(), factory);
//? }

//? if neoforge {
		/*ModEventBusClient.BLOCK_ENTITY_RENDERERS.add(event -> event.registerBlockEntityRenderer(blockEntityType.get(), factory));
//
*///? }
	}

	public static void registerBlockRenderType(RenderType renderLayer, ObjectHolder<Block> block) {
//? if fabric {
		BlockRenderLayerMap.INSTANCE.putBlock(block.get(), renderLayer);
//? }

//? if neoforge {
		/*ModEventBusClient.CLIENT_OBJECTS_TO_REGISTER.add(() -> ItemBlockRenderTypes.setRenderLayer(block.get(), renderLayer));
//
*///? }
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

	@SafeVarargs
	public static void registerBlockColors(BlockColor blockColorProvider, ObjectHolder<Block>... blocks) {
//? if fabric {
		ColorProviderRegistry.BLOCK.register(blockColorProvider, Arrays.stream(blocks).map(ObjectHolder::get).toArray(Block[]::new));
//? }

//? if neoforge {
		/*ModEventBusClient.BLOCK_COLORS.add(event -> event.getBlockColors().register(blockColorProvider, Arrays.stream(blocks).map(ObjectHolder::get).toArray(Block[]::new)));
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
