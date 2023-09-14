package org.mtr.mod.packet;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ScreenExtension;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IPacket {

	static void openScreen(ScreenExtension screenExtension, Predicate<ScreenExtension> isInstance) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final Screen screen = minecraftClient.getCurrentScreenMapped();
		if (screen == null || screen.data instanceof ScreenExtension && !isInstance.test((ScreenExtension) screen.data)) {
			minecraftClient.openScreen(new Screen(screenExtension));
		}
	}

	static void getBlockEntity(BlockPos blockPos, Consumer<BlockEntity> consumer) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld != null) {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
			if (blockEntity != null) {
				consumer.accept(blockEntity);
			}
		}
	}
}
