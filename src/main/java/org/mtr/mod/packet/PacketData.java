package org.mtr.mod.packet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.mtr.core.data.*;
import org.mtr.core.serializers.JsonReader;
import org.mtr.core.serializers.JsonWriter;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.registry.Registry;
import org.mtr.mod.client.ClientData;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

public final class PacketData extends PacketHandler {

	private final IntegrationServlet.Operation operation;
	private final JsonObject jsonObject;
	private final Callback callback;

	public static PacketData fromStations(IntegrationServlet.Operation operation, ObjectSet<Station> dataSet, Consumer<ObjectAVLTreeSet<Station>> consumer) {
		return new PacketData(operation, dataSet, null, null, null, null, (stations, platforms, sidings, routes, depots) -> consumer.accept(stations));
	}

	public static PacketData fromPlatforms(IntegrationServlet.Operation operation, ObjectSet<Platform> dataSet, Consumer<ObjectAVLTreeSet<Platform>> consumer) {
		return new PacketData(operation, null, dataSet, null, null, null, (stations, platforms, sidings, routes, depots) -> consumer.accept(platforms));
	}

	public static PacketData fromSidings(IntegrationServlet.Operation operation, ObjectSet<Siding> dataSet, Consumer<ObjectAVLTreeSet<Siding>> consumer) {
		return new PacketData(operation, null, null, dataSet, null, null, (stations, platforms, sidings, routes, depots) -> consumer.accept(sidings));
	}

	public static PacketData fromRoutes(IntegrationServlet.Operation operation, ObjectSet<Route> dataSet, Consumer<ObjectAVLTreeSet<Route>> consumer) {
		return new PacketData(operation, null, null, null, dataSet, null, (stations, platforms, sidings, routes, depots) -> consumer.accept(routes));
	}

	public static PacketData fromDepots(IntegrationServlet.Operation operation, ObjectSet<Depot> dataSet, Consumer<ObjectAVLTreeSet<Depot>> consumer) {
		return new PacketData(operation, null, null, null, null, dataSet, (stations, platforms, sidings, routes, depots) -> consumer.accept(depots));
	}

	public PacketData(IntegrationServlet.Operation operation, ObjectSet<Station> stations, ObjectSet<Platform> platforms, ObjectSet<Siding> sidings, ObjectSet<Route> routes, ObjectSet<Depot> depots, Callback callback) {
		this.operation = operation;
		jsonObject = new JsonObject();
		jsonObject.add("stations", writeDataSetToJsonArray(stations));
		jsonObject.add("platforms", writeDataSetToJsonArray(platforms));
		jsonObject.add("sidings", writeDataSetToJsonArray(sidings));
		jsonObject.add("routes", writeDataSetToJsonArray(routes));
		jsonObject.add("depots", writeDataSetToJsonArray(depots));
		this.callback = callback;
	}

	public PacketData(IntegrationServlet.Operation operation, JsonObject jsonObject) {
		this.operation = operation;
		this.jsonObject = jsonObject;
		this.callback = (stations, platforms, sidings, routes, depots) -> {
		};
	}

	public PacketData(PacketBuffer packetBuffer) {
		this(EnumHelper.valueOf(IntegrationServlet.Operation.UPDATE, readString(packetBuffer)), readString(packetBuffer), (stations, platforms, sidings, routes, depots) -> {
		});
	}

	private PacketData(IntegrationServlet.Operation operation, String string, Callback callback) {
		this.operation = operation;
		JsonObject newJsonObject = new JsonObject();
		try {
			newJsonObject = JsonParser.parseString(string).getAsJsonObject();
		} catch (Exception ignored) {
		}
		jsonObject = newJsonObject;
		this.callback = callback;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		writeString(packetBuffer, operation.toString());
		writeString(packetBuffer, jsonObject.toString());
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final HttpPost request = new HttpPost("http://localhost:8888/mtr/api/data/" + operation.toString().toLowerCase(Locale.ENGLISH));
		request.addHeader("content-type", "application/json");

		try {
			request.setEntity(new StringEntity(jsonObject.toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (final CloseableHttpClient closeableHttpClient = HttpClients.createDefault(); final CloseableHttpResponse response = closeableHttpClient.execute(request)) {
			final String data = EntityUtils.toString(response.getEntity());
			MinecraftServerHelper.iteratePlayers(serverPlayerEntity.getServerWorld(), worldPlayer -> Registry.sendPacketToClient(worldPlayer, new PacketData(operation, data, callback)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void runClientQueued() {
		final JsonObject dataObject = jsonObject.getAsJsonObject("data");
		callback.accept(
				writeJsonObjectToDataSet(ClientData.instance.stations, dataObject.getAsJsonArray("stations"), a -> new Station(a, ClientData.instance)),
				writeJsonObjectToDataSet(ClientData.instance.platforms, dataObject.getAsJsonArray("platforms"), a -> new Platform(a, ClientData.instance)),
				writeJsonObjectToDataSet(ClientData.instance.sidings, dataObject.getAsJsonArray("sidings"), a -> new Siding(a, ClientData.instance)),
				writeJsonObjectToDataSet(ClientData.instance.routes, dataObject.getAsJsonArray("routes"), a -> new Route(a, ClientData.instance)),
				writeJsonObjectToDataSet(ClientData.instance.depots, dataObject.getAsJsonArray("depots"), a -> new Depot(a, ClientData.instance))
		);
		writeJsonObjectToDataSet(ClientData.instance.rails, dataObject.getAsJsonArray("rails"), Rail::new);
	}

	private <T extends SerializedDataBase> ObjectAVLTreeSet<T> writeJsonObjectToDataSet(ObjectAVLTreeSet<T> dataSet, JsonArray jsonArray, Function<JsonReader, T> function) {
		if (jsonArray == null) {
			return new ObjectAVLTreeSet<>();
		} else {
			final ObjectAVLTreeSet<T> newData = new ObjectAVLTreeSet<>();
			final LongAVLTreeSet idList = new LongAVLTreeSet();

			jsonArray.forEach(jsonElement -> {
				final T data = function.apply(new JsonReader(jsonElement.getAsJsonObject()));
				newData.add(data);
				if (data instanceof NameColorDataBase) {
					idList.add(((NameColorDataBase) data).getId());
				}
			});

			if ((operation == IntegrationServlet.Operation.UPDATE || operation == IntegrationServlet.Operation.DELETE) && !idList.isEmpty()) {
				dataSet.removeIf(data -> data instanceof NameColorDataBase && idList.contains(((NameColorDataBase) data).getId()));
			}

			if (operation == IntegrationServlet.Operation.UPDATE) {
				dataSet.addAll(newData);
			}

			return newData;
		}
	}

	private static <T extends NameColorDataBase> JsonArray writeDataSetToJsonArray(ObjectSet<T> dataSet) {
		final JsonArray jsonArray = new JsonArray();
		if (dataSet != null) {
			dataSet.forEach(data -> {
				final JsonObject jsonObject = new JsonObject();
				data.serializeData(new JsonWriter(jsonObject));
				jsonArray.add(jsonObject);
			});
		}
		return jsonArray;
	}

	@FunctionalInterface
	public interface Callback {
		void accept(ObjectAVLTreeSet<Station> stations, ObjectAVLTreeSet<Platform> platforms, ObjectAVLTreeSet<Siding> sidings, ObjectAVLTreeSet<Route> routes, ObjectAVLTreeSet<Depot> depots);
	}
}
