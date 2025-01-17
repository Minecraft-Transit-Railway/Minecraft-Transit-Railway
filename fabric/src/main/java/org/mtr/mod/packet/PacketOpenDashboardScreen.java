package org.mtr.mod.packet;

import org.mtr.core.data.TransportMode;
import org.mtr.core.operation.ListDataResponse;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.tool.EnumHelper;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.client.MinecraftClientData;

import javax.annotation.Nonnull;

public final class PacketOpenDashboardScreen extends PacketRequestResponseBase {

	private final TransportMode transportMode;
	private final ScreenType screenType;
	private final long dataId;

	public PacketOpenDashboardScreen(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		transportMode = EnumHelper.valueOf(TransportMode.TRAIN, packetBufferReceiver.readString());
		screenType = EnumHelper.valueOf(ScreenType.DEFAULT, packetBufferReceiver.readString());
		dataId = packetBufferReceiver.readLong();
	}

	private PacketOpenDashboardScreen(String content, TransportMode transportMode) {
		super(content);
		this.transportMode = transportMode;
		this.screenType = ScreenType.DEFAULT;
		this.dataId = 0;
	}

	private PacketOpenDashboardScreen(String content, TransportMode transportMode, ScreenType screenType, long dataId) {
		super(content);
		this.transportMode = transportMode;
		this.screenType = screenType;
		this.dataId = dataId;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeString(transportMode.toString());
		packetBufferSender.writeString(screenType.toString());
		packetBufferSender.writeLong(dataId);
	}

	@Override
	protected void runClientInbound(JsonReader jsonReader) {
		new ListDataResponse(jsonReader, MinecraftClientData.getDashboardInstance()).write();
		ClientPacketHelper.openDashboardScreen(transportMode, screenType, dataId);
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketOpenDashboardScreen(content, transportMode, screenType, dataId);
	}

	@Override
	protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
		return new SerializedDataBase() {
			@Override
			public void updateData(ReaderBase readerBase) {
			}

			@Override
			public void serializeData(WriterBase writerBase) {
			}
		};
	}

	@Nonnull
	@Override
	protected String getKey() {
		return OperationProcessor.LIST_DATA;
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.PLAYER;
	}

	public static void sendDirectlyToServer(ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity, TransportMode transportMode) {
		new PacketOpenDashboardScreen(new JsonObject().toString(), transportMode).runServerOutbound(serverWorld, serverPlayerEntity);
	}

	public static void sendDirectlyToServer(ServerWorld serverWorld, ServerPlayerEntity serverPlayerEntity, TransportMode transportMode, ScreenType screenType, long id) {
		new PacketOpenDashboardScreen(new JsonObject().toString(), transportMode, screenType, id).runServerOutbound(serverWorld, serverPlayerEntity);
	}

	public enum ScreenType {DEFAULT, STATION, PLATFORM, DEPOT, SIDING}
}
