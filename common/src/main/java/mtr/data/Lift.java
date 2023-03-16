package mtr.data;

import mtr.block.BlockLiftTrackFloor;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.IBlock;
import mtr.mappings.Utilities;
import mtr.packet.IPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.*;

public abstract class Lift extends NameColorDataBase implements IPacket {

	public int liftHeight;
	public int liftWidth;
	public int liftDepth;
	public int liftOffsetX;
	public int liftOffsetY;
	public int liftOffsetZ;
	public boolean isDoubleSided;
	public LiftStyle liftStyle;
	public Direction facing;

	protected double currentPositionX;
	protected double currentPositionY;
	protected double currentPositionZ;
	protected LiftDirection liftDirection = LiftDirection.NONE;
	protected double speed;
	protected boolean doorOpen = true;
	protected float doorValue;
	protected boolean frontCanOpen;
	protected boolean backCanOpen;

	public final LiftInstructions liftInstructions;
	protected final List<BlockPos> floors = new ArrayList<>();
	protected final Set<UUID> ridingEntities = new HashSet<>();

	public static final int DOOR_MAX = 24;

	protected static final String KEY_LIFT_UPDATE = "lift_update";
	private static final String KEY_LIFT_HEIGHT = "lift_height";
	private static final String KEY_LIFT_WIDTH = "lift_width";
	private static final String KEY_LIFT_DEPTH = "lift_depth";
	private static final String KEY_LIFT_OFFSET_X = "lift_offset_x";
	private static final String KEY_LIFT_OFFSET_Y = "lift_offset_y";
	private static final String KEY_LIFT_OFFSET_Z = "lift_offset_z";
	private static final String KEY_IS_DOUBLE_SIDED = "is_double_sided";
	private static final String KEY_LIFT_STYLE = "lift_style";
	private static final String KEY_FACING = "facing";
	private static final String KEY_CURRENT_POSITION_X = "current_position_x";
	private static final String KEY_CURRENT_POSITION_Y = "current_position_y";
	private static final String KEY_CURRENT_POSITION_Z = "current_position_z";
	private static final String KEY_RIDING_ENTITIES = "riding_entities";
	private static final String KEY_FLOORS = "floors";

	public Lift(BlockPos pos, Direction facing) {
		liftHeight = 4;
		liftWidth = 2;
		liftDepth = 2;
		liftOffsetX = 0;
		liftOffsetY = 0;
		liftOffsetZ = 0;
		isDoubleSided = false;
		liftStyle = LiftStyle.TRANSPARENT;
		this.facing = facing;
		currentPositionX = pos.getX();
		currentPositionY = pos.getY();
		currentPositionZ = pos.getZ();
		liftInstructions = new LiftInstructions();
	}

	public Lift(Map<String, Value> map) {
		super(map);
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);

		liftHeight = messagePackHelper.getInt(KEY_LIFT_HEIGHT);
		liftWidth = messagePackHelper.getInt(KEY_LIFT_WIDTH);
		liftDepth = messagePackHelper.getInt(KEY_LIFT_DEPTH);
		liftOffsetX = messagePackHelper.getInt(KEY_LIFT_OFFSET_X);
		liftOffsetY = messagePackHelper.getInt(KEY_LIFT_OFFSET_Y);
		liftOffsetZ = messagePackHelper.getInt(KEY_LIFT_OFFSET_Z);
		isDoubleSided = messagePackHelper.getBoolean(KEY_IS_DOUBLE_SIDED);
		liftStyle = EnumHelper.valueOf(LiftStyle.TRANSPARENT, messagePackHelper.getString(KEY_LIFT_STYLE));
		facing = Direction.fromYRot(messagePackHelper.getInt(KEY_FACING));
		currentPositionX = messagePackHelper.getDouble(KEY_CURRENT_POSITION_X);
		currentPositionY = messagePackHelper.getDouble(KEY_CURRENT_POSITION_Y);
		currentPositionZ = messagePackHelper.getDouble(KEY_CURRENT_POSITION_Z);
		messagePackHelper.iterateArrayValue(KEY_RIDING_ENTITIES, value -> ridingEntities.add(UUID.fromString(value.asStringValue().asString())));
		messagePackHelper.iterateArrayValue(KEY_FLOORS, entry -> floors.add(BlockPos.of(entry.asIntegerValue().toLong())));

		liftInstructions = new LiftInstructions();
		doorOpen = true;
		doorValue = 0;
	}

	public Lift(FriendlyByteBuf packet) {
		super(packet);

		liftHeight = packet.readInt();
		liftWidth = packet.readInt();
		liftDepth = packet.readInt();
		liftOffsetX = packet.readInt();
		liftOffsetY = packet.readInt();
		liftOffsetZ = packet.readInt();
		isDoubleSided = packet.readBoolean();
		liftStyle = EnumHelper.valueOf(LiftStyle.TRANSPARENT, packet.readUtf(PACKET_STRING_READ_LENGTH));
		facing = Direction.fromYRot(packet.readInt());
		currentPositionX = packet.readDouble();
		currentPositionY = packet.readDouble();
		currentPositionZ = packet.readDouble();
		liftDirection = EnumHelper.valueOf(LiftDirection.NONE, packet.readUtf(PACKET_STRING_READ_LENGTH));
		speed = packet.readDouble();
		doorOpen = packet.readBoolean();
		doorValue = packet.readFloat();

		final int ridingEntitiesCount = packet.readInt();
		for (int i = 0; i < ridingEntitiesCount; i++) {
			ridingEntities.add(packet.readUUID());
		}

		final int floorCount = packet.readInt();
		for (int i = 0; i < floorCount; i++) {
			floors.add(packet.readBlockPos());
		}

		liftInstructions = new LiftInstructions(packet);
	}

	@Override
	public void toMessagePack(MessagePacker messagePacker) throws IOException {
		super.toMessagePack(messagePacker);

		messagePacker.packString(KEY_LIFT_HEIGHT).packInt(liftHeight);
		messagePacker.packString(KEY_LIFT_WIDTH).packInt(liftWidth);
		messagePacker.packString(KEY_LIFT_DEPTH).packInt(liftDepth);
		messagePacker.packString(KEY_LIFT_OFFSET_X).packInt(liftOffsetX);
		messagePacker.packString(KEY_LIFT_OFFSET_Y).packInt(liftOffsetY);
		messagePacker.packString(KEY_LIFT_OFFSET_Z).packInt(liftOffsetZ);
		messagePacker.packString(KEY_IS_DOUBLE_SIDED).packBoolean(isDoubleSided);
		messagePacker.packString(KEY_LIFT_STYLE).packString(liftStyle.toString());
		messagePacker.packString(KEY_FACING).packInt(Math.round(facing.toYRot()));
		final BlockPos closestFloor = getCurrentFloorBlockPos();
		messagePacker.packString(KEY_CURRENT_POSITION_X).packDouble(closestFloor.getX());
		messagePacker.packString(KEY_CURRENT_POSITION_Y).packDouble(closestFloor.getY());
		messagePacker.packString(KEY_CURRENT_POSITION_Z).packDouble(closestFloor.getZ());
		messagePacker.packString(KEY_RIDING_ENTITIES).packArrayHeader(ridingEntities.size());
		for (final UUID uuid : ridingEntities) {
			messagePacker.packString(uuid.toString());
		}
		messagePacker.packString(KEY_FLOORS).packArrayHeader(floors.size());
		for (final BlockPos floor : floors) {
			messagePacker.packLong(floor.asLong());
		}
	}

	@Override
	public int messagePackLength() {
		return super.messagePackLength() + 14;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(liftHeight);
		packet.writeInt(liftWidth);
		packet.writeInt(liftDepth);
		packet.writeInt(liftOffsetX);
		packet.writeInt(liftOffsetY);
		packet.writeInt(liftOffsetZ);
		packet.writeBoolean(isDoubleSided);
		packet.writeUtf(liftStyle.toString());
		packet.writeInt(Math.round(facing.toYRot()));
		packet.writeDouble(currentPositionX);
		packet.writeDouble(currentPositionY);
		packet.writeDouble(currentPositionZ);
		packet.writeUtf(liftDirection.toString());
		packet.writeDouble(speed);
		packet.writeBoolean(doorOpen);
		packet.writeFloat(doorValue);
		packet.writeInt(ridingEntities.size());
		ridingEntities.forEach(packet::writeUUID);
		packet.writeInt(floors.size());
		floors.forEach(packet::writeBlockPos);
		liftInstructions.writePacket(packet);
	}

	@Override
	public void update(String key, FriendlyByteBuf packet) {
		if (KEY_LIFT_UPDATE.equals(key)) {
			liftHeight = packet.readInt();
			liftWidth = packet.readInt();
			liftDepth = packet.readInt();
			liftOffsetX = packet.readInt();
			liftOffsetY = packet.readInt();
			liftOffsetZ = packet.readInt();
			isDoubleSided = packet.readBoolean();
			liftStyle = EnumHelper.valueOf(LiftStyle.TRANSPARENT, packet.readUtf(PACKET_STRING_READ_LENGTH));
			facing = Direction.fromYRot(packet.readInt());
		} else {
			super.update(key, packet);
		}
	}

	@Override
	protected boolean hasTransportMode() {
		return false;
	}

	public void setFloors(List<BlockPos> floors) {
		this.floors.clear();
		this.floors.addAll(floors);
	}

	public boolean hasFloor(BlockPos pos) {
		return floors.contains(pos);
	}

	public double getPositionX() {
		return currentPositionX;
	}

	public double getPositionY() {
		return currentPositionY;
	}

	public double getPositionZ() {
		return currentPositionZ;
	}

	public LiftDirection getLiftDirection() {
		return liftDirection;
	}

	public void hasUpDownButtonForFloor(int checkFloor, boolean[] hasButton) {
		floors.forEach(floor -> {
			if (floor.getY() > checkFloor) {
				hasButton[0] = true;
			}
			if (floor.getY() < checkFloor) {
				hasButton[1] = true;
			}
		});
	}

	public void pressButton(int floor) {
		final boolean movingUp = liftDirection == LiftDirection.UP;
		liftInstructions.addInstruction((int) (movingUp ? Math.floor(currentPositionY) : Math.ceil(currentPositionY)), movingUp, floor);
	}

	public BlockPos getCurrentFloorBlockPos() {
		double distance = Double.MAX_VALUE;
		BlockPos closestFloor = null;
		for (final BlockPos floor : floors) {
			final double difference = Math.abs(currentPositionY - floor.getY());
			if (difference < distance) {
				distance = difference;
				closestFloor = floor;
			} else {
				return closestFloor;
			}
		}
		return closestFloor;
	}

	public boolean isInvalidLift(Level world) {
		if (floors.isEmpty()) {
			return true;
		}
		for (final BlockPos checkFloor : floors) {
			if (RailwayData.chunkLoaded(world, checkFloor) && !(world.getBlockState(checkFloor).getBlock() instanceof BlockLiftTrackFloor)) {
				return true;
			}
		}
		return false;
	}

	protected void tick(Level world, float ticksElapsed) {
		if (liftInstructions.hasInstructions() && doorValue == DOOR_MAX * 2) {
			doorOpen = false;
			liftInstructions.getTargetFloor(targetFloor -> liftDirection = targetFloor > currentPositionY ? LiftDirection.UP : LiftDirection.DOWN);
		} else if (!liftInstructions.hasInstructions()) {
			liftDirection = LiftDirection.NONE;
		}

		if (!doorOpen && doorValue == 0) {
			liftInstructions.getTargetFloor(targetFloor -> {
				final double stoppingDistance = Math.abs(targetFloor - currentPositionY);
				liftDirection = stoppingDistance < Train.ACCELERATION_DEFAULT ? LiftDirection.NONE : targetFloor > currentPositionY ? LiftDirection.UP : LiftDirection.DOWN;

				if (liftDirection == LiftDirection.NONE) {
					speed = 0;
					doorOpen = true;
					currentPositionY = targetFloor;
					liftInstructions.arrived();

					if (!world.isClientSide) {
						final BlockEntity blockEntity = world.getBlockEntity(getBlockPos());
						if (blockEntity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor && ((BlockLiftTrackFloor.TileEntityLiftTrackFloor) blockEntity).getShouldDing()) {
							world.playSound(null, getBlockPos(), Utilities.unwrapSoundEvent(SoundEvents.NOTE_BLOCK_PLING), SoundSource.BLOCKS, 16, 2);
						}
					}
				} else {
					if (stoppingDistance < 0.5 * speed * speed / Train.ACCELERATION_DEFAULT) {
						speed = Math.max(speed - 0.5 * speed * speed / stoppingDistance * ticksElapsed, Train.ACCELERATION_DEFAULT);
					} else {
						speed = Math.min(speed + Train.ACCELERATION_DEFAULT * ticksElapsed, 1);
					}

					currentPositionY += speed * liftDirection.speedMultiplier * ticksElapsed;
				}
			});
		} else {
			if (!doorOpen && doorValue > 0 || doorOpen && doorValue < DOOR_MAX * 2) {
				if (doorOpen) {
					doorValue = Math.min(doorValue + ticksElapsed, DOOR_MAX * 2);
				} else {
					doorValue = Math.max(doorValue - ticksElapsed, 0);
				}
			}

			frontCanOpen = checkDoor(world, true);
			if (isDoubleSided) {
				backCanOpen = checkDoor(world, false);
			}
		}
	}

	protected float getYaw() {
		return (float) Math.toRadians(-facing.getClockWise().toYRot());
	}

	private BlockPos getBlockPos() {
		return RailwayData.newBlockPos(currentPositionX, currentPositionY, currentPositionZ);
	}

	private boolean checkDoor(Level world, boolean front) {
		final Direction directionClockwise = facing.getClockWise();
		final int sign = front ? 1 : -1;
		boolean hasDoor = false;
		for (int i = -1; i <= 1; i++) {
			final BlockPos checkPos = RailwayData.newBlockPos(currentPositionX + liftOffsetX / 2F - facing.getStepX() * sign * (liftDepth / 2F + 0.5) + directionClockwise.getStepX() * i, currentPositionY + liftOffsetY, currentPositionZ + liftOffsetZ / 2F - facing.getStepZ() * sign * (liftDepth / 2F + 0.5) + directionClockwise.getStepZ() * i);
			if (world.getNearestPlayer(currentPositionX, currentPositionY, currentPositionZ, Train.MAX_CHECK_DISTANCE, entity -> true) != null && RailwayData.chunkLoaded(world, checkPos) && RailwayData.chunkLoaded(world, checkPos.above())) {
				final BlockEntity entity1 = world.getBlockEntity(checkPos);
				final BlockEntity entity2 = world.getBlockEntity(checkPos.above());
				if (entity1 instanceof BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase && entity2 instanceof BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase && IBlock.getStatePropertySafe(world, checkPos, BlockPSDAPGDoorBase.UNLOCKED) && IBlock.getStatePropertySafe(world, checkPos.above(), BlockPSDAPGDoorBase.UNLOCKED)) {
					if (!world.isClientSide) {
						((BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase) entity1).setOpen(Math.min(Math.round(doorValue), DOOR_MAX));
						((BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase) entity2).setOpen(Math.min(Math.round(doorValue), DOOR_MAX));
					}
					hasDoor = true;
				}
			}
		}

		return hasDoor;
	}

	public enum LiftDirection {
		NONE(0), UP(1), DOWN(-1);

		private final int speedMultiplier;

		LiftDirection(int speedMultiplier) {
			this.speedMultiplier = speedMultiplier;
		}
	}

	public enum LiftStyle {TRANSPARENT, OPAQUE}
}
