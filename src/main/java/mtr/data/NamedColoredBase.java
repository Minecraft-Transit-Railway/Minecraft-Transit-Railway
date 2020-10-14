package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public abstract class NamedColoredBase implements Comparable<NamedColoredBase> {

	public String name;
	public int color;

	private static final String KEY_NAME = "name";
	private static final String KEY_COLOR = "color";

	public NamedColoredBase() {
		name = "";
	}

	public NamedColoredBase(CompoundTag tag) {
		name = tag.getString(KEY_NAME);
		color = tag.getInt(KEY_COLOR);
	}

	public NamedColoredBase(PacketByteBuf packet) {
		name = packet.readString();
		color = packet.readInt();
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putString(KEY_NAME, name);
		tag.putInt(KEY_COLOR, color);
		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeString(name);
		packet.writeInt(color);
	}

	@Override
	public int compareTo(NamedColoredBase compare) {
		return name.toLowerCase().compareTo(compare.name.toLowerCase());
	}
}
