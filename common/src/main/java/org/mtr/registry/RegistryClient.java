package org.mtr.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.mtr.packet.PacketHandler;

public final class RegistryClient {

	@ExpectPlatform
	public static <T extends BlockEntity, U extends T> void registerBlockEntityRenderer(ObjectHolder<BlockEntityType<U>> blockEntityType, BlockEntityRendererFactory<T> factory) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends Entity, U extends T> void registerEntityRenderer(ObjectHolder<EntityType<U>> entityType, EntityRendererFactory<T> factory) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerBlockRenderType(RenderLayer renderLayer, ObjectHolder<Block> block) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void registerKeyBinding(KeyBinding keyBinding) {
		throw new AssertionError();
	}

	@ExpectPlatform
	@SafeVarargs
	public static void registerBlockColors(BlockColorProvider blockColorProvider, ObjectHolder<Block>... blocks) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void setupPackets() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static <T extends PacketHandler> void sendPacketToServer(T data) {
		throw new AssertionError();
	}
}
