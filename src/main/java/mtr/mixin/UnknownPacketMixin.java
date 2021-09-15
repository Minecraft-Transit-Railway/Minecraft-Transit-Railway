package mtr.mixin;

import mtr.gui.ClientData;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class UnknownPacketMixin implements IPacket {

	@Shadow
	private MinecraftClient client;
	private Identifier channel;
	private boolean process;

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/CustomPayloadS2CPacket;getChannel()Lnet/minecraft/util/Identifier;"), method = "onCustomPayload")
	private Identifier getChannel(CustomPayloadS2CPacket customPayloadS2CPacket) {
		final Identifier channel = customPayloadS2CPacket.getChannel();
		this.channel = channel;
		process = false;
		return channel;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"), method = "onCustomPayload")
	private void redirectLogger(Logger logger, String message, Object p0) {
		process = true;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;release()Z"), method = "onCustomPayload")
	private boolean processPacket(PacketByteBuf packetByteBuf) {
		if (process) {
			if (PACKET_CHUNK_S2C.equals(channel)) {
				PacketTrainDataGuiClient.receiveChunk(client, packetByteBuf);
			} else if (PACKET_UPDATE_SIDING.equals(channel)) {
				PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(client, packetByteBuf, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, false);
			} else if (PACKET_WRITE_RAILS.equals(channel)) {
				ClientData.writeRails(client, packetByteBuf);
			} else {
				System.out.println("Unknown custom packed identifier: " + channel);
			}
		}

		return packetByteBuf.release();
	}
}
