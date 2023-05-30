package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class RailwayDataFileSaveModule extends RailwayDataModuleBase {

	private boolean canAutoSave = false;
	private boolean dataLoaded = false;
	private boolean useReducedHash = true;
	private int filesWritten;
	private int filesDeleted;
	private long autoSaveStartMillis;

	private final SignalBlocks signalBlocks;

	private final List<Long> dirtyStationIds = new ArrayList<>();
	private final List<Long> dirtyPlatformIds = new ArrayList<>();
	private final List<Long> dirtySidingIds = new ArrayList<>();
	private final List<Long> dirtyRouteIds = new ArrayList<>();
	private final List<Long> dirtyDepotIds = new ArrayList<>();
	private final List<Long> dirtyLiftIds = new ArrayList<>();
	private final List<BlockPos> dirtyRailPositions = new ArrayList<>();
	private final List<SignalBlocks.SignalBlock> dirtySignalBlocks = new ArrayList<>();

	private final Map<Path, Integer> existingFiles = new HashMap<>();
	private final List<Path> checkFilesToDelete = new ArrayList<>();

	private final Path stationsPath;
	private final Path platformsPath;
	private final Path sidingsPath;
	private final Path routesPath;
	private final Path depotsPath;
	private final Path liftsPath;
	private final Path railsPath;
	private final Path signalBlocksPath;

	public RailwayDataFileSaveModule(RailwayData railwayData, Level world, Map<BlockPos, Map<BlockPos, Rail>> rails, Path savePath, SignalBlocks signalBlocks) {
		super(railwayData, world, rails);
		this.signalBlocks = signalBlocks;

		stationsPath = savePath.resolve("stations");
		platformsPath = savePath.resolve("platforms");
		sidingsPath = savePath.resolve("sidings");
		routesPath = savePath.resolve("routes");
		depotsPath = savePath.resolve("depots");
		liftsPath = savePath.resolve("lifts");
		railsPath = savePath.resolve("rails");
		signalBlocksPath = savePath.resolve("signal-blocks");

		try {
			Files.createDirectories(stationsPath);
			Files.createDirectories(platformsPath);
			Files.createDirectories(sidingsPath);
			Files.createDirectories(routesPath);
			Files.createDirectories(depotsPath);
			Files.createDirectories(liftsPath);
			Files.createDirectories(railsPath);
			Files.createDirectories(signalBlocksPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		existingFiles.clear();
		readMessagePackFromFile(stationsPath, Station::new, railwayData.stations::add, false);
		readMessagePackFromFile(platformsPath, Platform::new, railwayData.platforms::add, true);
		readMessagePackFromFile(sidingsPath, Siding::new, railwayData.sidings::add, true);
		readMessagePackFromFile(routesPath, Route::new, railwayData.routes::add, false);
		readMessagePackFromFile(depotsPath, Depot::new, railwayData.depots::add, false);
		readMessagePackFromFile(liftsPath, LiftServer::new, railwayData.lifts::add, true);
		readMessagePackFromFile(railsPath, RailEntry::new, railEntry -> rails.put(railEntry.pos, railEntry.connections), true);
		readMessagePackFromFile(signalBlocksPath, SignalBlocks.SignalBlock::new, signalBlocks.signalBlocks::add, true);

		System.out.println("Minecraft Transit Railway data successfully loaded for " + world.dimension().location());
		canAutoSave = true;
		dataLoaded = true;
	}

	public void fullSave() {
		useReducedHash = false;
		dirtyStationIds.clear();
		dirtyPlatformIds.clear();
		dirtySidingIds.clear();
		dirtyRouteIds.clear();
		dirtyDepotIds.clear();
		dirtyLiftIds.clear();
		dirtyRailPositions.clear();
		dirtySignalBlocks.clear();
		checkFilesToDelete.clear();
		autoSave();
		while (true) {
			if (autoSaveTick()) {
				break;
			}
		}
		canAutoSave = false;
	}

	public void autoSave() {
		if (!dataLoaded) {
			dataLoaded = true;
			canAutoSave = true;
		}

		if (canAutoSave && checkFilesToDelete.isEmpty()) {
			autoSaveStartMillis = System.currentTimeMillis();
			filesWritten = 0;
			filesDeleted = 0;
			dirtyStationIds.addAll(railwayData.dataCache.stationIdMap.keySet());
			dirtyPlatformIds.addAll(railwayData.dataCache.platformIdMap.keySet());
			dirtySidingIds.addAll(railwayData.dataCache.sidingIdMap.keySet());
			dirtyRouteIds.addAll(railwayData.dataCache.routeIdMap.keySet());
			dirtyDepotIds.addAll(railwayData.dataCache.depotIdMap.keySet());
			dirtyLiftIds.addAll(railwayData.dataCache.liftsServerIdMap.keySet());
			dirtyRailPositions.addAll(rails.keySet());
			dirtySignalBlocks.addAll(signalBlocks.signalBlocks);
			checkFilesToDelete.addAll(existingFiles.keySet());
		}
	}

	public boolean autoSaveTick() {
		if (canAutoSave) {
			final boolean deleteEmptyOld = checkFilesToDelete.isEmpty();

			boolean hasSpareTime = writeDirtyDataToFile(dirtyStationIds, railwayData.dataCache.stationIdMap::get, id -> id, stationsPath);
			if (hasSpareTime) {
				hasSpareTime = writeDirtyDataToFile(dirtyPlatformIds, railwayData.dataCache.platformIdMap::get, id -> id, platformsPath);
			}
			if (hasSpareTime) {
				hasSpareTime = writeDirtyDataToFile(dirtySidingIds, railwayData.dataCache.sidingIdMap::get, id -> id, sidingsPath);
			}
			if (hasSpareTime) {
				hasSpareTime = writeDirtyDataToFile(dirtyRouteIds, railwayData.dataCache.routeIdMap::get, id -> id, routesPath);
			}
			if (hasSpareTime) {
				hasSpareTime = writeDirtyDataToFile(dirtyDepotIds, railwayData.dataCache.depotIdMap::get, id -> id, depotsPath);
			}
			if (hasSpareTime) {
				hasSpareTime = writeDirtyDataToFile(dirtyLiftIds, railwayData.dataCache.liftsServerIdMap::get, id -> id, liftsPath);
			}
			if (hasSpareTime) {
				hasSpareTime = writeDirtyDataToFile(dirtyRailPositions, pos -> rails.containsKey(pos) ? new RailEntry(pos, rails.get(pos)) : null, BlockPos::asLong, railsPath);
			}
			if (hasSpareTime) {
				hasSpareTime = writeDirtyDataToFile(dirtySignalBlocks, signalBlock -> signalBlock, signalBlock -> signalBlock.id, signalBlocksPath);
			}

			final boolean doneWriting = dirtyStationIds.isEmpty() && dirtyPlatformIds.isEmpty() && dirtySidingIds.isEmpty() && dirtyRouteIds.isEmpty() && dirtyDepotIds.isEmpty() && dirtyLiftIds.isEmpty() && dirtyRailPositions.isEmpty() && dirtySignalBlocks.isEmpty();
			if (hasSpareTime && !checkFilesToDelete.isEmpty() && doneWriting) {
				final Path path = checkFilesToDelete.remove(0);
				try {
					Files.deleteIfExists(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				existingFiles.remove(path);
				filesDeleted++;
			}

			if (!deleteEmptyOld && checkFilesToDelete.isEmpty()) {
				final Map<Long, Map<BlockPos, TrainDelay>> trainDelays = railwayData.getTrainDelays();
				final List<Long> routeIdsToRemove = new ArrayList<>();
				final List<BlockPos> posToRemove = new ArrayList<>();
				trainDelays.forEach((routeId, trainDelaysForRouteId) -> trainDelaysForRouteId.forEach((pos, trainDelay) -> {
					if (trainDelay.isExpired()) {
						routeIdsToRemove.add(routeId);
						posToRemove.add(pos);
					}
				}));

				if (!useReducedHash || filesWritten > 0 || filesDeleted > 0) {
					System.out.println("Minecraft Transit Railway save complete for " + world.dimension().location() + " in " + (System.currentTimeMillis() - autoSaveStartMillis) / 1000 + " second(s)");
					if (filesWritten > 0) {
						System.out.println("- Changed: " + filesWritten);
					}
					if (filesDeleted > 0) {
						System.out.println("- Deleted: " + filesDeleted);
					}
					if (!routeIdsToRemove.isEmpty()) {
						System.out.println("- Delays Cleared: " + routeIdsToRemove.size());
					}
				}

				for (int i = 0; i < routeIdsToRemove.size(); i++) {
					final long routeId = routeIdsToRemove.get(i);
					trainDelays.get(routeId).remove(posToRemove.get(i));
					if (trainDelays.get(routeId).isEmpty()) {
						trainDelays.remove(routeId);
					}
				}
			}

			return doneWriting && checkFilesToDelete.isEmpty();
		} else {
			return true;
		}
	}

	private <T extends SerializedDataBase> void readMessagePackFromFile(Path path, Function<Map<String, Value>, T> getData, Consumer<T> callback, boolean skipVerify) {
		try (final Stream<Path> pathStream = Files.list(path)) {
			pathStream.forEach(idFolder -> {
				try (final Stream<Path> folderStream = Files.list(idFolder)) {
					folderStream.forEach(idFile -> {
						try (final InputStream inputStream = Files.newInputStream(idFile)) {
							try (final MessageUnpacker messageUnpacker = MessagePack.newDefaultUnpacker(inputStream)) {
								final int size = messageUnpacker.unpackMapHeader();
								final HashMap<String, Value> result = new HashMap<>(size);

								try {
									for (int i = 0; i < size; i++) {
										result.put(messageUnpacker.unpackString(), messageUnpacker.unpackValue());
									}

									final T data = getData.apply(result);
									if (skipVerify || !(data instanceof NameColorDataBase) || !((NameColorDataBase) data).name.isEmpty()) {
										callback.accept(data);
									}

									existingFiles.put(idFile, getHash(data, true));
								} catch (Exception e) {
									e.printStackTrace();
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Path writeMessagePackToFile(SerializedDataBase data, long id, Path path) {
		final Path parentPath = path.resolve(String.valueOf(id % 100));
		try {
			Files.createDirectories(parentPath);
			final Path dataPath = parentPath.resolve(String.valueOf(id));
			final int hash = getHash(data, useReducedHash);

			if (!existingFiles.containsKey(dataPath) || hash != existingFiles.get(dataPath)) {
				final MessagePacker messagePacker = MessagePack.newDefaultPacker(Files.newOutputStream(dataPath, StandardOpenOption.CREATE));
				messagePacker.packMapHeader(data.messagePackLength());
				data.toMessagePack(messagePacker);
				messagePacker.close();

				existingFiles.put(dataPath, hash);
				filesWritten++;
			}

			return dataPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private <T extends SerializedDataBase, U> boolean writeDirtyDataToFile(List<U> dirtyData, Function<U, T> getId, Function<U, Long> idToLong, Path path) {
		final long millis = System.currentTimeMillis();
		while (!dirtyData.isEmpty()) {
			final U id = dirtyData.remove(0);
			final T data = getId.apply(id);
			if (data != null) {
				final Path newPath = writeMessagePackToFile(data, idToLong.apply(id), path);
				if (newPath != null) {
					checkFilesToDelete.remove(newPath);
				}
			}
			if (System.currentTimeMillis() - millis >= 2) {
				return false;
			}
		}
		return true;
	}

	private static int getHash(SerializedDataBase data, boolean useReducedHash) {
		try {
			final MessageBufferPacker messageBufferPacker = MessagePack.newDefaultBufferPacker();

			if (useReducedHash && data instanceof IReducedSaveData) {
				messageBufferPacker.packMapHeader(((IReducedSaveData) data).reducedMessagePackLength());
				((IReducedSaveData) data).toReducedMessagePack(messageBufferPacker);
			} else {
				messageBufferPacker.packMapHeader(data.messagePackLength());
				data.toMessagePack(messageBufferPacker);
			}

			final int hash = Arrays.hashCode(messageBufferPacker.toByteArray());
			messageBufferPacker.close();

			return hash;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private static class RailEntry extends SerializedDataBase {

		public final BlockPos pos;
		public final Map<BlockPos, Rail> connections;

		private static final String KEY_NODE_POS = "node_pos";
		private static final String KEY_RAIL_CONNECTIONS = "rail_connections";

		public RailEntry(BlockPos pos, Map<BlockPos, Rail> connections) {
			this.pos = pos;
			this.connections = connections;
		}

		public RailEntry(Map<String, Value> map) {
			final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
			pos = BlockPos.of(messagePackHelper.getLong(KEY_NODE_POS));
			connections = new HashMap<>();
			messagePackHelper.iterateArrayValue(KEY_RAIL_CONNECTIONS, value -> {
				final Map<String, Value> mapSK = RailwayData.castMessagePackValueToSKMap(value);
				connections.put(BlockPos.of(new MessagePackHelper(mapSK).getLong(KEY_NODE_POS)), new Rail(mapSK));
			});
		}

		@Override
		public void toMessagePack(MessagePacker messagePacker) throws IOException {
			messagePacker.packString(KEY_NODE_POS).packLong(pos.asLong());

			messagePacker.packString(KEY_RAIL_CONNECTIONS).packArrayHeader(connections.size());
			for (final Map.Entry<BlockPos, Rail> entry : connections.entrySet()) {
				final BlockPos endNodePos = entry.getKey();
				messagePacker.packMapHeader(entry.getValue().messagePackLength() + 1);
				messagePacker.packString(KEY_NODE_POS).packLong(endNodePos.asLong());
				entry.getValue().toMessagePack(messagePacker);
			}
		}

		@Override
		public int messagePackLength() {
			return 2;
		}

		@Override
		public void writePacket(FriendlyByteBuf packet) {
		}
	}
}
