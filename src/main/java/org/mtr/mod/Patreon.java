package mtr;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.data.IGui;
import mtr.data.RailType;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Patreon implements Keys, IGui, Comparable<Patreon> {

	public final String name;
	public final String tierTitle;
	public final int tierAmount;
	public final int tierColor;
	private final int totalAmount;

	public Patreon(JsonObject jsonObjectPatron, JsonObject jsonObjectTiers) {
		name = jsonObjectPatron.get("full_name").getAsString();
		totalAmount = jsonObjectPatron.get("lifetime_support_cents").getAsInt();
		tierTitle = jsonObjectTiers.get("title").getAsString();
		tierAmount = jsonObjectTiers.get("amount_cents").getAsInt();

		int color = ARGB_WHITE;
		try {
			color = RailType.valueOf(tierTitle.toUpperCase(Locale.ENGLISH)).color | ARGB_BLACK;
		} catch (Exception ignored) {
		}
		tierColor = color;
	}

	public static void getPatreonList(List<Patreon> patreonList) {
		patreonList.clear();
		CompletableFuture.runAsync(() -> openConnectionSafeJson("https://www.patreon.com/api/oauth2/v2/campaigns/7782318/members?include=currently_entitled_tiers&fields%5Bmember%5D=full_name,lifetime_support_cents,patron_status&fields%5Btier%5D=title,amount_cents&page%5Bcount%5D=" + Integer.MAX_VALUE, jsonElement -> {
			final JsonObject jsonObjectData = jsonElement.getAsJsonObject();
			final Map<String, JsonObject> idMap = new HashMap<>();
			jsonObjectData.getAsJsonArray("included").forEach(jsonElementData -> {
				final JsonObject jsonObject = jsonElementData.getAsJsonObject();
				idMap.put(jsonObject.get("id").getAsString(), jsonObject.getAsJsonObject("attributes"));
			});

			jsonObjectData.getAsJsonArray("data").forEach(jsonElementData -> {
				final JsonObject jsonObjectAttributes = jsonElementData.getAsJsonObject().getAsJsonObject("attributes");
				final JsonArray jsonObjectTiers = jsonElementData.getAsJsonObject().getAsJsonObject("relationships").getAsJsonObject("currently_entitled_tiers").getAsJsonArray("data");
				if (!jsonObjectAttributes.get("patron_status").isJsonNull() && jsonObjectAttributes.get("patron_status").getAsString().equals("active_patron") && jsonObjectTiers.size() > 0) {
					patreonList.add(new Patreon(jsonObjectAttributes, idMap.get(jsonObjectTiers.get(0).getAsJsonObject().get("id").getAsString())));
				}
			});

			Collections.sort(patreonList);
		}, "Authorization", "Bearer " + PATREON_API_KEY));
	}

	public static void openConnectionSafe(String url, Consumer<InputStream> callback, String... requestProperties) {
		try {
			final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setUseCaches(false);

			for (int i = 0; i < requestProperties.length / 2; i++) {
				connection.setRequestProperty(requestProperties[2 * i], requestProperties[2 * i + 1]);
			}

			try (final InputStream inputStream = connection.getInputStream()) {
				callback.accept(inputStream);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void openConnectionSafeJson(String url, Consumer<JsonElement> callback, String... requestProperties) {
		openConnectionSafe(url, inputStream -> {
			try (final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
				callback.accept(new JsonParser().parse(inputStreamReader));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, requestProperties);
	}

	@Override
	public int compareTo(Patreon patreon) {
		return patreon.tierAmount == tierAmount ? patreon.totalAmount - totalAmount : patreon.tierAmount - tierAmount;
	}
}
