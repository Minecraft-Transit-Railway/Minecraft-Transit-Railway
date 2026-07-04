package org.mtr.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.data.PersistentStateData;
import org.mtr.registry.RegistryServer;

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
	public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayerEntity) {
		final PersistentStateData persistentStateData = serverPlayerEntity.serverLevel().getDataStorage().computeIfAbsent(new SavedData.Factory<>(PersistentStateData::new, (nbt, wrapperLookup) -> new PersistentStateData(nbt), DataFixTypes.LEVEL), MTR.MOD_ID);
		RegistryServer.sendPacketToClient(serverPlayerEntity, new PacketGetUniqueWorldId(persistentStateData.getUniqueWorldId()));
	}

	@Override
	public void runClient() {
		MTRClient.processUniqueWorldId(uniqueWorldId);
	}
}
