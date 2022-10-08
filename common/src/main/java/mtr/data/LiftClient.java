package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.KeyMappings;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.screen.LiftSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.function.Consumer;

public class LiftClient extends Lift {

	private final VehicleRidingClient vehicleRidingClient = new VehicleRidingClient(ridingEntities, PACKET_UPDATE_LIFT_PASSENGER_POSITION);

	public LiftClient(FriendlyByteBuf packet) {
		super(packet);
	}

	public void tickClient(Level world, RenderLift renderLift, float ticksElapsed) {
		tick(world, ticksElapsed);
		renderLift.renderLift(currentPositionX, currentPositionY, currentPositionZ, frontCanOpen ? Math.min(doorValue, DOOR_MAX) : 0, backCanOpen ? Math.min(doorValue, DOOR_MAX) : 0);

		final Vec3 offset = vehicleRidingClient.renderPlayerAndGetOffset();
		vehicleRidingClient.begin();
		if (ticksElapsed > 0) {
			vehicleRidingClient.movePlayer(uuid -> {
				vehicleRidingClient.setOffsets(uuid, currentPositionX, currentPositionY, currentPositionZ, facing.toYRot(), 0, liftWidth, liftDepth, frontCanOpen, backCanOpen, transportMode.hasPitch, 0, speed > 0, doorValue == 0, () -> {
				});
				vehicleRidingClient.moveSelf(id, uuid, liftWidth, liftDepth, facing.toYRot(), 0, 1, frontCanOpen, backCanOpen, true, ticksElapsed);
			});
		}
		vehicleRidingClient.end();

		final Minecraft minecraftClient = Minecraft.getInstance();
		final LocalPlayer player = minecraftClient.player;
		if (player != null && ridingEntities.contains(player.getUUID())) {
			if (KeyMappings.LIFT_MENU.isDown() && !(minecraftClient.screen instanceof LiftSelectionScreen)) {
				UtilitiesClient.setScreen(minecraftClient, new LiftSelectionScreen(this));
			}
			player.displayClientMessage(Text.translatable("gui.mtr.press_to_select_floor", KeyMappings.LIFT_MENU.getTranslatedKeyMessage()), true);
		}
	}

	public void copyFromLift(LiftClient lift) {
		liftHeight = lift.liftHeight;
		liftWidth = lift.liftWidth;
		liftDepth = lift.liftDepth;
		liftOffsetX = lift.liftOffsetX;
		liftOffsetY = lift.liftOffsetY;
		liftOffsetZ = lift.liftOffsetZ;
		isDoubleSided = lift.isDoubleSided;
		liftStyle = lift.liftStyle;
		facing = lift.facing;

		currentPositionX = lift.currentPositionX;
		currentPositionY = lift.currentPositionY;
		currentPositionZ = lift.currentPositionZ;
		liftDirection = lift.liftDirection;
		speed = lift.speed;
		doorOpen = lift.doorOpen;
		doorValue = lift.doorValue;
		frontCanOpen = lift.frontCanOpen;
		backCanOpen = lift.backCanOpen;

		ridingEntities.clear();
		ridingEntities.addAll(lift.ridingEntities);
		floors.clear();
		floors.addAll(lift.floors);

		liftInstructions.copyFrom(lift.liftInstructions);
	}

	public void setExtraData(Consumer<FriendlyByteBuf> sendPacket) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		packet.writeUtf(transportMode.toString());
		packet.writeUtf(KEY_LIFT_UPDATE);
		packet.writeInt(liftHeight);
		packet.writeInt(liftWidth);
		packet.writeInt(liftDepth);
		packet.writeInt(liftOffsetX);
		packet.writeInt(liftOffsetY);
		packet.writeInt(liftOffsetZ);
		packet.writeBoolean(isDoubleSided);
		packet.writeUtf(liftStyle.toString());
		packet.writeInt(Math.round(facing.toYRot()));
		liftInstructions.writePacket(packet);
		sendPacket.accept(packet);
	}

	public void startRidingClient(UUID uuid, float percentageX, float percentageZ) {
		vehicleRidingClient.startRiding(uuid, percentageX, percentageZ);
	}

	public void updateRiderPercentages(UUID uuid, float percentageX, float percentageZ) {
		vehicleRidingClient.updateRiderPercentages(uuid, percentageX, percentageZ);
	}

	public void iterateFloors(Consumer<BlockPos> consumer) {
		floors.forEach(consumer);
	}

	@FunctionalInterface
	public interface RenderLift {
		void renderLift(double currentPositionX, double currentPositionY, double currentPositionZ, float frontDoorValue, float backDoorValue);
	}
}
