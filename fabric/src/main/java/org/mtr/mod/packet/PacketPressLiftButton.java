package org.mtr.mod.packet;

import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockLiftButtons;

public final class PacketPressLiftButton extends PacketHandler {

	private final LiftDirection pressDirection;
	private final ObjectOpenHashSet<BlockPos> trackPositions;

	public PacketPressLiftButton(LiftDirection pressDirection, ObjectOpenHashSet<BlockPos> trackPositions) {
		this.pressDirection = pressDirection;
		this.trackPositions = trackPositions;
	}

	public PacketPressLiftButton(PacketBuffer packetBuffer) {
		pressDirection = EnumHelper.valueOf(LiftDirection.NONE, readString(packetBuffer));
		trackPositions = new ObjectOpenHashSet<>();
		final int count = packetBuffer.readInt();
		for (int i = 0; i < count; i++) {
			trackPositions.add(packetBuffer.readBlockPos());
		}
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		writeString(packetBuffer, pressDirection.toString());
		packetBuffer.writeInt(trackPositions.size());
		trackPositions.forEach(packetBuffer::writeBlockPos);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final PressLift pressLift = new PressLift();
		trackPositions.forEach(blockPos -> pressLift.add(Init.blockPosToPosition(blockPos), pressDirection));
		PacketDataBase.sendHttpRequest("operation/press-lift", Utilities.getJsonObjectFromData(pressLift), jsonObject -> {
		});
	}

	@Override
	public void runClientQueued() {
		// has down button, has up button
		final boolean[] buttonStates = {false, false};
		trackPositions.forEach(trackPosition -> BlockLiftButtons.hasButtonsClient(trackPosition, buttonStates, (floor, lift) -> {
		}));
		final LiftDirection newPressDirection;
		if (buttonStates[0] && buttonStates[1]) {
			newPressDirection = pressDirection;
		} else {
			newPressDirection = buttonStates[0] ? LiftDirection.DOWN : LiftDirection.UP;
		}
		RegistryClient.sendPacketToServer(new PacketPressLiftButton(newPressDirection, trackPositions));
	}
}
