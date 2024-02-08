package org.mtr.mod.packet;

import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.core.tool.EnumHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockLiftButtons;

public final class PacketPressLiftButton extends PacketHandler {

	private final LiftDirection pressDirection;
	private final ObjectOpenHashSet<BlockPos> trackPositions;

	public PacketPressLiftButton(LiftDirection pressDirection, ObjectOpenHashSet<BlockPos> trackPositions) {
		this.pressDirection = pressDirection;
		this.trackPositions = trackPositions;
	}

	public PacketPressLiftButton(PacketBufferReceiver packetBufferReceiver) {
		pressDirection = EnumHelper.valueOf(LiftDirection.NONE, packetBufferReceiver.readString());
		trackPositions = new ObjectOpenHashSet<>();
		final int count = packetBufferReceiver.readInt();
		for (int i = 0; i < count; i++) {
			trackPositions.add(BlockPos.fromLong(packetBufferReceiver.readLong()));
		}
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeString(pressDirection.toString());
		packetBufferSender.writeInt(trackPositions.size());
		trackPositions.forEach(blockPos -> packetBufferSender.writeLong(blockPos.asLong()));
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final PressLift pressLift = new PressLift();
		trackPositions.forEach(blockPos -> pressLift.add(Init.blockPosToPosition(blockPos), pressDirection));
		PacketDataBase.sendHttpRequest("operation/press-lift", Utilities.getJsonObjectFromData(pressLift), jsonObject -> {
		});
	}

	@Override
	public void runClient() {
		// Array order: has down button, has up button
		final boolean[] buttonStates = {false, false};
		trackPositions.forEach(trackPosition -> BlockLiftButtons.hasButtonsClient(trackPosition, buttonStates, (floor, lift) -> {
		}));
		final LiftDirection newPressDirection;
		if (buttonStates[0] && buttonStates[1]) {
			newPressDirection = pressDirection;
		} else {
			newPressDirection = buttonStates[0] ? LiftDirection.DOWN : LiftDirection.UP;
		}
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketPressLiftButton(newPressDirection, trackPositions));
	}
}
