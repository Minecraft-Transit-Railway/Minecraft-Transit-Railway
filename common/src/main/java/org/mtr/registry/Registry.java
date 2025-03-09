package org.mtr.registry;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.packet.PacketBufferReceiver;
import org.mtr.packet.PacketHandler;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Registry {

	@ExpectPlatform
	public static ObjectHolder<Block> registerBlock(String registryName, Function<AbstractBlock.Settings, Block> factory) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static ObjectHolder<Item> registerItem(String registryName, Function<Item.Settings, Item> factory, @Nullable String itemGroupRegistryName) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends BlockEntity> ObjectHolder<BlockEntityType<T>> registerBlockEntityType(String registryName, BiFunction<BlockPos, BlockState, T> factory, Supplier<Block> blockSupplier) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends Entity> ObjectHolder<EntityType<T>> registerEntityType(String registryName, BiFunction<EntityType<T>, World, T> factory, float width, float height) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static String registerItemGroup(String registryName, Supplier<ItemStack> iconSupplier) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static ObjectHolder<SoundEvent> registerSoundEvent(String registryName, Supplier<SoundEvent> supplier) {
		throw new AssertionError();
	}

	@ExpectPlatform
	@SafeVarargs
	public static void registerCommands(LiteralArgumentBuilder<ServerCommandSource>... commands) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void setupPackets() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends PacketHandler> void registerPacket(Class<T> classObject, Function<PacketBufferReceiver, T> getInstance) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends PacketHandler> void sendPacketToClient(ServerPlayerEntity serverPlayerEntity, T data) {
		throw new AssertionError();
	}
}
