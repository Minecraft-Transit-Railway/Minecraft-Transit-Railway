package mtr.tile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mtr.MTR;
import mtr.block.BlockOBAController;
import mtr.message.MessageOBAData;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityOBAController extends TileEntity implements ITickable {

	public EnumFacing displayBlock;
	public String title;
	public String[] routes, destinations, arrivals, vehicleIds, statuses;

	private String stopIds;
	private int tickCounter;

	@Override
	public void update() {
		if (!world.isRemote) {
			if (tickCounter == 0) {
				final Thread thread = new Thread(new OBAData());
				thread.start();
			}

			tickCounter++;
			if (tickCounter == 200)
				tickCounter = 0;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		stopIds = compound.getString("stopIds");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("stopIds", getStops());
		return compound;
	}

	public String getStops() {
		return stopIds == null ? "" : stopIds;
	}

	public void setStops(String message) {
		stopIds = message;
	}

	private static String readWebsite(String url) {
		try {
			HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
			c.addRequestProperty("User-Agent", "Mozilla/4.0");
			while (c.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP || c.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM) {
				c = (HttpURLConnection) new URL(c.getHeaderField("Location")).openConnection();
				c.addRequestProperty("User-Agent", "Mozilla/4.0");
			}
			final BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String line, text = "";
			while ((line = in.readLine()) != null)
				text += line;
			return text;
		} catch (final Exception e) {
			return "";
		}
	}

	private class OBAData implements Runnable {

		@Override
		public void run() {
			displayBlock = BlockOBAController.getScreenDirection(world, pos);

			final String url = "http://api.pugetsound.onebusaway.org/api/where/arrivals-and-departures-for-stop/" + stopIds + ".json?key=TEST&minutesBefore=0&minutesAfter=300";
			final JsonObject data = new JsonParser().parse(readWebsite(url)).getAsJsonObject().getAsJsonObject("data");

			title = data.getAsJsonObject("references").getAsJsonArray("stops").get(0).getAsJsonObject().getAsJsonPrimitive("name").getAsString();
			final JsonArray arrivalArray = data.getAsJsonObject("entry").getAsJsonArray("arrivalsAndDepartures");

			final int size = arrivalArray.size();
			routes = new String[size];
			destinations = new String[size];
			arrivals = new String[size];
			vehicleIds = new String[size];
			statuses = new String[size];
			for (int i = 0; i < size; i++) {
				final JsonObject arrivalObject = arrivalArray.get(i).getAsJsonObject();
				routes[i] = arrivalObject.getAsJsonPrimitive("routeShortName").getAsString();
				destinations[i] = arrivalObject.getAsJsonPrimitive("tripHeadsign").getAsString();

				final long predicted = arrivalObject.getAsJsonPrimitive("predictedArrivalTime").getAsLong();
				final long scheduled = arrivalObject.getAsJsonPrimitive("scheduledArrivalTime").getAsLong();
				final int arrivalMin = (int) Math.round(((predicted == 0 ? scheduled : predicted) - Instant.now().toEpochMilli()) / 60000D);
				arrivals[i] = arrivalMin == 0 ? I18n.format("gui.oba_now") : String.valueOf(arrivalMin) + I18n.format("gui.oba_min");

				vehicleIds[i] = arrivalObject.getAsJsonPrimitive("vehicleId").getAsString();

				if (predicted == 0)
					statuses[i] = I18n.format("gui.oba_scheduled");
				else if (Math.abs(predicted - scheduled) < 30000)
					statuses[i] = I18n.format("gui.oba_on_time");
				else
					statuses[i] = I18n.format(predicted > scheduled ? "gui.oba_delay_before" : "gui.oba_early_before") + String.valueOf((int) Math.round(Math.abs(predicted - scheduled) / 60000D)) + I18n.format(predicted > scheduled ? "gui.oba_delay_after" : "gui.oba_early_after");
			}

			MTR.INSTANCE.sendToAll(new MessageOBAData(pos, displayBlock, title, routes, destinations, arrivals, vehicleIds, statuses));
		}
	}
}
