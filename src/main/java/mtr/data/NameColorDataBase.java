package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.Random;

public abstract class NameColorDataBase extends SerializedDataBase implements Comparable<NameColorDataBase> {

	public final long id;
	public String name;
	public int color;

	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_COLOR = "color";

	public NameColorDataBase() {
		id = new Random().nextInt(Integer.MAX_VALUE);
		name = "";
	}

	public NameColorDataBase(CompoundTag tag) {
		id = tag.getLong(KEY_ID);
		name = tag.getString(KEY_NAME);
		color = tag.getInt(KEY_COLOR);
	}

	public NameColorDataBase(PacketByteBuf packet) {
		id = packet.readLong();
		name = packet.readString(PACKET_STRING_READ_LENGTH).replace(" |", "|").replace("| ", "|");
		color = packet.readInt();
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putLong(KEY_ID, id);
		tag.putString(KEY_NAME, name);
		tag.putInt(KEY_COLOR, color);
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		packet.writeLong(id);
		packet.writeString(name);
		packet.writeInt(color);
	}

	@Override
	public int compareTo(NameColorDataBase compare) {
		return (name.toLowerCase() + color).compareTo((compare.name + compare.color).toLowerCase());
	}
}
