package org.mtr.mod.packet;

import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.integration.Response;
import org.mtr.core.operation.VehicleLiftResponse;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.client.VehicleRidingMovement;
import org.mtr.mod.data.VehicleExtension;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.LongConsumer;
import java.util.function.ToLongFunction;

public final class PacketUpdateVehiclesLifts extends PacketRequestResponseBase {

	public PacketUpdateVehiclesLifts(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
	}

	public PacketUpdateVehiclesLifts(Response contentObject) {
		super(contentObject.getJson().toString());
	}

	private PacketUpdateVehiclesLifts(String content) {
		super(content);
	}

	@Override
	protected void runClientInbound(Response response) {
		final MinecraftClientData minecraftClientData = MinecraftClientData.getInstance();
		final VehicleLiftResponse vehicleLiftResponse = response.getData(jsonReader -> new VehicleLiftResponse(jsonReader, minecraftClientData));
		final boolean hasUpdate1 = updateVehiclesOrLifts(minecraftClientData.vehicles, vehicleLiftResponse::iterateVehiclesToKeep, vehicleLiftResponse::iterateVehiclesToUpdate, vehicleUpdate -> vehicleUpdate.getVehicle().getId(), vehicleUpdate -> new VehicleExtension(vehicleUpdate, minecraftClientData));
		final boolean hasUpdate2 = updateVehiclesOrLifts(minecraftClientData.lifts, vehicleLiftResponse::iterateLiftsToKeep, vehicleLiftResponse::iterateLiftsToUpdate, NameColorDataBase::getId, lift -> lift);

		if (hasUpdate1 || hasUpdate2) {
			if (hasUpdate1) {
				minecraftClientData.vehicles.forEach(vehicle -> vehicle.vehicleExtraData.immutablePath.forEach(pathData -> pathData.writePathCache(minecraftClientData, vehicle.getTransportMode())));
			}
			minecraftClientData.sync();
		}
	}

	@Override
	protected PacketRequestResponseBase getInstance(String content) {
		return new PacketUpdateVehiclesLifts(content);
	}

	@Nonnull
	@Override
	protected String getEndpoint() {
		return "";
	}

	@Override
	protected PacketRequestResponseBase.ResponseType responseType() {
		return PacketRequestResponseBase.ResponseType.NONE;
	}

	private static <T extends NameColorDataBase, U> boolean updateVehiclesOrLifts(ObjectArraySet<T> dataSet, Consumer<LongConsumer> iterateKeep, Consumer<Consumer<U>> iterateUpdate, ToLongFunction<U> getId, Function<U, T> createInstance) {
		final LongAVLTreeSet keepIds = new LongAVLTreeSet();
		iterateKeep.accept(keepIds::add);
		VehicleRidingMovement.writeVehicleId(keepIds);

		final LongAVLTreeSet updateIds = new LongAVLTreeSet();
		final ObjectArrayList<U> dataSetToUpdate = new ObjectArrayList<>();
		iterateUpdate.accept(dataToUpdate -> {
			dataSetToUpdate.add(dataToUpdate);
			updateIds.add(getId.applyAsLong(dataToUpdate));
		});

		final boolean removedItems = dataSet.removeIf(data -> !keepIds.contains(data.getId()) || updateIds.contains(data.getId()));
		dataSetToUpdate.forEach(dataToUpdate -> dataSet.add(createInstance.apply(dataToUpdate)));
		return !dataSetToUpdate.isEmpty() || removedItems;
	}
}
