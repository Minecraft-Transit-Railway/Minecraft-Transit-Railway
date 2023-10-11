package org.mtr.mod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.socket.client.IO;
import io.socket.client.Socket;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectBooleanImmutablePair;
import org.mtr.core.Main;
import org.mtr.core.client.Client;
import org.mtr.core.generated.ClientGroupSchema;
import org.mtr.core.serializers.JsonWriter;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tools.Position;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.registry.EventRegistry;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.tool.DummyClass;
import org.mtr.mod.data.RailActionModule;
import org.mtr.mod.packet.*;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Init {

	private static Main main;
	private static Socket socket;


	public static final String MOD_ID = "mtr";
	public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final String CHANNEL = "update";
	private static final Object2ObjectArrayMap<ServerWorld, RailActionModule> RAIL_ACTION_MODULES = new Object2ObjectArrayMap<>();
	private static final Object2ObjectArrayMap<Identifier, IntObjectImmutablePair<ObjectArraySet<ObjectBooleanImmutablePair<ServerPlayerEntity>>>> PLAYERS_TO_UPDATE = new Object2ObjectArrayMap<>();

	public static void init() {
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
		Registry.registerPacket(PacketData.class, PacketData::new);
		Registry.registerPacket(PacketDeleteRailAction.class, PacketDeleteRailAction::new);
		Registry.registerPacket(PacketDriveTrain.class, PacketDriveTrain::new);
		Registry.registerPacket(PacketOpenBlockEntityScreen.class, PacketOpenBlockEntityScreen::new);
		Registry.registerPacket(PacketOpenDashboardScreen.class, PacketOpenDashboardScreen::new);
		Registry.registerPacket(PacketOpenLiftCustomizationScreen.class, PacketOpenLiftCustomizationScreen::new);
		Registry.registerPacket(PacketOpenPIDSConfigScreen.class, PacketOpenPIDSConfigScreen::new);
		Registry.registerPacket(PacketOpenResourcePackCreatorScreen.class, PacketOpenResourcePackCreatorScreen::new);
		Registry.registerPacket(PacketOpenTicketMachineScreen.class, PacketOpenTicketMachineScreen::new);
		Registry.registerPacket(PacketRequestData.class, PacketRequestData::new);
		Registry.registerPacket(PacketUpdateArrivalProjectorConfig.class, PacketUpdateArrivalProjectorConfig::new);
		Registry.registerPacket(PacketUpdateLiftTrackFloorConfig.class, PacketUpdateLiftTrackFloorConfig::new);
		Registry.registerPacket(PacketUpdatePIDSConfig.class, PacketUpdatePIDSConfig::new);
		Registry.registerPacket(PacketUpdateRailwaySignConfig.class, PacketUpdateRailwaySignConfig::new);
		Registry.registerPacket(PacketUpdateTrainSensorConfig.class, PacketUpdateTrainSensorConfig::new);

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
			main = new Main(
					1200000,
					minecraftServer.getOverworld().getTime(),
					minecraftServer.getSavePath(WorldSavePath.getRootMapped()).resolve("mtr"),
					8888,
					worldNames.toArray(new String[0])
			);

			// Set up the socket
			try {
				socket = IO.socket("http://localhost:8888").connect();
				socket.on(CHANNEL, args -> {
					try {
						final JsonObject responseObject = JsonParser.parseString(args[0].toString()).getAsJsonObject();
						responseObject.keySet().forEach(playerUuid -> {
							final ServerPlayerEntity serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(UUID.fromString(playerUuid));
							if (serverPlayerEntity != null) {
								Registry.sendPacketToClient(serverPlayerEntity, new PacketData(IntegrationServlet.Operation.LIST, responseObject.getAsJsonObject(playerUuid)));
							}
						});
					} catch (Exception e) {
						logException(e);
					}
				});
			} catch (Exception e) {
				logException(e);
			}
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
