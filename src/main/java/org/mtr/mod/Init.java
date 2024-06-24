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
import org.mtr.mod.data.RailActionModule;
import org.mtr.mod.packet.*;

import java.net.URI;
import java.util.UUID;
import java.util.function.Consumer;

public final class Init {

	private static Main main;
	private static Socket socket;
	private static final String CHANNEL = "update";
	private static final Object2ObjectArrayMap<ServerWorld, RailActionModule> RAIL_ACTION_MODULES = new Object2ObjectArrayMap<>();

	public static void init() {
		System.out.println(Blocks.APG_GLASS);
		System.out.println(Items.APG_GLASS);
		System.out.println(Blocks.APG_GLASS);
		System.out.println(Items.APG_GLASS);
		System.out.println(BlockEntityTypes.APG_GLASS);
		System.out.println(CreativeModeTabs.CORE);
		System.out.println(SoundEvents.TICKET_BARRIER);

		// Register packets
		Registry.setupPackets(new Identifier(MTR.MOD_ID, "packet"));
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
			socket = IO.socket(URI.create("http://localhost:8888"), IO.Options.builder().setForceNew(false).build());
			socket.on(CHANNEL, args -> {
				try {
					final JsonObject jsonObject = JsonParser.parseString(args[0].toString()).getAsJsonObject();
					jsonObject.keySet().forEach(key -> {
						final ServerPlayerEntity serverPlayerEntity = minecraftServer.getPlayerManager().getPlayer(UUID.fromString(key));
						if (serverPlayerEntity != null) {
							Registry.sendPacketToClient(serverPlayerEntity, new PacketData(IntegrationServlet.Operation.UPDATE, jsonObject.getAsJsonObject(key)));
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
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
