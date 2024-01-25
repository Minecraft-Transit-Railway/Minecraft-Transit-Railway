package org.mtr.mod;

import org.mtr.core.Main;
import org.mtr.core.data.Client;
import org.mtr.core.data.Data;
import org.mtr.core.data.Position;
import org.mtr.core.generated.data.ClientGroupSchema;
import org.mtr.core.integration.Integration;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.JsonWriter;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.io.socket.client.IO;
import org.mtr.libraries.io.socket.client.Socket;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.mapper.WorldHelper;
import org.mtr.mapping.registry.EventRegistry;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.tool.DummyClass;
import org.mtr.mod.data.RailActionModule;
import org.mtr.mod.packet.*;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Init implements Utilities {

	private static Main main;
	private static Socket socket;
	private static Runnable sendWorldTimeUpdate;
	private static int serverTick;


	public static final String MOD_ID = "mtr";
	public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public static final int SECONDS_PER_MC_HOUR = 50;

	private static final String CHANNEL = "update";
	private static final int MILLIS_PER_MC_DAY = SECONDS_PER_MC_HOUR * MILLIS_PER_SECOND * HOURS_PER_DAY;
	private static final Object2ObjectArrayMap<ServerWorld, RailActionModule> RAIL_ACTION_MODULES = new Object2ObjectArrayMap<>();
	private static final Object2ObjectArrayMap<Identifier, IntObjectImmutablePair<ObjectArraySet<ObjectBooleanImmutablePair<ServerPlayerEntity>>>> PLAYERS_TO_UPDATE = new Object2ObjectArrayMap<>();

	public static void init() {
		AsciiArt.print();
		Blocks.init();
		Items.init();
		BlockEntityTypes.init();
		EntityTypes.init();
		CreativeModeTabs.init();
		SoundEvents.init();
		DummyClass.enableLogging();

		// Register packets
		Registry.setupPackets(new Identifier(MOD_ID, "packet"));
		Registry.registerPacket(PacketAddBalance.class, PacketAddBalance::new);
		Registry.registerPacket(PacketBroadcastRailActions.class, PacketBroadcastRailActions::new);
		Registry.registerPacket(PacketData.class, PacketData::create);
		Registry.registerPacket(PacketDeleteRailAction.class, PacketDeleteRailAction::new);
		Registry.registerPacket(PacketDriveTrain.class, PacketDriveTrain::new);
		Registry.registerPacket(PacketFetchArrivals.class, PacketFetchArrivals::new);
		Registry.registerPacket(PacketOpenBlockEntityScreen.class, PacketOpenBlockEntityScreen::new);
		Registry.registerPacket(PacketOpenDashboardScreen.class, PacketOpenDashboardScreen::create);
		Registry.registerPacket(PacketOpenLiftCustomizationScreen.class, PacketOpenLiftCustomizationScreen::new);
		Registry.registerPacket(PacketOpenPIDSConfigScreen.class, PacketOpenPIDSConfigScreen::new);
		Registry.registerPacket(PacketOpenTicketMachineScreen.class, PacketOpenTicketMachineScreen::new);
		Registry.registerPacket(PacketPressLiftButton.class, PacketPressLiftButton::new);
		Registry.registerPacket(PacketRequestData.class, PacketRequestData::new);
		Registry.registerPacket(PacketUpdateLiftTrackFloorConfig.class, PacketUpdateLiftTrackFloorConfig::new);
		Registry.registerPacket(PacketUpdatePIDSConfig.class, PacketUpdatePIDSConfig::new);
		Registry.registerPacket(PacketUpdateRailwaySignConfig.class, PacketUpdateRailwaySignConfig::new);
		Registry.registerPacket(PacketUpdateTrainSensorConfig.class, PacketUpdateTrainSensorConfig::new);
		Registry.registerPacket(PacketUpdateVehicleRidingEntities.class, PacketUpdateVehicleRidingEntities::new);

		// Register events
		EventRegistry.registerServerStarted(minecraftServer -> {
			// Start up the backend
			final ObjectArrayList<String> worldNames = new ObjectArrayList<>();
			RAIL_ACTION_MODULES.clear();
			PLAYERS_TO_UPDATE.clear();
			final int[] index = {0};
			MinecraftServerHelper.iterateWorlds(minecraftServer, serverWorld -> {
				final Identifier identifier = MinecraftServerHelper.getWorldId(new World(serverWorld.data));
				worldNames.add(String.format("%s/%s", identifier.getNamespace(), identifier.getPath()));
				RAIL_ACTION_MODULES.put(serverWorld, new RailActionModule(serverWorld));
				PLAYERS_TO_UPDATE.put(identifier, new IntObjectImmutablePair<>(index[0], new ObjectArraySet<>()));
				index[0]++;
			});
			main = new Main(minecraftServer.getSavePath(WorldSavePath.getRootMapped()).resolve("mtr"), 8888, worldNames.toArray(new String[0]));

			// Set up the socket
			try {
				socket = IO.socket("http://localhost:8888").connect();
				socket.on(CHANNEL, args -> {
					final JsonObject responseObject = Utilities.parseJson(args[0].toString());
					responseObject.keySet().forEach(playerUuid -> {
						final ServerPlayerEntity serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(UUID.fromString(playerUuid));
						if (serverPlayerEntity != null) {
							Registry.sendPacketToClient(serverPlayerEntity, new PacketData(IntegrationServlet.Operation.LIST, new Integration(new JsonReader(responseObject.getAsJsonObject(playerUuid)), new Data()), true, false));
						}
					});
				});
			} catch (Exception e) {
				logException(e);
			}

			serverTick = 0;
			sendWorldTimeUpdate = () -> {
				final JsonObject timeObject = new JsonObject();
				timeObject.addProperty("gameMillis", (WorldHelper.getTimeOfDay(minecraftServer.getOverworld()) + 6000) * SECONDS_PER_MC_HOUR);
				timeObject.addProperty("millisPerDay", MILLIS_PER_MC_DAY);
				PacketData.sendHttpRequest("operation/set-time", timeObject, responseObject -> {
				});
			};
		});

		EventRegistry.registerServerStopping(minecraftServer -> {
			if (main != null) {
				main.stop();
			}
		});

		EventRegistry.registerStartServerTick(() -> {
			if (socket != null) {
				PLAYERS_TO_UPDATE.forEach((identifier, worldDetails) -> {
					final ObjectArraySet<ObjectBooleanImmutablePair<ServerPlayerEntity>> playerDetails = worldDetails.right();
					if (!playerDetails.isEmpty()) {
						final ClientGroupNew clientGroupNew = new ClientGroupNew(worldDetails.leftInt());
						playerDetails.forEach(clientGroupNew::addClient);
						playerDetails.clear();
						final JsonObject jsonObject = new JsonObject();
						clientGroupNew.serializeDataJson(new JsonWriter(jsonObject));
						socket.emit(CHANNEL, jsonObject.toString());
					}
				});
			}

			if (sendWorldTimeUpdate != null && serverTick % (SECONDS_PER_MC_HOUR * 10) == 0) {
				sendWorldTimeUpdate.run();
			}
			serverTick++;
		});

		EventRegistry.registerEndWorldTick(serverWorld -> {
			final RailActionModule railActionModule = RAIL_ACTION_MODULES.get(serverWorld);
			if (railActionModule != null) {
				railActionModule.tick();
			}
		});

		// Finish registration
		Registry.init();
	}

	public static void schedulePlayerUpdate(ServerPlayerEntity serverPlayerEntity, boolean forceUpdate) {
		final IntObjectImmutablePair<ObjectArraySet<ObjectBooleanImmutablePair<ServerPlayerEntity>>> worldDetails = PLAYERS_TO_UPDATE.get(MinecraftServerHelper.getWorldId(new World(serverPlayerEntity.getServerWorld().data)));
		if (worldDetails != null) {
			worldDetails.right().add(new ObjectBooleanImmutablePair<>(serverPlayerEntity, forceUpdate));
		}
	}

	public static void getRailActionModule(ServerWorld serverWorld, Consumer<RailActionModule> consumer) {
		final RailActionModule railActionModule = RAIL_ACTION_MODULES.get(serverWorld);
		if (railActionModule != null) {
			consumer.accept(railActionModule);
		}
	}

	public static BlockPos positionToBlockPos(Position position) {
		return new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ());
	}

	public static Position blockPosToPosition(BlockPos blockPos) {
		return new Position(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public static BlockPos newBlockPos(double x, double y, double z) {
		return new BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
	}

	public static void logException(Exception e) {
		LOGGER.log(Level.INFO, e.getMessage(), e);
	}

	private static class ClientGroupNew extends ClientGroupSchema {

		private final int dimensionIndex;

		private ClientGroupNew(int dimensionIndex) {
			this.dimensionIndex = dimensionIndex;
			updateRadius = 16 * 16; // 16 chunks
		}

		private void serializeDataJson(JsonWriter jsonWriter) {
			super.serializeData(jsonWriter);
			jsonWriter.writeInt("dimension", dimensionIndex);
		}

		private void addClient(ObjectBooleanImmutablePair<ServerPlayerEntity> player) {
			clients.add(new ClientNew(player.left(), player.rightBoolean()));
		}
	}

	private static class ClientNew extends Client {

		protected ClientNew(ServerPlayerEntity serverPlayerEntity, boolean forceUpdate) {
			super(serverPlayerEntity.getUuid());
			final BlockPos blockPos = serverPlayerEntity.getBlockPos();
			position = new Position(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			this.forceUpdate = forceUpdate;
		}
	}
}
