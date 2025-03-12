package org.mtr.registry.fabric;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.fabric.MTRFabric;
import org.mtr.packet.CustomPacket;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketBufferSender;
import org.mtr.packet.PacketHandler;
import org.mtr.registry.ObjectHolder;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class RegistryImpl {

	private static final Object2ObjectOpenHashMap<String, ObjectArrayList<Supplier<ItemConvertible>>> ITEM_GROUP_ENTRIES = new Object2ObjectOpenHashMap<>();
	private static final ObjectArrayList<Runnable> OBJECTS_TO_REGISTER = new ObjectArrayList<>();

	public static void init() {
		OBJECTS_TO_REGISTER.forEach(Runnable::run);
	}

	public static ObjectHolder<Block> registerBlock(String registryName, Function<AbstractBlock.Settings, Block> factory) {
		return register(Registries.BLOCK, RegistryKeys.BLOCK, registryName, dataRegistryKey -> factory.apply(AbstractBlock.Settings.create().registryKey(dataRegistryKey)));
	}

	public static ObjectHolder<Item> registerItem(String registryName, Function<Item.Settings, Item> factory, @Nullable String itemGroupRegistryName) {
		final ObjectHolder<Item> objectHolder = register(Registries.ITEM, RegistryKeys.ITEM, registryName, dataRegistryKey -> factory.apply(new Item.Settings().registryKey(dataRegistryKey)));
		if (itemGroupRegistryName != null) {
			ITEM_GROUP_ENTRIES.computeIfAbsent(itemGroupRegistryName, key -> new ObjectArrayList<>()).add(objectHolder::createAndGet);
		}
		return objectHolder;
	}

	public static <T extends BlockEntity> ObjectHolder<BlockEntityType<T>> registerBlockEntityType(String registryName, BiFunction<BlockPos, BlockState, T> factory, Supplier<Block> blockSupplier) {
		return register(Registries.BLOCK_ENTITY_TYPE, RegistryKeys.BLOCK_ENTITY_TYPE, registryName, dataRegistryKey -> FabricBlockEntityTypeBuilder.create(factory::apply, blockSupplier.get()).build());
	}

	public static <T extends Entity> ObjectHolder<EntityType<T>> registerEntityType(String registryName, BiFunction<EntityType<T>, World, T> factory, float width, float height) {
		return register(Registries.ENTITY_TYPE, RegistryKeys.ENTITY_TYPE, registryName, dataRegistryKey -> EntityType.Builder.create(factory::apply, SpawnGroup.MISC).dimensions(width, height).build(dataRegistryKey));
	}

	public static String registerItemGroup(String registryName, Supplier<ItemStack> iconSupplier) {
		register(Registries.ITEM_GROUP, RegistryKeys.ITEM_GROUP, registryName, dataRegistryKey -> FabricItemGroup.builder().icon(iconSupplier).displayName(Text.literal("itemGroup." + registryName)).entries((displayContext, entries) -> ITEM_GROUP_ENTRIES.getOrDefault(registryName, new ObjectArrayList<>()).forEach(itemSupplier -> entries.add(itemSupplier.get()))).build());
		return registryName;
	}

	public static ObjectHolder<SoundEvent> registerSoundEvent(String registryName, Supplier<SoundEvent> supplier) {
		return register(Registries.SOUND_EVENT, RegistryKeys.SOUND_EVENT, registryName, dataRegistryKey -> supplier.get());
	}

	public static void registerCommands(Consumer<CommandDispatcher<ServerCommandSource>> consumer) {
		CommandRegistrationCallback.EVENT.register((dispatcher, commandRegistryAccess, environment) -> consumer.accept(dispatcher));
	}

	public static void setupPackets() {
		PayloadTypeRegistry.playS2C().register(MTR.PACKETS_IDENTIFIER, PacketCodec.of(CustomPacket::encode, registryByteBuf -> new CustomPacket(MTR.PACKETS_IDENTIFIER, registryByteBuf)));
		PayloadTypeRegistry.playC2S().register(MTR.PACKETS_IDENTIFIER, PacketCodec.of(CustomPacket::encode, registryByteBuf -> new CustomPacket(MTR.PACKETS_IDENTIFIER, registryByteBuf)));
		ServerPlayNetworking.registerGlobalReceiver(MTR.PACKETS_IDENTIFIER, (customPacket, context) -> PacketBufferReceiver.receive(customPacket.packetByteBuf(), packetBufferReceiver -> {
			final Function<PacketBufferReceiver, ? extends PacketHandler> getInstance = MTRFabric.PACKETS.get(packetBufferReceiver.readString());
			if (getInstance != null) {
				getInstance.apply(packetBufferReceiver).runServer(context.server(), context.player());
			}
		}, context.server()::execute));
	}

	public static <T extends PacketHandler> void registerPacket(Class<T> classObject, Function<PacketBufferReceiver, T> getInstance) {
		MTRFabric.PACKETS.put(classObject.getName(), getInstance);
	}

	public static <T extends PacketHandler> void sendPacketToClient(ServerPlayerEntity serverPlayerEntity, T data) {
		final PacketBufferSender packetBufferSender = new PacketBufferSender(PacketByteBufs::create);
		packetBufferSender.writeString(data.getClass().getName());
		data.write(packetBufferSender);
		packetBufferSender.send(byteBuf -> ServerPlayNetworking.send(serverPlayerEntity, new CustomPacket(MTR.PACKETS_IDENTIFIER, byteBuf instanceof PacketByteBuf ? (PacketByteBuf) byteBuf : new PacketByteBuf(byteBuf))), serverPlayerEntity.server::execute);
	}

	private static <T extends U, U> ObjectHolder<T> register(Registry<U> registry, RegistryKey<Registry<U>> registryKey, String registryName, Function<RegistryKey<U>, T> factory) {
		final RegistryKey<U> dataRegistryKey = RegistryKey.of(registryKey, Identifier.of(MTR.MOD_ID, registryName));
		final ObjectHolder<T> objectHolder = new ObjectHolder<>(() -> Registry.register(registry, dataRegistryKey, factory.apply(dataRegistryKey)));
		OBJECTS_TO_REGISTER.add(objectHolder::createAndGet);
		return objectHolder;
	}
}
