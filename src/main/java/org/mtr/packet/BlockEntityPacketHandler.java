package org.mtr.packet;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.mtr.registry.RegistryClient;

public abstract class BlockEntityPacketHandler extends PacketHandler {

	public abstract void write(PacketBufferSender packetBufferSender);

	@Override
	public final void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayerEntity) {
		setData(serverPlayerEntity.getCommandSenderWorld());
	}

	@Override
	public final void runClient() {
	}

	/**
	 * After creating an instance of this class, call this method to send it to the server.
	 * This updates the block entity's data clientside as well.
	 *
	 * @param clientWorld the client world
	 */
	public final void send(@Nullable Level clientWorld) {
		setData(clientWorld);
		RegistryClient.sendPacketToServer(this);
	}

	protected abstract void setData(@Nullable Level world);
}
