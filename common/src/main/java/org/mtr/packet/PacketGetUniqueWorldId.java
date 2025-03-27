package org.mtr.packet;

import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.data.PersistentStateData;
import org.mtr.registry.Registry;

public final class PacketGetUniqueWorldId extends PacketHandler {

	private final String uniqueWorldId;

	public PacketGetUniqueWorldId(PacketBufferReceiver packetBufferReceiver) {
		uniqueWorldId = packetBufferReceiver.readString();
	}

	public PacketGetUniqueWorldId() {
		uniqueWorldId = "";
	}

	private PacketGetUniqueWorldId(String uniqueWorldId) {
		this.uniqueWorldId = uniqueWorldId;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeString(uniqueWorldId);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final PersistentStateData persistentStateData = serverPlayerEntity.getServerWorld().getPersistentStateManager().getOrCreate(new PersistentState.Type<>(PersistentStateData::new, (nbt, wrapperLookup) -> new PersistentStateData(nbt), DataFixTypes.LEVEL), MTR.MOD_ID);
		Registry.sendPacketToClient(serverPlayerEntity, new PacketGetUniqueWorldId(persistentStateData.getUniqueWorldId()));
	}

	@Override
	public void runClient() {
		MTRClient.processUniqueWorldId(uniqueWorldId);
	}
}
