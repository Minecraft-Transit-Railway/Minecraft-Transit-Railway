package mtr.mixin;

import mtr.client.ClientData;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public abstract class UnknownPacketMixin implements IPacket {

	@Shadow
	private Minecraft minecraft;
	private ResourceLocation channel;
	private boolean process;

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundCustomPayloadPacket;getIdentifier()Lnet/minecraft/resources/ResourceLocation;"), method = "handleCustomPayload")
	private ResourceLocation getChannel(ClientboundCustomPayloadPacket customPayloadS2CPacket) {
		final ResourceLocation channel = customPayloadS2CPacket.getIdentifier();
		this.channel = channel;
		process = false;
		return channel;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lorg/@path1@/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V"), method = "handleCustomPayload")
	private void redirectLogger(org.@path2@.Logger instance, String s, Object o) {
		process = true;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;release()Z"), method = "handleCustomPayload")
	private boolean processPacket(FriendlyByteBuf packetByteBuf) {
		if (process) {
			if (PACKET_CHUNK_S2C.equals(channel)) {
				PacketTrainDataGuiClient.receiveChunk(minecraft, packetByteBuf);
			} else if (PACKET_UPDATE_SIDING.equals(channel)) {
				PacketTrainDataGuiClient.receiveUpdateOrDeleteS2C(minecraft, packetByteBuf, ClientData.SIDINGS, ClientData.DATA_CACHE.sidingIdMap, null, false);
			} else if (PACKET_WRITE_RAILS.equals(channel)) {
				ClientData.writeRails(minecraft, packetByteBuf);
			} else if (PACKET_UPDATE_SCHEDULE.equals(channel)) {
				ClientData.updateSchedule(minecraft, packetByteBuf);
			} else if (PACKET_UPDATE_TRAINS.equals(channel)) {
				ClientData.updateTrains(minecraft, packetByteBuf);
			} else if (PACKET_DELETE_TRAINS.equals(channel)) {
				ClientData.deleteTrains(minecraft, packetByteBuf);
			} else if (PACKET_UPDATE_LIFTS.equals(channel)) {
				ClientData.updateLifts(minecraft, packetByteBuf);
			} else if (PACKET_DELETE_LIFTS.equals(channel)) {
				ClientData.deleteLifts(minecraft, packetByteBuf);
			} else {
				System.out.println("Unknown custom packed identifier: " + channel);
			}
		}

		return packetByteBuf.release();
	}
}
