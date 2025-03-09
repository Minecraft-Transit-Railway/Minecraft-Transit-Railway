package org.mtr.registry.neoforge;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import org.mtr.MTR;
import org.mtr.neoforge.MTRNeoForge;
import org.mtr.neoforge.MainEventBus;
import org.mtr.neoforge.ModEventBus;
import org.mtr.packet.CustomPacket;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketBufferSender;
import org.mtr.packet.PacketHandler;
import org.mtr.registry.ObjectHolder;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class RegistryImpl {

	private static final Object2ObjectOpenHashMap<String, ObjectArrayList<Supplier<ItemConvertible>>> ITEM_GROUP_ENTRIES = new Object2ObjectOpenHashMap<>();

	public static ObjectHolder<Block> registerBlock(String registryName, Function<AbstractBlock.Settings, Block> factory) {
		return new ObjectHolder<>(MTRNeoForge.BLOCKS.register(registryName, identifier -> factory.apply(AbstractBlock.Settings.create().registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier)))));
	}

	public static ObjectHolder<Item> registerItem(String registryName, Function<Item.Settings, Item> factory, @Nullable String itemGroupRegistryName) {
		final ObjectHolder<Item> objectHolder = new ObjectHolder<>(MTRNeoForge.ITEMS.register(registryName, identifier -> factory.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, identifier)))));
		if (itemGroupRegistryName != null) {
			ITEM_GROUP_ENTRIES.computeIfAbsent(itemGroupRegistryName, key -> new ObjectArrayList<>()).add(objectHolder::createAndGet);
		}
		return objectHolder;
	}

	public static <T extends BlockEntity> ObjectHolder<BlockEntityType<T>> registerBlockEntityType(String registryName, BiFunction<BlockPos, BlockState, T> factory, Supplier<Block> blockSupplier) {
		return new ObjectHolder<>(MTRNeoForge.BLOCK_ENTITY_TYPES.register(registryName, () -> new BlockEntityType<>(factory::apply, blockSupplier.get())));
	}

	public static <T extends Entity> ObjectHolder<EntityType<T>> registerEntityType(String registryName, BiFunction<EntityType<T>, World, T> factory, float width, float height) {
		return new ObjectHolder<>(MTRNeoForge.ENTITY_TYPES.register(registryName, identifier -> EntityType.Builder.create(factory::apply, SpawnGroup.MISC).dimensions(width, height).build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(MTR.MOD_ID, registryName)))));
	}

	public static String registerItemGroup(String registryName, Supplier<ItemStack> iconSupplier) {
		MTRNeoForge.ITEM_GROUPS.register(registryName, () -> ItemGroup.builder().icon(iconSupplier).entries((displayContext, entries) -> ITEM_GROUP_ENTRIES.getOrDefault(registryName, new ObjectArrayList<>()).forEach(itemSupplier -> entries.add(itemSupplier.get()))).build());
		return registryName;
	}

	@SafeVarargs
	public static void registerCommands(LiteralArgumentBuilder<ServerCommandSource>... commands) {
		MainEventBus.COMMANDS.addAll(Arrays.asList(commands));
	}

	public static void setupPackets() {
		final CustomPayload.Id<CustomPacket> id = new CustomPayload.Id<>(ModEventBus.PACKETS_IDENTIFIER);
		ModEventBus.PAYLOAD_HANDLERS.add(payloadRegistrar -> payloadRegistrar.playToServer(id, PacketCodec.of(CustomPacket::encode, registryByteBuf -> new CustomPacket(id, registryByteBuf)), new DirectionalPayloadHandler<>((customPacket, context) -> {
		}, (customPacket, context) -> {
			final PlayerEntity player = context.player();
			if (player instanceof ServerPlayerEntity) {
				PacketBufferReceiver.receive(customPacket.packetByteBuf(), packetBufferReceiver -> {
					final Function<PacketBufferReceiver, ? extends PacketHandler> getInstance = ModEventBus.PACKETS.get(packetBufferReceiver.readString());
					if (getInstance != null) {
						getInstance.apply(packetBufferReceiver).runServer(((ServerPlayerEntity) player).server, (ServerPlayerEntity) player);
					}
				}, ((ServerPlayerEntity) player).server::execute);
			}
		})));
	}

	public static <T extends PacketHandler> void registerPacket(Class<T> classObject, Function<PacketBufferReceiver, T> getInstance) {
		ModEventBus.PACKETS.put(classObject.getName(), getInstance);
	}

	public static <T extends PacketHandler> void sendPacketToClient(ServerPlayerEntity serverPlayerEntity, T data) {
		final CustomPayload.Id<CustomPacket> id = new CustomPayload.Id<>(ModEventBus.PACKETS_IDENTIFIER);
		final PacketBufferSender packetBufferSender = new PacketBufferSender(Unpooled::buffer);
		packetBufferSender.writeString(data.getClass().getName());
		data.write(packetBufferSender);
		packetBufferSender.send(byteBuf -> PacketDistributor.sendToPlayer(serverPlayerEntity, new CustomPacket(id, byteBuf instanceof PacketByteBuf ? (PacketByteBuf) byteBuf : new PacketByteBuf(byteBuf))), serverPlayerEntity.server::execute);
	}

	public static ObjectHolder<SoundEvent> registerSoundEvent(String registryName, Supplier<SoundEvent> supplier) {
		return new ObjectHolder<>(MTRNeoForge.SOUND_EVENTS.register(registryName, supplier));
	}
}
