package mtr.data;

import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class RailwayDataLoggingModule extends RailwayDataModuleBase {

	private final Path logsPath;
	private final Path filePath;
	private final List<String> queuedEvents = new ArrayList<>();

	public RailwayDataLoggingModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails, Path savePath) {
		super(railwayData, world, rails);
		logsPath = savePath.resolve("logs");
		filePath = logsPath.resolve(new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date()) + ".csv");
	}

	public void save() {
		if (!queuedEvents.isEmpty()) {
			try {
				if (!Files.exists(filePath)) {
					Files.createDirectories(logsPath);
					queuedEvents.add(0, String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
							"Timestamp",
							"Player Name",
							"Player UUID",
							"Class",
							"ID",
							"Name",
							"Position",
							"Change",
							"Old Data",
							"New Data"
					));
					Files.write(filePath, queuedEvents);
				} else {
					Files.write(filePath, queuedEvents, StandardOpenOption.APPEND);
				}
				queuedEvents.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void addEvent(ServerPlayer player, Class<?> dataClass, List<String> oldData, List<String> newData, BlockPos... positions) {
		addEvent(player, dataClass, 0, "", oldData, newData, positions);
	}

	public void addEvent(ServerPlayer player, Class<?> dataClass, long id, String name, List<String> oldData, List<String> newData, BlockPos... positions) {
		final List<String> oldDataDiff;
		final List<String> newDataDiff;

		if (oldData.size() == newData.size()) {
			oldDataDiff = new ArrayList<>();
			newDataDiff = new ArrayList<>();

			for (int i = 0; i < oldData.size(); i++) {
				final String oldDataString = oldData.get(i);
				final String newDataString = newData.get(i);
				if (!oldDataString.equals(newDataString)) {
					oldDataDiff.add(oldDataString);
					newDataDiff.add(newDataString);
				}
			}
		} else {
			oldDataDiff = oldData;
			newDataDiff = newData;
		}

		final List<String> positionsList = new ArrayList<>();
		for (final BlockPos pos : positions) {
			if (pos != null) {
				positionsList.add(String.format("(%s, %s, %s)", pos.getX(), pos.getY(), pos.getZ()));
			}
		}
		Collections.sort(positionsList);

		if (!oldDataDiff.isEmpty() || !newDataDiff.isEmpty()) {
			queuedEvents.add(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
					formatString(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(new Date())),
					formatString(player.getName().getString()),
					formatString(player.getUUID().toString()),
					formatString(dataClass.getName()),
					formatString(id == 0 ? "" : String.format("[%s]", id)),
					formatString(IGui.formatStationName(name)),
					formatString(String.join("\n", positionsList)),
					formatString((oldDataDiff.isEmpty() ? LoggingEditType.CREATE : newDataDiff.isEmpty() ? LoggingEditType.DELETE : LoggingEditType.EDIT).toString()),
					formatString(RailwayData.prettyPrint(new JsonParser().parse(String.format("{%s}", String.join(",", oldDataDiff))))),
					formatString(RailwayData.prettyPrint(new JsonParser().parse(String.format("{%s}", String.join(",", newDataDiff)))))
			));
		}
	}

	public static <T extends SerializedDataBase> List<String> getData(T data) {
		try (final MessageBufferPacker messageBufferPacker = MessagePack.newDefaultBufferPacker()) {
			if (data instanceof IReducedSaveData) {
				((IReducedSaveData) data).toReducedMessagePack(messageBufferPacker);
			} else {
				data.toMessagePack(messageBufferPacker);
			}
			try (final MessageUnpacker messageUnpacker = MessagePack.newDefaultUnpacker(messageBufferPacker.toByteArray())) {
				final List<String> dataList = new ArrayList<>();
				String previousData = null;

				while (messageUnpacker.hasNext()) {
					final String newValue = messageUnpacker.unpackValue().toJson();
					if (previousData == null) {
						previousData = newValue;
					} else {
						dataList.add(String.format("%s:%s", previousData, newValue));
						previousData = null;
					}
				}

				return dataList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	public static List<String> getData(CompoundTag compoundTag) {
		final List<String> dataList = new ArrayList<>();

		compoundTag.getAllKeys().forEach(key -> {
			final String value = convertTag(compoundTag.get(key));
			if (value != null) {
				dataList.add(String.format("%s:%s", key, value));
			}
		});

		return dataList;
	}

	private static String convertTag(Tag tag) {
		if (tag instanceof CollectionTag) {
			final List<String> valueTags = new ArrayList<>();
			((CollectionTag<?>) tag).forEach(data -> valueTags.add(convertTag(data)));
			return String.format("[%s]", String.join(",", valueTags));
		} else if (tag != null) {
			final String tempValue = tag.getAsString();
			if (tempValue.equals("0b")) {
				return String.valueOf(false);
			} else if (tempValue.equals("1b")) {
				return String.valueOf(true);
			} else if (tag instanceof NumericTag) {
				return ((NumericTag) tag).getAsNumber().toString();
			} else {
				return tempValue.isEmpty() ? "\"\"" : tempValue;
			}
		}

		return null;
	}

	private static String formatString(String text) {
		return String.format("\"%s\"", text.replace("\"", "\"\""));
	}

	private enum LoggingEditType {
		CREATE, EDIT, DELETE
	}
}
