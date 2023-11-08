package org.mtr.mod.packet;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonArray;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.client.ClientData;

import java.util.Collections;

public class PacketFetchArrivals extends PacketHandler implements Utilities {

	private final long platformId;
	private final JsonObject jsonObject;

	public static long millisOffset;

	public PacketFetchArrivals(PacketBuffer packetBuffer) {
		platformId = packetBuffer.readLong();
		jsonObject = PacketData.parseJson(readString(packetBuffer));
	}

	public PacketFetchArrivals(long platformId) {
		this.platformId = platformId;
		jsonObject = new JsonObject();
	}

	public PacketFetchArrivals(long platformId, JsonObject jsonObject) {
		this.platformId = platformId;
		this.jsonObject = jsonObject;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeLong(platformId);
		packetBuffer.writeString(jsonObject.toString());
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		PacketData.sendHttpRequest(String.format("oba/api/where/arrivals-and-departures-for-stop/%s?minutesBefore=0&minutesAfter=%s", Utilities.numberToPaddedHexString(platformId), 60), new JsonObject(), data -> {
			final ObjectArrayList<Arrival> arrivals = new ObjectArrayList<>();
			data.getAsJsonObject("data").getAsJsonObject("entry").getAsJsonArray("arrivalsAndDepartures").forEach(arrivalElement -> Arrival.addFromOBA(arrivalElement.getAsJsonObject(), arrivals));
			Collections.sort(arrivals);
			final JsonObject newObject = new JsonObject();
			final JsonArray newArray = new JsonArray();
			for (int i = 0; i < Math.min(arrivals.size(), 10); i++) {
				newArray.add(arrivals.get(i).toReducedJson());
			}
			newObject.add("data", newArray);
			newObject.addProperty("currentTime", data.get("currentTime").getAsLong());
			Registry.sendPacketToClient(serverPlayerEntity, new PacketFetchArrivals(platformId, newObject));
		});
	}

	@Override
	public void runClientQueued() {
		ClientData.instance.writeArrivalRequest(platformId, jsonObject);
		millisOffset = jsonObject.get("currentTime").getAsLong() - System.currentTimeMillis();
	}

	public static class Arrival implements Comparable<Arrival> {

		public final long arrivalTime;
		public final long delay;
		public final String destination;

		public Arrival(JsonObject jsonObject) {
			this(jsonObject.get("arrivalTime").getAsLong(), jsonObject.get("delay").getAsLong(), jsonObject.get("destination").getAsString());
		}

		private Arrival(long arrivalTime, long delay, String destination) {
			this.arrivalTime = arrivalTime;
			this.delay = delay;
			this.destination = destination;
		}

		public JsonObject toReducedJson() {
			final JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("arrivalTime", arrivalTime);
			jsonObject.addProperty("delay", delay);
			jsonObject.addProperty("destination", destination);
			return jsonObject;
		}

		public static void addFromOBA(JsonObject jsonObject, ObjectArrayList<Arrival> arrivals) {
			if (jsonObject.get("predicted").getAsBoolean()) {
				final long predictedArrivalTime = jsonObject.get("predictedArrivalTime").getAsLong();
				arrivals.add(new Arrival(predictedArrivalTime, predictedArrivalTime - jsonObject.get("scheduledArrivalTime").getAsLong(), jsonObject.get("tripHeadsign").getAsString()));
			}
		}

		@Override
		public int compareTo(Arrival object) {
			return Long.compare(arrivalTime, object.arrivalTime);
		}
	}
}
