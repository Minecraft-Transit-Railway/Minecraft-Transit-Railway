package org.mtr.mod;

import com.mojang.brigadier.arguments.StringArgumentType;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mtr.core.Main;
import org.mtr.core.data.Position;
import org.mtr.core.operation.GenerateOrClearByDepotName;
import org.mtr.core.operation.SetTime;
import org.mtr.core.servlet.Webserver;
import org.mtr.core.simulation.Simulator;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.org.eclipse.jetty.servlet.ServletHolder;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GameRule;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.mapper.WorldHelper;
import org.mtr.mapping.registry.CommandBuilder;
import org.mtr.mapping.registry.Registry;
import org.mtr.mapping.tool.DummyClass;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.ArrivalsCacheServer;
import org.mtr.mod.data.RailActionModule;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.*;
import org.mtr.mod.servlet.Tunnel;
import org.mtr.mod.servlet.VehicleLiftServlet;

import javax.annotation.Nullable;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public final class Init implements Utilities {

	private static Main main;
	private static Webserver webserver;
	private static Tunnel tunnel;
	private static int serverPort;
	private static Runnable sendWorldTimeUpdate;
	private static boolean canSendWorldTimeUpdate = true;
	private static int serverTick;

	public static final String MOD_ID = "mtr";
	public static final String MOD_ID_NTE = "mtrsteamloco";
	public static final Logger LOGGER = LogManager.getLogger("MinecraftTransitRailway");
	public static final Registry REGISTRY = new Registry();
	public static final int SECONDS_PER_MC_HOUR = 50;

	private static final int MILLIS_PER_MC_DAY = SECONDS_PER_MC_HOUR * MILLIS_PER_SECOND * HOURS_PER_DAY;
	private static final Object2ObjectArrayMap<ServerWorld, RailActionModule> RAIL_ACTION_MODULES = new Object2ObjectArrayMap<>();
	private static final ObjectArrayList<String> WORLD_ID_LIST = new ObjectArrayList<>();

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
		REGISTRY.registerPacket(PacketDepotClear.class, PacketDepotClear::new);
		REGISTRY.registerPacket(PacketDepotGenerate.class, PacketDepotGenerate::new);
		REGISTRY.registerPacket(PacketDriveTrain.class, PacketDriveTrain::new);
		REGISTRY.registerPacket(PacketFetchArrivals.class, PacketFetchArrivals::new);
		REGISTRY.registerPacket(PacketForwardClientRequest.class, PacketForwardClientRequest::new);
		REGISTRY.registerPacket(PacketOpenBlockEntityScreen.class, PacketOpenBlockEntityScreen::new);
		REGISTRY.registerPacket(PacketOpenDashboardScreen.class, PacketOpenDashboardScreen::new);
		REGISTRY.registerPacket(PacketOpenLiftCustomizationScreen.class, PacketOpenLiftCustomizationScreen::new);
		REGISTRY.registerPacket(PacketOpenPIDSConfigScreen.class, PacketOpenPIDSConfigScreen::new);
		REGISTRY.registerPacket(PacketOpenTicketMachineScreen.class, PacketOpenTicketMachineScreen::new);
		REGISTRY.registerPacket(PacketPressLiftButton.class, PacketPressLiftButton::new);
		REGISTRY.registerPacket(PacketRequestData.class, PacketRequestData::new);
		REGISTRY.registerPacket(PacketTurnOnBlockEntity.class, PacketTurnOnBlockEntity::new);
		REGISTRY.registerPacket(PacketUpdateData.class, PacketUpdateData::new);
		REGISTRY.registerPacket(PacketUpdateEyeCandyConfig.class, PacketUpdateEyeCandyConfig::new);
		REGISTRY.registerPacket(PacketUpdateLastRailStyles.class, PacketUpdateLastRailStyles::new);
		REGISTRY.registerPacket(PacketUpdateLiftTrackFloorConfig.class, PacketUpdateLiftTrackFloorConfig::new);
		REGISTRY.registerPacket(PacketUpdatePIDSConfig.class, PacketUpdatePIDSConfig::new);
		REGISTRY.registerPacket(PacketUpdateRailwaySignConfig.class, PacketUpdateRailwaySignConfig::new);
		REGISTRY.registerPacket(PacketUpdateSignalConfig.class, PacketUpdateSignalConfig::new);
		REGISTRY.registerPacket(PacketUpdateTrainAnnouncerConfig.class, PacketUpdateTrainAnnouncerConfig::new);
		REGISTRY.registerPacket(PacketUpdateTrainScheduleSensorConfig.class, PacketUpdateTrainScheduleSensorConfig::new);
		REGISTRY.registerPacket(PacketUpdateTrainSensorConfig.class, PacketUpdateTrainSensorConfig::new);
		REGISTRY.registerPacket(PacketUpdateVehiclesLifts.class, PacketUpdateVehiclesLifts::new);
		REGISTRY.registerPacket(PacketUpdateVehicleRidingEntities.class, PacketUpdateVehicleRidingEntities::new);

		// Register command
		REGISTRY.registerCommand("mtr", commandBuilderMtr -> {
			// Generate depot(s) by name
			commandBuilderMtr.then("generate", commandBuilderGenerate -> generateOrClearDepotsFromCommand(commandBuilderGenerate, true));
			// Clear depot(s) by name
			commandBuilderMtr.then("clear", commandBuilderClear -> generateOrClearDepotsFromCommand(commandBuilderClear, false));
			// Force copy a world backup from one folder another
			commandBuilderMtr.then("forceCopyWorld", commandBuilderForceCopy -> {
				commandBuilderForceCopy.permissionLevel(4);
				commandBuilderForceCopy.then("worldDirectory", StringArgumentType.string(), innerCommandBuilder1 -> innerCommandBuilder1.then("backupDirectory", StringArgumentType.string(), innerCommandBuilder2 -> innerCommandBuilder2.executes(contextHandler -> {
					final Path runPath = contextHandler.getServer().getRunDirectory().toPath();
					final Path worldDirectory = runPath.resolve(contextHandler.getString("worldDirectory"));
					final Path backupDirectory = runPath.resolve(contextHandler.getString("backupDirectory"));
					final boolean worldDirectoryExists = Files.isDirectory(worldDirectory);
					final boolean backupDirectoryExists = Files.isDirectory(backupDirectory);
					if (worldDirectoryExists && backupDirectoryExists) {
						try {
							if (main != null) {
								main.stop();
							}
							if (webserver != null) {
								webserver.stop();
							}
							contextHandler.sendSuccess(String.format("Restoring world backup from %s to %s...", backupDirectory, worldDirectory), true);
							FileUtils.deleteDirectory(worldDirectory.toFile());
							contextHandler.sendSuccess("Deleting world complete", true);
							FileUtils.copyDirectory(backupDirectory.toFile(), worldDirectory.toFile());
							contextHandler.sendSuccess("Restoring world backup complete", true);
							System.exit(0);
							return 1;
						} catch (Exception e) {
							contextHandler.sendFailure("Restoring world backup failed");
							LOGGER.error("", e);
							return -1;
						}
					} else {
						if (backupDirectoryExists) {
							contextHandler.sendFailure("World directory not found");
						} else if (worldDirectoryExists) {
							contextHandler.sendFailure("Backup directory not found");
						} else {
							contextHandler.sendFailure("Directories not found");
						}
						return -1;
					}
				})));
			});
		}, "minecrafttransitrailway");

		// Register events
		REGISTRY.eventRegistry.registerServerStarted(minecraftServer -> {
			// Start up the backend
			RAIL_ACTION_MODULES.clear();
			WORLD_ID_LIST.clear();
			MinecraftServerHelper.iterateWorlds(minecraftServer, serverWorld -> {
				RAIL_ACTION_MODULES.put(serverWorld, new RailActionModule(serverWorld));
				WORLD_ID_LIST.add(getWorldId(new World(serverWorld.data)));
			});

			Config.init(minecraftServer.getRunDirectory());
			final int defaultPort = Config.getServer().getWebserverPort();
			serverPort = findFreePort(defaultPort);
			tunnel = new Tunnel(minecraftServer.getRunDirectory(), defaultPort, () -> {
			});

			final int port = findFreePort(serverPort + 1);
			main = new Main(minecraftServer.getSavePath(WorldSavePath.getRootMapped()).resolve("mtr"), serverPort, port, Config.getServer().getUseThreadedSimulation(), WORLD_ID_LIST.toArray(new String[0]));
			webserver = new Webserver(port);
			webserver.addServlet(new ServletHolder(new VehicleLiftServlet(minecraftServer)), "/vehicles-lifts");
			webserver.start();

			serverTick = 0;
			sendWorldTimeUpdate = () -> {
				if (canSendWorldTimeUpdate) {
					canSendWorldTimeUpdate = false;
					sendHttpRequest(
							"operation/set-time",
							null,
							Utilities.getJsonObjectFromData(new SetTime(
									(WorldHelper.getTimeOfDay(minecraftServer.getOverworld()) + 6000) * SECONDS_PER_MC_HOUR,
									MILLIS_PER_MC_DAY,
									GameRule.DO_DAYLIGHT_CYCLE.getBooleanGameRule(minecraftServer)
							)).toString(),
							response -> canSendWorldTimeUpdate = true
					);
				} else {
					Main.LOGGER.error("Transport Simulation Core not responding; stopping Minecraft server!");
					if (main != null) {
						main.stop();
					}
					if (webserver != null) {
						webserver.stop();
					}
					Main.LOGGER.error("Shutting down all threads");
					System.exit(0);
				}
			};
		});

		REGISTRY.eventRegistry.registerServerStopping(minecraftServer -> {
			if (tunnel != null) {
				tunnel.stop();
			}
			if (main != null) {
				main.stop();
			}
			if (webserver != null) {
				webserver.stop();
			}
		});

		REGISTRY.eventRegistry.registerStartServerTick(() -> {
			if (sendWorldTimeUpdate != null && serverTick % (SECONDS_PER_MC_HOUR * 10) == 0) {
				sendWorldTimeUpdate.run();
			}
			ArrivalsCacheServer.tickAll();
			serverTick++;
			if (!Config.getServer().getUseThreadedSimulation()) {
				main.manualTick();
			}
		});

		REGISTRY.eventRegistry.registerEndWorldTick(serverWorld -> {
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

	public static void sendHttpRequest(String endpoint, @Nullable String content, @Nullable Consumer<String> consumer) {
		Simulator.REQUEST_HELPER.sendRequest(String.format("http://localhost:%s%s", serverPort, endpoint), content, consumer);
	}

	public static void sendHttpRequest(String endpoint, @Nullable World world, String content, @Nullable Consumer<String> consumer) {
		sendHttpRequest(String.format("/mtr/api/%s?%s", endpoint, world == null ? "dimensions=all" : "dimension=" + WORLD_ID_LIST.indexOf(getWorldId(world))), content, consumer);
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

	public static String getWorldId(World world) {
		final Identifier identifier = MinecraftServerHelper.getWorldId(world);
		return String.format("%s/%s", identifier.getNamespace(), identifier.getPath());
	}

	public static String getTunnelUrl() {
		return tunnel.getTunnelUrl();
	}

	public static int findFreePort(int startingPort) {
		for (int i = Math.max(1024, startingPort); i <= 65535; i++) {
			// Start with port 80, then search from 1025 onwards
			try (final ServerSocket serverSocket = new ServerSocket(i == 1024 ? 80 : i)) {
				final int port = serverSocket.getLocalPort();
				LOGGER.info("Found available port: {}", port);
				return port;
			} catch (Exception ignored) {
			}
		}
		return 0;
	}

	private static void generateOrClearDepotsFromCommand(CommandBuilder<?> commandBuilder, boolean isGenerate) {
		commandBuilder.permissionLevel(2);
		commandBuilder.executes(contextHandler -> {
			contextHandler.sendSuccess((isGenerate ? TranslationProvider.COMMAND_MTR_GENERATE_ALL : TranslationProvider.COMMAND_MTR_CLEAR_ALL).key, true);
			return generateOrClearDepotsFromCommand(contextHandler.getWorld(), "", isGenerate);
		});
		commandBuilder.then("name", StringArgumentType.greedyString(), innerCommandBuilder -> innerCommandBuilder.executes(contextHandler -> {
			final String filter = contextHandler.getString("name");
			contextHandler.sendSuccess((isGenerate ? TranslationProvider.COMMAND_MTR_GENERATE_FILTER : TranslationProvider.COMMAND_MTR_CLEAR_FILTER).key, true, filter);
			return generateOrClearDepotsFromCommand(contextHandler.getWorld(), filter, isGenerate);
		}));
	}

	private static int generateOrClearDepotsFromCommand(World world, String filter, boolean isGenerate) {
		final GenerateOrClearByDepotName generateByDepotName = new GenerateOrClearByDepotName();
		generateByDepotName.setFilter(filter);
		sendHttpRequest(isGenerate ? "operation/generate-by-depot-name" : "operation/clear-by-depot-name", world, Utilities.getJsonObjectFromData(generateByDepotName).toString(), null);
		return 1;
	}
}
