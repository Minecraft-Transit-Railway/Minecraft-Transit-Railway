package mtr.data;

import net.minecraft.network.FriendlyByteBuf;

public class ScheduleEntry implements Comparable<mtr.data.ScheduleEntry> {

	public final long arrivalMillis;
	public final int trainCars;
	public final long platformId;
	public final long routeId;
	public final String destination;
	public final boolean isTerminating;

	public ScheduleEntry(long arrivalMillis, int trainCars, long platformId, long routeId, String destination, boolean isTerminating) {
		this.arrivalMillis = arrivalMillis;
		this.trainCars = trainCars;
		this.platformId = platformId;
		this.routeId = routeId;
		this.destination = destination;
		this.isTerminating = isTerminating;
	}

	public ScheduleEntry(FriendlyByteBuf packet) {
		arrivalMillis = packet.readLong();
		trainCars = packet.readInt();
		platformId = packet.readLong();
		routeId = packet.readLong();
		destination = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		isTerminating = packet.readBoolean();
	}

	public void writePacket(FriendlyByteBuf packet) {
		packet.writeLong(arrivalMillis);
		packet.writeInt(trainCars);
		packet.writeLong(platformId);
		packet.writeLong(routeId);
		packet.writeUtf(destination);
		packet.writeBoolean(isTerminating);
	}

	@Override
	public int compareTo(mtr.data.ScheduleEntry o) {
		if (arrivalMillis == o.arrivalMillis) {
			return destination.compareTo(o.destination);
		} else {
			return arrivalMillis > o.arrivalMillis ? 1 : -1;
		}
	}
}
