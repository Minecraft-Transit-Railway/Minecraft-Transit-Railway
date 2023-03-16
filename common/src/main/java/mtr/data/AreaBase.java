package mtr.data;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Tuple;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AreaBase extends NameColorDataBase {

	public Tuple<Integer, Integer> corner1, corner2;

	private static final String KEY_X_MIN = "x_min";
	private static final String KEY_Z_MIN = "z_min";
	private static final String KEY_X_MAX = "x_max";
	private static final String KEY_Z_MAX = "z_max";
	private static final String KEY_CORNERS = "corners";

	public AreaBase() {
		super();
	}

	public AreaBase(long id) {
		super(id);
	}

	public AreaBase(TransportMode transportMode) {
		super(transportMode);
	}

	public AreaBase(long id, TransportMode transportMode) {
		super(id, transportMode);
	}

	public AreaBase(Map<String, Value> map) {
		super(map);
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
		setCorners(messagePackHelper.getInt(KEY_X_MIN), messagePackHelper.getInt(KEY_Z_MIN), messagePackHelper.getInt(KEY_X_MAX), messagePackHelper.getInt(KEY_Z_MAX));
	}

	@Deprecated
	public AreaBase(CompoundTag compoundTag) {
		super(compoundTag);
		setCorners(compoundTag.getInt(KEY_X_MIN), compoundTag.getInt(KEY_Z_MIN), compoundTag.getInt(KEY_X_MAX), compoundTag.getInt(KEY_Z_MAX));
	}

	public AreaBase(FriendlyByteBuf packet) {
		super(packet);
		setCorners(packet.readInt(), packet.readInt(), packet.readInt(), packet.readInt());
	}

	@Override
	public void toMessagePack(MessagePacker messagePacker) throws IOException {
		super.toMessagePack(messagePacker);

		messagePacker.packString(KEY_X_MIN).packInt(corner1 == null ? 0 : corner1.getA());
		messagePacker.packString(KEY_Z_MIN).packInt(corner1 == null ? 0 : corner1.getB());
		messagePacker.packString(KEY_X_MAX).packInt(corner2 == null ? 0 : corner2.getA());
		messagePacker.packString(KEY_Z_MAX).packInt(corner2 == null ? 0 : corner2.getB());
	}

	@Override
	public int messagePackLength() {
		return super.messagePackLength() + 4;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(corner1 == null ? 0 : corner1.getA());
		packet.writeInt(corner1 == null ? 0 : corner1.getB());
		packet.writeInt(corner2 == null ? 0 : corner2.getA());
		packet.writeInt(corner2 == null ? 0 : corner2.getB());
	}

	@Override
	public void update(String key, FriendlyByteBuf packet) {
		if (key.equals(KEY_CORNERS)) {
			setCorners(packet.readInt(), packet.readInt(), packet.readInt(), packet.readInt());
		} else {
			super.update(key, packet);
		}
	}

	public void setCorners(Consumer<FriendlyByteBuf> sendPacket) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		packet.writeUtf(transportMode.toString());
		packet.writeUtf(KEY_CORNERS);
		packet.writeInt(corner1 == null ? 0 : corner1.getA());
		packet.writeInt(corner1 == null ? 0 : corner1.getB());
		packet.writeInt(corner2 == null ? 0 : corner2.getA());
		packet.writeInt(corner2 == null ? 0 : corner2.getB());
		sendPacket.accept(packet);
	}

	public boolean inArea(int x, int z) {
		return nonNullCorners(this) && RailwayData.isBetween(x, corner1.getA(), corner2.getA()) && RailwayData.isBetween(z, corner1.getB(), corner2.getB());
	}

	public boolean intersecting(AreaBase areaBase) {
		return nonNullCorners(this) && nonNullCorners(areaBase) && (inThis(areaBase) || areaBase.inThis(this));
	}

	public BlockPos getCenter() {
		return nonNullCorners(this) ? RailwayData.newBlockPos((corner1.getA() + corner2.getA()) / 2, 0, (corner1.getB() + corner2.getB()) / 2) : null;
	}

	private void setCorners(int corner1a, int corner1b, int corner2a, int corner2b) {
		corner1 = corner1a == 0 && corner1b == 0 ? null : new Tuple<>(corner1a, corner1b);
		corner2 = corner2a == 0 && corner2b == 0 ? null : new Tuple<>(corner2a, corner2b);
	}

	private boolean inThis(AreaBase areaBase) {
		return inArea(areaBase.corner1.getA(), areaBase.corner1.getB()) || inArea(areaBase.corner1.getA(), areaBase.corner2.getB()) || inArea(areaBase.corner2.getA(), areaBase.corner1.getB()) || inArea(areaBase.corner2.getA(), areaBase.corner2.getB());
	}

	public static boolean nonNullCorners(AreaBase station) {
		return station != null && station.corner1 != null && station.corner2 != null;
	}
}
