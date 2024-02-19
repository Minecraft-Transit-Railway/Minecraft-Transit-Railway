package org.mtr.mod;

import com.mojang.brigadier.arguments.StringArgumentType;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.servlet.ServletHolder;
import org.mtr.core.Main;
import org.mtr.core.data.Position;
import org.mtr.core.operation.GenerateByDepotName;
import org.mtr.core.operation.SetTime;
import org.mtr.core.servlet.Webserver;
import org.mtr.core.tool.RequestHelper;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.mapper.WorldHelper;
import org.mtr.mapping.registry.EventRegistry;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.tool.DummyClass;
import org.mtr.mod.data.RailActionModule;
import org.mtr.mod.packet.*;

import javax.annotation.Nullable;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

public final class Init implements Utilities {

	private static Main main;
	private static Webserver webserver;
	private static int serverPort;
	private static Runnable sendWorldTimeUpdate;
	private static int serverTick;

	public static final String MOD_ID = "mtr";
	public static final Logger LOGGER = LogManager.getLogger("MinecraftTransitRailway");
	public static final Registry REGISTRY = new Registry();
	public static final int SECONDS_PER_MC_HOUR = 50;

	private static final int MILLIS_PER_MC_DAY = SECONDS_PER_MC_HOUR * MILLIS_PER_SECOND * HOURS_PER_DAY;
	private static final Object2ObjectArrayMap<ServerWorld, RailActionModule> RAIL_ACTION_MODULES = new Object2ObjectArrayMap<>();
	private static final ObjectArrayList<String> WORLD_ID_LIST = new ObjectArrayList<>();
	private static final RequestHelper REQUEST_HELPER = new RequestHelper(false);

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
		REGISTRY.setupPackets(new Identifier(MOD_ID, "packet"));
		REGISTRY.registerPacket(PacketAddBalance.class, PacketAddBalance::new);
		REGISTRY.registerPacket(PacketBroadcastRailActions.class, PacketBroadcastRailActions::new);
		REGISTRY.registerPacket(PacketDeleteData.class, PacketDeleteData::new);
		REGISTRY.registerPacket(PacketDeleteRailAction.class, PacketDeleteRailAction::new);
		REGISTRY.registerPacket(PacketDepotGenerate.class, PacketDepotGenerate::new);
		REGISTRY.registerPacket(PacketDriveTrain.class, PacketDriveTrain::new);
		REGISTRY.registerPacket(PacketFetchArrivals.class, PacketFetchArrivals::new);
		REGISTRY.registerPacket(PacketOpenBlockEntityScreen.class, PacketOpenBlockEntityScreen::new);
		REGISTRY.registerPacket(PacketOpenDashboardScreen.class, PacketOpenDashboardScreen::new);
		REGISTRY.registerPacket(PacketOpenLiftCustomizationScreen.class, PacketOpenLiftCustomizationScreen::new);
		REGISTRY.registerPacket(PacketOpenPIDSConfigScreen.class, PacketOpenPIDSConfigScreen::new);
		REGISTRY.registerPacket(PacketOpenTicketMachineScreen.class, PacketOpenTicketMachineScreen::new);
		REGISTRY.registerPacket(PacketPressLiftButton.class, PacketPressLiftButton::new);
		REGISTRY.registerPacket(PacketRequestData.class, PacketRequestData::new);
		REGISTRY.registerPacket(PacketUpdateData.class, PacketUpdateData::new);
		REGISTRY.registerPacket(PacketUpdateLiftTrackFloorConfig.class, PacketUpdateLiftTrackFloorConfig::new);
		REGISTRY.registerPacket(PacketUpdatePIDSConfig.class, PacketUpdatePIDSConfig::new);
		REGISTRY.registerPacket(PacketUpdateRailwaySignConfig.class, PacketUpdateRailwaySignConfig::new);
		REGISTRY.registerPacket(PacketUpdateTrainSensorConfig.class, PacketUpdateTrainSensorConfig::new);
		REGISTRY.registerPacket(PacketUpdateVehiclesLifts.class, PacketUpdateVehiclesLifts::new);
		REGISTRY.registerPacket(PacketUpdateVehicleRidingEntities.class, PacketUpdateVehicleRidingEntities::new);

		// Register command
		REGISTRY.registerCommand("mtr", commandBuilderMtr -> {
			commandBuilderMtr.then("generate", commandBuilderGenerate -> {
				commandBuilderGenerate.permissionLevel(2);
				commandBuilderGenerate.executes(contextHandler -> {
					contextHandler.sendSuccess("command.mtr.generate_all", true);
					return generateDepotsFromCommand(contextHandler.getWorld(), "");
				});
				commandBuilderGenerate.then("name", StringArgumentType.greedyString(), innerCommandBuilder -> innerCommandBuilder.executes(contextHandler -> {
					final String filter = contextHandler.getString("name");
					contextHandler.sendSuccess("command.mtr.generate_filter", true, filter);
					return generateDepotsFromCommand(contextHandler.getWorld(), filter);
				}));
			});
			commandBuilderMtr.then("clear", commandBuilderGenerate -> {
				commandBuilderGenerate.permissionLevel(2);
				commandBuilderGenerate.executes(contextHandler -> {
					contextHandler.sendSuccess("command.mtr.clear", true);
					return 1;
				});
			});
		}, "minecrafttransitrailway");

		// Register events
		EventRegistry.registerServerStarted(minecraftServer -> {
			// Start up the backend
			RAIL_ACTION_MODULES.clear();
			WORLD_ID_LIST.clear();
			MinecraftServerHelper.iterateWorlds(minecraftServer, serverWorld -> {
				RAIL_ACTION_MODULES.put(serverWorld, new RailActionModule(serverWorld));
				WORLD_ID_LIST.add(getWorldId(new World(serverWorld.data)));
			});

			final int defaultPort = getDefaultPortFromConfig(minecraftServer);
			serverPort = findFreePort(defaultPort);
			final int port = findFreePort(serverPort + 1);
			main = new Main(minecraftServer.getSavePath(WorldSavePath.getRootMapped()).resolve("mtr"), serverPort, port, WORLD_ID_LIST.toArray(new String[0]));
			webserver = new Webserver(port);
			webserver.addServlet(new ServletHolder(new SocketServlet(minecraftServer)), "/");
			webserver.start();

			serverTick = 0;
			sendWorldTimeUpdate = () -> sendHttpRequest(
					"operation/set-time",
					null,
					Utilities.getJsonObjectFromData(new SetTime((WorldHelper.getTimeOfDay(minecraftServer.getOverworld()) + 6000) * SECONDS_PER_MC_HOUR, MILLIS_PER_MC_DAY)).toString(),
					null
			);
		});

		EventRegistry.registerServerStopping(minecraftServer -> {
			if (main != null) {
				main.stop();
			}
			if (webserver != null) {
				webserver.stop();
			}
		});

		EventRegistry.registerStartServerTick(() -> {
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
		REGISTRY.init();
	}

	public static void getRailActionModule(ServerWorld serverWorld, Consumer<RailActionModule> consumer) {
		final RailActionModule railActionModule = RAIL_ACTION_MODULES.get(serverWorld);
		if (railActionModule != null) {
			consumer.accept(railActionModule);
		}
	}

	public static void sendHttpRequest(String endpoint, @Nullable World world, String content, @Nullable Consumer<String> consumer) {
		REQUEST_HELPER.sendPostRequest(String.format(
				"http://localhost:%s/mtr/api/%s?%s",
				serverPort,
				endpoint,
				world == null ? "dimensions=all" : "dimension=" + WORLD_ID_LIST.indexOf(getWorldId(world))
		), content, consumer);
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

	public static boolean isChunkLoaded(World world, ChunkManager chunkManager, BlockPos blockPos) {
		return chunkManager.getWorldChunk(blockPos.getX() / 16, blockPos.getZ() / 16) != null && world.isRegionLoaded(blockPos, blockPos);
	}

	private static int getDefaultPortFromConfig(MinecraftServer minecraftServer) {
		final Path filePath = minecraftServer.getRunDirectory().toPath().resolve("config/mtr_webserver_port.txt");
		final int defaultPort = 8888;

		try {
			return Integer.parseInt(FileUtils.readFileToString(filePath.toFile(), StandardCharsets.UTF_8));
		} catch (Exception ignored) {
			try {
				Files.write(filePath, String.valueOf(defaultPort).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}

		return defaultPort;
	}

	private static int findFreePort(int startingPort) {
		for (int i = Math.max(1025, startingPort); i <= 65535; i++) {
			try (final ServerSocket serverSocket = new ServerSocket(i)) {
				final int port = serverSocket.getLocalPort();
				LOGGER.info("Found available port: " + port);
				return port;
			} catch (Exception ignored) {
			}
		}
		return 0;
	}

	private static String getWorldId(World world) {
		final Identifier identifier = MinecraftServerHelper.getWorldId(world);
		return String.format("%s/%s", identifier.getNamespace(), identifier.getPath());
	}

	private static int generateDepotsFromCommand(World world, String filter) {
		final GenerateByDepotName generateByDepotName = new GenerateByDepotName();
		generateByDepotName.setFilter(filter);
		sendHttpRequest("operation/generate-by-depot-name", world, Utilities.getJsonObjectFromData(generateByDepotName).toString(), null);
		return 1;
	}
}
