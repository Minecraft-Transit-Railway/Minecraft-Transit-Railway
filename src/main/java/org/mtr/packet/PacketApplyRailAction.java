package org.mtr.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.mtr.item.ItemNodeModifierBase;
import org.mtr.item.ItemNodeModifierSelectableBlockBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;

/**
 * Server-bound packet that applies a bridge/tunnel/wall action across a multi-node rail path.
 * The client computes the path via BFS and sends each rail segment to be processed on the server.
 */
public final class PacketApplyRailAction extends PacketHandler {

	private final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, BlockPos>> railPairs;

	public PacketApplyRailAction(PacketBufferReceiver packetBufferReceiver) {
		final int count = packetBufferReceiver.readInt();
		final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, BlockPos>> railPairs = new ObjectArrayList<>();

		for (int i = 0; i < count; i++) {
			final BlockPos start = BlockPos.of(packetBufferReceiver.readLong());
			final BlockPos end = BlockPos.of(packetBufferReceiver.readLong());
			railPairs.add(new ObjectObjectImmutablePair<>(start, end));
		}

		this.railPairs = railPairs;
	}

	public PacketApplyRailAction(ObjectArrayList<ObjectObjectImmutablePair<BlockPos, BlockPos>> railPairs) {
		this.railPairs = railPairs;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeInt(railPairs.size());
		for (final ObjectObjectImmutablePair<BlockPos, BlockPos> pair : railPairs) {
			packetBufferSender.writeLong(pair.left().asLong());
			packetBufferSender.writeLong(pair.right().asLong());
		}
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayerEntity) {
		final ItemStack itemStack = serverPlayerEntity.getMainHandItem();

		if (!(itemStack.getItem() instanceof ItemNodeModifierSelectableBlockBase item)) {
			return;
		}

		final int capturedRadius = item.getRadius();
		final int capturedHeight = item.getHeight();

		for (final ObjectObjectImmutablePair<BlockPos, BlockPos> pair : railPairs) {
			ItemNodeModifierBase.getRail(
				serverPlayerEntity.serverLevel(),
				pair.left(),
				pair.right(),
				serverPlayerEntity,
				rail -> item.onConnect(rail, serverPlayerEntity, itemStack, capturedRadius, capturedHeight)
			);
		}
	}
}
