package mtr.tile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mtr.Keys;
import mtr.MTR;
import mtr.block.BlockOBAController;
import mtr.block.BlockOBAScreen;
import mtr.message.MessageOBAData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class TileEntityOBAController extends TileEntity implements ITickable, Keys {

	public EnumFacing displayBlock;
	public int screenX, screenY, screenWidth, screenHeight;
	public String title;
	public String[] routes, destinations, arrivals, vehicleIds, deviation;

	private String stopIds;
	private int tickCounter;

	@Override
	public void update() {
		if (!world.isRemote) {
			if (getStops() == "" || !getStops().contains("_")) {
				tickCounter = 0;
			} else {
				if (tickCounter == 0) {
					displayBlock = BlockOBAController.getScreenDirection(world, pos);
					if (displayBlock != null) {
						screenX = findScreenBounds(displayBlock.rotateY());
						screenY = findScreenBounds(EnumFacing.UP);
						screenWidth = findScreenBounds(displayBlock.rotateYCCW()) + screenX + 1;
						screenHeight = findScreenBounds(EnumFacing.DOWN) + screenY + 1;
					}

					final Thread thread = new Thread(new OBAData());
					thread.start();
				}

				tickCounter++;
				if (tickCounter == 200)
					tickCounter = 0;
			}
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

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	public String getStops() {
		return stopIds == null ? "" : stopIds;
	}

	public void setStops(String message) {
		stopIds = message;
	}

	private int findScreenBounds(EnumFacing direction) {
		int count = -1;
		BlockPos startPos = pos.offset(displayBlock);
		while (world.getBlockState(startPos).getBlock() instanceof BlockOBAScreen) {
			count++;
			startPos = startPos.offset(direction);
		}
		return count;
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
			try {
				final String url = "http://api.pugetsound.onebusaway.org/api/where/arrivals-and-departures-for-stop/" + getStops() + ".json?key=" + OBA_KEY + "&minutesBefore=0&minutesAfter=300";
				final JsonObject data = new JsonParser().parse(readWebsite(url)).getAsJsonObject().getAsJsonObject("data");

				title = data.getAsJsonObject("references").getAsJsonArray("stops").get(0).getAsJsonObject().getAsJsonPrimitive("name").getAsString();
				final JsonArray arrivalArray = data.getAsJsonObject("entry").getAsJsonArray("arrivalsAndDepartures");

				final int size = arrivalArray.size();
				routes = new String[size];
				destinations = new String[size];
				arrivals = new String[size];
				vehicleIds = new String[size];
				deviation = new String[size];
				for (int i = 0; i < size; i++) {
					final JsonObject arrivalObject = arrivalArray.get(i).getAsJsonObject();
					routes[i] = arrivalObject.getAsJsonPrimitive("routeShortName").getAsString();
					// if (arrivalObject.getAsJsonPrimitive("totalStopsInTrip").getAsInt() ==
					// arrivalObject.getAsJsonPrimitive("stopSequence").getAsInt() + 1)
					// routes[i] = "*" + routes[i];
					destinations[i] = arrivalObject.getAsJsonPrimitive("tripHeadsign").getAsString();

					final long predicted = arrivalObject.getAsJsonPrimitive("predictedArrivalTime").getAsLong();
					final long scheduled = arrivalObject.getAsJsonPrimitive("scheduledArrivalTime").getAsLong();
					final int arrivalMin = (int) Math.round(((predicted == 0 ? scheduled : predicted) - Instant.now().toEpochMilli()) / 60000D);
					// arrivals[i] = arrivalMin == 0 ? I18n.format("gui.oba_now") :
					// String.valueOf(arrivalMin) + I18n.format("gui.oba_min");
					arrivals[i] = arrivalMin == 0 ? "NOW" : String.valueOf(arrivalMin) + " min";

					vehicleIds[i] = arrivalObject.getAsJsonPrimitive("vehicleId").getAsString();

					if (predicted == 0)
						deviation[i] = "Scheduled"; // I18n.format("gui.oba_scheduled");
					else if (Math.abs(predicted - scheduled) < 30000)
						deviation[i] = "On time"; // I18n.format("gui.oba_on_time");
					else
						// deviation[i] = I18n.format(predicted > scheduled ? "gui.oba_delay_before" :
						// "gui.oba_early_before") + String.valueOf((int) Math.round(Math.abs(predicted
						// - scheduled) / 60000D)) + I18n.format(predicted > scheduled ?
						// "gui.oba_delay_after" : "gui.oba_early_after");
						deviation[i] = String.valueOf((int) Math.round(Math.abs(predicted - scheduled) / 60000D)) + (predicted > scheduled ? " min delay" : " min early");
				}

				// System.out.println("Successfully fetched data for " + stopIds);
			} catch (final Exception e) {
				displayBlock = null;
				title = "";
				routes = new String[0];
				destinations = new String[0];
				arrivals = new String[0];
				vehicleIds = new String[0];
				deviation = new String[0];

				System.err.println("Failed to fetch data for " + stopIds);
			}

			MTR.NETWORK.sendToAll(new MessageOBAData(pos, displayBlock, screenX, screenY, screenWidth, screenHeight, title, routes, destinations, arrivals, vehicleIds, deviation));
		}
	}
}
