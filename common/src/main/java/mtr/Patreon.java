package mtr;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.data.IGui;
import mtr.data.RailType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Patreon implements Keys, IGui, Comparable<Patreon> {

	public final String name;
	public final String tierTitle;
	public final int tierAmount;
	public final int tierColor;
	private final String joinedDate;

	public Patreon(JsonObject jsonObjectPatron, JsonObject jsonObjectReward, String joinedDate) {
		name = jsonObjectPatron.get("full_name").getAsString();
		tierTitle = jsonObjectReward.get("title").getAsString();
		tierAmount = jsonObjectReward.get("amount_cents").getAsInt();
		this.joinedDate = joinedDate;

		int color = ARGB_WHITE;
		try {
			color = RailType.valueOf(tierTitle.toUpperCase(Locale.ENGLISH)).color | ARGB_BLACK;
		} catch (Exception ignored) {
		}
		tierColor = color;
	}

	public static List<Patreon> getPatreonList() {
		final List<Patreon> patreonList = new ArrayList<>();

		try {
			final HttpURLConnection connection = (HttpURLConnection) new URL("https://www.patreon.com/api/oauth2/api/campaigns/7782318/pledges").openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + PATREON_API_KEY);
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			final StringBuilder content = new StringBuilder();
			while ((inputLine = bufferedReader.readLine()) != null) {
				content.append(inputLine);
			}

			final JsonObject jsonObjectData = new JsonParser().parse(content.toString()).getAsJsonObject();
			final Map<String, JsonObject> idMap = new HashMap<>();
			jsonObjectData.getAsJsonArray("included").forEach(jsonElement -> {
				final JsonObject jsonObject = jsonElement.getAsJsonObject();
				idMap.put(jsonObject.get("id").getAsString(), jsonObject);
			});

			jsonObjectData.getAsJsonArray("data").forEach(jsonElement -> {
				final JsonObject jsonObject = jsonElement.getAsJsonObject();
				final JsonObject jsonObjectRelationships = jsonObject.getAsJsonObject("relationships");
				final JsonObject jsonObjectPatron = idMap.get(jsonObjectRelationships.get("patron").getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString()).getAsJsonObject("attributes");
				final JsonObject jsonObjectReward = idMap.get(jsonObjectRelationships.get("reward").getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString()).getAsJsonObject("attributes");
				final String joinedDate = jsonObject.getAsJsonObject("attributes").get("created_at").getAsString();
				patreonList.add(new Patreon(jsonObjectPatron, jsonObjectReward, joinedDate));
			});

			Collections.sort(patreonList);
			bufferedReader.close();
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return patreonList;
	}

	@Override
	public int compareTo(Patreon patreon) {
		return patreon.tierAmount == tierAmount ? joinedDate.compareTo(patreon.joinedDate) : patreon.tierAmount - tierAmount;
	}
}
