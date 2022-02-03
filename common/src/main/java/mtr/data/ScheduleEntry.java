package mtr.data;

import net.minecraft.network.FriendlyByteBuf;

public class ScheduleEntry implements Comparable<mtr.data.ScheduleEntry> {

	public final long arrivalMillis;
	public final int trainCars;
	public final long platformId;
	public final long routeId;
	public final String routeNumber;
	public final String destination;
	public final boolean isTerminating;

	public ScheduleEntry(long arrivalMillis, int trainCars, long platformId, long routeId, String routeNumber, String destination, boolean isTerminating) {
		this.arrivalMillis = arrivalMillis;
		this.trainCars = trainCars;
		this.platformId = platformId;
		this.routeId = routeId;
		this.routeNumber = routeNumber;
		this.destination = destination;
		this.isTerminating = isTerminating;
	}

	public ScheduleEntry(FriendlyByteBuf packet) {
		arrivalMillis = packet.readLong();
		trainCars = packet.readInt();
		platformId = packet.readLong();
		routeId = packet.readLong();
		routeNumber = ""; //TODO
		destination = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		isTerminating = packet.readBoolean();
	}

	public void writePacket(FriendlyByteBuf packet) {
		packet.writeLong(arrivalMillis);
		packet.writeInt(trainCars);
		packet.writeLong(platformId);
		packet.writeLong(routeId);

		// TODO temporary code start
		final String[] lastStationSplit = destination.split("\\|");
		final StringBuilder destination = new StringBuilder();
		for (final String lastStationSplitPart : lastStationSplit) {
			destination.append("|").append(routeNumber.isEmpty() ? "" : routeNumber + " ").append(lastStationSplitPart);
		}

		packet.writeUtf(destination.length() > 0 ? destination.substring(1) : "");
		// TODO temporary code end

		packet.writeBoolean(isTerminating);
	}

	@Override
	public int compareTo(mtr.data.ScheduleEntry o) {
		if (arrivalMillis == o.arrivalMillis) {
			return (routeNumber + destination).compareTo(o.routeNumber + o.destination);
		} else {
			return arrivalMillis > o.arrivalMillis ? 1 : -1;
		}
	}
}
