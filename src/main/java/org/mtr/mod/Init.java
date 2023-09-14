package org.mtr.mod;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.socket.client.IO;
import io.socket.client.Socket;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.core.Main;
import org.mtr.core.client.Client;
import org.mtr.core.generated.ClientGroupSchema;
import org.mtr.core.serializers.JsonWriter;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.core.tools.Position;
import org.mtr.init.MTR;
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

	public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final String CHANNEL = "update";
	private static final Object2ObjectArrayMap<ServerWorld, RailActionModule> RAIL_ACTION_MODULES = new Object2ObjectArrayMap<>();

	public static void init() {
		Blocks.init();
		Items.init();
		BlockEntityTypes.init();
		CreativeModeTabs.init();
		SoundEvents.init();
		DummyClass.enableLogging();

		// Register packets
		Registry.setupPackets(new Identifier(MTR.MOD_ID, "packet"));
		Registry.registerPacket(PacketAddBalance.class, PacketAddBalance::new);
		Registry.registerPacket(PacketBroadcastRailActions.class, PacketBroadcastRailActions::new);
		Registry.registerPacket(PacketCloseDashboardScreen.class, packetBuffer -> new PacketCloseDashboardScreen());
		Registry.registerPacket(PacketData.class, PacketData::new);
		Registry.registerPacket(PacketDeleteRailAction.class, PacketDeleteRailAction::new);
		Registry.registerPacket(PacketDriveTrain.class, PacketDriveTrain::new);
		Registry.registerPacket(PacketOpenBlockEntityScreen.class, PacketOpenBlockEntityScreen::new);
		Registry.registerPacket(PacketOpenDashboardScreen.class, PacketOpenDashboardScreen::new);
		Registry.registerPacket(PacketOpenLiftCustomizationScreen.class, PacketOpenLiftCustomizationScreen::new);
		Registry.registerPacket(PacketOpenPIDSConfigScreen.class, PacketOpenPIDSConfigScreen::new);
		Registry.registerPacket(PacketOpenResourcePackCreatorScreen.class, PacketOpenResourcePackCreatorScreen::new);
		Registry.registerPacket(PacketOpenTicketMachineScreen.class, PacketOpenTicketMachineScreen::new);
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
			MinecraftServerHelper.iterateWorlds(minecraftServer, serverWorld -> {
				final Identifier identifier = MinecraftServerHelper.getWorldId(new World(serverWorld.data));
				worldNames.add(String.format("%s/%s", identifier.getNamespace(), identifier.getPath()));
				RAIL_ACTION_MODULES.put(serverWorld, new RailActionModule(serverWorld));
			});
			main = new Main(
					1200000,
					minecraftServer.getOverworld().getTime(),
					minecraftServer.getSavePath(WorldSavePath.getRootMapped()).resolve("mtr"),
					"website",
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

		EventRegistry.registerPlayerJoin(Init::updatePlayerPosition);

		EventRegistry.registerEndWorldTick(serverWorld -> {
			final RailActionModule railActionModule = RAIL_ACTION_MODULES.get(serverWorld);
			if (railActionModule != null) {
				railActionModule.tick();
			}
		});

		// Finish registration
		Registry.init();
	}

	public static void updatePlayerPosition(MinecraftServer minecraftServer, ServerPlayerEntity... serverPlayerEntities) {
		if (socket != null) {
			final Object2ObjectArrayMap<Identifier, ClientGroupNew> worlds = new Object2ObjectArrayMap<>();
			final int[] index = {0};
			MinecraftServerHelper.iterateWorlds(minecraftServer, serverWorld -> {
				worlds.put(MinecraftServerHelper.getWorldId(new World(serverWorld.data)), new ClientGroupNew(index[0]));
				index[0]++;
			});

			for (final ServerPlayerEntity serverPlayerEntity : serverPlayerEntities) {
				worlds.get(MinecraftServerHelper.getWorldId(new World(serverPlayerEntity.getServerWorld().data))).addClient(serverPlayerEntity);
			}

			worlds.forEach((identifier, clientGroupNew) -> {
				if (clientGroupNew.shouldUpdate()) {
					final JsonObject jsonObject = new JsonObject();
					clientGroupNew.serializeDataJson(new JsonWriter(jsonObject));
					socket.emit(CHANNEL, jsonObject.toString());
				}
			});
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
		return new BlockPos(net.minecraft.util.math.MathHelper.floor(x), net.minecraft.util.math.MathHelper.floor(y), net.minecraft.util.math.MathHelper.floor(z));
	}

	public static void logException(Exception e) {
		LOGGER.log(Level.INFO, e.getMessage(), e);
	}

	private static class ClientGroupNew extends ClientGroupSchema {

		private final int dimensionIndex;

		private ClientGroupNew(int dimensionIndex) {
			this.dimensionIndex = dimensionIndex;
			updateRadius = 64;
		}

		private void serializeDataJson(JsonWriter jsonWriter) {
			super.serializeData(jsonWriter);
			jsonWriter.writeInt("dimension", dimensionIndex);
		}

		private void addClient(ServerPlayerEntity serverPlayerEntity) {
			clients.add(new ClientNew(serverPlayerEntity));
		}

		private boolean shouldUpdate() {
			return !clients.isEmpty();
		}
	}

	private static class ClientNew extends Client {

		protected ClientNew(ServerPlayerEntity serverPlayerEntity) {
			super(serverPlayerEntity.getUuid());
			final BlockPos blockPos = serverPlayerEntity.getBlockPos();
			position = new Position(blockPos.getX(), blockPos.getY(), blockPos.getZ());
		}
	}
}
