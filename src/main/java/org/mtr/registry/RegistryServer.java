package org.mtr.registry;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.packet.*;

import java.util.function.*;

//? if fabric {
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import org.mtr.fabric.MTRFabric;
//? }

//? if neoforge {
/*import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.mtr.neoforge.MTRNeoForge;
import org.mtr.neoforge.MainEventBus;
import org.mtr.neoforge.ModEventBus;
*///? }

public final class RegistryServer {

//? if neoforge {
	/*public static BiConsumer<CustomPacketS2C, IPayloadContext> s2cClientHandler = (packet, context) -> {
	};
*///? }

	private static final ObjectArrayList<Runnable> OBJECTS_TO_REGISTER = new ObjectArrayList<>();
	private static final Object2ObjectOpenHashMap<String, ObjectArrayList<Supplier<ItemLike>>> ITEM_GROUP_ENTRIES = new Object2ObjectOpenHashMap<>();

	public static void init() {
//? if fabric {
		OBJECTS_TO_REGISTER.forEach(Runnable::run);
//? }
	}

	public static ObjectHolder<Block> registerBlock(String registryName, Function<BlockBehaviour.Properties, Block> factory) {
//? if fabric {
		return register(BuiltInRegistries.BLOCK, Registries.BLOCK, registryName, dataRegistryKey -> factory.apply(BlockBehaviour.Properties.of()
//? if >= 1.21.4 {
				.setId(dataRegistryKey)
//? }
		));
//? }

//? if neoforge {
		/*return new ObjectHolder<>(MTRNeoForge.BLOCKS.register(registryName, identifier -> factory.apply(BlockBehaviour.Properties.of()
//? if >= 1.21.4 {
			.setId(ResourceKey.create(Registries.BLOCK, identifier))
//? }
		)));
*///? }
	}

	public static ObjectHolder<Item> registerItem(String registryName, Function<Item.Properties, Item> factory, @Nullable String itemGroupRegistryName) {
//? if fabric {
		final ObjectHolder<Item> objectHolder = register(BuiltInRegistries.ITEM, Registries.ITEM, registryName, dataRegistryKey -> factory.apply(new Item.Properties()
//? if >= 1.21.4 {
				.setId(dataRegistryKey)
//? }
		));
		if (itemGroupRegistryName != null) {
			ITEM_GROUP_ENTRIES.computeIfAbsent(itemGroupRegistryName, key -> new ObjectArrayList<>()).add(objectHolder::get);
		}
		return objectHolder;
//? }

//? if neoforge {
		/*final ObjectHolder<Item> objectHolder = new ObjectHolder<>(MTRNeoForge.ITEMS.register(registryName, identifier -> factory.apply(new Item.Properties()
//? if >= 1.21.4 {
			.setId(ResourceKey.create(Registries.ITEM, identifier))
//? }
		)));
		if (itemGroupRegistryName != null) {
			ITEM_GROUP_ENTRIES.computeIfAbsent(itemGroupRegistryName, key -> new ObjectArrayList<>()).add(objectHolder::get);
		}
		return objectHolder;
*///? }
	}

	public static <T extends BlockEntity> ObjectHolder<BlockEntityType<T>> registerBlockEntityType(String registryName, BiFunction<BlockPos, BlockState, T> factory, Supplier<Block> blockSupplier) {
//? if fabric {
		return register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Registries.BLOCK_ENTITY_TYPE, registryName, dataRegistryKey -> FabricBlockEntityTypeBuilder.create(factory::apply, blockSupplier.get()).build());
//? }

//? if neoforge {
/*//? if >= 1.21.4 {
		return new ObjectHolder<>(MTRNeoForge.BLOCK_ENTITY_TYPES.register(registryName, () -> new BlockEntityType<>(factory::apply, blockSupplier.get())));
//? } else {
		return new ObjectHolder<>(MTRNeoForge.BLOCK_ENTITY_TYPES.register(registryName, () -> BlockEntityType.Builder.of(factory::apply, blockSupplier.get()).build(null)));
//? }
*///? }
	}

	public static String registerItemGroup(String registryName, Supplier<ItemStack> iconSupplier) {
//? if fabric {
		register(BuiltInRegistries.CREATIVE_MODE_TAB, Registries.CREATIVE_MODE_TAB, registryName, dataRegistryKey -> FabricItemGroup.builder().icon(iconSupplier).title(Component.translatable(String.format("itemGroup.%s.%s", MTR.MOD_ID, registryName))).displayItems((displayContext, entries) -> ITEM_GROUP_ENTRIES.getOrDefault(registryName, new ObjectArrayList<>()).forEach(itemSupplier -> entries.accept(itemSupplier.get()))).build());
		return registryName;
//? }

//? if neoforge {
		/*MTRNeoForge.ITEM_GROUPS.register(registryName, () -> CreativeModeTab.builder().icon(iconSupplier).title(Component.translatable(String.format("itemGroup.%s.%s", MTR.MOD_ID, registryName))).displayItems((displayContext, entries) -> ITEM_GROUP_ENTRIES.getOrDefault(registryName, new ObjectArrayList<>()).forEach(itemSupplier -> entries.accept(itemSupplier.get()))).build());
		return registryName;
*///? }
	}

	public static ObjectHolder<SoundEvent> registerSoundEvent(String registryName, Supplier<SoundEvent> supplier) {
//? if fabric {
		return register(BuiltInRegistries.SOUND_EVENT, Registries.SOUND_EVENT, registryName, dataRegistryKey -> supplier.get());
//? }

//? if neoforge {
		/*return new ObjectHolder<>(MTRNeoForge.SOUND_EVENTS.register(registryName, supplier));
//
*///? }
	}

	public static void registerCommands(Consumer<CommandDispatcher<CommandSourceStack>> consumer) {
//? if fabric {
		CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, environment) -> consumer.accept(dispatcher));
//? }

//? if neoforge {
		/*MainEventBus.commandConsumer = consumer;
//
*///? }
	}

	public static <T> ObjectHolder<DataComponentType<T>> registerDataComponentType(String registryName, Supplier<DataComponentType<T>> supplier) {
//? if fabric {
		return register(BuiltInRegistries.DATA_COMPONENT_TYPE, Registries.DATA_COMPONENT_TYPE, registryName, dataRegistryKey -> supplier.get());
//? }

//? if neoforge {
		/*return new ObjectHolder<>(MTRNeoForge.DATA_COMPONENT_TYPES.register(registryName, supplier));
//
*///? }
	}

	public static void setupPackets() {
//? if fabric {
		PayloadTypeRegistry.playS2C().register(MTR.PACKET_IDENTIFIER_S2C, StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, CustomPacketS2C::buffer, CustomPacketS2C::new));
		PayloadTypeRegistry.playC2S().register(MTR.PACKET_IDENTIFIER_C2S, StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, CustomPacketC2S::buffer, CustomPacketC2S::new));
		ServerPlayNetworking.registerGlobalReceiver(MTR.PACKET_IDENTIFIER_C2S, (customPacketC2S, context) -> PacketBufferReceiver.receive(customPacketC2S.buffer(), packetBufferReceiver -> {
			final Function<PacketBufferReceiver, ? extends PacketHandler> getInstance = MTRFabric.PACKETS.get(packetBufferReceiver.readString());
			if (getInstance != null) {
				getInstance.apply(packetBufferReceiver).runServer(context.player().server, context.player());
			}
		}, context.player().server::execute));
//? }

//? if neoforge {
		/*ModEventBus.PAYLOAD_HANDLERS.add(payloadRegistrar -> payloadRegistrar.playBidirectional(MTR.PACKET_IDENTIFIER_C2S, StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, CustomPacketC2S::buffer, CustomPacketC2S::new), new DirectionalPayloadHandler<>((customPacketC2S, context) -> {
		}, (customPacketC2S, context) -> {
			final Player player = context.player();
			if (player instanceof ServerPlayer) {
				PacketBufferReceiver.receive(customPacketC2S.buffer(), packetBufferReceiver -> {
					final Function<PacketBufferReceiver, ? extends PacketHandler> getInstance = ModEventBus.PACKETS.get(packetBufferReceiver.readString());
					if (getInstance != null) {
						getInstance.apply(packetBufferReceiver).runServer(((ServerPlayer) player).server, (ServerPlayer) player);
					}
				}, ((ServerPlayer) player).server::execute);
			}
		})));
		ModEventBus.PAYLOAD_HANDLERS.add(payloadRegistrar -> payloadRegistrar.playBidirectional(MTR.PACKET_IDENTIFIER_S2C, StreamCodec.composite(ByteBufCodecs.BYTE_ARRAY, CustomPacketS2C::buffer, CustomPacketS2C::new), new DirectionalPayloadHandler<>(s2cClientHandler::accept, (customPacketS2C, context) -> {
		})));
*///? }
	}

	public static <T extends PacketHandler> void registerPacket(Class<T> classObject, Function<PacketBufferReceiver, T> getInstance) {
//? if fabric {
		MTRFabric.PACKETS.put(classObject.getName(), getInstance);
//? }

//? if neoforge {
		/*ModEventBus.PACKETS.put(classObject.getName(), getInstance);
//
*///? }
	}

	public static <T extends PacketHandler> void sendPacketToClient(ServerPlayer serverPlayerEntity, T data) {
//? if fabric {
		final PacketBufferSender packetBufferSender = new PacketBufferSender();
		packetBufferSender.writeString(data.getClass().getName());
		data.write(packetBufferSender);
		packetBufferSender.send(bytes -> ServerPlayNetworking.send(serverPlayerEntity, new CustomPacketS2C(bytes)), serverPlayerEntity.server::execute);
//? }

//? if neoforge {
		/*final PacketBufferSender packetBufferSender = new PacketBufferSender();
		packetBufferSender.writeString(data.getClass().getName());
		data.write(packetBufferSender);
		packetBufferSender.send(bytes -> PacketDistributor.sendToPlayer(serverPlayerEntity, new CustomPacketS2C(bytes)), serverPlayerEntity.server::execute);
*///? }
	}

	private static <T extends U, U> ObjectHolder<T> register(Registry<U> registry, ResourceKey<Registry<U>> registryKey, String registryName, Function<ResourceKey<U>, T> factory) {
		final ResourceKey<U> dataRegistryKey = ResourceKey.create(registryKey, ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, registryName));
		final T data = Registry.register(registry, dataRegistryKey, factory.apply(dataRegistryKey));
		final ObjectHolder<T> objectHolder = new ObjectHolder<>(() -> data);
		OBJECTS_TO_REGISTER.add(objectHolder::get);
		return objectHolder;
	}
}
