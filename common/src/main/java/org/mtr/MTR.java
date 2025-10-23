package org.mtr;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mtr.config.Config;
import org.mtr.core.Main;
import org.mtr.core.data.Position;
import org.mtr.core.operation.DepotOperationByName;
import org.mtr.core.operation.SetTime;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.servlet.QueueObject;
import org.mtr.core.servlet.Webserver;
import org.mtr.core.tool.Utilities;
import org.mtr.data.ArrivalsCacheServer;
import org.mtr.data.RailActionModule;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.mixin.PlayerTeleportationStateAccessor;
import org.mtr.packet.*;
import org.mtr.registry.*;
import org.mtr.servlet.MinecraftOperationProcessor;
import org.mtr.servlet.RequestHelper;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public final class MTR {

	private static Main main;
	private static int serverPort;
	private static Runnable sendWorldTimeUpdate;
	private static boolean canSendWorldTimeUpdate = true;
	private static boolean isDedicatedServer = true;
	private static int serverTick;
	private static long lastSavedMillis;
	private static Consumer<Webserver> webserverSetup;

	public static final String MOD_ID = "mtr";
	public static final CustomPayload.Id<CustomPacketS2C> PACKET_IDENTIFIER_S2C = new CustomPayload.Id<>(Identifier.of(MOD_ID, "packet_s2c"));
	public static final CustomPayload.Id<CustomPacketC2S> PACKET_IDENTIFIER_C2S = new CustomPayload.Id<>(Identifier.of(MOD_ID, "packet_c2s"));
	public static final Logger LOGGER = LogManager.getLogger("MinecraftTransitRailway");
	public static final int SECONDS_PER_MC_HOUR = 50;
	public static final int AUTOSAVE_INTERVAL = 30000;
	public static final RequestHelper REQUEST_HELPER = new RequestHelper();

	private static final int MILLIS_PER_MC_DAY = SECONDS_PER_MC_HOUR * Utilities.MILLIS_PER_SECOND * Utilities.HOURS_PER_DAY;
	private static final Object2ObjectArrayMap<ServerWorld, RailActionModule> RAIL_ACTION_MODULES = new Object2ObjectArrayMap<>();
	private static final ObjectArrayList<String> WORLD_ID_LIST = new ObjectArrayList<>();
	private static final Object2ObjectAVLTreeMap<UUID, Runnable> RIDING_PLAYERS = new Object2ObjectAVLTreeMap<>();

	public static void init() {
		LOGGER.info("Starting Minecraft with arguments:\n{}", String.join("\n", ManagementFactory.getRuntimeMXBean().getInputArguments()));
		AsciiArt.print();
		Blocks.init();
		Items.init();
		BlockEntityTypes.init();
		ItemGroups.init();
		SoundEvents.init();
		DataComponentTypes.init();

		Registry.init();

		// Register packets
		Registry.setupPackets();
		Registry.registerPacket(PacketAddBalance.class, PacketAddBalance::new);
		Registry.registerPacket(PacketBlockRails.class, PacketBlockRails::new);
		Registry.registerPacket(PacketBroadcastRailActions.class, PacketBroadcastRailActions::new);
		Registry.registerPacket(PacketCheckRouteIdHasDisabledAnnouncements.class, PacketCheckRouteIdHasDisabledAnnouncements::new);
		Registry.registerPacket(PacketDeleteData.class, PacketDeleteData::new);
		Registry.registerPacket(PacketDeleteRailAction.class, PacketDeleteRailAction::new);
		Registry.registerPacket(PacketDepotClear.class, PacketDepotClear::new);
		Registry.registerPacket(PacketDepotInstantDeploy.class, PacketDepotInstantDeploy::new);
		Registry.registerPacket(PacketDepotGenerate.class, PacketDepotGenerate::new);
		Registry.registerPacket(PacketDriveTrain.class, PacketDriveTrain::new);
		Registry.registerPacket(PacketFetchArrivals.class, PacketFetchArrivals::new);
		Registry.registerPacket(PacketForwardClientRequest.class, PacketForwardClientRequest::new);
		Registry.registerPacket(PacketUpdateKeyDispenserConfig.class, PacketUpdateKeyDispenserConfig::new);
		Registry.registerPacket(PacketGetUniqueWorldId.class, PacketGetUniqueWorldId::new);
		Registry.registerPacket(PacketOpenBlockEntityScreen.class, PacketOpenBlockEntityScreen::new);
		Registry.registerPacket(PacketOpenDashboardScreen.class, PacketOpenDashboardScreen::new);
		Registry.registerPacket(PacketOpenLiftCustomizationScreen.class, PacketOpenLiftCustomizationScreen::new);
		Registry.registerPacket(PacketOpenPIDSConfigScreen.class, PacketOpenPIDSConfigScreen::new);
		Registry.registerPacket(PacketOpenTicketMachineScreen.class, PacketOpenTicketMachineScreen::new);
		Registry.registerPacket(PacketPressLiftButton.class, PacketPressLiftButton::new);
		Registry.registerPacket(PacketRequestData.class, PacketRequestData::new);
		Registry.registerPacket(PacketSetRouteIdHasDisabledAnnouncements.class, PacketSetRouteIdHasDisabledAnnouncements::new);
		Registry.registerPacket(PacketTurnOnBlockEntity.class, PacketTurnOnBlockEntity::new);
		Registry.registerPacket(PacketUpdateData.class, PacketUpdateData::new);
		Registry.registerPacket(PacketUpdateDynamicData.class, PacketUpdateDynamicData::new);
		Registry.registerPacket(PacketUpdateEyeCandyConfig.class, PacketUpdateEyeCandyConfig::new);
		Registry.registerPacket(PacketUpdateLastRailStyles.class, PacketUpdateLastRailStyles::new);
		Registry.registerPacket(PacketUpdateLiftTrackFloorConfig.class, PacketUpdateLiftTrackFloorConfig::new);
		Registry.registerPacket(PacketUpdatePIDSConfig.class, PacketUpdatePIDSConfig::new);
		Registry.registerPacket(PacketUpdateRailwaySignConfig.class, PacketUpdateRailwaySignConfig::new);
		Registry.registerPacket(PacketUpdateSignalConfig.class, PacketUpdateSignalConfig::new);
		Registry.registerPacket(PacketUpdateTrainAnnouncerConfig.class, PacketUpdateTrainAnnouncerConfig::new);
		Registry.registerPacket(PacketUpdateTrainScheduleSensorConfig.class, PacketUpdateTrainScheduleSensorConfig::new);
		Registry.registerPacket(PacketUpdateTrainSensorConfig.class, PacketUpdateTrainSensorConfig::new);
		Registry.registerPacket(PacketUpdateVehicleRidingEntities.class, PacketUpdateVehicleRidingEntities::new);

		// Register commands
		Registry.registerCommands(dispatcher ->
				{
					final LiteralCommandNode<ServerCommandSource> command = dispatcher.register(CommandManager.literal("mtr")
							// Generate depot(s) by name
							.then(depotOperationFromCommand(CommandManager.literal("generate"), DepotOperation.GENERATE))
							// Clear depot(s) by name
							.then(depotOperationFromCommand(CommandManager.literal("clear"), DepotOperation.CLEAR))
							// Instant deploy depot(s) by name
							.then(depotOperationFromCommand(CommandManager.literal("instantDeploy"), DepotOperation.INSTANT_DEPLOY))
							// Force copy a world backup from one folder another
							.then(CommandManager.literal("restoreWorld").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(4)).then(CommandManager.argument("worldDirectory", StringArgumentType.string()).then(CommandManager.argument("backupDirectory", StringArgumentType.string()).executes(contextHandler -> {
								final Path runPath = contextHandler.getSource().getServer().getRunDirectory();
								final Path worldDirectory = runPath.resolve(StringArgumentType.getString(contextHandler, "worldDirectory"));
								final Path backupDirectory = runPath.resolve(StringArgumentType.getString(contextHandler, "backupDirectory"));
								final boolean worldDirectoryExists = Files.isDirectory(worldDirectory);
								final boolean backupDirectoryExists = Files.isDirectory(backupDirectory);
								if (worldDirectoryExists && backupDirectoryExists) {
									try {
										if (main != null) {
											main.stop();
										}
										contextHandler.getSource().sendFeedback(() -> Text.literal(String.format("Restoring world backup from %s to %s...", backupDirectory, worldDirectory)), true);
										FileUtils.deleteDirectory(worldDirectory.toFile());
										contextHandler.getSource().sendFeedback(() -> Text.literal("Deleting world complete"), true);
										FileUtils.copyDirectory(backupDirectory.toFile(), worldDirectory.toFile());
										contextHandler.getSource().sendFeedback(() -> Text.literal("Restoring world backup complete"), true);
										System.exit(0);
										return 1;
									} catch (Exception e) {
										contextHandler.getSource().sendError(Text.literal("Restoring world backup failed"));
										LOGGER.error("", e);
										return -1;
									}
								} else {
									if (backupDirectoryExists) {
										contextHandler.getSource().sendError(Text.literal("World directory not found"));
									} else if (worldDirectoryExists) {
										contextHandler.getSource().sendError(Text.literal("Backup directory not found"));
									} else {
										contextHandler.getSource().sendError(Text.literal("Directories not found"));
									}
									return -1;
								}
							})))));
					dispatcher.register(CommandManager.literal("minecraftTransitRailway").redirect(command));
				}
		);

		// Register events
		EventRegistry.registerServerStarted(minecraftServer -> {
			// Start up the backend
			RAIL_ACTION_MODULES.clear();
			WORLD_ID_LIST.clear();
			minecraftServer.getWorlds().forEach(serverWorld -> {
				RAIL_ACTION_MODULES.put(serverWorld, new RailActionModule(serverWorld));
				WORLD_ID_LIST.add(getWorldId(serverWorld));
			});

			Config.init(minecraftServer.getRunDirectory());
			final int defaultPort = Config.getServer().getWebserverPort();
			serverPort = defaultPort <= 0 ? -1 : findFreePort(defaultPort);
			main = new Main(minecraftServer.getSavePath(WorldSavePath.ROOT).resolve("mtr"), serverPort, Config.getServer().getUseThreadedSimulation(), Config.getServer().getUseThreadedFileLoading(), webserverSetup, WORLD_ID_LIST.toArray(new String[0]));

			serverTick = 0;
			lastSavedMillis = System.currentTimeMillis();
			sendWorldTimeUpdate = () -> {
				if (canSendWorldTimeUpdate) {
					canSendWorldTimeUpdate = false;
					sendMessageC2S(
							OperationProcessor.SET_TIME,
							minecraftServer,
							null,
							new SetTime(
									(minecraftServer.getOverworld().getTimeOfDay() + 6000) * SECONDS_PER_MC_HOUR,
									MILLIS_PER_MC_DAY,
									minecraftServer.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)
							),
							response -> canSendWorldTimeUpdate = true,
							SerializedDataBase.class
					);
				} else {
					Main.LOGGER.error("Transport Simulation Core not responding; stopping Minecraft server!");
					minecraftServer.stop(false);
					canSendWorldTimeUpdate = true; // In singleplayer, this gives the player opportunity to re-enter world.
				}
			};

			LOGGER.info("Starting server as a {} server", isDedicatedServer ? "dedicated" : "non-dedicated");
			if (isDedicatedServer && Config.getServer().forceShutDownStrayThreads()) {
				StrayThreadManager.register(minecraftServer);
			}

			Main.CLIENT_NAME_RESOLVER = uuid -> {
				final ServerPlayerEntity serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(uuid);
				return serverPlayerEntity == null ? "" : serverPlayerEntity.getName().getString();
			};
		});

		EventRegistry.registerServerStopping(minecraftServer -> {
			if (main != null) {
				main.stop();
			}
			serverPort = 0;
			RIDING_PLAYERS.clear();
		});

		EventRegistry.registerStartServerTick(() -> {
			if (sendWorldTimeUpdate != null && serverTick % (SECONDS_PER_MC_HOUR * 10) == 0) {
				sendWorldTimeUpdate.run();
			}

			ArrivalsCacheServer.tickAll();
			serverTick++;

			if (main != null) {
				if (!Config.getServer().getUseThreadedSimulation()) {
					main.manualTick();
				}

				final long currentMillis = System.currentTimeMillis();
				if (currentMillis - lastSavedMillis > AUTOSAVE_INTERVAL) {
					main.save();
					lastSavedMillis = currentMillis;
				}
			}

			RIDING_PLAYERS.values().forEach(Runnable::run);
		});

		EventRegistry.registerEndWorldTick(serverWorld -> {
			final RailActionModule railActionModule = RAIL_ACTION_MODULES.get(serverWorld);
			if (railActionModule != null) {
				railActionModule.tick();
			}

			if (main != null) {
				final String dimension = getWorldId(serverWorld);
				main.processMessagesS2C(WORLD_ID_LIST.indexOf(dimension), queueObject -> MinecraftOperationProcessor.process(queueObject, serverWorld, dimension));
			}
		});

		EventRegistry.registerPlayerJoin((minecraftServer, serverPlayerEntity) -> updatePlayer(serverPlayerEntity, false));
		EventRegistry.registerPlayerDisconnect((minecraftServer, serverPlayerEntity) -> RIDING_PLAYERS.remove(serverPlayerEntity.getUuid()));
	}

	public static void getRailActionModule(ServerWorld serverWorld, Consumer<RailActionModule> consumer) {
		final RailActionModule railActionModule = RAIL_ACTION_MODULES.get(serverWorld);
		if (railActionModule != null) {
			consumer.accept(railActionModule);
		}
	}

	/**
	 * @return the port of the webserver started by Transport Simulation Core, not the clientside webserver.
	 * <br>{@code 0} means the integrated server is not running
	 * <br>{@code -1} means the webserver is disabled
	 */
	public static int getServerPort() {
		return serverPort;
	}

	public static <T extends SerializedDataBase> void sendMessageC2S(String key, @Nullable MinecraftServer minecraftServer, @Nullable World world, SerializedDataBase data, @Nullable Consumer<T> consumer, @Nullable Class<T> responseDataClass) {
		if (main != null) {
			main.sendMessageC2S(world == null ? null : WORLD_ID_LIST.indexOf(getWorldId(world)), new QueueObject(key, data, consumer == null || minecraftServer == null ? null : responseData -> minecraftServer.execute(() -> consumer.accept(responseData)), responseDataClass));
		}
	}

	public static BlockPos positionToBlockPos(Position position) {
		return new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ());
	}

	public static Position blockPosToPosition(BlockPos blockPos) {
		return new Position(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public static boolean isChunkLoaded(World world, BlockPos blockPos) {
		return world.getChunkManager().getWorldChunk(blockPos.getX() / 16, blockPos.getZ() / 16) != null && world.isRegionLoaded(blockPos, blockPos);
	}

	public static void updateRidingEntity(ServerPlayerEntity serverPlayerEntity, boolean dismount) {
		if (dismount) {
			RIDING_PLAYERS.remove(serverPlayerEntity.getUuid());
			updatePlayer(serverPlayerEntity, false);
		} else {
			RIDING_PLAYERS.put(serverPlayerEntity.getUuid(), () -> updatePlayer(serverPlayerEntity, true));
		}
	}

	public static String getWorldId(World world) {
		final Identifier identifier = world.getRegistryKey().getValue();
		return String.format("%s/%s", identifier.getNamespace(), identifier.getPath());
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

	public static void openConnectionSafe(String url, Consumer<InputStream> callback, String... requestProperties) {
		try {
			final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setUseCaches(false);

			for (int i = 0; i < requestProperties.length / 2; i++) {
				connection.setRequestProperty(requestProperties[2 * i], requestProperties[2 * i + 1]);
			}

			try (final InputStream inputStream = connection.getInputStream()) {
				callback.accept(inputStream);
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static void openConnectionSafeJson(String url, Consumer<JsonElement> callback, String... requestProperties) {
		openConnectionSafe(url, inputStream -> {
			try (final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
				callback.accept(JsonParser.parseReader(inputStreamReader));
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
		}, requestProperties);
	}

	public static void writeFromClient() {
		isDedicatedServer = false;
	}

	public static void createWebserverSetup(Consumer<Webserver> webserverSetup) {
		MTR.webserverSetup = webserverSetup;
	}

	public static String randomString() {
		return Integer.toHexString(new Random().nextInt());
	}

	private static LiteralCommandNode<ServerCommandSource> depotOperationFromCommand(LiteralArgumentBuilder<ServerCommandSource> commandBuilder, DepotOperation depotOperation) {
		return commandBuilder.requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2)).executes(contextHandler -> {
			contextHandler.getSource().sendFeedback(depotOperation.translationHolderAll::getText, true);
			return depotOperationFromCommand(contextHandler.getSource().getWorld(), "", depotOperation);
		}).then(CommandManager.argument("name", StringArgumentType.greedyString()).executes(contextHandler -> {
			final String filter = StringArgumentType.getString(contextHandler, "name");
			contextHandler.getSource().sendFeedback(() -> depotOperation.translationHolderName.getText(filter), true);
			return depotOperationFromCommand(contextHandler.getSource().getWorld(), filter, depotOperation);
		})).build();
	}

	private static int depotOperationFromCommand(World world, String filter, DepotOperation depotOperation) {
		final DepotOperationByName depotOperationByName = new DepotOperationByName();
		depotOperationByName.setFilter(filter);
		sendMessageC2S(depotOperation.operation, world.getServer(), world, depotOperationByName, null, null);
		return 1;
	}

	private static void updatePlayer(ServerPlayerEntity serverPlayerEntity, boolean isRiding) {
		serverPlayerEntity.fallDistance = 0;
		serverPlayerEntity.setNoGravity(isRiding);
		serverPlayerEntity.noClip = isRiding;
		((PlayerTeleportationStateAccessor) serverPlayerEntity).setInTeleportationState(isRiding);
	}

	private enum DepotOperation {
		GENERATE(TranslationProvider.COMMAND_MTR_GENERATE_ALL, TranslationProvider.COMMAND_MTR_GENERATE_FILTER, OperationProcessor.GENERATE_BY_DEPOT_NAME),
		CLEAR(TranslationProvider.COMMAND_MTR_CLEAR_ALL, TranslationProvider.COMMAND_MTR_CLEAR_FILTER, OperationProcessor.CLEAR_BY_DEPOT_NAME),
		INSTANT_DEPLOY(TranslationProvider.COMMAND_MTR_INSTANT_DEPLOY_ALL, TranslationProvider.COMMAND_MTR_INSTANT_DEPLOY_FILTER, OperationProcessor.INSTANT_DEPLOY_BY_DEPOT_NAME);

		private final TranslationProvider.TranslationHolder translationHolderAll;
		private final TranslationProvider.TranslationHolder translationHolderName;
		private final String operation;

		DepotOperation(TranslationProvider.TranslationHolder translationHolderAll, TranslationProvider.TranslationHolder translationHolderName, String operation) {
			this.translationHolderAll = translationHolderAll;
			this.translationHolderName = translationHolderName;
			this.operation = operation;
		}
	}
}
