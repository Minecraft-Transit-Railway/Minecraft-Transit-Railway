package mtr.data;

import net.minecraft.network.FriendlyByteBuf;

public class ScheduleEntry implements Comparable<mtr.data.ScheduleEntry> {

	public final long arrivalMillis;
	public final int trainCars;
	public final long routeId;
	public final int currentStationIndex;

	public ScheduleEntry(long arrivalMillis, int trainCars, long routeId, int currentStationIndex) {
		this.arrivalMillis = arrivalMillis;
		this.trainCars = trainCars;
		this.routeId = routeId;
		this.currentStationIndex = currentStationIndex;
	}

	public ScheduleEntry(FriendlyByteBuf packet) {
		arrivalMillis = packet.readLong();
		trainCars = packet.readInt();
		routeId = packet.readLong();
		currentStationIndex = packet.readInt();
	}

	public void writePacket(FriendlyByteBuf packet) {
		packet.writeLong(arrivalMillis);
		packet.writeInt(trainCars);
		packet.writeLong(routeId);
		packet.writeInt(currentStationIndex);
	}

	@Override
	public int compareTo(mtr.data.ScheduleEntry o) {
		if (arrivalMillis == o.arrivalMillis) {
			return routeId > o.routeId ? 1 : -1;
		} else {
			return arrivalMillis > o.arrivalMillis ? 1 : -1;
		}
	}
}
