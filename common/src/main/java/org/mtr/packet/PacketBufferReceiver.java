package org.mtr.packet;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.Long2ObjectAVLTreeMap;

import java.util.function.Consumer;

public final class PacketBufferReceiver {

	private int receivedCount;
	private int readIndex;
	private final int count;
	private final ByteBuf[] byteBufArray;
	private final Consumer<PacketBufferReceiver> onComplete;

	private static final Long2ObjectAVLTreeMap<PacketBufferReceiver> RECEIVED_PACKETS = new Long2ObjectAVLTreeMap<>();

	private PacketBufferReceiver(int count, Consumer<PacketBufferReceiver> onComplete) {
		this.count = count;
		this.onComplete = onComplete;
		byteBufArray = new ByteBuf[count];
	}

	public boolean readBoolean() {
		read();
		return byteBufArray[readIndex].readBoolean();
	}

	public char readChar() {
		read();
		return byteBufArray[readIndex].readChar();
	}

	public int readInt() {
		read();
		return byteBufArray[readIndex].readInt();
	}

	public float readFloat() {
		read();
		return byteBufArray[readIndex].readFloat();
	}

	public long readLong() {
		read();
		return byteBufArray[readIndex].readLong();
	}

	public double readDouble() {
		read();
		return byteBufArray[readIndex].readDouble();
	}

	public String readString() {
		final int stringLength = readInt();
		final char[] characters = new char[stringLength];
		for (int i = 0; i < stringLength; i++) {
			characters[i] = readChar();
		}
		return new String(characters);
	}

	private void read() {
		if (byteBufArray[readIndex].readableBytes() == 0) {
			readIndex++;
		}
	}

	private boolean receive(int index, ByteBuf byteBuf) {
		byteBufArray[index] = byteBuf;
		receivedCount++;
		if (receivedCount == count) {
			onComplete.accept(this);
			return true;
		} else {
			return false;
		}
	}

	public static void receive(ByteBuf byteBuf, Consumer<PacketBufferReceiver> onComplete, Consumer<Runnable> scheduler) {
		final ByteBuf byteBufCopy = byteBuf.copy();
		scheduler.accept(() -> {
			final long id = byteBufCopy.readLong();
			final int index = byteBufCopy.readInt();
			final int count = byteBufCopy.readInt();
			if (RECEIVED_PACKETS.computeIfAbsent(id, key -> new PacketBufferReceiver(count, onComplete)).receive(index, byteBufCopy)) {
				RECEIVED_PACKETS.remove(id);
			}
		});
	}
}
